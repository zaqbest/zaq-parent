package com.zaqbest.base.dao.autoconfiguration;

import com.zaqbest.base.dao.handlers.MbpMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.**.mapper")
public class ZaqBaseDaoAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(MbpMetaObjectHandler.class)
    public MbpMetaObjectHandler createMbpMetaObjectHandler(){
        return new MbpMetaObjectHandler();
    }
}
