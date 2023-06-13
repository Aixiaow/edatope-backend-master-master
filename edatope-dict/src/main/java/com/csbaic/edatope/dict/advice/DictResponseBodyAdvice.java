package com.csbaic.edatope.dict.advice;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.model.dto.DictTypeDTO;
import com.csbaic.edatope.dict.repository.DictCache;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ControllerAdvice
public class DictResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private IDictService dictService;


    /**
     * 緩存字典
     */
    @Autowired
    private DictCache dictCache;


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        /*
            獲取返回的數據對象
         */
        if(body instanceof Result){
            handleBody(((Result) body).getData());
        }else if(body instanceof IPage){
            handleBody(((IPage) body).getRecords());
        }else{
            handleBody(body);
        }
        return body;
    }


    /**
     * 回寫字典數據
     * @param object
     */
    public void handleBody(Object object){
        if(object == null){
            return;
        }
        String packageName = ClassUtils.getPackageName(object.getClass());
        boolean canHandle = packageName.startsWith("com.csbaic.edatope")
                || object instanceof Collection
                || object instanceof Map
                || object.getClass().isArray()
                || object instanceof IPage;

        if(!canHandle){
            return;
        }
        if(object instanceof IPage){
            ((IPage) object).getRecords().forEach(this::handleBody);
        }else if(object instanceof Collection){
            ((Collection) object).forEach(this::handleBody);
        }else if(object instanceof Map){
            ((Map) object).forEach((o, o2) -> handleBody(o2));
        }else if(object.getClass().isArray()) {
            int len = Array.getLength(object);
            for(int index = 0; index < len ; index++){
                handleBody(Array.get(object, index));
            }
        } else {
            //处理子对象
            ReflectionUtils.doWithFields(object.getClass(), field -> {
                field.setAccessible(true);
                Object value = field.get(object);
                handleBody(value);
            });

            List<DictPropertyHolder> holders = findDictPropertyHolders(object);
            if (CollectionUtils.isEmpty(holders)) {
                return;
            }

            holders.forEach(holder -> handleDictProperty(object, holder));
        }
    }

    /**
     * 处理字典属性
     * @param holder
     */
    public  void handleDictProperty(Object target, DictPropertyHolder holder) {
        List<DictDTO> dicts = dictCache.get(holder.getType());
        if (CollectionUtils.isEmpty(dicts)) {
            dicts = dictService.listFlatDictByType(Lists.newArrayList(holder.getType()));
            dictCache.save(holder.getType(), dicts);
        }

        Map<Object, DictDTO> dictItemMap = new HashMap<>();
        dicts.forEach(dictDTO -> dictItemMap.put(dictDTO.getValue(), dictDTO));
        if (MapUtils.isEmpty(dictItemMap)) {
            return;
        }

        Object value = ReflectionUtils.getField(holder.getValueField(), target);
        DictDTO dictItemDTO = dictItemMap.get(value);
        if (dictItemDTO == null) {
            return;
        }

        holder.getNameField().setAccessible(true);
        holder.getValueField().setAccessible(true);
        try {
            holder.getNameField().set(target, dictItemDTO.getName());
        } catch (IllegalAccessException e) {
            log.error("handleDictProperty error", e);
        }
    }

    /**
     * 查找字典属性
     * @param object
     * @return
     */
    public static List<DictPropertyHolder> findDictPropertyHolders(Object object){
        List<DictPropertyHolder> pairList = new ArrayList<>();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            DictPropertyHolder pair = findDictPropertyHolderWithField(field);
            if(pair != null){
                pairList.add(pair);
            }
        });
        return pairList;
    }

    public static DictPropertyHolder findDictPropertyHolderWithField(Field field){
        DictProperty property = AnnotationUtils.findAnnotation(field,DictProperty.class);
        if(property == null){
            return null;
        }

        DictPropertyHolder holder = new DictPropertyHolder();
        holder.setType(property.type());
        holder.setNameField(field);
        Field valueField = ReflectionUtils.findField(field.getDeclaringClass(), property.value());
        if(valueField == null){
            return null;
        }
        holder.setValueField(valueField);
        return holder;
    }



}
