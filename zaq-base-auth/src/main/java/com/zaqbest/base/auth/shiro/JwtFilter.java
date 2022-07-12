package com.zaqbest.base.auth.shiro;

import cn.hutool.http.HttpStatus;
import com.zaqbest.base.auth.constant.AppConstant;
import com.zaqbest.base.auth.manager.JwtManager;
import com.zaqbest.base.auth.manager.TokenManager;
import com.zaqbest.base.auth.utils.IpUtils;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LogManager.getLogger(getClass());

    private TokenManager tokenManager;

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected boolean isAccessAllowed(
            ServletRequest request, ServletResponse response, Object mappedValue) {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String token = httpServletRequest.getHeader(AppConstant.TOKEN_NAME);
            //通过ip判断Token请求是否合法
            if (StringUtils.isNotBlank(token)) {
                String requestIp = IpUtils.getIpAddr(httpServletRequest);
                String tokenIp = JwtManager.getLoginIp(token);
                executeLogin(request, response);
                logger.info("token中ip为:{}, 访问的ip为:{}", requestIp, tokenIp);
//				if(StringUtils.equals(requestIp, tokenIp)) {
//					executeLogin(request, response);
//				}else {
//					logger.info("token中ip为:{}, 访问的ip为:{}, 访问拒绝", requestIp, tokenIp);
//				}
            }

        } catch (Exception e) {

            logger.error("token解析出现异常,访问拒绝,异常信息为", e);
        }
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)
            throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader(AppConstant.TOKEN_NAME);
        JwtToken jwtToken = new JwtToken(token);
        getSubject(request, response).login(jwtToken);
        tokenManager.refreshTokenExpire(httpServletRequest, httpServletResponse);
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse
                .setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse
                .setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader(
                "Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        return super.preHandle(request, response);
    }
}
