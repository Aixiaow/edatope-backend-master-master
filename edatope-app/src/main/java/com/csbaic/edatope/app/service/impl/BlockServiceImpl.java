package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.entity.Block;
import com.csbaic.edatope.app.entity.Enterprise;
import com.csbaic.edatope.app.entity.WorkStage;
import com.csbaic.edatope.app.mapper.BlockMapper;
import com.csbaic.edatope.app.model.command.CreateBlockCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockCmd;
import com.csbaic.edatope.app.model.command.UpdateBlockCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.EnterpriseQuery;
import com.csbaic.edatope.app.model.query.PointUserBlockQuery;
import com.csbaic.edatope.app.model.query.QualityControlTasksQuery;
import com.csbaic.edatope.app.service.IBlockService;
import com.csbaic.edatope.app.service.IEnterpriseService;
import com.csbaic.edatope.app.service.IWorkStageService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.constants.DictConstants;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 地块 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Service
public class BlockServiceImpl extends ServiceImpl<BlockMapper, Block> implements IBlockService {


    @Autowired
    private IEnterpriseService enterpriseService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IWorkStageService workStageService;


    @Transactional
    @Override
    public void create(CreateBlockCmd cmd) {
        String code = genBlockCode(cmd);
        Block block = getOne(
                Wrappers.<Block>query().eq(Block.CODE, code)
        );
        if (block != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "地址编码已经存在");
        }

        block = new Block();
        EnterpriseDTO enterpriseDTO = cmd.getEnterprise();
        if (StringUtils.isEmpty(enterpriseDTO.getId())) {
            if (StringUtils.isEmpty(enterpriseDTO.getCode())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业编码不能为空");
            }

            if (StringUtils.isEmpty(enterpriseDTO.getType())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业类型不能为空");
            }

            if (CollectionUtils.isEmpty(enterpriseDTO.getCategory())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业行业类型不能为空");
            }

            Enterprise enterprise = enterpriseService.getOne(
                    Wrappers.<Enterprise>query().eq(Enterprise.CODE, enterpriseDTO.getCode())
            );

            if (enterprise != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业编码已经存在：" + enterpriseDTO.getCode());
            }

            enterprise = new Enterprise();
            enterprise.setCode(enterpriseDTO.getCode());
            enterprise.setName(enterpriseDTO.getName());
            enterprise.setType(enterpriseDTO.getType());
            enterprise.setCategory(String.join(",", enterpriseDTO.getCategory()));
            enterpriseService.save(enterprise);
            block.setEnterpriseId(enterprise.getId());
        }

