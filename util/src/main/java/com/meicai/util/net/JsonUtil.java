package com.meicai.util.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * Created by meicai on 2015/2/2.
 */
public class JsonUtil {
	public static <T> T json2Bean(String result, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(result, clazz);
	}

}