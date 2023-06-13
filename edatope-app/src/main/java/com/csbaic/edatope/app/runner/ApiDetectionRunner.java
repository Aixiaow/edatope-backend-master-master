package com.csbaic.edatope.app.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.app.entity.Api;
import com.csbaic.edatope.app.service.IApiService;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接口内省
 */
@Component
public class ApiDetectionRunner implements ApplicationRunner {

    private static final String DEFAULT_GROUP_NAME = "默认分组";

    @Autowired
    private IApiService apiService;

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<RequestMappingInfo, HandlerMethod>  handlerMethodMap = handlerMapping.getHandlerMethods();
        if (MapUtils.isEmpty(handlerMethodMap)) {
            return;
        }

        taskExecutor.execute(this::handleApi);
    }


    private void handleApi( ){
        List<Api> apis = detectAppApi(handlerMapping);
        Map<String, List<Api>> groupingBy = apis
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Api::getGid,
                                Collectors.toList()
                        )
                );

        List<String> groupNames = new ArrayList<>(groupingBy.keySet());
        groupNames.add(DEFAULT_GROUP_NAME);

        //创建接口分组
        groupNames.forEach((s) -> {
            Api group = apiService.getOne(Wrappers.<Api>query().eq(Api.NAME, s));
            if(group == null){
                Api apiGroup = new Api();
                apiGroup.setAnonymous(false);
                apiGroup.setApiGroup(true);
                apiGroup.setStatus(EnableStatus.ENABLED.toString());
                apiGroup.setName(s);
                apiService.save(apiGroup);
            }
        });

        apis.forEach(api -> {
            Api group = apiService.getOne(Wrappers.<Api>query().eq(Api.NAME, api.getGid()));
            Api created = apiService.getOne(
                    Wrappers.<Api>query().eq(Api.PATH, api.getPath())
            );

            if(created != null){
//                BeanCopyUtils.copyNonNullProperties(api, created);
                //还原组id
                created.setGid(group.getId());
                created.setName(api.getName());
                created.setPermissionCode(api.getPermissionCode());
            }

            api.setGid(group.getId());
            apiService.saveOrUpdate(created != null ? created : api);
        });

    }

    /**
     * 获取应用的接口
     * @return
     */
    private static List<Api> detectAppApi(RequestMappingHandlerMapping handlerMapping){
        Map<RequestMappingInfo, HandlerMethod>  handlerMethodMap = handlerMapping.getHandlerMethods();
        if (MapUtils.isEmpty(handlerMethodMap)) {
            return new ArrayList<>();
        }
        List<Api> apis = new ArrayList<>();
        handlerMethodMap.forEach((requestMappingInfo, handlerMethod) -> {
            Api api = resolveApi(handlerMethod);
            if(api != null){
                apis.add(api);
            }
        });
        return apis;
    }


    /**
     * 转换api对象
     * @param handlerMethod
     * @return
     */
    public static Api resolveApi(HandlerMethod handlerMethod){
        Class beanType = handlerMethod.getBeanType();
        io.swagger.annotations.Api apiInfo = AnnotationUtils.findAnnotation(beanType, io.swagger.annotations.Api.class);
        if(apiInfo == null){
            return null;
        }

        ApiOperation operation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiOperation.class);
        ApiPermission permission  = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiPermission.class);
        RequestMapping parentMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequestMapping.class);
        String prefixPath = path(parentMapping);
        PathMatcher pathMatcher = new AntPathMatcher("/");

        Api api = new Api();
        api.setAnonymous(permission != null && permission.anonymous());
        api.setApiGroup(false);
        //暂时设置成组名称
        String[] tags = apiInfo != null ? apiInfo.tags() : null;
        if (tags != null && tags.length > 0) {
            api.setGid(tags[0]);
        }else{
            api.setGid(DEFAULT_GROUP_NAME);
        }
        api.setStatus(EnableStatus.ENABLED.toString());
        api.setPath(pathMatcher.combine(prefixPath, path(requestMapping)));
        api.setName(operation != null ? operation.value() : handlerMethod.getMethod().getName());
        //权限编码设置
        if (permission != null) {
            api.setPermissionCode(StringUtils.isNotEmpty(permission.code()) ? permission.code() : permission.value());
        }

        return api;
    }

    public static String path(RequestMapping mapping){
        String[] paths = mapping != null ? mapping.path() : null;
        return paths != null && paths.length > 0 ? paths[0] : "/";
    }
}
