CREATE DATABASE IF NOT EXISTS edatope DEFAULT CHARACTER SET 'utf8mb4';
USE edatope;

DROP TABLE IF EXISTS sys_organization;
CREATE TABLE IF NOT EXISTS sys_organization
(
    id                 BIGINT PRIMARY KEY NOT NULL COMMENT '组织机构id',
    name               VARCHAR(64)        NOT NULL COMMENT '组织机构名称',
    code               VARCHAR(64)   NOT NULL DEFAULT '' COMMENT '组织机构编码',
    pid                VARCHAR(20)   NOT NULL DEFAULT '' COMMENT '上级组织机构id',
    category           VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构性质',
    type               VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构类型',
    biz_type           VARCHAR(256)  NOT NULL DEFAULT '' COMMENT '组织机构业务类型（逗号分隔）',
    province_code      VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构所在省',
    city_code          VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构所在市',
    district_code      VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构所在区县',
    address            VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '组织机构地址详情',
    legal_person       VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构法人名称',
    phone              VARCHAR(32)   NOT NULL DEFAULT '' COMMENT '组织机构联系电话',
    service_level      VARCHAR(64)   NOT NULL DEFAULT '' COMMENT '服务级别',
    establishment_date DATE COMMENT '组织机构成立日期',
    status             VARCHAR(32)   NOT NULL COMMENT '组织机构状态',
    update_by          VARCHAR(128) COMMENT '更新人',
    create_by          VARCHAR(128) COMMENT '创建人',
    update_time        DATETIME      NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time        DATETIME      NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted            TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    INDEX idx_code (code),
    INDEX idx_name (name),
    INDEX idx_area (province_code, city_code, district_code)
) COMMENT '系统组织机构表';


DROP TABLE IF EXISTS sys_organization;
CREATE TABLE IF NOT EXISTS sys_organization
(
    id                 BIGINT PRIMARY KEY NOT NULL COMMENT '组织机构id',
    name               VARCHAR(64)        NOT NULL COMMENT '组织机构名称',
    code               VARCHAR(64)        NOT NULL DEFAULT '' COMMENT '组织机构编码',
    pid                VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '上级组织机构id',
    category           VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构性质',
    type               VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构类型',
    biz_type           VARCHAR(256)       NOT NULL DEFAULT '' COMMENT '组织机构业务类型（逗号分隔）',
    province_code      VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构所在省',
    city_code          VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构所在市',
    district_code      VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构所在区县',
    address            VARCHAR(1024)      NOT NULL DEFAULT '' COMMENT '组织机构地址详情',
    legal_person       VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构法人名称',
    phone              VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '组织机构联系电话',
    service_level      VARCHAR(64)        NOT NULL DEFAULT '' COMMENT '服务级别',
    establishment_date DATE COMMENT '组织机构成立日期',
    status             VARCHAR(32)        NOT NULL COMMENT '组织机构状态',
    update_by          VARCHAR(128) COMMENT '更新人',
    create_by          VARCHAR(128) COMMENT '创建人',
    update_time        DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time        DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted            TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    INDEX idx_code (code),
    INDEX idx_name (name),
    INDEX idx_area (province_code, city_code, district_code)
) COMMENT '系统组织机构表';

DROP TABLE IF EXISTS sys_organization_biz_type;
CREATE TABLE IF NOT EXISTS sys_organization_biz_type
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '组织机构id',
    org_id      VARCHAR(20)        NOT NULL COMMENT '组织机构id',
    biz_type    VARCHAR(32)        NOT NULL COMMENT '业务类型',
    update_by   VARCHAR(128) COMMENT '更新人',
    create_by   VARCHAR(128) COMMENT '创建人',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'

) COMMENT '系统组织机构业务类型表';


DROP TABLE IF EXISTS sys_tech_organization_authorize;
CREATE TABLE IF NOT EXISTS sys_tech_organization_authorize
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '记录id',
    owner_id    VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '归属单位id',
    org_id      VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '技术单位的单位id',
    user_id     VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '技术负责人id',
    role_id     VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '技术单位角色id',
    area_code   VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '分配区域（逗号分隔）',
    enabled     TINYINT(1)         NOT NULL COMMENT '是否启用',
    update_by   VARCHAR(128) COMMENT '更新人',
    create_by   VARCHAR(128) COMMENT '创建人',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'

) COMMENT '技术单位授权表';

