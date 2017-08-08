package com.transparentmarket.util;

import java.io.File;
import java.io.InvalidClassException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;







import com.guide.ex.ecdemo.common.CCPAppManager;
import com.guide.ex.ecdemo.common.ClientUser;
import com.guide.ex.ecdemo.common.utils.ECPreferenceSettings;
import com.guide.ex.ecdemo.common.utils.ECPreferences;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class BaseService {

//	public final static String ServerPath = "114.215.189.99";
//		public static String ServerPath = "192.168.1.37:8080";
//	public static String ServerPath = "192.168.1.26:88";
//	public final static String ServerPath = "192.168.1.156:8080";
//	public final static String ServerPath = "t1.zed1.cn:88";
	public static String ServerPath = "112.33.1.143:88";
//	public static String ServerPath = "192.168.1.103:8080";
	public static String ApkVerUrl = "http://" + ServerPath
			+ "/app/transparentmarket.ver";
	public final static String NetTestUrl = "http://"+ServerPath+"/super_market/app/SuperMarket!getCarouselInfo.action";
	public final static String LoginUrl = "http://"+ServerPath+"/super_market/app/SuperMarketNew!login.action";
	public final static String WxLoginUrl = "http://"+ServerPath+"/super_market/app/SuperMarketNew!weixlogin.action";
	public static String ApkUrl = "http://" + ServerPath
	+ "/app/transparentmarket.apk";
	public final static String ImageBaseUrl = ServerPath+"/market_avatar";
	public final static String FileBaseUrl = ServerPath+"/supermarket_images/news";
	//public final static String OAPath = "http://" + ServerPath + "/oa/App";
	private static String imagePath=null;
	public static HashMap<String,String> account_map=new HashMap<String, String>();
	public static class UpdateInfo {
		public int code;
		public String name;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	};

	public static UpdateInfo getUpdateInfo() {
		UpdateInfo info = new UpdateInfo();
		try {
			String result = NetworkTool.getContent(ApkVerUrl);
			JSONObject objMap = new JSONObject(result);
			info.code = objMap.getInt("code");
			info.name = objMap.getString("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static boolean TestNet() {
		boolean flag = false;
		try {
			NetworkTool.getContent(NetTestUrl);
			flag = true;
            Log.i("ceshi",String.valueOf(flag));
		} catch (Exception e) {
			Log.i("ceshi","无");
			e.printStackTrace();
			return false;
		}
		return flag;
	}

	public static int login(String username, String pwd) {
		try {
			String result = NetworkTool
					.getContent(LoginUrl + "?username=" + username
							+ "&password=" + pwd);
			Log.i("ceshi", "普通登陆:"+result);
			JSONObject objMap = new JSONObject(result);
			int status = objMap.getInt("result");
			if (status == 1) {
				JSONObject obj = objMap.getJSONObject("user");
		        ClientUser user = new ClientUser().from(obj.toString());
		        CCPAppManager.setClientUser(user);	
				String Loginuser = CCPAppManager.getClientUser().toString();
				try {
					ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
							Loginuser, true);
				} catch (InvalidClassException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
		        CCPAppManager.setClientUser(null);	
				try {
					ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
							"", true);
				} catch (InvalidClassException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int wxlogin(String openid,String avatar, String nickname) {
		try {
			String result = NetworkTool
					.getContent(WxLoginUrl + "?openid=" + openid
							+ "&avatar=" + avatar+ "&nickname=" + nickname);
			Log.i("ceshi", "微信登陆:"+result);
			JSONObject objMap = new JSONObject(result);
			int status = objMap.getInt("result");
			if (status == 1) {
				JSONObject obj = objMap.getJSONObject("user");
		        ClientUser user = new ClientUser().from(obj.toString());
		        CCPAppManager.setClientUser(user);	
				String Loginuser = CCPAppManager.getClientUser().toString();
				try {
					ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
							Loginuser, true);
				} catch (InvalidClassException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
		        CCPAppManager.setClientUser(null);	
				try {
					ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
							"", true);
				} catch (InvalidClassException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static String getImagePath(Context context) {
		if (imagePath == null) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 判断sd卡是否存在
				File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
				File file = new File(sdDir.getAbsolutePath() + "/oa");
				if (file.isDirectory()) {
					imagePath = file.getAbsolutePath();
				} else {
					if (file.mkdirs()) {
						imagePath = file.getAbsolutePath();
					} else {
						imagePath = context.getFilesDir()
								.getAbsolutePath();
					}
				}
			} else {
				imagePath = context.getFilesDir().getAbsolutePath();
			}
		}
		return imagePath;
	}
	
	public static void saveGlobalInfo(String key,String value,Context context){
		ServiceForAccount account =new ServiceForAccount(context);
		account.saveKeyValue(key, value);
	}
	
	public static String readGlobalInfo(String key,Context context){
		ServiceForAccount account =new ServiceForAccount(context);
		return account.getValueByKey(key);
	}
	
	public static void saveNotGlobalInfo(String key,String value)
	{
		BaseService.account_map.put(key, value);
	}
	public static String readNotGlobalInfo(String key){
		return BaseService.account_map.get(key);
	}
	
//	public static String uploadPic(String picturePath) {
//		FileService up = new FileService();
//		try {
//				return up.send("http://"+ServerPath+"/super_market/app/SuperMarketNew!upload.action",picturePath);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	//提交注册图片接口
	public static String uploadPic(String picturePath) {
		FileService up = new FileService();
		try {
				return up.send("http://"+ServerPath+"/super_market/app/SuperMarketNew!upload.action",
						picturePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//修改用户头像接口
	public static String uploadPicModeAvatar(String picturePath,int userid) {
		FileService up = new FileService();
		try {
				return up.send("http://"+ServerPath+"/super_market/app/SuperMarketNew!submitAvatar.action?uid="+userid,
						picturePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
