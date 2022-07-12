/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.shiro;

import org.apache.shiro.authc.AuthenticationToken;

@SuppressWarnings("serial")
public class JwtToken implements AuthenticationToken{
	
	private String token;

	public JwtToken(String token) {
		this.token = token;
	}
	
	@Override
	public Object getPrincipal() {
		
		return token;
	}

	@Override
	public Object getCredentials() {
	
		return token;
	}

}
