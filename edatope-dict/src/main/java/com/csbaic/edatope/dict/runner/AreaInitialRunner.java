package com.csbaic.edatope.dict.runner;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.utils.TreeListUtils;
import com.csbaic.edatope.dict.constants.DictConstants;
import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.enums.DictStatus;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.ARG_IN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiConsumer;

import static com.csbaic.edatope.dict.constants.DictConstants.AREA_DICT_TYPE;

/**
 * 添加城市数据
 */
@Slf4j
@Component
public class AreaInitialRunner implements ApplicationRunner {

    /**
     * 字典排序
     */
    private static int areaSort = 0;

    /**
     * 字典服务
     */
    @Autowired
    private IDictService dictService;
    @Autowired
    private ResourceLoader resourceLoader;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        long count = dictService.count(Wrappers.<Dict>query().eq(Dict.TYPE, AREA_DICT_TYPE));
        if (count > 0) {
            return;
        }
        log.info("==========================================");
        log.info("开始初始化区域字典");
        Resource resource = resourceLoader.getResource("classpath:/area.csv");
        if (!resource.exists()) {
            log.warn("区域文件不存在，无法初始化");
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line;
        List<Area> areaList = new ArrayList<>();
        Map<String, Area> provinceMap = new TreeMap<>();
        //解析区域文件
        log.info("开始解析区域文件");
        Set<String> aresCodeSet = new HashSet<>();
        while ((line = in.readLine()) != null) {
            List<String> oneArea = Splitter.on(",").splitToList(line);
            Area area = new Area();
            area.name = oneArea.get(0);
            area.adCode = oneArea.get(1);
            area.cityCode = oneArea.size() > 2 ? oneArea.get(2) : "";
            aresCodeSet.add(area.adCode);
            areaList.add(area);

            if (area.adCode.endsWith("0000")) {
                area.parentAdCode = null;
            } else if (area.adCode.endsWith("00")) {
                area.parentAdCode = area.adCode.substring(0, 2) + "0000";
            } else {
                String cityCode = area.adCode.substring(0, 4) + "00";
                if (aresCodeSet.contains(cityCode)) {
                    area.parentAdCode = cityCode;
                } else {
                    //直辖县
                    area.parentAdCode = area.adCode.substring(0, 2) + "0000";
                }

            }

        }
        //按层级
        TreeListUtils.tree(areaList, a -> a.adCode, a -> a.parentAdCode, (parent, child) -> {
            if (parent == null) {
                provinceMap.put(child.adCode, child);
            } else {
                parent.children.add(child);
            }
        });
        log.info("解析区域文件完成");
        List<Dict> dictList = new ArrayList<>();
        provinceMap.forEach((s, area) -> saveArea(null, area, dictList));
        dictService.saveBatch(dictList);
        log.info("初始化区域完成");
        log.info("==========================================");
    }

    /**
     * 保存区域数据
     */
    private void saveArea(Dict parent, Area child, List<Dict> dictList) {
        Dict dict = new Dict();
        dict.setId(String.valueOf(IdWorker.getId()));
        dict.setValue(child.adCode);
        dict.setName(child.name);
        dict.setType(AREA_DICT_TYPE);
        dict.setSort(areaSort++);
        dict.setPid(parent != null ? parent.getValue() : null);
        dict.setStatus(EnableStatus.ENABLED.name());
        dictList.add(dict);
        //保存下级区域
        for (Area c : child.children) {
            saveArea(dict, c, dictList);
        }
    }

    private static class Area {
        /**
         * 名称
         */
        String name;
        /**
         * 编码
         */
        String adCode;
        /**
         * zip编码
         */
        String cityCode;

        /**
         * 上级编码
         */
        String parentAdCode;

        /**
         * 下级区域
         */
        List<Area> children = new ArrayList<>();


    }
}
