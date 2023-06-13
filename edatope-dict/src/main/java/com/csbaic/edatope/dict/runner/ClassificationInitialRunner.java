package com.csbaic.edatope.dict.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.common.utils.TreeListUtils;
import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.enums.DictStatus;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.base.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.csbaic.edatope.dict.constants.DictConstants.CLASSIFICATION_TYPE;

/**
 * 添加城市数据
 */
@Slf4j
@Component
public class ClassificationInitialRunner implements ApplicationRunner {

    /**
     * 字典排序
     */
    private static int sort = 0;

    /**
     * 字典服务
     */
    @Autowired
    private IDictService dictService;
    @Autowired
    private ResourceLoader resourceLoader;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        long count = dictService.count(Wrappers.<Dict>query().eq(Dict.TYPE, CLASSIFICATION_TYPE));
        if (count > 0) {
            return;
        }
        log.info("==========================================");
        log.info("开始初始化分类字典");
        Resource resource = resourceLoader.getResource("classpath:/national_industries_classification.json");
        if (!resource.exists()) {
            log.warn("行业分类文件不存在，无法初始化");
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = in.readLine()) != null) {
            content.append(line);
        }
        JSONArray array = JSON.parseArray(content.toString());
        List<Classification> classificationList = new ArrayList<>();
        collect(array, null, classificationList);
        //转换成字典
        List<Dict> dicts = classificationList
                .stream()
                .map(ClassificationInitialRunner::toDict)
                .collect(Collectors.toList());

        dictService.saveBatch(dicts);
    }


    private static Dict toDict(Classification classification) {
        Dict dict = new Dict();
        dict.setPid(classification.parentCode);
        dict.setName(classification.name);
        dict.setType(CLASSIFICATION_TYPE);
        dict.setDescription("行业分类：" + classification.name);
        dict.setSort(sort++);
        dict.setValue(classification.code);
        dict.setStatus(DictStatus.NORMAL.name());
        return dict;
    }

    private static void collect(JSONArray array, String parentCode, List<Classification> classificationList) {
        for (int index = 0; index < array.size(); index++) {
            log.info(JSON.toJSONString(array));
            JSONObject object = array.getJSONObject(index);
            Classification classification = new Classification();
            classification.name = object.getString("name");
            classification.code = object.getString("code");
            classification.parentCode = parentCode;
            classificationList.add(classification);
            JSONArray children = object.getJSONArray("children");
            if (children != null) {
                collect(children, classification.code, classificationList);
            }
        }
    }

    private static class Classification {
        /**
         * 名称
         */
        String name;
        /**
         * 编码
         */
        String code;

        /**
         * 上级分类code
         */
        String parentCode;

        /**
         * 下级区域
         */
        List<Classification> children = new ArrayList<>();


    }
}
