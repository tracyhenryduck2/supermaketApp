package com.transparentmarket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;















import com.guide.ex.ecdemo.common.CCPAppManager;
import com.guide.ex.ecdemo.common.utils.ECPreferenceSettings;
import com.guide.ex.ecdemo.common.utils.ECPreferences;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.transparentmarket.R;
import com.transparentmarket.util.BaseService;
import com.transparentmarket.util.Config;
import com.transparentmarket.util.FileUpload;
import com.transparentmarket.util.JsOperation;
import com.transparentmarket.util.ServiceForAccount;
import com.transparentmarket.util.SysApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.znv.yuv.YUVConvert;

public class WebActivity extends BaseActivity {
	private BroadReceiver mBroadReceiver;
	private WebView webView = null;
	private TextView titleView = null;
	private TextView rightbtn = null;
	private JsOperation client=null;
	private static WebActivity act=null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
	private final int CHOOSE_INFO=1;  //回调人员选择
	private  ServiceForAccount account=null;
	private int result;
	private String actionName=null;
	String picPath;
	String filename;
	private View sim;
    private static final int  TAKE_BIG_PICTURE=2; 
    private static final int  IMAGE_CUT=3;
    private static final int  COP_BIG_PICTURE=4;
    private String picturePath;
	private static final String IMAGE_FILE_LOCATION = "file:///mnt/sdcard/mlm/temp.jpg";//temp file
	private Uri imageUri =	Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
	private IWXAPI api=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act=this;
		api = WXAPIFactory.createWXAPI(this, Config.APP_ID,false);
		setContentView(R.layout.web);
		webView = (WebView) findViewById(R.id.toutput);
		client=new JsOperation(this,webView);
		init();
		setImmerseLayout2(sim);
		String url = getIntent().getStringExtra("url");
		open(url);
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		mBroadReceiver = new BroadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.znv.videoplayer.firstframe");    //ֻ�г�����ͬ��action�Ľ����߲��ܽ��մ˹㲥
        registerReceiver(mBroadReceiver, filter);
		webView.loadUrl("javascript:onMyResume()");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(mBroadReceiver);
	}
	
	class BroadReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Bitmap iBitmap = null;
			
			Bundle bundle=intent.getExtras();
			int width=bundle.getInt("width");
			int height=bundle.getInt("height");
			byte[] iRgb =bundle.getByteArray("firstFrame");
		    
			iBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			YUVConvert.rgbafillbitmap(iRgb, iBitmap);
			Log.v("width",width+"");
			Log.v("heigth",height+"");
		}
		
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
	
	public static void refresh(){
		try{
			act.webView.reload();
		}catch(Exception e){
			
		}
	}

	private void init() {
		titleView = (TextView) this.findViewById(R.id.title);
		rightbtn = (TextView)this.findViewById(R.id.btn_text_right);
		this.findViewById(R.id.back).setOnClickListener(cl);
		this.findViewById(R.id.home).setOnClickListener(cl);
		initWebView();
		account= new ServiceForAccount(this);
		sim =this.findViewById(R.id.sim);
	}


	
	private OnClickListener cl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.back) {
				goBack();
			} else if(v.getId()==R.id.home){
				//先提示再退出
				Intent intent = new Intent();  
				intent.setClass(WebActivity.this, HomeActivity.class);  
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面  
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity  
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
			else if(v.getId()==R.id.btn_text_right){
				webView.loadUrl("javascript:extraAction();");
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
      
		         if(url.indexOf(".jpg")!=-1 || url.indexOf(".jpeg")!=-1 || url.indexOf(".png")!=-1 || url.indexOf(".bmp")!=-1 || url.indexOf(".gif")!=-1
		        		 ||url.indexOf(".JPG")!=-1 || url.indexOf(".JPEG")!=-1 || url.indexOf(".PNG")!=-1 || url.indexOf(".BMP")!=-1 || url.indexOf(".GIF")!=-1)
		         {
		              Uri uri = Uri.parse(url); //url为你要链接的地址  
		    		  
//		              Intent intent =new Intent(Intent.ACTION_VIEW, uri);  
//		              intent.setDataAndType(uri, "image/*");
//		              startActivity(intent);   
		              
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
			        intent.setData(uri);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
			
			if(requestCode==COP_BIG_PICTURE)
			{
				String path = imageUri.getPath();
				File f = new File(path);
				if(f.exists())
				{
					  f.delete();
				}
			  
			}
	        return;
	    }

        if(requestCode==TAKE_BIG_PICTURE)
	    {
	    	 cropImageUri(imageUri, 300, 300, COP_BIG_PICTURE);
	    }
	    else if(requestCode==COP_BIG_PICTURE){
	   	  
//          Uri imageFileUri =data.getData(); 
			picturePath = imageUri.getPath();
			if(CCPAppManager.getClientUser()==null)
			uploadImg();
			else
		    uploadImg2();
          //webView.loadUrl("javascript:refreshtouxiang('"+imageUri.getPath()+"')");
//          int dw=getWindowManager().getDefaultDisplay().getWidth(); 
//          int dh=getWindowManager().getDefaultDisplay().getHeight()/2; 
//          //已屏幕宽 和一般的高作为图片显示的最大尺寸 
//          try{ 
//              BitmapFactory.Options factory=new BitmapFactory.Options(); 
//              factory.inJustDecodeBounds=true; //当为true时  允许查询图片不为 图片像素分配内存 
//              Bitmap bmp=BitmapFactory.decodeStream(getContentResolver() 
//                      .openInputStream(imageFileUri),null,factory); 
//              int hRatio=(int)Math.ceil(factory.outHeight/(float)dh); //图片是高度的几倍 
//              int wRatio=(int)Math.ceil(factory.outWidth/(float)dw); //图片是宽度的几倍 
//              System.out.println("hRatio:"+hRatio+"  wRatio:"+wRatio); 
//              //缩小到  1/ratio的尺寸和 1/ratio^2的像素 
//              if(hRatio>1||wRatio>1){ 
//                  if(hRatio>wRatio){ 
//                      factory.inSampleSize=hRatio;  
//                  } 
//                  else 
//                      factory.inSampleSize=wRatio; 
//              } 
//              factory.inJustDecodeBounds=false; 
//               bmp=BitmapFactory.decodeStream(getContentResolver() 
//                      .openInputStream(imageFileUri),null,factory); 
//               imageView.setImageBitmap(bmp); 
//          }catch(Exception ex){ 
//               
//          }
          	           	
          } 
          else if(requestCode==IMAGE_CUT){ 
	  			String fileName = BaseService.getImagePath(WebActivity.this)
						+"/"+ new Date().getTime() + ".jpg";
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				FileOutputStream b = null;
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					//Uri dsa = Uri.parse(fileName);
					picturePath = fileName;
					if(CCPAppManager.getClientUser()==null)
						uploadImg();
						else
					    uploadImg2();
					
					//webView.loadUrl("javascript:refreshtouxiang('"+dsa+"')");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
          }
	}


	public void showMsg(String str) {
		Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
	}
	
	public void showPhotoSheet(String actName) {
		actionName = actName;
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE); 
	    intent.setType(IMAGE_TYPE);
        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  
	    startActivityForResult(intent, IMAGE_CODE);
	}


	/**
	 * 获取剪切后的图片
	 */ 
	public void getImageClipIntent() { 
	
	    Intent intent = new Intent(Intent.ACTION_PICK, 
	            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
	    intent.setType("image/*"); 
	    intent.putExtra("crop", "true");  
	    intent.putExtra("aspectX", 1);//裁剪框比例 
	    intent.putExtra("aspectY", 1); 
	    intent.putExtra("outputX", 300);//输出图片大小 
	    intent.putExtra("outputY", 300); 
	    intent.putExtra("return-data", true); 
	    startActivityForResult(intent, IMAGE_CUT); 
	} 

	public void capture(){
		String fileName = BaseService.getImagePath(WebActivity.this)
				+"/"+ new Date().getTime() + ".jpg";
		imageUri = Uri.parse("file://"+fileName);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_BIG_PICTURE);
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
			    Intent intent = new Intent("com.android.camera.action.CROP");
			    intent.setDataAndType(uri, "image/*");
			    intent.putExtra("crop", "true");
			    intent.putExtra("aspectX", 1);
			    intent.putExtra("aspectY", 1);
			    intent.putExtra("outputX", outputX);
			    intent.putExtra("outputY", outputY);
			    intent.putExtra("scale", true);
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			    intent.putExtra("return-data", false);
			    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			    intent.putExtra("noFaceDetection", true); // no face detection
			    startActivityForResult(intent, requestCode);
			}

	
	private void uploadImg() {
		client.progress("Show", "正在提交", null);
		new Thread(){
			public void run(){
				System.out.println("小亨上传:"+picturePath);
				String  result = BaseService.uploadPic(picturePath);
				if(result!=null)
				{
					handler.sendMessage(handler.obtainMessage(1,result));
				}
				else
					
				handler.sendMessage(handler.obtainMessage(0));
			}
		}.start();
	}
	
	private void uploadImg2() {
		client.progress("Show", "正在提交", null);
		new Thread(){
			public void run(){
				String  result = BaseService.uploadPicModeAvatar(picturePath,CCPAppManager.getClientUser().getUserId());
				if(result!=null)
				{
					handler.sendMessage(handler.obtainMessage(2,result));
				}
				else
					
				handler.sendMessage(handler.obtainMessage(0));
			}
		}.start();
	}
	
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				client.progress("Error", "提交失败", null);
				break;
			case 1:
				String result = (String)msg.obj;
				client.progress("Success", "修改成功", "refreshtouxiang('"+result+"')");			
				break;
			case 2:
				String result1 = (String)msg.obj;
				CCPAppManager.getClientUser().setAvatar(result1);
				String Loginuser = CCPAppManager.getClientUser().toString();
				try {
					ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO,
							Loginuser, true);
				} catch (InvalidClassException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.progress("Success", "修改成功", "refreshtouxiang('"+result1+"')");			
				break;
			}
		}
	};
	
	public void LoginFromWX(){
	    // send oauth request 
		
	    final SendAuth.Req req = new SendAuth.Req();
	    req.scope = "snsapi_userinfo";
	    req.state = "wechat_sdk_demo_test";
	    
	    if (!api.isWXAppInstalled()) {
	    	client.progress("Error", "请安装微信", null);
            //提醒用户没有按照微信
            return;
        }
	    api.sendReq(req);
	}
}
