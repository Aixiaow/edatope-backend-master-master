package com.csbaic.edatope.common.enums;

public enum DataScopeEnum {
    /**
     * 只允许访问自己创建的数据
     */
    SELF,
    /**
     * 自己的数据和本单位的数据
     */
    ORG,
    /**
     * 自己的数据和本单位的数据以及下级数据
     */
    ORG_AND_CHILD,
    /**
     * 所有数据
     */
    ALL,
}
