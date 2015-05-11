package collect.meicai.com.collect.app.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import java.util.Random;

/**
 * 工具杂类集合
 */
public class OtherUtils {

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	/**
	 * 获取非Null字符串，若为Null则新建一串空字符串返回
	 * @param content 应用上下文
	 * @return 字符串
	 *
	 * @author何毅
	 */
	public static String getNotNullString(String content) {
		return content == null ? "" : content;
	}

	/**
	 * 获取随机数
	 *
	 * @return
	 */
	public static String getRandomNumber() {
		String s = "";
		int intCount = 0;
		intCount = (new Random()).nextInt(9999);// 随机产生一个大于等于0小于9999的数
		// ------即包含0不包含9999
		if (intCount < 1000)
			intCount += 1000;
		s = intCount + "";
		return s;
	}
}
