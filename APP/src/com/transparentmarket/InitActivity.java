package com.transparentmarket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;





















import com.guide.ex.ecdemo.common.utils.ECPreferenceSettings;
import com.guide.ex.ecdemo.common.utils.ECPreferences;




import com.transparentmarket.util.BaseService;
import com.transparentmarket.util.Config;
import com.transparentmarket.util.ServiceForAccount;
import com.transparentmarket.util.BaseService.UpdateInfo;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


public class InitActivity extends BaseActivity {
	  private UpdateInfo info;
	  public  int count=0;
	  private ProgressDialog pBar;
      public static String packageName = null;
	  private int progress = 0;
	  private final static int DOWN_UPDATE = 11;
	  private ServiceForAccount account=null;
	  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);
		setImmerseLayout();
		account = new ServiceForAccount(this);
		packageName = this.getClass().getPackage().getName();
		//init();
		initial();
		//chooseNet();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				gotoIndex();
				break;
			case 3:
				doNewVersionUpdate((UpdateInfo) msg.obj);
				break;
			case DOWN_UPDATE:
				pBar.setProgress(progress);
				break;
			case 4:
				initial();
				break;
			case 5:
				login();
				break;
			}
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	
	

	
	private void init() {
		new Thread() {
			public void run() {
				if (checkUpdate()) {
					handler.sendMessage(handler.obtainMessage(1));
				}
			}
		}.start();
	}

	private void chooseNet(){
		new Thread(){
			public void run(){
				boolean flag = BaseService.TestNet();
				if(!flag)  {
					BaseService.ServerPath = "26.ztoas.com:88";
				}
				handler.sendMessage(handler.obtainMessage(4));
			}
		}.start();
	}
	
	private void initial(){
		new Thread() {
			public void run() {
				
				while(count<3)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count++;	
				}
				
				if (checkUpdate()) {
					handler.sendMessage(handler.obtainMessage(5));
				}
			}
		}.start();   
	}
	
	
	private void login(){
		
		new Thread(){
			public void run(){
				if( isOwnLogin()){
					   String uname = BaseService.readGlobalInfo("username",InitActivity.this);
					   String cd    = BaseService.readGlobalInfo("password",InitActivity.this); 
					
					   if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(cd)){
						   handler.sendMessage(handler.obtainMessage(1));
					   }
					   else {
							if(BaseService.login(uname,cd)==1){
								handler.sendMessage(handler.obtainMessage(1));
							}
							else {
								handler.sendMessage(handler.obtainMessage(1));
							}
					   }
				}
				else {
					 String openid = getOpenId();
					 String avatar = getAvatar();
					 String nickname = getNickname();
					 
					 if(TextUtils.isEmpty(openid)||TextUtils.isEmpty(avatar)||TextUtils.isEmpty(nickname)){
						 handler.sendMessage(handler.obtainMessage(1));
					 }
					 else{
							if(BaseService.wxlogin(openid, avatar, nickname)==1){
								handler.sendMessage(handler.obtainMessage(1));
							}
							else {
								handler.sendMessage(handler.obtainMessage(1));
							}
					 }
				}
				

				   

				
			}
		}.start();
		

	}
	
	private void gotoIndex(){
		String firstflag = account.getValueByKey(ServiceForAccount.KEY_FIRST);
	


			Intent intent = new Intent();
			if(firstflag==null||firstflag.equals(""))
			intent.setClass(this, WelcomeActivity.class);
			else
			intent.setClass(this, HomeActivity.class);
			startActivity(intent);
			this.finish();
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		
	}
	
	private boolean checkUpdate() {
		info = BaseService.getUpdateInfo();
		if (Config.getVerCode(this, packageName) < info.code) {
			handler.sendMessage(handler.obtainMessage(3, info));
			return false;
		} else {
			return true;
		}
	}

	private void doNewVersionUpdate(UpdateInfo info) {

		int verCode = Config.getVerCode(this, packageName);
		String verName = Config.getVerName(this, packageName);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append(info.name);
		sb.append(" Code:");
		sb.append(info.code);
		sb.append(", 是否更新?");
		
		if(!checkVer())
		{
			Dialog dialog = new AlertDialog.Builder(InitActivity.this)
			.setTitle("软件更新")
			.setMessage(sb.toString())
			// 设置内容
			.setPositiveButton("更新",// 设置确定按钮
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							pBar = new ProgressDialog(InitActivity.this);
							pBar.setCanceledOnTouchOutside(false);
							pBar.setTitle("正在下载");
							pBar.setMessage("请稍候...");
							pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							downFile(BaseService.ApkUrl);
						}
					})
			.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// 点击"取消"按钮之后退出程序
					//finish();
					gotoIndex();
				}
			}).create();// 创建
			// 显示对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		else
		{
			Dialog dialog = new AlertDialog.Builder(InitActivity.this)
			.setTitle("软件更新")
			.setMessage(sb.toString())
			// 设置内容
			.setPositiveButton("更新",// 设置确定按钮
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							pBar = new ProgressDialog(InitActivity.this);
							pBar.setCanceledOnTouchOutside(false);
							pBar.setTitle("正在下载");
							pBar.setMessage("请稍候...");
							pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							downFile(BaseService.ApkUrl);
						}
					})
			.create();// 创建
			// 显示对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

	}

	private void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						System.out
								.println("Environment.getExternalStorageDirectory()==="
										+ Environment
												.getExternalStorageDirectory());
						File file = new File(
								Environment.getExternalStorageDirectory(),
								Config.UPDATE_APKNAME);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								progress = (int) (((float) count / length) * 100);
								handler.sendEmptyMessage(DOWN_UPDATE);
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

	private void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}
	
	//判断是否强制更新
	private boolean checkVer(){
		boolean flag = false;
		
		if( info!=null&& info.name.indexOf(".8")!=-1)
			{
			return true;
			}
		
		
		return flag;
	}
	
	private void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Config.UPDATE_APKNAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	private boolean isOwnLogin() {
		boolean OwnLogin = ECPreferences.getSharedPreferences().getBoolean(
				ECPreferenceSettings.SETTINGS_OWN_LOGIN.getId(),
				((Boolean) ECPreferenceSettings.SETTINGS_OWN_LOGIN
						.getDefaultValue()).booleanValue());
		return OwnLogin;
	}
	
	private String getOpenId() {
		String openid = ECPreferences.getSharedPreferences().getString(
				ECPreferenceSettings.SETTINGS_WX_OPENID.getId(),
				((String) ECPreferenceSettings.SETTINGS_WX_OPENID
						.getDefaultValue()).toString());
		return openid;
	}
	
	private String getAvatar() {
		String Avatar = ECPreferences.getSharedPreferences().getString(
				ECPreferenceSettings.SETTINGS_WX_AVATAR.getId(),
				((String) ECPreferenceSettings.SETTINGS_WX_AVATAR
						.getDefaultValue()).toString());
		return Avatar;
	}
	
	private String getNickname() {
		String Nickname = ECPreferences.getSharedPreferences().getString(
				ECPreferenceSettings.SETTINGS_WX_NICKNAME.getId(),
				((String) ECPreferenceSettings.SETTINGS_WX_NICKNAME
						.getDefaultValue()).toString());
		return Nickname;
	}
	
	private void startAct(Class<?> c) {
		Intent intent = new Intent();
		intent.setClass(InitActivity.this, c);
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
}