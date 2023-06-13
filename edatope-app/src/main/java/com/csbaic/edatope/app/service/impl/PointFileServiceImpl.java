package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.PointFile;
import com.csbaic.edatope.app.mapper.PointFileMapper;
import com.csbaic.edatope.app.model.dto.PointFileDTO;
import com.csbaic.edatope.app.service.IPointFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.file.entity.AppFile;
import com.csbaic.edatope.file.service.IAppFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 布点方案数据维护 服务实现类
 * </p>
 *
 * @author bug
 * @since 2022-04-26
 */
@Service
public class PointFileServiceImpl extends ServiceImpl<PointFileMapper, PointFile> implements IPointFileService {

    @Autowired
    private IAppFileService iAppFileService;

    /**
     * 根据地块工作阶段id查询方案文件
     *
     * @param blockWorkStageId
     * @return
     */
    @Override
    public List<PointFileDTO> listByBlockWorkStageId(String blockWorkStageId) {
        List<PointFile> pointFiles = getBaseMapper().selectList(Wrappers
                .<PointFile>lambdaQuery()
                .eq(PointFile::getBlockWorkStageId, blockWorkStageId));
        return pointFiles.stream().map(a -> {
            String fileId = a.getFileId();
            PointFileDTO pointFileDTO = new PointFileDTO();
            AppFile appFile = iAppFileService.getById(fileId);
            if (appFile != null){
                pointFileDTO.setFileName(appFile.getFileName());
                pointFileDTO.setFileUrl(appFile.getPath());
            }
            pointFileDTO.setFileId(fileId);
            pointFileDTO.setFileType(a.getType());
            return pointFileDTO;
        }).collect(Collectors.toList());
    }
}
