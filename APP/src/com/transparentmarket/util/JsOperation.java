package com.transparentmarket.util;

import java.io.InvalidClassException;
import java.util.List;

import com.guide.ex.ecdemo.common.CCPAppManager;
import com.guide.ex.ecdemo.common.ClientUser;
import com.guide.ex.ecdemo.common.utils.ECPreferenceSettings;
import com.guide.ex.ecdemo.common.utils.ECPreferences;
import com.guide.ex.ecdemo.common.utils.LogUtil;
import com.guide.ex.ecdemo.common.utils.ToastUtil;
import com.transparentmarket.ExWebActivity;
import com.transparentmarket.HomeActivity;
import com.transparentmarket.ImageViewActivity;
import com.transparentmarket.InitActivity;
import com.transparentmarket.R;
import com.transparentmarket.WebActivity;
import com.transparentmarket.WebShouActivity;
import com.transparentmarket.view.PhotoPopupWindow;
import com.transparentmarket.wxapi.WXEntryActivity;
import com.znv.ui.VideoPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class JsOperation {
	private Context context = null;
	private WebView webView = null;
	private String parm = null;
	private ProgressDialog progressDialog = null;
	private Dialog loadingDialog=null;
	
	public JsOperation(Context context) {
		this.context = context;
	}
	public JsOperation(Context context, WebView webView) {
		this.context = context;
		this.webView = webView;
	}

	public void setParm(String parm) {
		this.parm = parm;
	}

	public String getParm() {
		return parm;
	}

	public void showMsg(String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param type
	 *            ;Show,Dismiss,Success,Error
	 * @param msg
	 *            :提示信息
	 * @param method
	 *            :回调函数
	 */
	public void progress(String type, String text, String method) {
		if ("Show".equals(type)) {
			progressDialog = ProgressDialog.show(context, null, null, true,
					true);
			progressDialog.setCanceledOnTouchOutside(false);
		} else {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (!"Dismiss".equals(type)) {
				int resId = R.drawable.success;
				if ("Error".equals(type)) {
					resId = R.drawable.error;
				}
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				LinearLayout toastView = (LinearLayout) toast.getView();
				ImageView imageCodeProject = new ImageView(context);
				imageCodeProject.setImageResource(resId);
				toastView.addView(imageCodeProject, 0);
				toast.show();
			}
		}
		if (webView != null && method != null && method.length() > 0) {
			webView.loadUrl("javascript:" + method);
		}
	}

	public void confirm(String title, String text, final String method) {
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(text)
				// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (webView != null) {
									webView.loadUrl("javascript:" + method);
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 点击"取消"按钮之后退出程序
					}
				}).create();// 创建
		// 显示对话框
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public String getIpPort() {
		String result = BaseService.ServerPath;
		return result;
	}

	public String getavatarPort() {
		String result = BaseService.ImageBaseUrl;
		return result;
	}
	
	public String getfilePort(){
		String result = BaseService.FileBaseUrl;
		return result;
	}
	
	public String getUserJson() {
		if(CCPAppManager.getClientUser()==null)
		return "";
 
		String result =  CCPAppManager.getClientUser().toString();
		return result;
	}

	public void setUserJson(String json){

		
        ClientUser user = new ClientUser().from(json);

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
	  public void setLoginInfo(String username,String cd){
		  ServiceForAccount account = new ServiceForAccount(context);
		  account.saveKeyValue(ServiceForAccount.KEY_USERNAME, username);
		  account.saveKeyValue(ServiceForAccount.KEY_PASSWORD, cd);
		  
			try {
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_OWN_LOGIN,
						true, true);
			} catch (InvalidClassException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
	  }
	  
	  public void setWXLoginInfo(String openid,String avatar,String nickname){
		  
			try {
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_WX_OPENID,
						openid, true);
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_WX_AVATAR,
						avatar, true);
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_WX_NICKNAME,
						nickname, true);
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_OWN_LOGIN,
						false, true);
			} catch (InvalidClassException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
	  }
	  

    public void logout(){
			
			try {
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, "", true);
			} catch (InvalidClassException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CCPAppManager.setClientUser(null);
			
		}

	public void open(String url, int blank) {
		url = "file:///android_asset/html/" + url;
		if (blank == 0) {
			if (context instanceof WebActivity) {
				openUrl(url);
			}
		} else if(blank==1){
			Intent intent = new Intent(context, WebActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
		} 
	}

	public void gotoWeb(){
		Uri uri1 = Uri.parse("http://app1.sfda.gov.cn/datasearch/face3/dir.html");  
		Intent intent = new Intent(Intent.ACTION_VIEW, uri1);  
        intent.setData(uri1);
        startActivity(intent);  
	}
	
	private void openUrl(String url) {
		int index = 0;
		if ((index = url.indexOf('?')) > 0) {
			setParm(url.substring(index));
			webView.loadUrl(url.substring(0, index));
		} else {
			webView.loadUrl(url);
		}
	}

	public String getVerName() {
		return Config.getVerName(context, context.getClass().getPackage().getName());
	}

	public void call(String uri) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
		startActivity(intent);
	}
	
	   public void sendSMS(String phoneNumber){
           Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(phoneNumber));                
           startActivity(intent);
   }
	
	public void goBack() {
		if (context instanceof WebActivity) {
			((WebActivity) context).goBack();
		}
		else if(context instanceof WXEntryActivity){
			((WXEntryActivity) context).goBack();
		}
	}

	
	private void startActivity(Intent intent) {
		if (context instanceof Activity) {
			Activity a = (Activity) context;
			a.startActivity(intent);
			a.overridePendingTransition(R.anim.right_in, R.anim.left_out);
		} else {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	public String getPushStatus() {
		ServiceForAccount account = new ServiceForAccount(context);
		return account.getValueByKey(ServiceForAccount.KEY_PUSH);
	}
	
	public void shortcut(){
		   if(context instanceof HomeActivity)
			   ((HomeActivity) context).shortcut();  
	   }

	public void show(String uri){
		//"rtsp://120.199.66.102:8000/service?PUID-ChannelNo=120301111009287-1&PlayMethod=0&StreamingType=1&A=zxvnms"
		
		int index = uri.indexOf("rtsp://");
		int index_end = uri.indexOf("/service?");
		//showMsg(uri.substring(index+7, index_end-5));
		//showMsg(uri.substring(index_end-4,index_end));
		int index2 = uri.indexOf("ChannelNo=");
		int index_end2 =uri.indexOf("&");
		//showMsg(uri.substring(index2+10,index2+25));
		//showMsg(uri.substring(index2+26,index_end2));
		
		if(index==-1 || index_end==-1 || index2==-1 || index_end2==-1)
		{
			showMsg("视频地址格式有误");
		}
		else
		{
			Intent intent = new Intent(context,VideoPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putString("valip",uri.substring(index+7, index_end-5));
			bundle.putInt("port",Integer.parseInt(uri.substring(index_end-4,index_end)));
			bundle.putString("puid",uri.substring(index2+10,index2+25));
			bundle.putInt("channel",Integer.parseInt(uri.substring(index2+26,index_end2)));
			intent.putExtras(bundle);
	        startActivity(intent);  
		}
 
	}
	
	public String getImei(){
		TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);              
		String imei=telephonemanage.getDeviceId();
		return imei;
	}
	
	public void LoginFromWX(){
		   if(context instanceof HomeActivity)
			   ((HomeActivity) context).LoginFromWX();
		   else if(context instanceof WebActivity)
			   ((WebActivity) context).LoginFromWX();  
	}
	
	public void saveGlobalInfo(String key,String value){
	      BaseService.saveGlobalInfo(key,value,context);		
		}
	
	public String readGlobalInfo(String key){
		return BaseService.readGlobalInfo(key,context);
	}
		
    public void saveNotGlobalInfo(String key,String value){
	    	   BaseService.saveNotGlobalInfo(key,value);
	}
		
	public String readNotGlobalInfo(String key){
			return BaseService.readNotGlobalInfo(key);
	}
	
	public void openphoto(){
		PhotoPopupWindow photoWindow = new PhotoPopupWindow(((Activity)context));
		photoWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	  public void ShowPicture(int index,String showpictures){
		   
		    String[] arr = {};
		    if(!showpictures.equals(""))
		    {
		    	arr=showpictures.split(",");
				Intent intent = new Intent(context, ImageViewActivity.class);
				intent.putExtra("position", index);
				intent.putExtra("imageArray", arr);
				context.startActivity(intent);
				((Activity)context).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_nochange);
				
		    }

	}
	  
	public void ShowShoucangFlag(final int flag){

			   if(context instanceof WebShouActivity){
		           webView.post(new Runnable() {
		  	             @Override
		  	             public void run() {
		  				   ((WebShouActivity) context).showShowCang(flag);  
		  	             }
		  	         });
			   }


	}
	
	public void openShoucang(String url){
		url = "file:///android_asset/html/" + url;

			Intent intent = new Intent(context, WebShouActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);


  }
	
	public void openEx(String url,String text){
		url = "file:///android_asset/html/" + url;

			Intent intent = new Intent(context, ExWebActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("right", text);
			startActivity(intent);


  }
	
	public void saveUserName(String nickname){
		CCPAppManager.getClientUser().setNickname(nickname);	
		String Loginuser = CCPAppManager.getClientUser().toString();
		try {
			ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
					Loginuser, true);
		} catch (InvalidClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void savePoint(int point){
		ClientUser user = CCPAppManager.getClientUser();
		user.setPoint(point);
		try {
			ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
					user.toString(), true);
		} catch (InvalidClassException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getApp_Id(){
		return Config.APP_ID;
	}
	
	public String getApp_Secret(){
		return Config.APP_SECRET;
	}
	

	
}
