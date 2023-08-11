package com.zaqbest.base.web.autoconfiguration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

//@Configuration
//@RefreshScope
@Data
public abstract class BaseAppConfig {
    /**
     * 读取配置文件中的enable，true为显示，false为隐藏
     */
    @Value("${zaq.swagger.enabled:true}")
    private Boolean enableSwagger;

    @Value("${zaq.webtrace.enabled:true}")
    private Boolean enableWebTrace;

    @Value("${zaq.weblog.enabled:true}")
    private Boolean enableWebLog;
}
