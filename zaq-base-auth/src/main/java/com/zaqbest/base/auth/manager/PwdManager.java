/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.auth.manager;


import cn.hutool.core.util.StrUtil;

/**
 * ***************************************************************************
 *创建时间 : 2019年4月10日
 *实现功能 : 密码加密和校验工具
 *作者 : fan
 *版本 : v0.0.1
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2019年4月10日 v0.0.1 fan 创建
 ****************************************************************************
 */
public class PwdManager {

	/**
	 * ====================================================================
	 *功 能： 验证密码是否匹配
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2019年3月26日 v0.0.1 fan 创建
	====================================================================
	 */
	public static boolean checkUserPwd(String pwd, String salt, String pwdFromdb) {

		return StrUtil.equals(entryptPassword(pwd, salt), pwdFromdb);
	}

	public static String entryptPassword(String password, String saltstr) {
		return StrUtil.format("{}|{}", saltstr, password);
	}

}
