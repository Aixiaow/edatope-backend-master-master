package com.csbaic.edatope.common.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量处理信息
 */
@Data
public class BatchInfoItem {

    /**
     * 项目id
     */
    private String id;

    /**
     * 操作消息
     */
    private String msg;

    public BatchInfoItem(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }
}
