package com.csbaic.edatope.common.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量处理信息
 */
@Data
public class BatchInfo {

    /**
     * 成功的记录
     */
    private List<Object> successful = new ArrayList<>();
    /**
     * 失败的记录
     */
    private List<Object> failure = new ArrayList<>();

    /**
     * 忽略的记录
     */
    private List<Object> unhandled = new ArrayList<>();


}
