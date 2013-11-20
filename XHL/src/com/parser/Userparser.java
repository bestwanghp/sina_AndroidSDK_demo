package com.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.bean.UserBean;

public class Userparser {

    private UserBean userBean = null;

    public Userparser() {
        userBean = new UserBean();
    }

    public UserBean userInfoParser(String dataStr) {
        try {
            JSONObject jsonObject = new JSONObject(dataStr);
            userBean.setUid(jsonObject.getString("id")); 
            userBean.setScreen_name(jsonObject.getString("screen_name"));
            userBean.setName(jsonObject.getString("name"));
            userBean.setLocation(jsonObject.getString("location"));
            userBean.setProfile_image_url(jsonObject.getString("profile_image_url"));
            userBean.setGender(jsonObject.getString("gender"));
            userBean.setAvatar_large(jsonObject.getString("avatar_large")); 
        } catch (JSONException e) { 
            userBean = null;
            e.printStackTrace();
        }
        return userBean;
    }

}