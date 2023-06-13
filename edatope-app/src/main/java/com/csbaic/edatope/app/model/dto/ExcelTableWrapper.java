package com.csbaic.edatope.app.model.dto;

import com.alibaba.excel.metadata.Cell;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelTableWrapper<T> {

    /**
     * 数据所在行
     */
    private Integer row;

    /**
     * 原始数据
     */
    private Map<Integer, Cell> cellMap;

    /**
     * 数据
     */
    private T data;

    public ExcelTableWrapper(Integer row, Map<Integer, Cell> cellMap, T data) {
        this.row = row;
        this.data = data;
        this.cellMap = cellMap;
    }
}
