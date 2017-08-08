package com.transparentmarket;


import java.util.Arrays;









import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;











import com.transparentmarket.util.BaseService;
import com.transparentmarket.view.ZoomImageView;
import com.transparentmarket.view.ZoomImageView.Singlecallback;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageViewActivity extends Activity implements OnPageChangeListener,Singlecallback,OnClickListener {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private   DisplayImageOptions options;
	private   ViewPager pager;
	private   String[] imageUrls={};
	private   int imageLength;
	public static String ACTION_DELETE ="ImageViewActivity_image_delete";
    /**
     * 显示当前图片的页数
     */
    private TextView pageText;
	private ImageButton btn_back;
	private TextView btn_right;
	private RelativeLayout RelayoutTop;
	private ImagePagerAdapter adapter;
	private int current_pageNo;
	private String deleteflag=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		Bundle bundle = getIntent().getExtras();
		
		
		//imageUrls = bundle.getStringArray("ImageArray");
		int pagerPosition = bundle.getInt("position", 0);
		imageUrls = bundle.getStringArray("imageArray");
		// ���֮ǰ�б����û����
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt("currentItem");
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.NONE)
			.bitmapConfig(Bitmap.Config.ARGB_8888)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
		RelayoutTop =(RelativeLayout)findViewById(R.id.toubu);
		btn_back = (ImageButton)findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		btn_right = (TextView)findViewById(R.id.queding);
		btn_right.setOnClickListener(this);
		pager = (ViewPager)findViewById(R.id.pager);
		adapter = new ImagePagerAdapter(imageUrls);
		imageLength = imageUrls.length;
		pager.setAdapter(adapter);
		pager.setCurrentItem(pagerPosition);	// ��ʾ��ǰλ�õ�View
		pager.setOnPageChangeListener(this);
        pageText = (TextView) findViewById(R.id.page_text);
        // 设定当前的页数和总页数
        current_pageNo = pagerPosition;
        pageText.setText((pagerPosition + 1) + "/" + imageLength);
        initshowTop();
	}

	private void initshowTop(){
		
		
		Bundle bundle = getIntent().getExtras();
		deleteflag = bundle.getString("deleteflag");
		  
		  if(deleteflag==null){
			  RelayoutTop.setVisibility(View.GONE);
		  }

	}
	
	public ImageViewActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int currentPage) {
		// TODO Auto-generated method stub
	       // 每当页数发生改变时重新设定一遍当前的页数和总页数
		current_pageNo = currentPage;
      pageText.setText((currentPage + 1) + "/" + imageLength);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// �����û����
		outState.putInt("currentItem", pager.getCurrentItem());
	}
	
	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			  int length = images.length;
			   for(int i=0;i<images.length;i++){
				   if(images[i]==null){
					   length--;
				   }
			   }
			return length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.zoom_image_layout, view, false);
			ZoomImageView imageView = (ZoomImageView) imageLayout.findViewById(R.id.zoom_image_view);
			imageView.setSng(ImageViewActivity.this);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            
			if(images[position].contains("http")){
				imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {		// ��ȡͼƬʧ������
							case IO_ERROR:				// �ļ�I/O����
								message = "Input/Output error";
								break;
							case DECODING_ERROR:		// �������
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:		// �����ӳ�
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:		    // �ڴ治��
								message = "Out Of Memory error";
								break;
							case UNKNOWN:				// ԭ����
								message = "Unknown error";
								break;
						}

						spinner.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						spinner.setVisibility(View.GONE);		// ����ʾԲ�ν����
					}
				});
			}
			else {
				Bitmap bitmap = BitmapFactory.decodeFile(images[position]);
				imageView.setImageBitmap(bitmap);
			}


			((ViewPager) view).addView(imageLayout, 0);		// ��ͼƬ���ӵ�ViewPager
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public int getItemPosition(Object object) {
		    return POSITION_NONE;
		}
		
		@Override
	    public void destroyItem(View collection, int position, Object o) {
	        View view = (View)o;
	        ((ViewPager) collection).removeView(view);
	        view = null;
	    }
		
		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
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
        finish();


	}

	@Override
	public void onsinglclick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId()==R.id.back){
			finish();
		}
		else if(view.getId()==R.id.queding){
			confirm("提示","确定删除此图吗?");
		}
	}

	public void confirm(String title, String text) {

	}
	

	 
	 class Arr{
		 
		    private String[] arr;
		    private int len;//实际内容长度
		 
		    public Arr(String[] arr){
		        this.arr = arr;
		        this.len = this.arr.length;
		    }
		 
		    public void delete(int index){
		        if(index > arr.length){
		            return;
		        }
		 
		        for(int i = index; i < arr.length - 1; i ++){
		            arr[i] = arr[i + 1];
		        }
		 
		        len --;
		 
		        arr[arr.length - 1] = null;
		    }
		 
		    public void toString2(){
		        for(int i = 0; i < len; i ++){
		            if(i + 1 != len){
		                System.out.print(arr[i] + ",");
		            } else{
		                System.out.println(arr[i]);
		            }
		        }
		    }
		 
		    @Override
		    public String toString(){
		        // TODO Auto-generated method stub
		        return Arrays.toString(arr);
		    }
		}
	 
}
