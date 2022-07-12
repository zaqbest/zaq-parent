/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.constant;

/**
 * ***************************************************************************
 *创建时间 : 2019年4月9日
 *实现功能 : 权限模块常量
 *作者 : micha
 *版本 : v0.0.1
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2019年4月9日 v0.0.1 micha 创建
 ****************************************************************************
 */
public class AppConstant {

	/**
	 * 密码失效天数
	 */
	public static String PASSWORD_EXPIRE_DAYS = "PASSWORD_EXPIRE_DAYS";

	/**
	 * 密码失败尝试次数
	 */
	public static String PASSWORD_FAIL_TRY_TIMES = "PASSWORD_FAIL_TRY_TIMES";
	
	/**
	 * 登录失败锁定时间
	 */
	public static String PASSWORD_FAIL_LOCK_TIMES = "PASSWORD_FAIL_LOCK_TIMES";
	
	/**
	 * TOKEN超时时间,毫秒
	 */
	public final static long TOKEN_EXP_TIME = 1 * 3600 * 1000;
	
	/**
	 * TOKEN刷新时间,毫秒
	 */
	public final static long TOKEN_REFRESH_TIME = 1800 * 1000;

	/**
	 * jwt token中的loginName
	 */
	public static final String TOKEN_LOGINNAME = "loginName";

	/**
	 * jwt token中的userId
	 */
	public static final String TOKEN_USERID = "userId";

	/**
	 * jwt token中的登陆ip
	 */
	public static final String TOKEN_IP = "loginIp";
	
	/**
	 * token创建时间
	 */
	public static final String TOKEN_CREATEDATE = "createDate";
	
	/**
	 * token的变量名
	 */
	public static final String TOKEN_NAME = "authorization";

	public static final String TOKEN_ORG_ID = "OrgId";
	
	/**
	 * 默认权限
	 */
	public static final String DEFAULT_AUTH = "default";

	/**
	 * 构建redis key的分隔符
	 */
	public final static String REDIS_SEPARATOR = ":";
}
