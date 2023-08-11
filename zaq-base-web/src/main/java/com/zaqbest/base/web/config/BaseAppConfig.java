package com.zaqbest.base.web.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

//@Configuration
//@RefreshScope
@Data
public abstract class BaseAppConfig {
    /**
     * 读取配置文件中的enable，true为显示，false为隐藏
     */
    @Value("${app.swagger.enabled:true}")
    private Boolean enableSwagger;


    @Value("${app.weblog.enabled:true}")
    private Boolean enableWebLog;
}
