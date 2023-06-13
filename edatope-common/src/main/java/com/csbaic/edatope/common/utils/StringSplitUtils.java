package com.csbaic.edatope.common.utils;

import java.util.*;

public final class StringSplitUtils {

    /**
     * 将字符串切成list
     * @param content
     * @returnd
     */
    public static List<String> splitToList(String d, String content){
        String[] splited = content.split(d);
        if (splited != null && splited.length > 0) {
            return new ArrayList<>(Arrays.asList(splited));
        }
        return new ArrayList<>();
    }

    /**
     * 将字符串切成list
     * @param content
     * @returnd
     */
    public static Set<String> splitToSet(String d, String content){
        List<String> list = splitToList(d, content);
        return list != null ? new TreeSet<>(list) : new TreeSet<>();
    }

    /**
     * 拼接
     * @param d
     * @param itemList
     * @return
     */
    public static String join(String d, Collection<String> itemList){
        if(itemList == null || itemList.isEmpty()){
            return "";
        }
        return String.join(d, itemList);
    }
}
