package com.csbaic.edatope.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 系统文件表
 * </p>
 *
 * @author bage
 * @since 2022-04-05
 */
@Getter
@Setter
@TableName("sys_app_file")
public class AppFile extends BaseEntity {

    /**
     * 文件归属人
     */
    private String ownerId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 原始文件名称
     */
    private String originFileName;

    /**
     * 文件md5
     */
    private String md5;

    /**
     * 接收进度
     */
    private BigDecimal progress;

    /**
     * 文件长度
     */
    private Long length;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件状态（创建，上传中，已完成）
     */
    private String status;

    /**
     * 文件业务类型
     */
    private String bizType;

    /**
     * 文件备注
     */
    private String remark;


    public static final String OWNER_ID = "owner_id";

    public static final String FILE_NAME = "file_name";

    public static final String ORIGIN_FILE_NAME = "origin_file_name";

    public static final String MD5 = "md5";

    public static final String PROGRESS = "progress";

    public static final String LENGTH = "length";

    public static final String PATH = "path";

    public static final String STATUS = "status";

    public static final String BIZ_TYPE = "biz_type";

    public static final String REMARK = "remark";

}
