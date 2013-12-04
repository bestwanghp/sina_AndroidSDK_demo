package com.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.util.Log;

import com.bean.UserBean;
import com.bean.UserWeiboBean;

public class UserWeiboParser {
	private List<UserWeiboBean> userWeiboBean =new ArrayList<UserWeiboBean>();
    public  List<UserWeiboBean> userInfoParser(String dataStr) throws JSONException {
    	 System.out.println("aaa");
	     JSONObject json =new JSONObject(dataStr);
	     JSONArray js = json.getJSONArray("statuses");
	     System.out.println(js);
	     for(int i =0 ;i<js.length();i++){
	    	 UserWeiboBean user=new UserWeiboBean();
	    	 user.setText(js.getJSONObject(i).getString("text"));
	    	 user.setName(js.getJSONObject(i).getJSONObject("user").getString("screen_name"));
	    	 user.setTime(js.getJSONObject(i).getString("created_at"));
	    	 System.out.println(user);
	    	 userWeiboBean.add(user);
	     }
	     return userWeiboBean;
    }
   

}
