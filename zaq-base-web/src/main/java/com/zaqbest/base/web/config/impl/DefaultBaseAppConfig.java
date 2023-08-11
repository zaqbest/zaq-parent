package com.zaqbest.base.web.config.impl;

import com.zaqbest.base.web.config.BaseAppConfig;
import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@Data
public class DefaultBaseAppConfig extends BaseAppConfig {
}
