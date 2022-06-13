package com.zaqbest.security.component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zaqbest.comm.exception.Asserts;
import com.zaqbest.comm.utils.RequestUtil;
import com.zaqbest.security.domain.ZaqUserDetails;
import com.zaqbest.security.util.AppSecretUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * AppAccessToken授权过滤器
 * Created by macro on 2018/4/26.
 */
public class AppAccessTokenFilter  extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    @Qualifier("userDetailsServiceForApp")
    private UserDetailsService userDetailsServiceForApp;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String appKey = RequestUtil.getAppKey(request);
        String appSecret = RequestUtil.getAppSecret(request);
        LOGGER.info("开始检查appKey, appKey={}, appSecret={}", appKey, appSecret);
        if (StrUtil.isNotEmpty(appKey) && StrUtil.isNotEmpty(appSecret)
            && SecurityContextHolder.getContext().getAuthentication() == null){
            ZaqUserDetails userDetails = (ZaqUserDetails) userDetailsServiceForApp.loadUserByUsername(appKey);

            if (userDetails != null){
                if (userDetails.getExpiryTime() != null && new Date().after(userDetails.getExpiryTime())){
                    Asserts.fail("appKey已经过期，请重新申请");
                }
                AppSecretUtil.validateSecret(userDetails.getAppSecret(), appSecret);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                LOGGER.info("authenticated appKey:{}", appKey);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
