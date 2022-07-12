/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zaqbest.base.auth.constant.AppConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * ***************************************************************************
 *创建时间 : 2019年3月25日
 *实现功能 : 生成Token和校验Token公函
 *作者 : fan
 *版本 : v0.0.1
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2019年3月25日 v0.0.1 fan 创建
 ****************************************************************************
 */
public class JwtManager {

	private final static Logger logger = LogManager.getLogger(JwtManager.class);
	
	/**
	 * ====================================================================
	 *功 能： 生成Token
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年3月25日 v0.0.1 fan 创建
	====================================================================
	 */
	public static String sign(long userId, String loginName, String ip, String secret, Date createDate, long expTime) {
		
		Date expDate = new Date(System.currentTimeMillis() + expTime);
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create()
				.withClaim(AppConstant.TOKEN_USERID, userId)
				.withClaim(AppConstant.TOKEN_LOGINNAME, loginName)
				.withClaim(AppConstant.TOKEN_IP, ip)
				.withClaim(AppConstant.TOKEN_CREATEDATE, createDate)
				.withExpiresAt(expDate).sign(algorithm);
	}

	/**
	 * ====================================================================
	 *功 能： 生成带orgId的Token
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年5月17日 v0.0.1 fan 创建
	====================================================================
	 */
	public static String sign(long orgId, long userId, String loginName, String ip, String secret, Date createDate, long expTime) {
		
		Date expDate = new Date(System.currentTimeMillis() + expTime);
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create()
				.withClaim(AppConstant.TOKEN_ORG_ID, orgId)
				.withClaim(AppConstant.TOKEN_USERID, userId)
				.withClaim(AppConstant.TOKEN_LOGINNAME, loginName)
				.withClaim(AppConstant.TOKEN_IP, ip)
				.withClaim(AppConstant.TOKEN_CREATEDATE, createDate)
				.withExpiresAt(expDate).sign(algorithm);
	}
	/**
	 * ====================================================================
	 *功 能： 根据Token获取用户名
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年3月7日 v0.0.1 fan 创建
	====================================================================
	 */
	public static String getLoginName(String token) {

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim(AppConstant.TOKEN_LOGINNAME).asString();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			logger.error("jwt  token 解析用户名异常,异常信息为", e);
			return null;
		}
	}

	public static Long getUserId(String token) {

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim(AppConstant.TOKEN_USERID).asLong();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			logger.error("jwt  token 解析用户id异常,异常信息为", e);
			return null;
		}
	}
	
	public static Long getOrgId(String token) {

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim(AppConstant.TOKEN_ORG_ID).asLong();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			logger.error("jwt  token 解析机构id异常,异常信息为", e);
			return null;
		}
	}
	
	public static String getLoginIp(String token) {

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim(AppConstant.TOKEN_IP).asString();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			logger.error("jwt  token 解析登录ip异常,异常信息为", e);
			return null;
		}
	}
	
	public static Date getExpDate(String token) {

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim(PublicClaims.EXPIRES_AT).asDate();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			logger.error("jwt  token 解析失效日期异常,异常信息为", e);
			return null;
		}
	}

	public static Date getCreateDate(String token) {

		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim(AppConstant.TOKEN_CREATEDATE).asDate();
		} catch (JWTDecodeException e) {
			e.printStackTrace();
			logger.error("jwt  token 解析创建日期异常,异常信息为", e);
			return null;
		}
	}
	
	/**
	 * ====================================================================
	 *功 能： Token校验
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年3月7日 v0.0.1 fan 创建
	====================================================================
	 */
	public static boolean verify(String token, long userId, String loginName, String secret) {

		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier jwtVerifier = JWT.require(algorithm).withClaim(AppConstant.TOKEN_USERID, userId).withClaim(AppConstant.TOKEN_LOGINNAME, loginName).build();
			jwtVerifier.verify(token);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
