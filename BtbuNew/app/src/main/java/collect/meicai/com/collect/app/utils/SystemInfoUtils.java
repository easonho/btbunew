package collect.meicai.com.collect.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.android.volley.VolleyLog;

import org.apache.http.conn.util.InetAddressUtils;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static collect.meicai.com.collect.app.utils.OtherUtils.getNotNullString;


/**
 * 系统信息工具类
 * 
 * @author 何毅
 */
public class SystemInfoUtils {
	/**
	 * 收集设备参数信息
	 * @param context 程序上下文对象
	 * @return 存储了设备信息和异常信息的Map
	 *
	 * @author 何毅
	 */
	public static Map<String, String> collectDeviceInfo(Context context) {
		Map<String, String> infos = new HashMap<String, String>();
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = getNotNullString(pi.versionName);
				String versionCode = Integer.toString(pi.versionCode);
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (PackageManager.NameNotFoundException e) {
			VolleyLog.e("获取设备信息时发生错误：" + e.getMessage());
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				VolleyLog.e(field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				VolleyLog.e("获取设备信息时发生错误：" + e.getMessage());
			}
		}
		return infos;
	}

	/**
	 * 获取设备硬件序列号（自2.3以上推荐用此方法作为唯一标识。只有数字字母，并且不区分大小写。）
	 * @return 设备硬件序列号
	 *
	 * @author 何毅
	 */
	public static String getSerial() {
		return getNotNullString(Build.SERIAL);
	}

