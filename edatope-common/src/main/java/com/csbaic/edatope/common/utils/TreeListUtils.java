package com.csbaic.edatope.common.utils;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeListUtils {

    /**
     * 上级记录id分隔符
     */
    public static final String SEP = ",";

    /**
     * 树形化list
     *
     * @param list
     * @param id
     * @param pid
     * @param <T>
     * @return
     */
    public static <T> void tree(List<T> list, Function<T, String> id, Function<T, String> pid, BiConsumer<T, T> consumer) {
        Map<String, T> detailVoMap = list
                .stream().collect(Collectors.toMap(id, java.util.function.Function.identity()));

        Function<T, String> pidWrapper = t -> getParentId(pid.apply(t));
        list.forEach(node -> {
            String parentId = pidWrapper.apply(node);
            if (!Strings.isNullOrEmpty(parentId)) {
                T parent = detailVoMap.get(parentId);
                consumer.accept(parent, node);
            } else {
                consumer.accept(null, node);
            }
        });
    }


    /**
     * 拼接父节点
     *
     * @param ancestors
     * @param pid
     * @return
     */
    public static String append(String ancestors, String pid) {
        return Strings.isNullOrEmpty(ancestors) ? pid : ancestors + SEP + pid;
    }

    /**
     * 获取父节点id
     *
     * @param pid
     * @return
     */
    public static String getParentId(String pid) {
        if (Strings.isNullOrEmpty(pid)) {
            return pid;
        }
        if (!pid.contains(SEP)) {
            return pid;
        }

        String[] ids = StringUtils.commaDelimitedListToStringArray(pid);
        return ids[ids.length - 1];
    }

    /**
     * 获取所有的父节点id
     *
     * @param pid
     * @return
     */
    public static List<String> getAncestorsIds(String pid) {
        if (Strings.isNullOrEmpty(pid)) {
            return new ArrayList<>();
        }
        if (!pid.contains(SEP)) {
            return Lists.newArrayList(pid);
        }

        return new ArrayList<>(StringUtils.commaDelimitedListToSet(pid));
    }
}