DROP TABLE IF EXISTS sys_tech_organization_authorize_city;
CREATE TABLE IF NOT EXISTS sys_tech_organization_authorize_city
(
    id            BIGINT PRIMARY KEY NOT NULL COMMENT '记录id',
    authorize_id  VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '技术单位授权id',
    province_code VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '授权所在省',
    city_code     VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '授权所在市',
    district_code VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '授权所在区县',
    update_by     VARCHAR(128) COMMENT '更新人',
    create_by     VARCHAR(128) COMMENT '创建人',
    update_time   DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'

) COMMENT '技术单位授权城市表';



DROP TABLE IF EXISTS sys_user;
CREATE TABLE IF NOT EXISTS sys_user
(

    id                BIGINT PRIMARY KEY NOT NULL COMMENT '用户id',
    username          VARCHAR(64)        NOT NULL COMMENT '用户名',
    nick_name         VARCHAR(64)        NOT NULL DEFAULT '' COMMENT '用户姓名',
    avatar_url        VARCHAR(1024)      NOT NULL DEFAULT '' COMMENT '用户头像',
    mobile            VARCHAR(11)        NOT NULL DEFAULT '' COMMENT '用户手机号',
    password          VARCHAR(128)       NOT NULL COMMENT '用户密码',
    password_salt     VARCHAR(128)       NOT NULL DEFAULT '' COMMENT '用户密码盐值',
    org_id            VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '组织机构id',
    department        VARCHAR(64)        NOT NULL DEFAULT '' COMMENT '所在部门',
    need_set_password TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '是否需要重新设置密码',
    gender            VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '用户性别',
    type              VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '用户类型',
    education         VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '学历',
    id_card           VARCHAR(18)        NOT NULL DEFAULT '' COMMENT '身份证号',
    locked            TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '用户是否被锁定',
    status            VARCHAR(32)        NOT NULL COMMENT '用户状态',
    birthday          DATE COMMENT '出生日期',
    admin             TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '是否是超级管理员',
    update_by         VARCHAR(128) COMMENT '更新人',
    create_by         VARCHAR(128) COMMENT '创建人',
    update_time       DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time       DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted           TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    INDEX idx_username (username),
    INDEX idx_mobile (mobile)
) COMMENT '系统用户表';


DROP TABLE IF EXISTS sys_acl_permission;
CREATE TABLE IF NOT EXISTS sys_acl_permission
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '权限id',
    name        VARCHAR(64)        NOT NULL COMMENT '权限名称',
    code        VARCHAR(64)        NOT NULL COMMENT '权限编码',
    pid         VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '上级权限id',
    status      VARCHAR(32)        NOT NULL COMMENT '权限状态',
    type        VARCHAR(16)        NOT NULL DEFAULT '' COMMENT '权限类型（菜单，接口）',
    sort        INTEGER            NOT NULL DEFAULT 0 COMMENT '权限排序',
    update_by   VARCHAR(128) COMMENT '更新主体',
    create_by   VARCHAR(128) COMMENT '创建主体',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    UNIQUE uni_code (code),
    INDEX idx_pid (pid)
) COMMENT '系统权限表';

