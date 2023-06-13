package com.csbaic.edatope.common.utils;

import com.csbaic.edatope.common.dto.PageQuery;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户环境工具类
 */
public class EnvUtils {

    /**
     * 基础包名
     */
    private static final String BASE_PACKAGE = "com.csbaic.edatope";

    /**
     * 模块名称匹配器
     */
    private static final Pattern MODULE_PATTERN = Pattern.compile(BASE_PACKAGE + "\\.([a-zA-Z0-9]+)\\.?.*");

    /**
     * 获取基础包名
     *
     * @return
     */
    public static String getBasePackage() {
        return "com.csbaic.edatope";
    }


    /**
     * 解析模块名称
     *
     * @return
     */
    public static String resolveModuleName(Class<?> clz) {
        return resolveModuleName(clz.getPackage().getName());
    }

    /**
     * 解析模块名称
     *
     * @return
     */
    public static String resolveModuleName(String pkg) {
        if (StringUtils.isEmpty(pkg)) {
            throw new IllegalStateException("无法解析模块名称，包名为空");
        }

        Matcher matcher = MODULE_PATTERN.matcher(pkg);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return "";
    }
}
