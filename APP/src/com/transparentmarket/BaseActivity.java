
package com.transparentmarket;



import com.transparentmarket.util.ScreenUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(9)
public class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";

    public static String apkName;

    protected boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStart = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isStart = false;
        super.onDestroy();
    }

    
    
    protected void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
            }
            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                if (!(view instanceof AbsSpinner) && !(view instanceof AbsListView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }
        }
    }

    protected View inflateSubView(int subId, int inflateId) {
        View noNetSubTree = findViewById(inflateId);
        if (noNetSubTree == null) {
            ViewStub viewStub = (ViewStub) findViewById(subId);
            noNetSubTree = viewStub.inflate();
        }
        noNetSubTree.setVisibility(View.VISIBLE);
        return noNetSubTree;
    }
    
    
    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                view.setVisibility(View.VISIBLE);
            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
            
//            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20  
//
//            linearParams.height = linearParams.height + statusBarHeight;// 控件的宽强制设成30   
//
//            view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
    }
    
    protected void setImmerseLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    
    protected void setImmerseLayout2(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                view.setVisibility(View.VISIBLE);
            int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
//            view.setPadding(0, statusBarHeight, 0, 0);
            
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20  

            linearParams.height = linearParams.height + statusBarHeight;// 控件的宽强制设成30   

            view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
    }
    
}
