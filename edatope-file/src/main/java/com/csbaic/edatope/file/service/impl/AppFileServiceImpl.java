package com.csbaic.edatope.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.file.constants.FileConstants;
import com.csbaic.edatope.file.entity.AppFile;
import com.csbaic.edatope.file.enums.FileStatus;
import com.csbaic.edatope.file.mapper.FileMapper;
import com.csbaic.edatope.file.model.command.CreateFileCmd;
import com.csbaic.edatope.file.model.command.UploadFileCmd;
import com.csbaic.edatope.file.model.dto.FileDTO;
import com.csbaic.edatope.file.model.query.FileQuery;
import com.csbaic.edatope.file.model.vo.FileIdVO;
import com.csbaic.edatope.file.model.vo.UploadFileVO;
import com.csbaic.edatope.file.service.IAppFileService;
import com.csbaic.edatope.file.utils.AppFileUtils;
import com.csbaic.edatope.option.service.IOptionService;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nullable;
import java.io.*;
import java.math.BigDecimal;

/**
 * <p>
 * 系统文件表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-08
 */
@Service
public class AppFileServiceImpl extends ServiceImpl<FileMapper, AppFile> implements IAppFileService {

    /**
     * 配置服务
     */
    @Autowired
    private IOptionService optionService;

    @Transactional
    @Override
    public FileIdVO createFile(CreateFileCmd cmd) {
        AppFile appFile = new AppFile();
        BeanCopyUtils.copyNonNullProperties(cmd, appFile);
        appFile.setStatus(FileStatus.UPLOADING.name());
        String baseDir = optionService.getValueByKey(FileConstants.STORAGE_PATH);
        if (Strings.isNullOrEmpty(baseDir)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有配置文件存储路径，请联系管理员");
        }

        File root = new File(baseDir);
        if (!root.exists()) {
            try {
                if (!root.createNewFile()) {
                    throw BizRuntimeException.from(ResultCode.ERROR, "创建文件目录失败");
                }
            } catch (IOException e) {
                log.error("创建文件目录失败", e);
                throw BizRuntimeException.from(ResultCode.ERROR, "创建文件目录失败");
            }
        }

        File targetFile = new File(baseDir, appFile.getFileName());
        if (targetFile.exists()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "文件名已经存在，请不要重复创建");
        }

        PrincipalDetails details = PrincipalUtils.getOrThrow();
        appFile.setOwnerId(details.getId());
        BeanCopyUtils.copyNonNullProperties(cmd, appFile);
        save(appFile);
        return new FileIdVO(appFile.getId());
    }

    @Override
    public UploadFileVO uploadFile(UploadFileCmd cmd) {
        String originalFilename = cmd.getFile().getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "文件名称不能为空");
        }

        String ext = Files.getFileExtension(originalFilename);
        String filename = "";
        if (StringUtils.isNotEmpty(cmd.getFileName())) {
            filename = cmd.getFileName();
        } else {
            filename = IdWorker.getIdStr() + (StringUtils.isNotEmpty(ext) ? "." + ext : "");
        }
        String baseDir = optionService.getValueByKey(FileConstants.STORAGE_PATH);
        if (Strings.isNullOrEmpty(baseDir)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有配置文件存储路径，请联系管理员");
        }

        String fileServer = optionService.getValueByKey(FileConstants.STORAGE_SERVER);
        if (Strings.isNullOrEmpty(baseDir)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有配置存储服务器地址，请联系管理员");
        }

        try {
            File root = new File(baseDir);
            File tmpFile = File.createTempFile(filename, ".tmp", root);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream in = cmd.getFile().getInputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            in.close();
            os.close();

            byte[] fileBytes = os.toByteArray();
            String fileMd5 = DigestUtils.md5DigestAsHex(fileBytes);
            AppFile appFile = getOne(
                    Wrappers.<AppFile>query().eq(AppFile.MD5, fileMd5)
            );

            if (appFile == null) {
                FileOutputStream tmpFileOutput = new FileOutputStream(tmpFile);
                tmpFileOutput.write(fileBytes, 0, fileBytes.length);
                tmpFileOutput.close();

                File targetFile = new File(baseDir, filename);
                AppFileUtils.renameFile(tmpFile, targetFile.getName());
                appFile = new AppFile();
                appFile.setFileName(filename);
                appFile.setOriginFileName(originalFilename);
                appFile.setMd5(DigestUtils.md5DigestAsHex(fileBytes));
                appFile.setOwnerId(PrincipalUtils.getOrThrow().getId());
                appFile.setBizType(cmd.getBizType());
                appFile.setProgress(BigDecimal.valueOf(1));
                appFile.setStatus(FileStatus.SUCCESS.name());
                appFile.setLength((long) fileBytes.length);
                appFile.setPath(UriComponentsBuilder.
                        fromHttpUrl(fileServer).pathSegment(filename).toUriString());
                save(appFile);
            }

            UploadFileVO fileVO = new UploadFileVO();
            fileVO.setId(appFile.getId());
            fileVO.setUrl(appFile.getPath());
            return fileVO;
        } catch (IOException e) {
            log.error("上传文件失败：" + e.getMessage());
            throw BizRuntimeException.from(ResultCode.ERROR, "上传文件失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<FileDTO> listFilePage(FileQuery query) {
        QueryWrapper<AppFile> wrapper = Wrappers.<AppFile>query();
        if (!Strings.isNullOrEmpty(query.getName())) {
            wrapper.like(AppFile.FILE_NAME, query.getName());
        }
        if (!Strings.isNullOrEmpty(query.getId())) {
            wrapper.eq(AppFile.ID, query.getId());
        }

        IPage<AppFile> fileIPage = page(new Page<>(query.getPageIndex(), query.getPageSize()), wrapper);
        return fileIPage.convert(new Function<AppFile, FileDTO>() {
            @Nullable
            @Override
            public FileDTO apply(@Nullable AppFile input) {
                FileDTO dto = new FileDTO();
                BeanCopyUtils.copyNonNullProperties(input, dto);
                return dto;
            }
        });
    }
}
