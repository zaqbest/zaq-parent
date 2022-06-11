package com.zaqbest.security.component;

import cn.hutool.core.util.StrUtil;
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

/**
 * AppAccessToken授权过滤器
 * Created by macro on 2018/4/26.
 */
public class AppAccessTokenFilter  extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    @Qualifier("userDetailsServiceForApp")
    private UserDetailsService userDetailsServiceForApp;

    @Value("${auth.appKey}")
    private String appKey;

    @Value("${auth.appSecret}")
    private String appSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String appKey = request.getHeader(this.appKey);
        String appSecret = request.getHeader(this.appSecret);
        LOGGER.info("开始检查appKey, appKey={}, appSecret={}", appKey, appSecret);

        if (StrUtil.isNotEmpty(appKey) && StrUtil.isNotEmpty(appSecret)
            && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsServiceForApp.loadUserByUsername(appKey);

            if (userDetails != null){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                LOGGER.info("authenticated appKey:{}", appKey);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
