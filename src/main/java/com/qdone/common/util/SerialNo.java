package com.qdone.common.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class SerialNo {

	public static synchronized Integer getRomdomID() {
		return Integer.parseInt(RandomStringUtils.randomNumeric(9));
	}

	public static synchronized Long getRomdomLongID() {
		return Long.valueOf(RandomStringUtils.randomNumeric(11));
	}

	public static synchronized String getUNID() {
		String currentTime = DateUtil.getCurrentDateString("yyMMddHHmmssSSS");
		return (new StringBuilder(String.valueOf(currentTime))).append(RandomStringUtils.randomNumeric(5)).toString();
	}

	public static synchronized String getSmallUNID(String code) {
		String currentTime = "";

		if (StringUtils.isBlank(code) || code.length() < 4) {
			currentTime = DateUtil.getCurrentDateString("yyMMddHHmmssSSS");
		} else {
			try {
				// 取消掉除了数字以外的其他情况
				String number = Integer.valueOf(code).toString();
				currentTime = number.substring(0, 4) + DateUtil.getCurrentDateString("yyHHmmssSSS");
			} catch (Exception e) {
				currentTime = DateUtil.getCurrentDateString("yyMMddHHmmssSSS");
			}

		}
		return (new StringBuilder(String.valueOf(currentTime))).append(RandomStringUtils.randomNumeric(4)).toString();

	}

}
