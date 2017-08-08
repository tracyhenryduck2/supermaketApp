package com.transparentmarket.util;


import com.transparentmarket.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Config {
	private static final String TAG = "Config";
	public static String UPDATE_SERVER = "";
	public static final String UPDATE_APKNAME="supermarket.apk";
	public static String serverIp = "";
	public static String userName = "";
	public static String userPwd = "";
	public static String port;
	public static String APP_ID = "wx0a5a05321422afcf";
	public static String APP_SECRET ="389bee3aaabf97f8eae4614ba95c232e";
	
	
	
	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		Config.port = port;
	}

	public static String getServerIp() {
		return serverIp;
	}

	public static void setServerIp(String serverIp) {
		Config.serverIp = serverIp;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		Config.userName = userName;
	}

	public static String getUserPwd() {
		return userPwd;
	}

	public static void setUserPwd(String userPwd) {
		Config.userPwd = userPwd;
	}

	public static int getVerCode(Context context,String packageName) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	
	public static String getVerName(Context context,String packageName) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;	

	}
	
	public static String getAppName(Context context) {
		String verName = context.getResources()
		.getText(R.string.app_name).toString();
		return verName;
	}

}

