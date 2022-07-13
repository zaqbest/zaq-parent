/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.shiro;

import com.zaqbest.base.auth.manager.TokenManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ***************************************************************************
 *创建时间 : 2019年3月7日
 *实现功能 : shiro配置
 *作者 : fan
 *版本 : v0.0.1
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2019年3月7日 v0.0.1 fan 创建
 ****************************************************************************
 */

@Configuration
public class ShiroConfig {

	@Autowired
	private TokenManager tokenManager;
	
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		// 安全管理器
		factoryBean.setSecurityManager(securityManager);
		// 默认的登陆访问url
		factoryBean.setLoginUrl("/login");
		// 没有权限跳转的url
		factoryBean.setUnauthorizedUrl("/error/403");

		// 自定义filters
		Map<String, Filter> filters = factoryBean.getFilters();// 获取filters
		JwtFilter jwtFilter = new JwtFilter();
		jwtFilter.setTokenManager(tokenManager);
		filters.put("jwt", jwtFilter);
		factoryBean.setFilters(filters);

		Map<String, String> chains = new LinkedHashMap<>();
		// 不需要认证
		chains.put("/css/**", "anon");
		chains.put("/img/**", "anon");
		chains.put("/js/**", "anon");
		chains.put("/favicon.ico", "anon");

		chains.put("/webjars/**", "anon");
		chains.put("/swagger-resources/**", "anon");
		chains.put("/v2/**", "anon");
		chains.put("/swagger-ui.html", "anon");
		chains.put("/info", "anon");
		chains.put("/login", "anon");
		chains.put("/getVerifyCodeImage", "anon");
		chains.put("/noAuth/**", "anon");
		// 需要认证
		chains.put("/**", "jwt");
		factoryBean.setFilterChainDefinitionMap(chains);

		return factoryBean;
	}

	@Bean
	public SecurityManager securityManager(BaseJwtTokenRealm realm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm);

		// 关闭shiro的session
		DefaultSubjectDAO subjectDao = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
		evaluator.setSessionStorageEnabled(false);
		subjectDao.setSessionStorageEvaluator(evaluator);
		securityManager.setSubjectDAO(subjectDao);

		return securityManager;
	}

	/**
	 * ====================================================================
	 *功 能： 开启shiro注解
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年3月8日 v0.0.1 fan 创建
	====================================================================
	 */
	@Bean
	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
		defaultAAP.setProxyTargetClass(true);
		return defaultAAP;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}
