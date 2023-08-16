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

    @Value("${zaq.webTrace.enabled:true}")
    private Boolean enableWebTrace;

    @Value("${zaq.webLog.enabled:true}")
    private Boolean enableWebLog;

    @Value("${zaq.loadBalance.enabled:true}")
    private Boolean enableLoadBalance;
}
