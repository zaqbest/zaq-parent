package com.zaqbest.base.web.autoconfiguration.impl;

import com.zaqbest.base.web.autoconfiguration.properties.BaseAppConfig;
import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@Data
public class DefaultBaseAppConfig extends BaseAppConfig {
}
