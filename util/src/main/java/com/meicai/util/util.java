package com.meicai.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by siyun on 2015/2/11.
 */
public class util {
    public static  String getString(InputStream is) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

	public static void HttpTest(final Context mActivity) {
		if (!isNetworkAvailable(mActivity)) {
			AlertDialog.Builder builders = new AlertDialog.Builder(mActivity);
			builders.setTitle("抱歉，网络连接失败，是否进行网络设置？");
			builders.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 进入无线网络配置界面
							mActivity.startActivity(new Intent(
									Settings.ACTION_WIRELESS_SETTINGS));
							// startActivity(new
							// Intent(Settings.ACTION_WIFI_SETTINGS));
							// //进入手机中的wifi网络设置界面
						}
					});
			builders.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
											int whichButton) {
							// 关闭当前activity
							((Activity)mActivity).finish();
						}
					});
			builders.show();
		}

	}

	// 检测网络是否可用
	public static boolean isNetworkAvailable(Context mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getChinaNum(String str) {
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			count++;
		}
		return count;
	}

}
