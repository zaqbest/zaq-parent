package com.zaqbest.base.web.autoconfiguration.impl;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.zaqbest.base.web.autoconfiguration.properties.BaseSwaggerConfig;
import com.zaqbest.base.web.autoconfiguration.properties.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.Arrays;

@Configuration
@EnableOpenApi
@EnableKnife4j
public class DefaultBaseSwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        SwaggerProperties swaggerProperties =
                SwaggerProperties.builder()
                        .title("title-placeholder")
                        .description("desc-placeholder")
                        .contactUrl("http://www.zaqbest.com")
                        .version("v1.0.0")
                        .contactEmail("lipan.me@qq.com")
                        .apiBasePackageList(Arrays.asList("com.zaqbest"))
                        .build();
        return swaggerProperties;
    }
}
