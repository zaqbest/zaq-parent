package com.zaqbest.base.dao.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MbpMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.fillStrategy(metaObject, "createTime", new Date());
        this.fillStrategy(metaObject, "updateTime", new Date());
        this.fillStrategy(metaObject, "isDelete", 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.fillStrategy(metaObject, "updateTime", new Date());
    }
}