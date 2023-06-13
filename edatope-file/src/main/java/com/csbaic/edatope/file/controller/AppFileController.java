package com.csbaic.edatope.file.controller;


import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.file.model.command.UploadFileCmd;
import com.csbaic.edatope.file.model.vo.UploadFileVO;
import com.csbaic.edatope.file.service.IAppFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 系统文件表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-01-08
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/api/v1/files")
public class AppFileController {

    @Autowired
    private IAppFileService appFileService;

//    @ApiOperation("创建文件")
//    @PostMapping(value = "/create")
//    public Result<FileIdVO> create(@RequestBody @Validated CreateFileCmd cmd) {
//        return Result.ok(
//                appFileService.createFile(cmd)
//        );
//    }

    @ApiOperation(value = "上传文件", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<UploadFileVO> update(@RequestParam("file") MultipartFile file, @RequestParam(value = "bizType", required = false) String bizType) {
        UploadFileCmd cmd = new UploadFileCmd();
        cmd.setFile(file);
        cmd.setBizType(bizType);
        return Result.ok(
                appFileService.uploadFile(cmd)
        );
    }
}

