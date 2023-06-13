package com.csbaic.edatope.app.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Optional;


public class SimpleMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        Optional<PrincipalDetails> details = PrincipalUtils.get();
        details.ifPresent(principalDetails -> {
            strictInsertFill(metaObject, "createBy", String.class, principalDetails.getId());
            strictInsertFill(metaObject, "updateBy", String.class, principalDetails.getId());
        });
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        Optional<PrincipalDetails> details = PrincipalUtils.get();
        details.ifPresent(principalDetails -> {
            strictInsertFill(metaObject, "updateBy", String.class, principalDetails.getId());
        });
    }
}
