package com.csbaic.edatope.app.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.Block;
import com.csbaic.edatope.app.entity.Enterprise;
import com.csbaic.edatope.app.model.command.CreateBlockCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.service.IBlockService;
import com.csbaic.edatope.app.service.IEnterpriseService;
import com.csbaic.edatope.app.service.IProjectService;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.csbaic.edatope.dict.service.impl.AreaService;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlockImportService {

    private static Pattern LNG = Pattern.compile("([EW]{1,1})([0-9]+\\.[0-9]+)");
    private static Pattern LAT = Pattern.compile("([SN]{1,1})([0-9]+\\.[0-9]+)");
    private static Pattern ENTERPRISE_CATEGORY = Pattern.compile("([0-9a-zA-Z]+)\\.(.*)");


    @Autowired
    private IBlockService blockService;
    @Autowired
    private IEnterpriseService enterpriseService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private AreaService areaService ;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IDictService dictService;


    public Result<List<BlockImportCheckResult>> importBlock(MultipartFile file) {
        try {
            List<ExcelTableWrapper<BlockImportVO>> wrappers = new ArrayList<>();
            EasyExcel.read(file.getInputStream(), new ReadListener<BlockImportVO>() {
                @Override
                public void invoke(BlockImportVO data, AnalysisContext context) {
                    wrappers.add(new ExcelTableWrapper<>(context.readRowHolder().getRowIndex(), context.readRowHolder().getCellMap(), data));
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {

                }
            })
                    .headRowNumber(2)
                    .head(BlockImportVO.class)
                    .ignoreEmptyRow(false)
                    .doReadAllSync();

            //检查excel
            List<BlockImportCheckResult> resultList = new ArrayList<>();
            wrappers.forEach(blockImportVOExcelTableWrapper -> checkImportBlock(blockImportVOExcelTableWrapper, resultList));
            if (CollectionUtils.isNotEmpty(resultList)) {
                return Result.error(resultList);
            }

            List<Block> blocks = new ArrayList<>();
            wrappers.forEach(blockImportVOExcelTableWrapper -> blocks.add(createBlock(blockImportVOExcelTableWrapper.getData())));

            List<Enterprise> enterprises = blocks.stream().map(Block::getEnterprise).collect(Collectors.toList());
            transactionTemplate.execute((TransactionCallback<Void>) status -> {
                enterpriseService.saveOrUpdateBatch(enterprises);
                blockService.saveBatch(blocks);
                return null;
            });
            return Result.ok();
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return Result.error("读取文件失败: " + e.getMessage());
        }
    }

    /**
     * 创建地块
     * @param blockImportVO
     * @return
     */
    private Block createBlock(BlockImportVO blockImportVO){
        Block block = new Block();

        BeanCopyUtils.copyNonNullProperties(blockImportVO, block);
        block.setProvinceCode(areaService.getAreaByName(blockImportVO.getProvince()).getValue());
        block.setCityCode(areaService.getAreaByName(blockImportVO.getCity()).getValue());
        block.setDistrictCode(areaService.getAreaByName(blockImportVO.getDistrict()).getValue());

        Enterprise enterprise = enterpriseService.getOne(
                Wrappers.<Enterprise>query().eq(Enterprise.CODE, blockImportVO.getEnterpriseCode())
         );

        if(enterprise == null){
            enterprise = new Enterprise();
            enterprise.setId(IdWorker.getIdStr());
            enterprise.setCode(blockImportVO.getEnterpriseCode());
            enterprise.setName(blockImportVO.getEnterpriseName());
            DictDTO dictDTO = dictService.getDictByName("EnterpriseType", blockImportVO.getEnterpriseType());
            enterprise.setType(dictDTO.getValue());

            List<String> category = Splitter.on(",")
                    .splitToList(blockImportVO.getCategory());

            List<String> categoryCodes = new ArrayList<>();
            for(String c : category){
                Matcher matcher = ENTERPRISE_CATEGORY.matcher(c);
                if(matcher.matches()){
                    categoryCodes.add(matcher.group(1));
                }
            }
            enterprise.setCategory(String.join(",", categoryCodes));
            block.setEnterpriseId(enterprise.getId());
            block.setEnterprise(enterprise);
        }

        block.setEnterprise(enterprise);
        block.setEnterpriseId(enterprise.getId());

        Matcher lat = LAT.matcher(blockImportVO.getLatitude());
        if(lat.matches()){
            block.setLatitudeFlag(lat.group(1));
            block.setLatitude(new BigDecimal(lat.group(2)));
        }

        Matcher lng  = LNG.matcher(blockImportVO.getLongitude());
        if(lng.matches()){
            block.setLongitudeFlag(lng.group(1));
            block.setLongitude(new BigDecimal(lng.group(2)));
        }
        ProjectDTO project = projectService.getProjectByName(blockImportVO.getProjectName());
        block.setProjectId(project.getId());
        block.setCode(genBlockCode(block));
        return block;
    }

    private void checkImportBlock(ExcelTableWrapper<BlockImportVO> wrapper, List<BlockImportCheckResult> resultList){
        BlockImportVO blockImportVO = wrapper.getData();
        if (StringUtils.isEmpty(blockImportVO.getName())) {
            resultList.add(createCheckResult(1, wrapper, "地块名称不能为空") );
        }

        if (StringUtils.isEmpty(blockImportVO.getLongitude())) {
            resultList.add(createCheckResult(2, wrapper, "地块经度不能为空") );
        }else{
             if (!LNG.matcher(blockImportVO.getLongitude()).matches()) {
                resultList.add(createCheckResult(2, wrapper, "地块经度格式不正确") );
            }
        }

        if (StringUtils.isEmpty(blockImportVO.getLatitude())) {
            resultList.add(createCheckResult(3, wrapper, "地块纬度不能为空") );
        }else{
            if (!LAT.matcher(blockImportVO.getLatitude()).matches()) {
                resultList.add(createCheckResult(3, wrapper, "地块纬度格式不正确") );
            }
        }

        if (StringUtils.isEmpty(blockImportVO.getCategory())) {
            resultList.add(createCheckResult(4, wrapper, "企业行业类型不能为空") );
        }else{
            List<String> category = Splitter.on(",")
                    .splitToList(blockImportVO.getCategory());

            for(String c : category){
                if(!ENTERPRISE_CATEGORY.matcher(c).matches()){
                    resultList.add(createCheckResult(4, wrapper, "企业行业类型格式不正确") );
                    break;
                }
            }
        }

        if (StringUtils.isEmpty(blockImportVO.getProvince())) {
            resultList.add(createCheckResult(5, wrapper, "地块所属省不能为空") );
        }else{
            DictDTO dictDTO = areaService.getAreaByName(blockImportVO.getProvince());
            if(dictDTO == null){
                resultList.add(createCheckResult(5, wrapper, "地块所属省不正确，没有匹配到：" + blockImportVO.getProvince()) );
            }
        }

        if (StringUtils.isEmpty(blockImportVO.getCity())) {
            resultList.add(createCheckResult(6, wrapper, "地块所属市不能为空") );
        }else{
            DictDTO dictDTO = areaService.getAreaByName(blockImportVO.getCity());
            if(dictDTO == null){
                resultList.add(createCheckResult(6, wrapper, "地块所属市不正确，没有匹配到：" + blockImportVO.getProvince()) );
            }
        }


        if (StringUtils.isEmpty(blockImportVO.getDistrict())) {
            resultList.add(createCheckResult(7, wrapper, "地块所属县不能为空") );
        }else{
            DictDTO dictDTO = areaService.getAreaByName(blockImportVO.getDistrict());
            if(dictDTO == null){
                resultList.add(createCheckResult(7, wrapper, "地块所属县不正确，没有匹配到：" + blockImportVO.getProvince()) );
            }
        }

        if (StringUtils.isEmpty(blockImportVO.getContact())) {
            resultList.add(createCheckResult(8, wrapper, "联系人不能为空") );
        }

        if (StringUtils.isEmpty(blockImportVO.getContactPhone())) {
            resultList.add(createCheckResult(9, wrapper, "联系电话不能为空") );
        }

        if (StringUtils.isEmpty(blockImportVO.getEnterpriseName())) {
            resultList.add(createCheckResult(10, wrapper, "企业名称不能为空") );
        }

        if (StringUtils.isEmpty(blockImportVO.getEnterpriseCode())) {
            resultList.add(createCheckResult(11, wrapper, "统一社会信用代码不能为空") );
        }

        if (StringUtils.isEmpty(blockImportVO.getEnterpriseType())) {
            resultList.add(createCheckResult(12, wrapper, "企业类型不能为空") );
        }else{
            DictDTO dictDTO = dictService.getDictByName("EnterpriseType", blockImportVO.getEnterpriseType());
            if(dictDTO == null){
                resultList.add(createCheckResult(12, wrapper, "企业类型不正确") );
            }
        }

        if (StringUtils.isEmpty(blockImportVO.getProjectName())) {
            resultList.add(createCheckResult(13, wrapper, "项目名称不能为空") );
        }else{
            ProjectDTO project = projectService.getProjectByName(blockImportVO.getProjectName());
            if (project == null) {
                resultList.add(createCheckResult(13, wrapper, "没有找到项目：" + blockImportVO.getProjectName()) );
            }
        }
    }

    public static BlockImportCheckResult createCheckResult(Integer cellIndex, ExcelTableWrapper<BlockImportVO> wrapper, String msg){
        Map<Integer, Cell> cellMap = wrapper.getCellMap();
        Cell cell = cellMap.get(cellIndex);
        if(cell == null){
            ReadCellData readCellData = new ReadCellData<>();
            readCellData.setRowIndex(wrapper.getRow());
            readCellData.setStringValue("");
            readCellData.setColumnIndex(cellIndex);
            cell = readCellData;
        }
        return new BlockImportCheckResult( cell.getRowIndex() == null ? wrapper.getRow() : cell.getRowIndex() , cell.getColumnIndex(), cell instanceof CellData ? ((CellData) cell).getStringValue() : "", msg );
    }

    /**
     * 编码地址编码
     * 由13位代码组成，前6位行政区划代码，按照国家统计局于2017年3月发布的最新县及县以上行政区划代码（截至2016年7月31日）进行编码；
     * 1位地块类型代码（即企业类型），在产企业地块为
     * 1，关闭搬迁企业地块为2；
     * 2位行业大类代码；后4位流水号码，某区县内所有类型地块统一编码，
     * 从0001开始编码。提交后后台自动生成，且需进行唯一性校验，不可重复！
     *
     * @param block
     */
    public String genBlockCode(Block block) {
        long index = 0;
        String areaCode = "";
        if (StringUtils.isNotEmpty(block.getDistrictCode())) {
            index = blockService.count(
                    Wrappers.<Block>query().eq(Block.DISTRICT_CODE, block.getDistrictCode())
            );
            areaCode = block.getDistrictCode();
        } else if (StringUtils.isNotEmpty(block.getCityCode())) {
            index = blockService.count(
                    Wrappers.<Block>query().eq(Block.CITY_CODE, block.getCityCode())
            );
            areaCode = block.getCityCode();
        } else {
            index = blockService.count(
                    Wrappers.<Block>query().eq(Block.PROVINCE_CODE, block.getProvinceCode())
            );
            areaCode = block.getProvinceCode();
        }
        Enterprise enterprise = block.getEnterprise();
        String enterpriseType = enterprise.getType();
        String enterpriseCategory = Splitter.on(",").splitToList(enterprise.getCategory()).get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(areaCode).
                append(enterpriseType).
                append(enterpriseCategory.length() == 1 ? "0" + enterpriseCategory : enterpriseCategory)
                .append(String.valueOf(++index + 10000).substring(1));

        return sb.toString();
    }


}
