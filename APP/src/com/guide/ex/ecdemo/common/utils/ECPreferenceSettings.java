/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.guide.ex.ecdemo.common.utils;

/**
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-10
 * @version 4.0
 */
public enum ECPreferenceSettings {

    /**
     * Whether is the first use of the application
     *
     */
	/**第三方登录还是自己平台登录标识*/
    SETTINGS_OWN_LOGIN("com.market.ecdemo_own_login" , Boolean.TRUE),
    
    /**微信登录缓存信息*/
    SETTINGS_WX_AVATAR("com.market.ecdemo_wx_avatar" , ""),
    
    SETTINGS_WX_OPENID("com.market.ecdemo_wx_openid" , ""),
    
    SETTINGS_WX_NICKNAME("com.market.ecdemo_wx_nickname" , ""),
    
    SETTINGS_FIRST_USE("com.market.ecdemo_first_use" , Boolean.TRUE),
    /**坚持云通讯登陆账号*/
    SETTINGS_YUNTONGXUN_ACCOUNT("com.market.ecdemo_yun_account" , ""),

    /**检查是否需要自动登录*/
    SETTINGS_REGIST_AUTO("com.market.ecdemo_account" , ""),
    /**是否使用回车键发送消息*/
    SETTINGS_ENABLE_ENTER_KEY("com.market.ecdemo_sendmessage_by_enterkey" , Boolean.TRUE),
    /**聊天键盘的高度*/
    SETTINGS_KEYBORD_HEIGHT("com.market.ecdemo_keybord_height" , 0),
    /**新消息声音*/
    SETTINGS_NEW_MSG_SOUND("com.market.ecdemo_new_msg_sound" , true),
    /**新消息震动*/
    SETTINGS_NEW_MSG_SHAKE("com.market.ecdemo_new_msg_shake" , true),
    SETTING_CHATTING_CONTACTID("com.market.ecdemo_chatting_contactid" , ""),
    /**图片缓存路径*/
    SETTINGS_CROPIMAGE_OUTPUTPATH("com.market.ecdemo_CropImage_OutputPath" , ""),

    SETTINGS_APPKEY("com.market.ecdemo_appkey", "aaf98f8952b6f5730152ed9c1f6f3c35"),
    SETTINGS_TOKEN("com.market.ecdemo_token", "9647815f1ea7b4ac2033bea3cbe0233c"),
    //SETTINGS_APPKEY("com.yuntongxun.ecdemo_appkey", "20150314000000110000000000000010"),
    //SETTINGS_TOKEN("com.yuntongxun.ecdemo_token", "17E24E5AFDB6D0C1EF32F3533494502B"),
    SETTINGS_ABSOLUTELY_EXIT("com.market.ecdemo_absolutely_exit", Boolean.FALSE),
    SETTINGS_FULLY_EXIT("com.market.ecdemo_fully_exit", Boolean.FALSE),
    SETTINGS_PREVIEW_SELECTED("com.market.ecdemo_preview_selected", Boolean.FALSE),
    SETTINGS_OFFLINE_MESSAGE_VERSION("com.market.ecdemo_offline_version" , 0),
    /**设置是否是匿名聊天*/
    SETTINGS_SHOW_CHATTING_NAME("com.market.ecdemo_show_chat_name" , false),

    SETTINGS_CUSTOM_APPKEY("com.market.ecdemo_custom_appkey" , ""),
    SETTINGS_CUSTOM_TOKEN("com.market.ecdemo_custom_token" , ""),
    SETTINGS_SERVER_CUSTOM("com.market.ecdemo_setserver" , false),
    SETTINGS_NOTICE_CUSTOM("com.market.ecdemo_notice" , Boolean.FALSE);


    private final String mId;
    private final Object mDefaultValue;

    /**
     * Constructor of <code>CCPPreferenceSettings</code>.
     * @param id
     *            The unique identifier of the setting
     * @param defaultValue
     *            The default value of the setting
     */
    private ECPreferenceSettings(String id, Object defaultValue) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }

    /**
     * Method that returns the unique identifier of the setting.
     * @return the mId
     */
    public String getId() {
        return this.mId;
    }

    /**
     * Method that returns the default value of the setting.
     *
     * @return Object The default value of the setting
     */
    public Object getDefaultValue() {
        return this.mDefaultValue;
    }

    /**
     * Method that returns an instance of {@link com.guide.ex.ecdemo.common.utils.ECPreferenceSettings} from
     * its. unique identifier
     *
     * @param id
     *            The unique identifier
     * @return CCPPreferenceSettings The navigation sort mode
     */
    public static ECPreferenceSettings fromId(String id) {
        ECPreferenceSettings[] values = values();
        int cc = values.length;
        for (int i = 0; i < cc; i++) {
            if (values[i].mId == id) {
                return values[i];
            }
        }
        return null;
    }
}
