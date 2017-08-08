package com.transparentmarket;

import com.guide.ex.ecdemo.common.utils.ToastUtil;
import com.transparentmarket.util.BaseService;
import com.transparentmarket.util.JsOperation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	private View view = null;
	private WebView webView = null;
	private String url = null;
	private JsOperation client=null;
	private boolean showtop;
	private RelativeLayout top;
	private TextView titleView;

	public HomeFragment(String url,boolean showtop) {
		this.url = url;
		this.showtop = showtop;
	}
	
	public HomeFragment()
	{
	       super();

	}
	
	public void refresh(){
	
		if(webView!=null){
			webView.reload();
		}
	}
	
	public void onResume(){
		super.onResume();
		myResume();
	}
	
	public void myResume(){
		if(webView!=null){
			webView.loadUrl("javascript:onResume();");
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			view = inflater.inflate(R.layout.home_frag, container, false);
			webView = (WebView) view.findViewById(R.id.toutput);
			top = (RelativeLayout)view.findViewById(R.id.toplay);
			titleView = (TextView)view.findViewById(R.id.title);
			if(showtop) top.setVisibility(View.VISIBLE);
			else        top.setVisibility(View.GONE); 
			client=new JsOperation(this.getActivity(),webView);
			init();
			int index=0;
		    if (savedInstanceState != null  
		            && savedInstanceState.getString("url") != null) {  
		  
		    	url=(savedInstanceState.getString("url"));  
		    	BaseService.ServerPath = (savedInstanceState.getString("BaseUrl"));  
		    }  
			
			if((index=url.indexOf('?'))>0){
				client.setParm(url.substring(index));
				webView.loadUrl(url.substring(0,index));
			}else{
				webView.loadUrl(url);
			}
			return view;

	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("url", url);
		outState.putString("BaseUrl",BaseService.ServerPath);
		super.onSaveInstanceState(outState);
	}

	private void init() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSaveFormData(false);
		webSettings.setSavePassword(false);
		webSettings.setSupportZoom(false);
		webSettings.setAppCacheEnabled(true);
		webView.setVerticalScrollBarEnabled(false);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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
		webView.addJavascriptInterface(client, "client");
		/** 取消选择文字功能 */
		webView.setOnLongClickListener(new WebView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
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

}