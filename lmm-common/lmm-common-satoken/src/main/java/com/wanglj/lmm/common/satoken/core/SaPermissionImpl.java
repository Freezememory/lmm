package com.wanglj.lmm.common.satoken.core;

import cn.dev33.satoken.stp.StpInterface;
import com.wanglj.lmm.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * sa-token 权限管理实现类
 */
@Component
public class SaPermissionImpl implements StpInterface {

	/**
	 * 获取菜单权限列表
	 */
	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		return LoginHelper.getLoginUser().getMenuPermissions();
	}

	/**
	 * 获取角色权限列表
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		return LoginHelper.getLoginUser().getRoles();
	}

}
