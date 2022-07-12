/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.domain;

import com.zaqbest.base.auth.enums.TokenSatusEnum;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UserToken implements Serializable{
	
	//token
	private String token;
	
	//用户姓名
	private String userName;
	
	//用户登陆手机号
	private String mobilePhone;
	
	//token状态
	private TokenSatusEnum tokenStatus;
	
	//创建时间
	private Date createDate;
	
	//登录ip
	private String loginIp;
	
	//登录地区
	private String loginArea;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public TokenSatusEnum getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(TokenSatusEnum tokenStatus) {
		this.tokenStatus = tokenStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginArea() {
		return loginArea;
	}

	public void setLoginArea(String loginArea) {
		this.loginArea = loginArea;
	}
	
	

	public UserToken(
			String token, String userName, String mobilePhone, TokenSatusEnum tokenStatus,
			 String loginIp, String loginArea, Date createDate) {
		super();
		this.token = token;
		this.userName = userName;
		this.mobilePhone = mobilePhone;
		this.tokenStatus = tokenStatus;
		this.createDate = createDate;
		this.loginIp = loginIp;
		this.loginArea = loginArea;
	}

	public UserToken() {
		super();
	}
}
