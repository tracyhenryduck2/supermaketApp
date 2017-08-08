package com.transparentmarket.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ServiceForAccount {

	public final static String KEY_USERNAME = "username";
	public final static String KEY_PASSWORD = "password";
	public final static String KEY_CheckPass="check_pass";
	public final static String KEY_PUSH="push";
	public final static String KEY_PUSH_KEY="push_key";
	public final static String KEY_SERVER_ID="server_id";
	public final static String KEY_ONLINE="is_online";
	public final static String KEY_FIRST = "first";
	private SharedPreferences mSharedPre = null;
	
	public ServiceForAccount(Context context) {
		mSharedPre = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public Map<String, ?> getAllAccount() {
		return mSharedPre.getAll();
	}

	public void saveAcount(String username, String pwd) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(username, pwd);
	}
	
	public boolean saveKeyValue(String key, String value) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public String getValueByKey(String key) {
		return mSharedPre.getString(key, "");
	}
	
}
