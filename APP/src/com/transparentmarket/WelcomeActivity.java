package com.transparentmarket;

import java.util.ArrayList;
import java.util.List;
















import com.transparentmarket.util.TwoFragmnet;

import cn.jpush.android.api.JPushInterface;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WelcomeActivity extends FragmentActivity {
	private ViewPager viewPager;// 页卡内容
	private List<Fragment> fragments;// Tab页面列表
	/** 页卡总数 **/
	private static final int pageSize = 3;
	private ImageView ring_center;
	private ImageView ring_left;
	private ImageView ring_right;
	private ImageView ring_center_selec;
	private ImageView ring_left_selec;
	private ImageView ring_right_selec;
	private int currentIndex=0;
	private int move_distance=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		InitView();
		InitViewPager();
	}
   
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
	
	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {	
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		fragments.add(new TwoFragmnet(1,false));
		fragments.add(new TwoFragmnet(2,false));
		fragments.add(new TwoFragmnet(3,true));
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitView(){
		ring_center=(ImageView)findViewById(R.id.center);
		ring_left=(ImageView)findViewById(R.id.left);
		ring_right=(ImageView)findViewById(R.id.right);
		ring_center_selec=(ImageView)findViewById(R.id.center_selec);
		ring_left_selec=(ImageView)findViewById(R.id.left_selec);
		ring_right_selec=(ImageView)findViewById(R.id.right_selec);
				
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ring_left.getLayoutParams()); 
		lp1.addRule(RelativeLayout.ALIGN_RIGHT, ring_center.getId());
		lp1.setMargins(0, 0, 30, 0);  
		ring_left.setLayoutParams(lp1);
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ring_right.getLayoutParams()); 
		lp2.addRule(RelativeLayout.ALIGN_LEFT, ring_center.getId());
		lp2.setMargins(30, 0, 0, 0);  
		ring_right.setLayoutParams(lp2); 
		
		RelativeLayout.LayoutParams lp3= new RelativeLayout.LayoutParams(ring_left_selec.getLayoutParams()); 
		lp3.addRule(RelativeLayout.ALIGN_RIGHT, ring_center_selec.getId());
		lp3.setMargins(0, 0, 30, 0);  
		ring_left_selec.setLayoutParams(lp3);
		
		RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(ring_right_selec.getLayoutParams()); 
		lp4.addRule(RelativeLayout.ALIGN_LEFT, ring_center_selec.getId());
		lp4.setMargins(30, 0, 0, 0);  
		ring_right_selec.setLayoutParams(lp4); 
	    move_distance =lp3.rightMargin;

	}
	
	/**
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		
		
		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(currentIndex*move_distance, index*move_distance, 0, 0);// 显然这个比较简洁，只有一行代码。
			animation.setFillAfter(true);// True:图片停在动画结束位置
			currentIndex = index;
			animation.setDuration(300);
			ring_left_selec.startAnimation(animation);
			
		}
	}
	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentList;

		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}
}
