package collect.meicai.com.collect.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Coder: 何毅
 * Desc : 工具类
 * Date : 2015-05-05
 * Time : 22:35
 * Version:1.0
 */
public class SharedPreferencesUtil {

	private static SharedPreferences sharedPreferences;
	private static String CONFIG = "config";

	public static void saveStringData(Context context,String key,String value){
		if(sharedPreferences == null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static String getStringData(Context context,String key,String defValue){
		if(sharedPreferences == null){
			sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}


}
