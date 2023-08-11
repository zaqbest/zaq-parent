package com.zaqbest.base.web.autoconfiguration;

import com.zaqbest.base.web.config.BaseAppConfig;
import com.zaqbest.base.web.config.BaseSwaggerConfig;
import com.zaqbest.base.web.config.WebTraceConfig;
import com.zaqbest.base.web.config.impl.DefaultBaseAppConfig;
import com.zaqbest.base.web.config.impl.DefaultBaseSwaggerConfig;
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
    @ConditionalOnProperty(prefix = "app.weblog", name = "enabled", havingValue = "true",
            matchIfMissing = true)
    public WebTraceConfig createWebTraceConfig(){
        return new WebTraceConfig();
    }
}