	/**
	 * 获取设备唯一标识(GSM获得IMEI，CDMA获得MEID或者ESN，若没手机卡则无法获得)
	 * 注意：使用此函数请申请READ_PHONE_STATE权限
	 * @return 设备唯一标识
	 *
	 * @author 何毅
	 */
	public static String getDeviceId(Context context) {
		String DeviceId = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null)
			DeviceId = getNotNullString(telephonyManager.getDeviceId());
		return DeviceId;
	}

	/**
	 * 获取设备品牌
	 * @return 设备品牌
	 *
	 * @author 何毅
	 */
	public static String getBrand() {
		return getNotNullString(Build.BRAND);
	}

	/**
	 * 获取设备模组号（即一般来说的型号）
	 * @return 设备模组号
	 *
	 * @author 何毅
	 */
	public static String getModel() {
		return getNotNullString(Build.MODEL);
	}

	/**
	 * 获取系统版本号
	 * @return 系统版本号
	 *
	 * @author 何毅
	 */
	public static String getSDKInt() {
		return Integer.toString(Build.VERSION.SDK_INT);
	}

	/**
	 * 获取网内IPv4
	 * @return IPv4字符串
	 *
	 * @author 何毅
	 */
	private final static String ip_default = "0.0.0.0"; // 默认ip内容
	public static String getLocalIpAddressV4() {
		String ip = ip_default;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
						ip = inetAddress.getHostAddress();
						return ip == null ? ip_default : ip;
					}
				}
			}
		} catch (SocketException e) {
			VolleyLog.e("获取IP时发生错误：" + e.getMessage());
		}
		return ip;
	}
	/**
	 * 获取手机号（前提是如果手机号被写在SIM卡上的话。这种情况很少。） 注意：使用此函数请申请READ_PHONE_STATE权限
	 * @param context 程序上下文对象
	 * @return 手机号
	 *
	 * @author 何毅
	 */
	public static String getPhoneNumber(Context context) {
		String PhoneNumber = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null)
			PhoneNumber = getNotNullString(telephonyManager.getLine1Number());
		return PhoneNumber;
	}

	/**
	 * 获取SIM卡运营商 注意：使用此函数请申请READ_PHONE_STATE权限
	 * @param context 程序上下文对象
	 * @return SIM卡运营商
	 *
	 * @author 何毅
	 */
	public static String getSimOperatorName(Context context) {
		String getSimOperatorName = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null)
			getSimOperatorName = getNotNullString(telephonyManager
					.getSimOperatorName());
		return getSimOperatorName;
	}

	/**
	 * 获取网络类型
	 * @param context 程序上下文对象
	 * @return 网络类型
	 *
	 * @author 何毅
	 */
	public static String getNetConnectType(Context context) {
		String type = "";
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();

			/* 如果网络为WIFI的话，返回类型字符串（字符串WIFI）。如果为手机网络的话，返回具体的网络类型字符串。 */
			if (networkInfo != null)
				type = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
						? getNotNullString(networkInfo.getSubtypeName())
						: getNotNullString(networkInfo.getTypeName());
		}
		return type;
	}

	/**
	 * 获取SIM卡状态
	 * @param context 程序上下文对象
	 * @return SIM卡状态：
	 *         SIM_STATE_UNKNOWN = 0 SIM卡状态：不可知。该状态意味着SIM卡处于两种状态的转换过程之中。
	 *         例如当SIM卡在PIN_REQUIRED的情况下，用户输入了PIN码，那么在SIM卡变为SIM_STATE_READY之前都会返回这个状态。
	 *         SIM_STATE_ABSENT = 1 SIM卡状态：设备中没有可用的SIM卡。
	 *         SIM_STATE_PIN_REQUIRED = 2 SIM卡状态：锁定中。需要SIM卡的PIN码来解锁。
	 *         SIM_STATE_PUK_REQUIRED = 3 SIM卡状态：锁定中。需要SIM卡的PUK码来解锁。
	 *         SIM_STATE_NETWORK_LOCKED = 4 SIM卡状态：锁定中。需要网络PIN码来解锁。
	 *         SIM_STATE_READY = 5 SIM卡状态：正常。
	 *
	 * @author 何毅
	 */
	public static int getSimState(Context context) {
		int state = -1;
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null)
			state = telephonyManager.getSimState();
		return state;
	}

	/**
	 * 获取手机类型
	 * @param context 程序上下文
	 * @return 手机类型：
	 * 		   PHONE_TYPE_NONE = 0
	 * 		   PHONE_TYPE_GSM = 1
	 * 		   PHONE_TYPE_CDMA = 2
	 * 		   PHONE_TYPE_SIP = 3
	 *
	 * @author 何毅
	 */
	public static int getPhoneType(Context context) {
		int type = -1;
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null)
			type = telephonyManager.getPhoneType();
		return type;
	}

	/**
	 * 手机是否处于漫游状态
	 * @param context 程序上下文
	 * @return 是否漫游
	 */
	public static boolean isNetworkRoaming(Context context) {
		boolean isNetworkRoaming = false;
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null)
			isNetworkRoaming = telephonyManager.isNetworkRoaming();
		return isNetworkRoaming;
	}

	/***
	 * 获取当前日期
	 *
	 * @param
	 * @return
	 */
	public static String geDate() {
		String date = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");// 转换格式为 年月日
		date = format.format(new Date(System.currentTimeMillis()));// //格式化语言环境敏感的信息
		// (当前的时间)并转为String类型
		return date;

	}

	/***
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String geTime() {
		String time = "";
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");// 转换格式为 时分秒
		time = format.format(new Date(System.currentTimeMillis()));// //格式化语言环境敏感的信息
		// (当前的时间)并转为String类型
		return time;

	}

	/***
	 * 获取当前日期+时间
	 *
	 * @return
	 */
	public static String geDateTime() {
		String datetime = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 转换格式为
		// 年月日时分秒
		datetime = format.format(new Date(System.currentTimeMillis()));// 格式化语言环境敏感的信息
		// (当前的时间)并转为String类型
		return datetime;

	}

	/**
	 * 获取网络连接状态 NET_NO：没有网络 NET_2G:2g网络 NET_3G：3g网络 NET_4G：4g网络 NET_WIFI：wifi
	 * NET_UNKNOWN：未知网络
	 *
	 * @param context
	 * @return 状态码
	 */
	public static String getNetWorkType(Context context) {
		String stateCode = "NET_NO";
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnectedOrConnecting()) {
			switch (ni.getType()) {

				case ConnectivityManager.TYPE_WIFI :
					stateCode = "NET_WIFI";
					VolleyLog.v("当前网络为WIFI网络");
					break;
				case ConnectivityManager.TYPE_MOBILE :
					switch (ni.getSubtype()) {
						case TelephonyManager.NETWORK_TYPE_GPRS : // 联通2g
						case TelephonyManager.NETWORK_TYPE_CDMA : // 电信2g
						case TelephonyManager.NETWORK_TYPE_EDGE : // 移动2g
						case TelephonyManager.NETWORK_TYPE_1xRTT :
						case TelephonyManager.NETWORK_TYPE_IDEN :
							stateCode = "NET_2G";
							VolleyLog.v("当前网络为2G网络");
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_A : // 电信3g
						case TelephonyManager.NETWORK_TYPE_UMTS :
						case TelephonyManager.NETWORK_TYPE_EVDO_0 :
						case TelephonyManager.NETWORK_TYPE_HSDPA :
						case TelephonyManager.NETWORK_TYPE_HSUPA :
						case TelephonyManager.NETWORK_TYPE_HSPA :
						case TelephonyManager.NETWORK_TYPE_EVDO_B :
						case TelephonyManager.NETWORK_TYPE_EHRPD :
						case TelephonyManager.NETWORK_TYPE_HSPAP :
							stateCode = "NET_3G";
							VolleyLog.v("当前网络为3G网络");
							break;
						case TelephonyManager.NETWORK_TYPE_LTE :
							stateCode = "NET_4G";
							VolleyLog.v("当前网络为4G网络");
							break;
						default :
							stateCode = "NET_UNKNOWN";
							VolleyLog.v("当前网络未知");
					}
					break;
				default :
					stateCode = "NET_UNKNOWN";
					VolleyLog.v("当前网络未知");
			}

		} else {
			stateCode = "NET_NO";
			VolleyLog.v("网络未连接");
		}
		return stateCode;
	}

	/**
	 * 获取网络是否连接 isNetworkAvailable(Context context)，context中传入上下文对象 返回值为
	 * boolean类型 1）true：网络已连接 2）false 网络未连接 需添加权限
	 * <uses-permissionandroid:name="android.permission.ACCESS_NETWORK_STATE"/>
	 *
	 */

	public static boolean isNetworkAvailable(Context context) {
		boolean available = false;
		if (null != context) {
			// ConnectivityManager主要管理和网络连接相关的操作。
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (null != networkInfo) {
				// 获取当前的网络连接是否可用
				available = networkInfo.isAvailable();
				if (available) {
					VolleyLog.v("当前的网络连接可用");
				} else {
					VolleyLog.v("当前的网络连接不可用");
				}
			} else {
				VolleyLog.v("上下文对象不能为空");
			}
		} else {
			return available = false;
		}
		return available;
	}
}
