package com.meicai.util.factory;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建fragment工厂
 */
public class FragmentFactory {
	private static ConcurrentHashMap<Class<?>, Fragment> map = new ConcurrentHashMap<>();

	public static Fragment newInstance(Class<?> clazz) {
		try {
			if (map.get(clazz) == null) {
				map.putIfAbsent(clazz, (Fragment) clazz.newInstance());
			}
			return map.get(clazz);
		} catch (Exception e) {
			throw new RuntimeException("Class Not Create Successfully");
		}
	}

	/**
	 * 从Map中获得Fragment 对象
	 * @param clazz
	 * @return
	 */
	public static Fragment getInstance(Class<?> clazz){
		return map.get(clazz);
	}


	/**
	 * 在Activity destroy时候 清除所有fragment
	 */
	public static void  clearAllFragment(){
		if(map != null)
		map.clear();
	}
}
