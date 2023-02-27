package com.wanglj.lmm.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户设备类型
 *
 */
@Getter
@AllArgsConstructor
public enum DeviceType {

	/**
	 * pc端
	 */
	PC("PC");

	private final String device;

}
