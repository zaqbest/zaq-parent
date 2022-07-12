/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.shiro;

import cn.hutool.core.collection.CollUtil;
import com.zaqbest.base.auth.constant.AppConstant;
import com.zaqbest.base.auth.domain.User;
import com.zaqbest.base.auth.domain.UserToken;
import com.zaqbest.base.auth.enums.AuthRedisEnum;
import com.zaqbest.base.auth.enums.TokenSatusEnum;
import com.zaqbest.base.auth.manager.JwtManager;
import com.zaqbest.base.auth.service.CacheService;
import com.zaqbest.base.auth.service.UserFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 自定义Realm
 */
public abstract class BaseUserRealm extends AuthorizingRealm {

	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	@Lazy
	private UserFacade userFacade;

	@Autowired
	private CacheService redisClient;
	
	@Override
	public boolean supports(AuthenticationToken token) {

		return token instanceof JwtToken;
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		Long userId = JwtManager.getUserId(principals.toString());

		SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();

		Set<String> permSet = new HashSet<>();
		// 设置默认权限
		permSet.add(AppConstant.DEFAULT_AUTH);

		List<String> authList = getAuthList(userId);
		if (CollUtil.isNotEmpty(authList)) {
			permSet.addAll(authList);
		}
		
		authInfo.setStringPermissions(permSet);
		return authInfo;
	}

	protected abstract List<String> getAuthList(Long userId);

	/**
	 * 认证
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
		
		if(authToken.getCredentials() == null) {
			
			logger.info("token凭证为空");
			throw new AuthenticationException("无效凭证");
		}
		
		String token = authToken.getCredentials().toString();
		//从Token中获取用户名信息
		String userName = JwtManager.getLoginName(token);
		Long userId = JwtManager.getUserId(token);
		
		//检查token中取出的参数是否有效
		if (userName == null
				|| userId == null) {
			
			logger.info("token:{} 无效", token);
			throw new AuthenticationException("无效Token");
		}

		//检查用户是否存在
		User user =  userFacade.selectById(userId);
		if (user == null) {
			logger.info("token:{} 对应的用户不存在", token);
			throw new AuthenticationException("用户不存在");
		}

		//Token认证
		if (!JwtManager.verify(token, user.getId(), userName, user.getPassword())) {
			
			logger.info("token:{} 认证不通过", token);
			throw new AuthenticationException("Token认证不通过");
		}
		
		Map<String, UserToken> tokenMap = (Map<String, UserToken>) redisClient.get(userName, AuthRedisEnum.AUTH_TOKEN.getPrefix());
		if(tokenMap != null) {
			//检查token状态是否为LOGIN或者CLEANING
			UserToken userToken = tokenMap.get(token);
			if(userToken.getTokenStatus() != TokenSatusEnum.LOGIN
					&&  userToken.getTokenStatus() != TokenSatusEnum.CLEANING) {
				
				logger.info("token:{} 已失效, token状态为:{}", token, userToken.getTokenStatus());
				throw new AuthenticationException("Token已失效");
			}
			
		}else {
			logger.info("手机号:{} 未获取到缓存内容", userName);
		}
		return new SimpleAuthenticationInfo(token, token, getName());
	}

}
