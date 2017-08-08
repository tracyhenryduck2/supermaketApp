package com.transparentmarket.util;

import android.os.Parcel;
import android.os.Parcelable;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jorstin on 2015/3/18.
 */
public class ClientUser implements Parcelable {

    public static final Parcelable.Creator<ClientUser> CREATOR = new Parcelable.Creator<ClientUser>() {
        public ClientUser createFromParcel(Parcel in) {
            return new ClientUser(in);
        }

        public ClientUser[] newArray(int size) {
            return new ClientUser[size];
        }
    };

    /**用户注册V账号*/
    private int id;
    /**用户昵称*/

	private String nickname;
	private String avatar;
	
    
    
    


	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

    public int getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = id;
    }








    



    public ClientUser() {

    }

    private ClientUser(Parcel in) {
        this.id = in.readInt();
        this.avatar=in.readString();
        this.nickname=in.readString();
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id" , id);

            jsonObject.put("nickname" , nickname);
            jsonObject.put("avatar" , avatar);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ClientUser{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +

                '}';
    }

    public ClientUser from(String input) {
        JSONObject object = null;
        try {
            object = new JSONObject(input);
            if(object.has("id")){
            	this.id = object.getInt("id");	
            }
            
            if(object.has("avatar")) {
            	this.avatar= object.getString("avatar");
            }
            if(object.has("nickname")) {
            	this.nickname = object.getString("nickname");
            }

            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.avatar);
        dest.writeString(this.nickname);

    }
}
