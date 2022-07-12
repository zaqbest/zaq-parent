/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.manager;

import cn.hutool.core.util.StrUtil;
import com.zaqbest.base.auth.constant.AppConstant;
import com.zaqbest.base.auth.domain.User;
import com.zaqbest.base.auth.domain.UserToken;
import com.zaqbest.base.auth.enums.AuthRedisEnum;
import com.zaqbest.base.auth.enums.TokenSatusEnum;
import com.zaqbest.base.auth.service.CacheService;
import com.zaqbest.base.auth.service.UserFacade;
import com.zaqbest.base.comm.utils.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************************
 *创建时间 : 2019年4月11日
 *实现功能 : token缓存、刷新和失效处理
 *作者 : fan
 *版本 : v0.0.1
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2019年4月11日 v0.0.1 fan 创建
 ****************************************************************************
 */
@Component
@Slf4j
public class TokenManager {

	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private UserFacade userFacade;
	
	@Autowired
	private CacheService cacheService;

	/**
	 * ====================================================================
	 *功 能： 刷新即将过期的token
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年5月29日 v0.0.1 fan 创建
	====================================================================
	 */
	public void  refreshTokenExpire(HttpServletRequest request, HttpServletResponse response) {
		
		String token = request.getHeader(AppConstant.TOKEN_NAME);
		
		if(StrUtil.isBlank(token)) {
			Exceptions.throwBusinessException("请求参数有误");
		}
		String newToken = null;

		Date expDate = JwtManager.getExpDate(token);
		Long expTime = (expDate.getTime() - Calendar.getInstance().getTime().getTime());
		//判断是否即将失效
		if(expTime.compareTo(0L)> 0
				&& expTime.compareTo(AppConstant.TOKEN_REFRESH_TIME) < 0) {
			
			Long userId = JwtManager.getUserId(token);
			Long orgId = JwtManager.getOrgId(token);

			newToken = refreshToken(userId, token);

			response.setHeader(AppConstant.TOKEN_NAME, newToken);
			response.setHeader("Access-Control-Expose-Headers", AppConstant.TOKEN_NAME);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String refreshToken(Long userId,  String oldToken) {

		if(userId == null
				|| StrUtil.isBlank(oldToken)) {
			Exceptions.throwBusinessException("请求参数有误");
		}

		String loginName = JwtManager.getLoginName(oldToken);
		Map<String, UserToken> tokenMap =  (Map<String, UserToken>) cacheService.get(loginName, AuthRedisEnum.AUTH_TOKEN.getPrefix());
		UserToken oriUserToken = tokenMap.get(oldToken);
		
		String loginIp = oriUserToken.getLoginIp();
		String loginArea = oriUserToken.getLoginArea();
		String userName = oriUserToken.getUserName();
		
		//将原token设置为失效
//		disableToken(oldToken, TokenSatusEnum.LOGOUT);
		User user = userFacade.selectById(userId);
		Date currDate = new Date();
		String token = JwtManager.sign(userId, user.getLoginName(), loginIp, user.getPassword(), currDate, AppConstant.TOKEN_EXP_TIME);
		
		//缓存新的token
		cacheToken(token, user.getMobilePhone(), userName, currDate, loginIp, loginArea, TokenSatusEnum.CLEANING);
		return token;
	}
	
	/**
	 * ====================================================================
	 *功 能： token刷新，从旧token中获取ip
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年4月18日 v0.0.1 fan 创建
	====================================================================
	 */
	@SuppressWarnings("unchecked")
	public String refreshToken(Long userId, Long orgId, String oldToken) {

		if(userId == null
				|| orgId == null
				|| StrUtil.isBlank(oldToken)) {
			Exceptions.throwBusinessException("请求参数有误");
		}

		String loginName = JwtManager.getLoginName(oldToken);
		Map<String, UserToken> tokenMap =  (Map<String, UserToken>) cacheService.get(loginName, AuthRedisEnum.AUTH_TOKEN.getPrefix());
		UserToken oriUserToken = tokenMap.get(oldToken);
		
		String loginIp = oriUserToken.getLoginIp();
		String loginArea = oriUserToken.getLoginArea();
		String userName = oriUserToken.getUserName();
		
		//将原token设置为失效
//		disableToken(oldToken, TokenSatusEnum.LOGOUT);
		User user = userFacade.selectById(userId);
		Date currDate = new Date();
		String token = JwtManager.sign(orgId, userId, user.getLoginName(), loginIp, user.getPassword(), currDate, AppConstant.TOKEN_EXP_TIME);
		
		//缓存新的token
		cacheToken(token, user.getMobilePhone(), userName, currDate, loginIp, loginArea, TokenSatusEnum.CLEANING);
		return token;
	}
	
	/**
	 * ====================================================================
	 *功 能： token缓存处理，客户新登陆后将已缓存Login状态的token状态设置为指定状态
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年4月11日 v0.0.1 fan 创建
	====================================================================
	 */
	@SuppressWarnings("unchecked")
	public void cacheToken(String token, String mobilePhone, String userName, Date loginDate, String loginIp, String loginArea, TokenSatusEnum oriTokenStatus) {
		
	//构建userToken
	UserToken userToken = new UserToken(token, userName, mobilePhone, TokenSatusEnum.LOGIN, loginIp, loginArea, loginDate);
	Map<String, UserToken> tokenMap = null;
	//缓存token
	Object val = cacheService.get(mobilePhone, AuthRedisEnum.AUTH_TOKEN.getPrefix());
	if(val == null) {
		//存放token对应的用户信息
		tokenMap = new HashMap<String, UserToken>();
		tokenMap.put(token, userToken);
		
	}else {
		tokenMap =  (Map<String, UserToken>) val;
		//将其它token设置为退出状态
		for(String key : tokenMap.keySet()) {
			UserToken tokenVal = tokenMap.get(key);
			if(tokenVal.getTokenStatus() == TokenSatusEnum.LOGIN) {
				tokenVal.setTokenStatus(oriTokenStatus);
				tokenMap.put(key, tokenVal);
			}
		}
		tokenMap.put(token, userToken);
	}
	cacheService.set(mobilePhone, tokenMap, AuthRedisEnum.AUTH_TOKEN.getPrefix(), Long.valueOf(AuthRedisEnum.AUTH_TOKEN.getTimeout()));
}
	
	/**
	 * ====================================================================
	 *功 能： 设置缓存状态为LOGOUT或者CLAERUP，在token校验时 状态为 LOGOUT或者CLAERUP被校验不通过
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年4月15日 v0.0.1 fan 创建
	====================================================================
	 */
	@SuppressWarnings("unchecked")
	public void disableToken(String token, TokenSatusEnum status) {
		String userName = JwtManager.getLoginName(token);
		logger.info("Token为:{}, 手机号为:{} 的用户准备退出", token, userName);
		Object val = cacheService.get(userName, AuthRedisEnum.AUTH_TOKEN.getPrefix());
		if(val == null) {
			logger.info("Token为:{} 无效token", token);
			return;
		}
		Map<String, UserToken> tokenMap = (Map<String, UserToken>) val;
		logger.info("Token为:{}, 缓存中内容为:{}", token, tokenMap);
		UserToken userToken = tokenMap.get(token);
		if(userToken == null) {
			logger.info("Token为:{} 缓存内容为空", token);
			return;
		}
		if(userToken.getTokenStatus() == status) {
			logger.info("Token为:{} 状态已经是{}", token, status);
			return;
		}
		userToken.setTokenStatus(status);
		tokenMap.put(token, userToken);
		cacheService.set(userName, tokenMap, AuthRedisEnum.AUTH_TOKEN.getPrefix(), Long.valueOf(AuthRedisEnum.AUTH_TOKEN.getTimeout()));
	}
	
	/**
	 * ====================================================================
	 *功 能： 根据手机号将用户登出，用于用户禁用和删除功能，保证用户在被禁用和删除后立即登出,该操作指定后
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年5月16日 v0.0.1 fan 创建
	====================================================================
	 */
	@SuppressWarnings("unchecked")
	public void disableTokenByMobilePhoneAndOrgId(String mobilePhone, Long orgId) {
		
		logger.info("准备将手机号为{}的用户设置为登出", mobilePhone);
		Object val = cacheService.get(mobilePhone, AuthRedisEnum.AUTH_TOKEN.getPrefix());
		if(val == null) {
			logger.info("手机号为:{} 的用户未登录", mobilePhone);
			return;
		}
		Map<String, UserToken> tokenMap = (Map<String, UserToken>) val;
		for(String key : tokenMap.keySet()) {
			
			UserToken token = tokenMap.get(key);
			Long orgIdInToken = JwtManager.getOrgId(token.getToken());
			if(orgId.equals(orgIdInToken)) {
				token.setTokenStatus(TokenSatusEnum.LOGOUT);
				tokenMap.put(key, token);
			}
		}
		//刷新缓存
		cacheService.set(mobilePhone, tokenMap, AuthRedisEnum.AUTH_TOKEN.getPrefix(), Long.valueOf(AuthRedisEnum.AUTH_TOKEN.getTimeout()));
	}
}
