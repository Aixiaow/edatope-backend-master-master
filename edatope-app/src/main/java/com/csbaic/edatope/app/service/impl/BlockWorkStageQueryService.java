package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.dto.BlockVO;
import com.csbaic.edatope.app.model.dto.BlockWorkStageQueryResultVO;
import com.csbaic.edatope.app.model.dto.EnterpriseBlockListVO;
import com.csbaic.edatope.app.model.dto.EnterpriseVO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.service.IBlockService;
import com.csbaic.edatope.app.service.IEnterpriseService;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockWorkStageQueryService {

    @Autowired
    private IBlockService blockService;
    @Autowired
    private IEnterpriseService enterpriseService;
    @Autowired
    private IOrganizationService organizationService;

    /**
     * 查询地块
     *
     * @param query
     * @return
     */
    public IPage<BlockWorkStageQueryResultVO> listPage(BlockQuery query) {
        IPage<BlockVO> page = blockService.listPage(query);
        return page.convert(blockVO -> {
            BlockWorkStageQueryResultVO resultVO = new BlockWorkStageQueryResultVO();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                resultVO.setOrganization(organizationService.getDetail(resultVO.getProject().getOrgId()));
            }
            return resultVO;
        });
    }

    public EnterpriseBlockListVO   getBlockForEnterprise(String enterpriseId){
        EnterpriseVO enterprise = enterpriseService.getDetailById(enterpriseId);
        if(enterprise == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到企业");
        }
        EnterpriseBlockListVO enterpriseBlockListVO = new EnterpriseBlockListVO();
        BeanCopyUtils.copyNonNullProperties(enterprise, enterpriseBlockListVO);
        List<BlockVO>  list = blockService.listByEnterpriseId(enterpriseId);
        enterpriseBlockListVO.setBlockList(list);
        return enterpriseBlockListVO;
    }


    /**
     * 查下一级地块
     * @param blockQuery
     * @return
     */
    public IPage<BlockVO> listNextLevelBlock(BlockQuery blockQuery){
        if (StringUtils.isEmpty(blockQuery.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "管理单位id不能为空");
        }

        return blockService.listPage(blockQuery);
    }





}