        BeanCopyUtils.copyNonNullProperties(cmd, block);
        block.setCode(code);
        save(block);
    }

    @Override
    public void update(UpdateBlockCmd cmd) {
        Block block = getById(cmd.getId());
        if (block == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块");
        }

        EnterpriseDTO enterpriseDTO = cmd.getEnterprise();
        if (StringUtils.isEmpty(enterpriseDTO.getId())) {
            if (StringUtils.isEmpty(enterpriseDTO.getCode())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业编码不能为空");
            }

            if (StringUtils.isEmpty(enterpriseDTO.getType())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业类型不能为空");
            }

            if (CollectionUtils.isEmpty(enterpriseDTO.getCategory())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业行业类型不能为空");
            }

            Enterprise enterprise = enterpriseService.getOne(
                    Wrappers.<Enterprise>query().eq(Enterprise.CODE, enterpriseDTO.getCode())
            );

            if (enterprise != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "企业编码已经存在：" + enterpriseDTO.getCode());
            }

            enterprise = new Enterprise();
            enterprise.setCode(enterpriseDTO.getCode());
            enterprise.setName(enterpriseDTO.getName());
            enterprise.setType(enterpriseDTO.getType());
            enterprise.setCategory(String.join(",", enterpriseDTO.getCategory()));
            enterpriseService.save(enterprise);
            block.setEnterpriseId(enterprise.getId());
        }

        BeanCopyUtils.copyNonNullProperties(block, cmd);
        updateById(block);
    }

    @Override
    public IPage<BlockVO> listPage(BlockQuery blockQuery) {
        return getBaseMapper()
                .listPage(new Page<>(blockQuery.getPageIndex(), blockQuery.getPageSize()), blockQuery)
                .convert(this::convert);
    }

    /**
     * 布点人员地块查询
     *
     * @param blockQuery
     * @return
     */
    @Override
    public IPage<BlockVO> pointUserListPage(PointUserBlockQuery blockQuery) {
        return getBaseMapper()
                .pointUserListPage(new Page<>(blockQuery.getPageIndex(), blockQuery.getPageSize()), blockQuery)
                .convert(this::convert);
    }

    /**
     * 布点人员地块查询
     *
     * @param blockQuery
     * @return
     */
    @Override
    public IPage<BlockVO> pointUserTaskListPage(PointUserBlockQuery blockQuery) {
        return getBaseMapper()
                .pointUserTaskListPage(new Page<>(blockQuery.getPageIndex(), blockQuery.getPageSize()), blockQuery)
                .convert(this::convert);
    }


    @Override
    public List<BlockVO> list(BlockQuery blockQuery) {
        return getBaseMapper()
                .list(blockQuery)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockVO> listByEnterpriseId(String enterpriseId) {
        return getBaseMapper().listByEnterpriseId(enterpriseId)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockVO> listBlockByProjectId(String projectId) {
        return getBaseMapper().listByProjectId(projectId)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public BlockVO getDetailById(String id) {
        Block block = getBaseMapper().getDetailById(id);
        if(block == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块");
        }


        return convert(block);
    }

    @Override
    public void delete(DeleteBlockCmd cmd) {
        Block block = getById(cmd.getId());
        if (block != null) {
            removeById(block);
        }

    }

    @Override
    public List<EnterpriseVO> listEnterpriseByName(EnterpriseQuery query) {
        return enterpriseService.listByName(query);
    }

    @Override
    public IPage<BlockVO> qualityControlTaskPage(QualityControlTasksQuery query) {
        return getBaseMapper()
                .qualityControlTaskPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convert);
    }

    @Override
    public IPage<BlockVO> sendBackPage(QualityControlTasksQuery query) {
        return getBaseMapper()
                .sendBackPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convert);
    }

    /**
     * 布点质控专家组查询列表
     * @param query
     * @return
     */
    @Override
    public IPage<BlockVO> specialistPage(QualityControlTasksQuery query) {
        return getBaseMapper()
                .specialistPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convert);
    }

    public BlockVO convert(Block block) {
        BlockVO blockVO = new BlockVO();
        BeanCopyUtils.copyNonNullProperties(block, blockVO);
        if (block.getProject() != null) {
            ProjectDTO projectDTO = new ProjectDTO();
            BeanCopyUtils.copyNonNullProperties(block.getProject(), projectDTO);
            blockVO.setProject(projectDTO);
        }
        if (block.getEnterprise() != null) {
            Enterprise enterprise = block.getEnterprise();
            EnterpriseVO enterpriseVO = new EnterpriseVO();
            BeanCopyUtils.copyNonNullProperties(enterprise, enterpriseVO);
            enterpriseVO.setCategory(Splitter.on(",").splitToList(block.getEnterprise().getCategory()));

            if (CollectionUtils.isNotEmpty(enterpriseVO.getCategory())) {
                List<String> categoryDesc = new ArrayList<>();
                for (String category : enterpriseVO.getCategory()) {
                    DictDTO dict = dictService.getDictByValue(DictConstants.CLASSIFICATION_TYPE, category);
                    categoryDesc.add(dict.getValue() + "." + dict.getName());
                }
                enterpriseVO.setCategoryDesc(String.join(",", categoryDesc));
            }
            blockVO.setEnterprise(enterpriseVO);
        }

        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(block.getWorkStageList())) {
            blockVO.setWorkStageList(
                    block.getWorkStageList().stream()
                            .map(blockWorkStage -> {
                                WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
                                BlockWorkStageDTO dto = new BlockWorkStageDTO();
                                BeanCopyUtils.copyNonNullProperties(blockWorkStage, dto);
                                dto.setWorkStageName(workStage.getName());
                                return dto;
                            }).collect(Collectors.toList())
            );
            blockVO.setWorkStageCount(block.getWorkStageList().size());
            blockVO.setPhaseCount(block.getWorkStageList().size());
        }

        return blockVO;
    }

    /**
     * 编码地址编码
     * 由13位代码组成，前6位行政区划代码，按照国家统计局于2017年3月发布的最新县及县以上行政区划代码（截至2016年7月31日）进行编码；
     * 1位地块类型代码（即企业类型），在产企业地块为
     * 1，关闭搬迁企业地块为2；
     * 2位行业大类代码；后4位流水号码，某区县内所有类型地块统一编码，
     * 从0001开始编码。提交后后台自动生成，且需进行唯一性校验，不可重复！
     *
     * @param cmd
     */
    public String genBlockCode(CreateBlockCmd cmd) {
        long index = 0;
        String areaCode = "";
        if (StringUtils.isNotEmpty(cmd.getDistrictCode())) {
            index = count(
                    Wrappers.<Block>query().eq(Block.DISTRICT_CODE, cmd.getDistrictCode())
            );
            areaCode = cmd.getDistrictCode();
        } else if (StringUtils.isNotEmpty(cmd.getCityCode())) {
            index = count(
                    Wrappers.<Block>query().eq(Block.CITY_CODE, cmd.getCityCode())
            );
            areaCode = cmd.getCityCode();
        } else {
            index = count(
                    Wrappers.<Block>query().eq(Block.PROVINCE_CODE, cmd.getProvinceCode())
            );
            areaCode = cmd.getProvinceCode();
        }
        EnterpriseDTO enterpriseVO = cmd.getEnterprise();
        String enterpriseType = enterpriseVO.getType();
        String enterpriseCategory = enterpriseVO.getCategory().get(0);
        if (StringUtils.isNotEmpty(enterpriseVO.getId())) {
            Enterprise enterprise = enterpriseService.getById(enterpriseVO.getId());
            enterpriseType = enterprise.getType();
            enterpriseCategory = Splitter.on(",").splitToList(enterpriseCategory).get(0);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(areaCode).
                append(enterpriseType).
                append(enterpriseCategory.length() == 1 ? "0" + enterpriseCategory : enterpriseCategory)
                .append(String.valueOf(++index + 10000).substring(1));

        return sb.toString();
    }
}
