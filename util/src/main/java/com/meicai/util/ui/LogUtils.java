package com.meicai.util.ui;

import android.util.Log;

/**
 * Created by qzf on 2015/2/26.
 */
public class LogUtils {

	private static final String TAG = "meicai";

	private static final int ERROR_LEVEL = 5;
	private static final int WARN_LEVEL = 4;
	private static final int INFO_LEVEL = 3;
	private static final int DEBUG_LEVEL = 2;
	private static final int VERB_LEVEL = 1;

	private static int level = 0;

	public static void e(String log) {
		if (level <= ERROR_LEVEL) {
			Log.e(TAG, log);
		}
	}

	public static void e(String tag, String log) {
		if (level <= ERROR_LEVEL) {
			Log.e(tag, log);
		}
	}

	public static void e(Throwable e) {
		if (level <= ERROR_LEVEL) {
			e.printStackTrace();
		}
	}

	public static void w(String log) {
		if (level <= WARN_LEVEL) {
			Log.w(TAG, log);
		}
	}

	public static void w(String tag, String log) {
		if (level <= WARN_LEVEL) {
			Log.w(tag, log);
		}
	}

	public static void w(Throwable e) {
		if (level <= WARN_LEVEL) {
			e.printStackTrace();
		}
	}

	public static void i(String log) {
		if (level <= INFO_LEVEL) {
			Log.i(TAG, log);
		}
	}

	public static void i(String tag, String log) {
		if (level <= INFO_LEVEL) {
			Log.i(tag, log);
		}
	}

	public static void i(Throwable e) {
		if (level <= INFO_LEVEL) {
			e.printStackTrace();
		}
	}

	public static void d(String log) {
		if (level <= DEBUG_LEVEL) {
			Log.d(TAG, log);
		}
	}

	public static void d(String tag, String log) {
		if (level <= DEBUG_LEVEL) {
			Log.d(tag, log);
		}
	}

	public static void d(Throwable e) {
		if (level <= DEBUG_LEVEL) {
			e.printStackTrace();
		}
	}

	public static void v(String log) {
		if (level <= VERB_LEVEL) {
			Log.v(TAG, log);
		}
	}

	public static void v(String tag, String log) {
		if (level <= VERB_LEVEL) {
			Log.v(tag, log);
		}
	}

	public static void v(Throwable e) {
		if (level <= VERB_LEVEL) {
			e.printStackTrace();
		}
	}


}
