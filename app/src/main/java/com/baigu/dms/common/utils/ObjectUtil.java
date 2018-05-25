package com.baigu.dms.common.utils;

import com.google.gson.GsonBuilder;

/**
 *  Object对象工具
 */
public class ObjectUtil {

	public static String objectToString(Object object) {

		return StringUtils.encodeString(new GsonBuilder().create().toJson(object));
	}

	public static <T> T stringToObject(String str, Class<T> clazz) {

		return new GsonBuilder().create().fromJson(StringUtils.decodeString(str), clazz);
	}
}
