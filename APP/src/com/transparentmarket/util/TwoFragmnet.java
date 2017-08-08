package com.transparentmarket.util;





import com.transparentmarket.HomeActivity;
import com.transparentmarket.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TwoFragmnet extends Fragment {
    private boolean show_flag;
	private ServiceForAccount account=null;
	private int index;
	
	
	public TwoFragmnet(int index, boolean show_flag) {
		this.show_flag = show_flag;
		this.index = index;
	}
	
	public TwoFragmnet()
	{
	       super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置activity的布局
		View view = inflater.inflate(R.layout.fragment_two, null);
		Button btn = (Button)view.findViewById(R.id.roundButton);
		ImageView rel = (ImageView)view.findViewById(R.id.ba);
		btn.setOnClickListener(l);
		if(show_flag)
		{
			btn.setVisibility(View.VISIBLE);
		}
		else  btn.setVisibility(View.GONE);
		
		if(index==1)
        rel.setBackgroundResource(R.drawable.wel1);
		else if(index==2)
		rel.setBackgroundResource(R.drawable.wel2);
		else if(index==3)
		rel.setBackgroundResource(R.drawable.wel3);			
		
		return view;
	}
	
	private OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			gotoIndex();
		}
	};
	
	private void gotoIndex(){
		account=new ServiceForAccount(this.getActivity());
		account.saveKeyValue(ServiceForAccount.KEY_FIRST, "1");
		Intent intent = new Intent();
		intent.setClass(this.getActivity(), HomeActivity.class);
		startActivity(intent);
		this.getActivity().finish();
		this.getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
}