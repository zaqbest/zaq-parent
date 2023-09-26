package com.zaqbest.base.web.autoconfiguration;

import com.zaqbest.base.web.autoconfiguration.impl.DefaultBaseAppConfig;
import com.zaqbest.base.web.autoconfiguration.impl.DefaultBaseGlobalExceptionHandler;
import com.zaqbest.base.web.autoconfiguration.impl.DefaultBaseSwaggerConfig;
import com.zaqbest.base.web.autoconfiguration.properties.BaseAppConfig;
import com.zaqbest.base.web.autoconfiguration.properties.BaseSwaggerConfig;
import com.zaqbest.base.web.autoconfiguration.properties.WebTraceConfig;
import com.zaqbest.base.web.exception.BaseGlobalExceptionHandler;
import com.zaqbest.base.web.log.interceptor.LogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZaqBaseWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BaseAppConfig.class)
    public BaseAppConfig createAppConfig(){
        return new DefaultBaseAppConfig();
    }

    @Bean
    @ConditionalOnMissingBean(BaseSwaggerConfig.class)
    public BaseSwaggerConfig createSeaggerConfig(){
        return new DefaultBaseSwaggerConfig();
    }

    @Bean
    @ConditionalOnProperty(prefix = "zaq.webTrace", name = "enabled", havingValue = "true",
            matchIfMissing = true)
    public WebTraceConfig createWebTraceConfig(){
        return new WebTraceConfig();
    }

    @Bean
    @ConditionalOnProperty(prefix = "zaq.webTrace", name = "enabled", havingValue = "true",
            matchIfMissing = true)
    public LogInterceptor createLogInterceptor(){
        return new LogInterceptor();
    }
}
