package com.transparentmarket.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;













import com.transparentmarket.R;
import com.transparentmarket.WebActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PhotoPopupWindow extends PopupWindow implements OnClickListener{



	private View mMenuView;
    private Button btn_cancel;
    private Button btn_takephoto;
    private Button btn_choosephoto;
    private Context context;

	public PhotoPopupWindow(Activity context) {
		super(context);
		this.context=context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.photo_dialog, null);
		btn_cancel=(Button)mMenuView.findViewById(R.id.cancel);
		btn_takephoto =(Button)mMenuView.findViewById(R.id.takephoto);
		btn_choosephoto=(Button)mMenuView.findViewById(R.id.choosephoto);
        btn_cancel.setOnClickListener(this);
        btn_takephoto.setOnClickListener(this);
        btn_choosephoto.setOnClickListener(this);
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AnimBottom);
		//ʵ��һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ�����������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.too).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId()==R.id.cancel)
			{
			 dismiss();
			}
		else if(arg0.getId()==R.id.takephoto)
		{
			 dismiss();
    		if (context instanceof WebActivity) {
   			((WebActivity) context).capture();
   		}


		}
		else if(arg0.getId()==R.id.choosephoto)
		{
			 dismiss();
     		if (context instanceof WebActivity) {
    			((WebActivity) context).getImageClipIntent();
    		}
		}
	}
    
}
