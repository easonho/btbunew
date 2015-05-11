package collect.meicai.com.collect.app.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Coder: 何毅
 * Desc : 显示对话框
 * Date : 2015-04-18
 * Time : 16:07
 * Version:1.0
 */
public class ToastUtil {

	public static  void show(Activity context,String content){
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
}
