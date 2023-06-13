package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 专家组组员
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Getter
@Setter
@TableName("sys_specialist_user")
public class SpecialistUser extends BaseEntity {

    /**
     * 专家组id;专家组表(sys_specialist)id
     */
    private String specialistId;

    /**
     * 专家性质;字典类型 D021 内部专家 外聘专家
     */
    private String nature;

    /**
     * 工作单位id
     */
    private Long orgId;

    /**
     * 专家用户id
     */
    private String userId;

    /**
     * 专家身份;字典类型 D022 组长 组员
     */
    private String specialistIdentity;


    public static final String SPECIALIST_ID = "specialist_id";

    public static final String NATURE = "nature";

    public static final String ORG_ID = "org_id";

    public static final String USER_ID = "user_id";

    public static final String SPECIALIST_IDENTITY = "specialist_identity";

}