DROP TABLE IF EXISTS `sys_acl_permission_data_rule`;
CREATE TABLE `sys_acl_permission_data_rule`
(
    `id`            varchar(32)  NOT NULL COMMENT 'id',
    `permission_id` varchar(32)  NULL     DEFAULT NULL COMMENT '权限id',
    `rule_name`     varchar(50)  NULL     DEFAULT NULL COMMENT '规则名称',
    `column`        varchar(50)  NULL     DEFAULT NULL COMMENT '字段名称',
    `conditions`    varchar(50)  NULL     DEFAULT NULL COMMENT '规则条件',
    `value`         varchar(300) NULL     DEFAULT NULL COMMENT '规则值',
    `status`        varchar(3)   NULL     DEFAULT NULL COMMENT '权限有效状态1有0否',
    update_by       VARCHAR(128) COMMENT '更新主体',
    create_by       VARCHAR(128) COMMENT '创建主体',
    update_time     DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time     DATETIME     NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '数据权限规则';

DROP TABLE IF EXISTS sys_acl_role;
CREATE TABLE IF NOT EXISTS sys_acl_role
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '角色id',
    name        VARCHAR(64)        NOT NULL COMMENT '角色名称',
    description VARCHAR(128)       NOT NULL DEFAULT '' COMMENT '角色说明',
    code        VARCHAR(64)        NOT NULL COMMENT '角色编码',
    type        VARCHAR(32)        NOT NULL COMMENT '角色类型',
    org_id      VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '关联单位',
    property    VARCHAR(32)        NOT NULL COMMENT '角色性质',
    level       VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '角色级别',
    status      VARCHAR(32)        NOT NULL COMMENT '角色状态',
    update_by   VARCHAR(128) COMMENT '更新主体',
    create_by   VARCHAR(128) COMMENT '创建主体',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    UNIQUE uni_code (code)
) COMMENT '系统角色表';

CREATE TABLE IF NOT EXISTS sys_acl_role_permission
(
    id            BIGINT PRIMARY KEY NOT NULL COMMENT '角色与权限关联记录id',
    role_id       BIGINT             NOT NULL COMMENT '角色id',
    permission_id BIGINT             NOT NULL COMMENT '权限id',
    update_by     VARCHAR(128) COMMENT '更新人',
    create_by     VARCHAR(128) COMMENT '创建人',
    update_time   DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) COMMENT '系统角色与权限关联表';

CREATE TABLE IF NOT EXISTS sys_acl_user_role
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '角色与用户关联记录id',
    role_id     BIGINT             NOT NULL COMMENT '角色id',
    user_id     BIGINT             NOT NULL COMMENT '用户id',
    update_by   VARCHAR(128) COMMENT '更新人',
    create_by   VARCHAR(128) COMMENT '创建人',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (user_id)
) COMMENT '系统角色与用户关联表';


CREATE TABLE IF NOT EXISTS sys_acl_menu
(
    id            BIGINT PRIMARY KEY NOT NULL COMMENT '菜单id',
    name          VARCHAR(64)        NOT NULL COMMENT '菜单名称',
    title         VARCHAR(64)        NOT NULL COMMENT '菜单标题',
    component     VARCHAR(256)       NOT NULL DEFAULT '' COMMENT '菜单组件',
    pid           VARCHAR(20)        NULL COMMENT '上级菜单id',
    permission_id VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '系统权限id',
    type          VARCHAR(32)        NOT NULL COMMENT '菜单类型：菜单项、按钮',
    path          VARCHAR(128) COMMENT '菜单路径',
    icon          VARCHAR(128) COMMENT '菜单图标',
    sort          VARCHAR(32) COMMENT '菜单排序',
    external      TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '是否为外部链接',
    status        VARCHAR(32)        NOT NULL COMMENT '菜单状态：正常、隐藏、禁用',
    update_by     VARCHAR(128) COMMENT '更新主体',
    create_by     VARCHAR(128) COMMENT '创建主体',
    update_time   DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',

    INDEX idx_type (type)
) COMMENT '系统菜单表';


DROP TABLE IF EXISTS sys_acl_api;
CREATE TABLE IF NOT EXISTS sys_acl_api
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '接口id',
    name        VARCHAR(64)        NOT NULL COMMENT '接口名称',
    gid         VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '接口组id',
    path        VARCHAR(128)                DEFAULT '' COMMENT '接口路径',
    anonymous   TINYINT(1)                  DEFAULT 0 COMMENT '接口是否可以匿名访问',
    status      VARCHAR(32)        NOT NULL COMMENT '菜单状态：正常、隐藏、禁用',
    sort        INTEGER            NOT NULL DEFAULT 0 COMMENT '接口排序',
    api_group   TINYINT            NOT NULL DEFAULT 0 COMMENT '是否是接口组',
    update_by   VARCHAR(128) COMMENT '更新主体',
    create_by   VARCHAR(128) COMMENT '创建主体',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '系统接口表';

