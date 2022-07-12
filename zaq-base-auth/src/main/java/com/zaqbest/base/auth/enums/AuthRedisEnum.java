/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.enums;

/**
 * ***************************************************************************
 *创建时间 : 2019年3月27日
 *实现功能 : 定义授权模块Redis使用的key前缀和超时时间
 *作者 : fan
 *版本 : v0.0.1
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2019年3月27日 v0.0.1 fan 创建
 ****************************************************************************
 */
public enum AuthRedisEnum {

	AUTH_TOKEN("AUTH:TOKEN", 7200),
	/**
	 * 验证码,key为uuid
	 */
	AUTH_CAP("AUTH:CAP", 3600),
	
	/**
	 * 短信码,key为手机号
	 */
	AUTH_MSG("AUTH:MSG", 120),
	
	/**
	 * 密码失败尝试次数
	 */
	AUTH_PASSWORD_FAIL_TRY_TIMES("AUTH:PASSWORD_FAIL_TRY_TIMES", 3600*1),
	
	/**
	 * 账户锁定标志
	 */
	AUTH_ACCOUNT_LOCK_FLAG("AUTH:ACCOUNT_LOCK_FLAG", 3600*2),
	;
	
	/**
	 * @param prefix redis存储的前缀
	 * @param timeout reids超时时间,单位为s
	 */
	private AuthRedisEnum(String prefix, Integer timeout) {
		this.prefix = prefix;
		this.timeout = timeout;
	}
	
	private String prefix;
	
	private Integer timeout;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
}
