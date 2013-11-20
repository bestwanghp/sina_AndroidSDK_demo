package com.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import com.bean.UserFriendsBean;

import android.util.Log;
public class UserFriendParser {
	private List<UserFriendsBean> userFriendBean =new ArrayList<UserFriendsBean>();
    public  List<UserFriendsBean> userInfoParser(String dataStr) throws JSONException {
    	 System.out.println("aaa");
	     JSONObject json =new JSONObject(dataStr);
	     JSONArray js = json.getJSONArray("users");
	     System.out.println(js);
	     for(int i =0 ;i<js.length();i++){
	    	 UserFriendsBean user=new UserFriendsBean();
	    	 user.setName(js.getJSONObject(i).getString("screen_name"));
	    	 user.setId(js.getJSONObject(i).getString("id"));
	    	 System.out.println(user);
	    	 userFriendBean.add(user);
	     }
	     return userFriendBean;
    }
   

}
