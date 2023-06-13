package com.csbaic.edatope.dict.utils;

public class AreaUtils {

    public static boolean isProvince(String code){
        return code.endsWith("0000");
    }

    public static boolean isCity(String code){
        return code.endsWith("00");
    }

    public static boolean isDistrict(String code){
        return !code.endsWith("00");
    }
}
