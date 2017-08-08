package com.transparentmarket.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.app.Activity;

public class SysApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static SysApplication instance;

    private SysApplication() {
           
    }

    public synchronized static SysApplication getInstance() {
            if (null == instance) {
                    instance = new SysApplication();
            }
            return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
            mList.add(activity);
    }
   
    public void backRemoveActivity() {
            mList.remove(mList.size()-1);
    }
    
    public int getMlist(){
    	return mList.size();
    }
   
    //移除
    public void removeActivity(int index){
            int length = mList.size();
            try {
                    for(int i=length-1;i>=length-index;i--){
                            mList.get(i).finish();
                            mList.remove(i);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }
    
    public void removelastActivity(){
        int length = mList.size();
        try {
                for(int i=length-1;i>=length-2;i--){
                        mList.get(i).finish();
                        mList.remove(i);
                }
        } catch (Exception e) {
                e.printStackTrace();
        }
}
    
   
    public void exit() {
            try {
                    for (Activity activity : mList) {
                            if (activity != null)
                                    activity.finish();
                    }
            } catch (Exception e) {
                    e.printStackTrace();
            } finally {
                    System.exit(0);
            }
    }

    /*public void onLowMemory() {
            super.onLowMemory();
            System.gc();
   
   
    }*/
}
