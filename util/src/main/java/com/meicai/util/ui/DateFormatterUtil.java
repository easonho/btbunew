package com.meicai.util.ui;

import java.text.SimpleDateFormat;

/**
 * Created by meicai on 2015/1/21.
 */
public class DateFormatterUtil {

	public static String getTimeStamp(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStamp = simpleDateFormat.format(System.currentTimeMillis());
		return timeStamp;
	}

	public static String getDayOfMonth(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
		String timeStamp = simpleDateFormat.format(System.currentTimeMillis());
		return timeStamp;
	}

	public static String getDayFromPHP(Object time){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return simpleDateFormat.format(time);
	}
	public static String getHourFromPHP(Object time){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
		return simpleDateFormat.format(time);
	}

}
