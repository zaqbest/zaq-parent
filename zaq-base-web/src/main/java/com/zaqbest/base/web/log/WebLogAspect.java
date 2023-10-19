package com.zaqbest.base.web.log;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.zaqbest.base.comm.utils.MapperUtils;
import com.zaqbest.base.web.log.annotation.WebLog;
import com.zaqbest.base.web.log.dto.WebLogDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Aspect
@Component
@Slf4j
@ConditionalOnMissingBean(WebLogAspect.class)
public class WebLogAspect{

    @Around(value = "@annotation(webLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, WebLog webLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息(通过Logstash传入Elasticsearch)
        WebLogDto webLogDto = new WebLogDto();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            webLogDto.setDescription(log.value());
        }
        long endTime = System.currentTimeMillis();
        String urlStr = request.getRequestURL().toString();
        webLogDto.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
        webLogDto.setUsername(request.getRemoteUser());
        webLogDto.setIp(request.getRemoteAddr());
        webLogDto.setMethod(request.getMethod());
        webLogDto.setParameter(getParameter(method, joinPoint.getArgs()));

        webLogDto.setSpendTime((int) (endTime - startTime));
        webLogDto.setStartTime(new Date(startTime));
        webLogDto.setUri(request.getRequestURI());
        webLogDto.setUrl(request.getRequestURL().toString());

        log.info("-------------<<<<<< begin execute {}", MapperUtils.obj2json(webLogDto));

        Object result = null;
        Throwable throwable = null;
        try {
            result = joinPoint.proceed();

            webLogDto.setResult(result);
        } catch (Throwable t){
            throwable = t;
            webLogDto.setException(t.getMessage());
        }

        log.info("------------->>>>>> end execute {}", MapperUtils.obj2json(webLogDto));

        if (throwable != null)
            throw throwable;

        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
