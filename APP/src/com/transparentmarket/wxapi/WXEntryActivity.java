package com.transparentmarket.wxapi;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.transparentmarket.HomeActivity;
import com.transparentmarket.R;
import com.transparentmarket.WebActivity;
import com.transparentmarket.R.layout;
import com.transparentmarket.util.BaseService;
import com.transparentmarket.util.Config;
import com.transparentmarket.util.JsOperation;
import com.transparentmarket.util.ServiceForAccount;
import com.transparentmarket.util.SysApplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private IWXAPI api;
	private BaseResp resp = null;
	private WebView webView = null;
	private TextView titleView = null;
	private JsOperation client=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		api = WXAPIFactory.createWXAPI(this, Config.APP_ID, false);
		api.handleIntent(getIntent(), this);
		init();
	}
	
	

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		 
		 //Log.i("ceshi",code);
		 //Toast.makeText(this, code, Toast.LENGTH_LONG).show();
		switch(resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result ="发送成功";
				String code = ((SendAuth.Resp)resp).code;
				BaseService.saveNotGlobalInfo("code",code);
				//Toast.makeText(this, code, Toast.LENGTH_LONG).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "发送取消";
				//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
				//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
			default:
				result = "发送返回";
				//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
		finish();
	}
	
	private void open(String url){
		int index=0;
		if((index=url.indexOf('?'))>0){
			client.setParm(url.substring(index));
			webView.loadUrl(url.substring(0,index));
		}else{
			webView.loadUrl(url);
		}
	}
	

	private void init() {
		webView = (WebView) findViewById(R.id.toutput);
		client=new JsOperation(this,webView);
		titleView = (TextView) this.findViewById(R.id.title);
		this.findViewById(R.id.back).setOnClickListener(cl);
		this.findViewById(R.id.home).setVisibility(View.GONE);
		initWebView();
		open("file:///android_asset/html/aa.html");
	}

	private OnClickListener cl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.back) {
				goBack();
			} else if(v.getId()==R.id.home){
				//先提示再退出
				Intent intent = new Intent();  
				intent.setClass(WXEntryActivity.this, HomeActivity.class);  
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity  
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		}
	};

	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSaveFormData(false);
		webSettings.setSavePassword(false);
		webSettings.setSupportZoom(false);
		webSettings.setAppCacheEnabled(true);
		webView.addJavascriptInterface(client, "client");
		/* 获取标题 */
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				titleView.setText(title);
			}
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if(newProgress==100)
					webView.loadUrl("javascript:addNativeOK()");
			}
			
		});
		
		
		
		/** 设置在同一webview加载 */
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
 
		         if(url.indexOf(".jpg")!=-1 || url.indexOf(".jpeg")!=-1 || url.indexOf(".png")!=-1 || url.indexOf(".bmp")!=-1 || url.indexOf(".gif")!=-1)
		         {
		              Uri uri = Uri.parse(url); //url为你要链接的地址  
		    		  
		              Intent intent =new Intent(Intent.ACTION_VIEW, uri);  
		              intent.setDataAndType(uri, "image/*");
		              startActivity(intent);   
		         }
		         else  view.loadUrl(url);
 
				return true;
			}

		});
		/** 取消选择文字功能 */
		webView.setOnLongClickListener(new WebView.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		webView.setDownloadListener(new DownloadListener(){
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition,
					String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);  
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void goBack() {
		//先判断本页面后退
		if("1".equals(client.getParm())){
			webView.loadUrl("javascript:goBack();");
		}else{
			if (webView.canGoBack()) {
				webView.goBack(); // 后退
				//webView.reload();
			} else {
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		}
	}
	
}
