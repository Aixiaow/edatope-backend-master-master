package com.csbaic.edatope.app.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum YesOrNoEnum {

    YES("是"),

    NO("否");

    private String value;

    YesOrNoEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
