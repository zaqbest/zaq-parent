package com.zaqbest.base.web.autoconfiguration.impl;

import com.zaqbest.base.web.exception.BaseGlobalExceptionHandler;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Conditional(RestControllerAdviceCondition.class)
public class DefaultBaseGlobalExceptionHandler extends BaseGlobalExceptionHandler {
}

class RestControllerAdviceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getBeanFactory().getBeansOfType(RestControllerAdvice.class).size() == 0;
    }

}
