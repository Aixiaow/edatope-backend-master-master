package com.csbaic.edatope.app.enums;


public enum SampleTypeEnums {

    SOIL("土壤"),

    WATER("地下水");

    private String value;

    SampleTypeEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
