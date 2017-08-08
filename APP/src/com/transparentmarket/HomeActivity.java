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

import com.guide.ex.ecdemo.common.utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.transparentmarket.util.BaseService;
import com.transparentmarket.util.Config;
import com.transparentmarket.util.ServiceForAccount;
import com.transparentmarket.util.BaseService.UpdateInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.service.textservice.SpellCheckerService.Session;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {

	private RadioGroup bottomRg;
	private RadioButton radio;
	private static Fragment[] fragmens=new Fragment[4];
	private Fragment curFragment=null;
	private ProgressDialog pBar;
	private int progress = 0;
	private final static int DOWN_UPDATE = 11;
	private IWXAPI api=null;
	private  ServiceForAccount account=null;
	private View sim = null;
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
		api = WXAPIFactory.createWXAPI(this, Config.APP_ID,false);
        setContentView(R.layout.home_group);  
        initView();
        setImmerseLayout2(sim);
        initFrament();
        account=new ServiceForAccount(this);
        //api.handleIntent(getIntent(), this);
    }
	
	public static void refresh(){
		try{
			for(int i=0;i<fragmens.length;i++){
			if((HomeFragment)fragmens[i]!=null)	((HomeFragment)fragmens[i]).refresh();			
			}	
		}catch(Exception e){
			
		}
		
	}
	
	public void onResume(){
		super.onResume();
	}
	
	private OnClickListener cl=new OnClickListener(){
		@Override
		public void onClick(View v) {
			goSetting();
		}
	};
	
	private void startAct(Class<?> c) {
		Intent intent = new Intent();
		intent.setClass(this, c);
		startActivity(intent);
		this.finish();
	}
	
	private void goSetting(){
		Intent intent = new Intent();
		intent.setClass(this, WebActivity.class);
		intent.putExtra("url", "file:///android_asset/html/setting.html");
		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
	
	private void startFragment(int id,String title,String uri){

		if(fragmens[id]==null){
			if(id==0) fragmens[id]=new HomeFragment(uri,false);
			else      fragmens[id]=new HomeFragment(uri,false);
		}
		switchContent(fragmens[id]);
		if(id==0){
			((HomeFragment)fragmens[0]).myResume();
		}
	}
	
	public void switchContent(Fragment to) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();  
		if(curFragment==null){
			curFragment=to;
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();  
			fragmentTransaction.add(R.id.frame_content, curFragment);
			fragmentTransaction.commit(); 
		}else{
	            FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(
	            		R.anim.right_in, R.anim.left_out);
	            if (!to.isAdded()) {    // 先判断是否被add过
	                transaction.hide(curFragment).add(R.id.frame_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
	            } else {
	                transaction.hide(curFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
	            }
	            curFragment = to;
		}
    }
	
	private void initFrament(){
		radio=(RadioButton)this.findViewById(R.id.rb0);
		radio.setTextColor(getResources().getColor(R.color.bottom_sel));
		setRadioImage(R.drawable.ioc_17);
		startFragment(0,"首页","file:///android_asset/html/index.html");
	}
	
	private void initView(){
		bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
		bottomRg.setOnCheckedChangeListener(brgListener);

		sim = findViewById(R.id.sim);
	}
	
	
	private void resetRadioButton(RadioButton button){
		button.setTextColor(Color.GRAY);
		switch(button.getId()){
		case R.id.rb0:
			setRadioImage(R.drawable.bottom_0_st);
			break;
		case R.id.rb1:
			setRadioImage(R.drawable.bottom_1_st);
			break;
		case R.id.rb2:
			setRadioImage(R.drawable.bottom_2_st);
			break;
		case R.id.rb3:
			setRadioImage(R.drawable.bottom_3_st);
			break;
		}
	}
	
	private void setRadioImage(int id){
		Drawable myImage=getResources().getDrawable(id);
		radio.setCompoundDrawablesWithIntrinsicBounds(null, myImage, null, null);
	}
	
	/**
	 * 底部菜单事件
	 */
	private OnCheckedChangeListener brgListener=new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(radio==null||(radio!=null&&radio.getId()!=checkedId)){
				if(radio!=null){
					resetRadioButton(radio);
				}
				radio=(RadioButton)group.findViewById(checkedId);
				radio.setTextColor(getResources().getColor(R.color.bottom_sel));
				switch(checkedId){
				case R.id.rb0:
					setRadioImage(R.drawable.ioc_17);
					startFragment(0,"首页","file:///android_asset/html/index.html");
					break;
				case R.id.rb1:
					setRadioImage(R.drawable.ioc2_19);
					startFragment(1,"行情速递","file:///android_asset/html/gongshi.html");
					break;
				case R.id.rb2:
					setRadioImage(R.drawable.ioc2_21);
					startFragment(2,"小贴士","file:///android_asset/html/zhitongche.html");
					break;
				case R.id.rb3:
					setRadioImage(R.drawable.ioc2_23);
					startFragment(3,"留言板","file:///android_asset/html/wode.html");
					break;
				}
			}
			
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private long exitTime = 0;
	
	private void goBack(){
		if(radio.getId()!=R.id.rb0){
			((RadioButton)(this.findViewById(R.id.rb0))).setChecked(true);
		}else{
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				this.finish();
			}
		}
	}
    
	public void shortcut(){
//		((RadioButton)this.findViewById(R.id.radioButton2)).Perform();
		setSimulateClick((RadioButton)this.findViewById(R.id.rb2), 30, 30);
//		View view = (RadioButton)this.findViewById(R.id.rb2);
//		view.performClick();
	}
	
	private void setSimulateClick(View view,float x, float y) {
		long downTime = SystemClock.uptimeMillis();
		final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
				MotionEvent.ACTION_DOWN, x, y, 0);
		downTime += 1000;
		final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
				MotionEvent.ACTION_UP, x, y, 0);
		view.onTouchEvent(downEvent);
		view.onTouchEvent(upEvent);
		downEvent.recycle();
		upEvent.recycle();
	}
	









	public void LoginFromWX(){
	    // send oauth request 
	    final SendAuth.Req req = new SendAuth.Req();
	    req.scope = "snsapi_userinfo";
	    req.state = "wechat_sdk_demo_test";
	    
	    if (!api.isWXAppInstalled()) {
	    	Toast.makeText(this, "请安装微信", Toast.LENGTH_LONG).show();
            //提醒用户没有按照微信
            return;
        }
	    api.sendReq(req);
	}
	
}
