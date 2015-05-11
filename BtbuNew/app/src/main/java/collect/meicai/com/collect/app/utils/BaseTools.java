package collect.meicai.com.collect.app.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Coder: 何毅
 * Desc : 得道屏幕宽度
 * Date : 2015-04-04
 * Time : 14:48
 * Version:1.0
 */
public class BaseTools {


	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
