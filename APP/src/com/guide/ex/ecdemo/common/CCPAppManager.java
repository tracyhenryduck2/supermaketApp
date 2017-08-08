/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.guide.ex.ecdemo.common;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.guide.ex.ecdemo.common.utils.ECPreferenceSettings;
import com.guide.ex.ecdemo.common.utils.ECPreferences;
import com.guide.ex.ecdemo.common.utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

import java.io.File;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 存储SDK一些全局性的常量
 * Created by Jorstin on 2015/3/17.
 */
public class CCPAppManager {
    public static Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
    /**Android 应用上下文*/
    private static Context mContext = null;
    /**包名*/
    public static String pkgName = "com.yuntongxun.ecdemo";
    /**SharedPreferences 存储名字前缀*/
    public static final String PREFIX = "com.yuntongxun.ecdemo_";
    public static final int FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 0x10000000;
    /**IM功能UserData字段默认文字*/
    public static final String USER_DATA = "yuntongxun.ecdemo";
    public static HashMap<String, Integer> mPhotoCache = new HashMap<String, Integer>();

    

    public static String getPackageName() {
        return pkgName;
    }
    private static ClientUser mClientUser;
    /**
     * 返回SharePreference配置文件名称
     * @return
     */
    public static String getSharePreferenceName() {
        return pkgName + "_preferences";
    }

    public static SharedPreferences getSharePreference() {
        if (mContext != null) {
            return mContext.getSharedPreferences(getSharePreferenceName(), 0);
        }
        return null;
    }

    /**
     * 返回上下文对象
     * @return
     */
    public static Context getContext(){
        return mContext;
    }
    
    public static void sendRemoveMemberBR(){
    	
    	getContext().sendBroadcast(new Intent("com.yuntongxun.ecdemo.removemember"));
    }

    /**
     * 设置上下文对象
     * @param context
     */
    public static void setContext(Context context) {
        mContext = context;
        pkgName = context.getPackageName();
        LogUtil.d(LogUtil.getLogUtilsTag(CCPAppManager.class),
                "setup application context for package: " + pkgName);
    }


    /**
     * 缓存账号注册信息
     * @param user
     */
    public static void setClientUser(ClientUser user) {
        mClientUser = user;
    }

    /**
     * 保存注册账号信息
     * @return
     */
    public static ClientUser getClientUser() {
    
        if(mClientUser != null) {
            return mClientUser;
        }
        String registAccount = getAutoRegistAccount();
        if(!TextUtils.isEmpty(registAccount)) {
            mClientUser = new ClientUser();

            return mClientUser.from(registAccount);
        }
        return null;
    }

    public static int getUserId() {
        return getClientUser().getUserId();
    }

    private static String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(), (String) registAuto.getDefaultValue());
        return registAccount;
    }
    





    /**
     * 获取应用程序版本名称
     * @return
     */
    public static String getVersion() {
        String version = "0.0.0";
        if(mContext == null) {
            return version;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取应用版本号
     * @return 版本号
     */
    public static int getVersionCode() {
        int code = 1;
        if(mContext == null) {
            return code;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }


    

    /**
     * 打开浏览器下载新版本
     * @param context
     */
    public static void startUpdater(Context context) {
        Uri uri = Uri.parse("http://dwz.cn/F8Amj");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static HashMap<String, Object> prefValues = new HashMap<String, Object>();

    /**
     *
     * @param key
     * @param value
     */
    public static void putPref(String key , Object value) {
        prefValues.put(key, value);
    }

    public static Object getPref(String key) {
        return prefValues.remove(key);
    }

    public static void removePref(String key) {
        prefValues.remove(key);
    }



}
