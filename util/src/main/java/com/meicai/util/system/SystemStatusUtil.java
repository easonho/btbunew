package com.meicai.util.system;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by qzf on 2015/2/6.
 */
public class SystemStatusUtil {
	/**
	 * 校验某个服务是否正在运行
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String packageName) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(100);
		for (ActivityManager.RunningServiceInfo service : list) {
			String serviceName = service.service.getClassName();
			if (serviceName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验某个App是否正在运行
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppRunning(Context context,String packageName){
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}
}