DROP TABLE IF EXISTS sys_dict;
CREATE TABLE IF NOT EXISTS sys_dict
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '字典id',
    name        VARCHAR(64)        NOT NULL COMMENT '字典名称',
    value       VARCHAR(1024)      NOT NULL DEFAULT '' COMMENT '字典值',
    type        VARCHAR(128)       NULL COMMENT '字典类型',
    pid         VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '上级字典id',
    description VARCHAR(512)       NOT NULL DEFAULT '' COMMENT '字典说明',
    status      VARCHAR(64)        NOT NULL COMMENT '字典状态',
    sort        INTEGER            NOT NULL COMMENT '字典排序',
    update_by   VARCHAR(128)       NULL COMMENT '更新主体',
    create_by   VARCHAR(128)       NULL COMMENT '创建主体',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '系统字典表';


DROP TABLE IF EXISTS sys_option;
CREATE TABLE IF NOT EXISTS sys_option
(
    id          BIGINT PRIMARY KEY NOT NULL COMMENT '配置id',
    name        VARCHAR(64)        NOT NULL COMMENT '配置名称',
    `key`       VARCHAR(64)        NOT NULL COMMENT '配置键值',
    value       VARCHAR(1024)      NULL     DEFAULT '' COMMENT '配置值',
    description VARCHAR(512)       NOT NULL DEFAULT '' COMMENT '配置描述',
    update_by   VARCHAR(128) COMMENT '更新主体',
    create_by   VARCHAR(128) COMMENT '创建主体',
    update_time DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '系统配置表';

DROP TABLE IF EXISTS sys_app_file;
CREATE TABLE IF NOT EXISTS sys_app_file
(
    id               BIGINT PRIMARY KEY NOT NULL COMMENT '文件id',
    owner_id         BIGINT             NOT NULL COMMENT '文件归属人',
    file_name        VARCHAR(64)        NOT NULL DEFAULT '' COMMENT '文件名称',
    origin_file_name VARCHAR(64)        NOT NULL DEFAULT '' COMMENT '原始文件名称',
    md5              VARCHAR(64)        NOT NULL DEFAULT '' NOT NULL COMMENT '文件md5',
    progress         DECIMAL(4, 2)      NOT NULL DEFAULT 0 COMMENT '接收进度',
    length           BIGINT COMMENT '文件大小（字节）'   DEFAULT 0 COMMENT '文件长度',
    path             VARCHAR(1024)      NOT NULL DEFAULT '' COMMENT '文件路径',
    status           VARCHAR(32)        NOT NULL DEFAULT '' COMMENT '文件状态（创建，上传中，已完成）',
    biz_type         VARCHAR(128)       NOT NULL DEFAULT '' COMMENT '文件业务类型',
    remark           VARCHAR(1024)      NOT NULL DEFAULT '' COMMENT '文件备注',
    update_by        VARCHAR(128) COMMENT '更新主体',
    create_by        VARCHAR(128) COMMENT '创建主体',
    update_time      DATETIME           NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME           NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '系统文件表';


DROP TABLE IF EXISTS sys_operate_log;
CREATE TABLE IF NOT EXISTS sys_operate_log
(
    id            BIGINT PRIMARY KEY NOT NULL COMMENT '日志id',
    log_type      VARCHAR(64)        NOT NULL COMMENT '日志类型（登陆日志，操作日志，请求日志，错误日志）',
    module        VARCHAR(128)       NOT NULL DEFAULT '' COMMENT '所属模块',
    operator_name VARCHAR(128) NOT NULL DEFAULT '' COMMENT '操作人姓名',
    operator_id   VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '操作人id',
    operate_type  VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '操作类型',
    operate_time  DATETIME     NOT NULL DEFAULT NOW() COMMENT '操作时间',
    remark        VARCHAR(512) NULL COMMENT '日志备注',
    request_path  VARCHAR(256) NOT NULL DEFAULT '' COMMENT '请求路径',
    request_param TEXT         NULL COMMENT '请求参数',
    request_body  TEXT         NULL COMMENT '请求实体',
    request_addr  VARCHAR(32)  NOT NULL COMMENT '请求ip地址',
    target_id     VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '关联目标id',
    target_name   VARCHAR(128) NOT NULL DEFAULT '' COMMENT '操作对象名称',
    update_by     VARCHAR(128) COMMENT '更新主体',
    create_by     VARCHAR(128) COMMENT '创建主体',
    update_time   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME     NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    INDEX idx_operate_type (operate_type)

) COMMENT '业务操作日志';

DROP TABLE IF EXISTS sys_project;
CREATE TABLE IF NOT EXISTS sys_project
(
    id            BIGINT PRIMARY KEY COMMENT '项目id',
    name          VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '项目名称',
    org_id        VARCHAR(32) NOT NULL DEFAULT '' COMMENT '管理单位id',
    province_code VARCHAR(6)   NOT NULL DEFAULT '' COMMENT '项目所属省份编码',
    city_code     VARCHAR(6)   NOT NULL DEFAULT '' COMMENT '项目所属城市编码',
    district_code VARCHAR(6)   NOT NULL DEFAULT '' COMMENT '项目所属区县编码',
    begin_date    DATE         NOT NULL COMMENT '项目开始日期',
    end_date      DATE         NOT NULL COMMENT '项目结束日期',
    remark        VARCHAR(128) NOT NULL COMMENT '项目备注',
    update_by     VARCHAR(128) COMMENT '更新主体',
    create_by     VARCHAR(128) COMMENT '创建主体',
    update_time   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME     NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
);

DROP TABLE IF EXISTS sys_project;
CREATE TABLE IF NOT EXISTS sys_project
(
    id            BIGINT PRIMARY KEY COMMENT '项目id',
    name          VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '项目名称',
    province_code VARCHAR(6)   NOT NULL DEFAULT '' COMMENT '项目所属省份编码',
    city_code     VARCHAR(6)   NOT NULL DEFAULT '' COMMENT '项目所属城市编码',
    district_code VARCHAR(6)   NOT NULL DEFAULT '' COMMENT '项目所属区县编码',
    begin_date    DATE         NOT NULL COMMENT '项目开始日期',
    end_date      DATE         NOT NULL COMMENT '项目结束日期',
    remark        VARCHAR(128) NOT NULL COMMENT '项目备注',
    update_by     VARCHAR(128) COMMENT '更新主体',
    create_by     VARCHAR(128) COMMENT '创建主体',
    update_time   DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME     NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
);

DROP TABLE IF EXISTS sys_block;
CREATE TABLE IF NOT EXISTS sys_block
(
    id             BIGINT PRIMARY KEY COMMENT '地块id',
    code           VARCHAR(13)    NOT NULL DEFAULT '' COMMENT '地块编码',
    name           VARCHAR(32)    NOT NULL DEFAULT '' COMMENT '地区名称',
    enterprise_id  VARCHAR(20)    NOT NULL DEFAULT '' COMMENT '企业名称',
    project_id     VARCHAR(20)    NOT NULL DEFAULT '' COMMENT '项目id',
    province_code  VARCHAR(6)     NOT NULL DEFAULT '' COMMENT '地区所属省份编码',
    city_code      VARCHAR(6)     NOT NULL DEFAULT '' COMMENT '地区所属城市编码',
    district_code  VARCHAR(6)     NOT NULL DEFAULT '' COMMENT '地区所属区县编码',
    latitude       DECIMAL(19, 6) NOT NULL COMMENT '纬度',
    latitude_flag  CHAR(1)        NOT NULL DEFAULT '' COMMENT '纬度符号',
    longitude      DECIMAL(10, 6) NOT NULL COMMENT '经度',
    longitude_flag CHAR(1)        NOT NULL DEFAULT '' COMMENT '经度符号',
    address        VARCHAR(1024)  NOT NULL DEFAULT '' COMMENT '地块地址',
    contact        VARCHAR(32)    NOT NULL DEFAULT '' COMMENT '联系人',
    contact_phone  VARCHAR(16)    NOT NULL DEFAULT '' COMMENT '联系电话',
    update_by      VARCHAR(128) COMMENT '更新主体',
    create_by      VARCHAR(128) COMMENT '创建主体',
    update_time    DATETIME       NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time    DATETIME       NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted        TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '地块';

DROP TABLE IF EXISTS sys_block_work_stage;
CREATE TABLE IF NOT EXISTS sys_block_work_stage
(
    id            BIGINT PRIMARY KEY COMMENT '地块id',
    name          VARCHAR(32) NOT NULL DEFAULT '' COMMENT '地区名称',
    block_id      VARCHAR(20) NOT NULL DEFAULT '' COMMENT '项目id',
    work_stage_id VARCHAR(20) NOT NULL DEFAULT '' COMMENT '工作任务id',
    deadline      DATE        NOT NULL COMMENT '任务期限',
    verify        TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否需要核实',
    org_id        VARCHAR(20) NOT NULL DEFAULT '' COMMENT '牵头单位',
    status        VARCHAR(32) NOT NULL DEFAULT '' COMMENT '工作阶段状态',
    update_by     VARCHAR(128) COMMENT '更新主体',
    create_by     VARCHAR(128) COMMENT '创建主体',
    update_time   DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time   DATETIME    NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '地块工作阶段';

DROP TABLE IF EXISTS sys_enterprise;
CREATE TABLE IF NOT EXISTS sys_enterprise
(
    id          BIGINT PRIMARY KEY COMMENT '企业id',
    name        VARCHAR(32) NOT NULL DEFAULT '' COMMENT '企业名称',
    code        VARCHAR(20) NOT NULL DEFAULT '' COMMENT '企业统一信用编码',
    category    VARCHAR(20) not null DEFAULT '' COMMENT '大分类',
    type        VARCHAR(20) NOT NULL DEFAULT '' COMMENT '企业类型',
    update_by   VARCHAR(128) COMMENT '更新主体',
    create_by   VARCHAR(128) COMMENT '创建主体',
    update_time DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME    NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '被调查的企业';


DROP TABLE IF EXISTS sys_device;
CREATE TABLE IF NOT EXISTS sys_device
(
    id          BIGINT PRIMARY KEY COMMENT 'id',
    model       VARCHAR(32) NOT NULL DEFAULT '' COMMENT '设备型号',
    brand       VARCHAR(20) NOT NULL DEFAULT '' COMMENT '设备品牌',
    identifier  VARCHAR(20) not null DEFAULT '' COMMENT '设备标识（取MEID）MEID',
    update_by   VARCHAR(128) COMMENT '更新主体',
    create_by   VARCHAR(128) COMMENT '创建主体',
    update_time DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time DATETIME    NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted     TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '设备';

DROP TABLE IF EXISTS sys_device_authorize;
CREATE TABLE IF NOT EXISTS sys_device_authorize
(
    id               BIGINT PRIMARY KEY COMMENT 'id',
    user_id          VARCHAR(20) NOT NULL DEFAULT '' COMMENT '用户id',
    org_id           VARCHAR(20) NOT NULL DEFAULT '' COMMENT '单位id',
    authorizer_id    VARCHAR(20) NOT NULL DEFAULT '' COMMENT '授权人',
    first_login_time DATETIME    NOT NULL DEFAULT NOW() COMMENT '首次登陆时间',
    authorize_time   DATETIME COMMENT '授权时间',
    device_id        VARCHAR(20) NOT NULL DEFAULT '' COMMENT '设备id',
    status           VARCHAR(20) NOT NULL DEFAULT '' COMMENT '授权状态',
    update_by        VARCHAR(128) COMMENT '更新主体',
    create_by        VARCHAR(128) COMMENT '创建主体',
    update_time      DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '记录更新时间',
    create_time      DATETIME    NOT NULL DEFAULT NOW() COMMENT '记录创建时间',
    deleted          TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '设备授权';

