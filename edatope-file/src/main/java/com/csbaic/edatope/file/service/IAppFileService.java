package com.csbaic.edatope.file.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.file.entity.AppFile;
import com.csbaic.edatope.file.model.command.CreateFileCmd;
import com.csbaic.edatope.file.model.command.UploadFileCmd;
import com.csbaic.edatope.file.model.dto.FileDTO;
import com.csbaic.edatope.file.model.query.FileQuery;
import com.csbaic.edatope.file.model.vo.FileIdVO;
import com.csbaic.edatope.file.model.vo.UploadFileVO;

/**
 * <p>
 * 系统文件表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-01-08
 */
public interface IAppFileService extends IService<AppFile> {


    /**
     * 创建文件
     *
     * @param cmd
     * @return
     */
    FileIdVO createFile(CreateFileCmd cmd);


    /**
     * 上传文件
     *
     * @param cmd
     */
    UploadFileVO uploadFile(UploadFileCmd cmd);

    /**
     * 文件查询
     * @param query
     * @return
     */
    IPage<FileDTO> listFilePage(FileQuery query);
}
