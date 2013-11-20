package com.njust.testapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import util.AccessTokenKeeper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adpters.UserWeiBoInfoAdpter;
import com.asyn.GetUserInfoTask;
import com.bean.UserWeiboBean;
import com.parser.UserWeiboParser;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.InviteAPI;
import com.sina.weibo.sdk.openapi.LogoutAPI;

public class MainActivity extends Activity {
	private final String TAG = "MainActivity"; 
	private final int ERROR_RESPONSE = 0;
	private final int SUCC_RESPONSE = 1;
	private final int OBTAIN_TOKEN = 2;
	private final int REVOKE_AUTH = 3;
	private final int RECIVEMIFO = 4;
	private final int INVITE = 5;
	private final int GETWEIBOINFO = 6;
	private final int SHOWWEIBO =7;
	private final int SHOWWEIBOFAILURE = 8;
   
	private Button authlogin;
	private Button login;
	private Button sendMessge;
	private Button getUserInfo;
	private Button mLoginOut;
	private Button getUserWeiBo;
	private TextView userinfo;
	private EditText inviteNumber;
	private Button invteBtn;
	private LayoutInflater inflater;
	private View weiBoView;
	private ListView mLVWeiBoView;
	private List<UserWeiboBean> userweiboList;
	private UserWeiBoInfoAdpter userweiAdapter;
	
	  /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;
    /** 微博 Web 授权类，提供登陆等功能  */
    private WeiboAuth mWeiboAuth;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    private InviteRequestListener mInviteRequestListener = new InviteRequestListener();

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    private MyisHandler mHandle ;
    private String uid = "";
    private String userName = "";
    private String access_token ="";
    

    class MyisHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
              switch(msg.what){
              case ERROR_RESPONSE:
            	  if (!TextUtils.isEmpty((String) msg.obj)) {
            		  Log.i(">>>",msg.obj.toString());
                      Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                  }
                  break;
              case SUCC_RESPONSE:
            	  Toast.makeText(getApplicationContext(), getString(R.string.invite_succ), Toast.LENGTH_LONG).show();
            	  break;
              case OBTAIN_TOKEN:
            	  /*TextView tv = (TextView) findViewById(R.id.result);
                  Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getApplicationContext());
                  if (token != null && !TextUtils.isEmpty(token.getToken())) {
                      tv.setText(token.getToken());
                  }*/
                  break;
              case REVOKE_AUTH:
            	  Toast.makeText(getApplicationContext(), "登出成功", Toast.LENGTH_SHORT).show();
                  login.setVisibility(View.VISIBLE); 
                  mLoginOut.setVisibility(View.GONE);
            	  break;
              case RECIVEMIFO:
            	  GetUserInfoTask getuser = new GetUserInfoTask(userinfo);
            	  String url = "https://api.weibo.com/2/users/show.json?uid=" + uid + "&access_token=" + access_token+"&source="+Constants.APP_KEY;
            	  System.out.println(uid+"======="+access_token);
                  getuser.execute(url);
                  break;
              case GETWEIBOINFO:
            	  GetUserWeiboTask getUserInfo = new GetUserWeiboTask();
            	  String urlweibo = "https://api.weibo.com/2/statuses/user_timeline.json?access_token=" + access_token+"&source="+Constants.APP_KEY +"&count="+100;
            	  getUserInfo.execute(urlweibo);
            	  break;
              case SHOWWEIBO:
            	  showDialog(0x113);
            	  break;
              case SHOWWEIBOFAILURE:
            	  Toast.makeText(getApplicationContext(), "获取信息失败", Toast.LENGTH_SHORT).show();
            	  break;
              
              }
        }
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mHandle = new MyisHandler();
        authlogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWeiboAuth.anthorize(new Authlistener());
				
			}
		});
        login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler = new SsoHandler(MainActivity.this, mWeiboAuth);
				mSsoHandler.authorize(new Authlistener());
				
			}
		});
        //send button
        sendMessge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SendMessgeToWeiBo.class);
				startActivity(intent);
			}
		});
        mLoginOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
				if(mAccessToken!=null&&mAccessToken.isSessionValid()){
					new LogoutAPI(mAccessToken).logout(new LogoutListener());
				}else{
					mTokenText.setText(R.string.logout_failed);
				}
				
			}
		});
        getUserInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = mHandle.obtainMessage();
				msg.what = RECIVEMIFO;
				mHandle.sendMessage(msg);
				
			}
		});
        invteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				  JSONObject jsonObject = new JSONObject();
	  				try {
	  					jsonObject.put(InviteAPI.KEY_TEXT, "这是一个测试新浪Api的例子");
	  					jsonObject.put(InviteAPI.KEY_URL, "http://weibo.cn/pub/");
	  					// jsonObject.put(InviteApi.KEY_INVITE_LOGO, "")
	  				} catch (JSONException e) {
	  					e.printStackTrace();
	  				}
	  			
	  				String uid = inviteNumber.getText().toString();
	                     if (!TextUtils.isEmpty(uid)) {
	                         Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(MainActivity.this);
	                         if (!TextUtils.isEmpty(accessToken.getToken())) {
	                      	     new InviteAPI(accessToken).sendInvite(uid,jsonObject,mInviteRequestListener);
	                             
	                         } else {
	                             Toast.makeText(getApplicationContext(), "AccessToken不存在！", Toast.LENGTH_LONG).show();
	                         }
	                     } else {
	                         Toast.makeText(getApplicationContext(), "请输入被邀请人的Uid!", Toast.LENGTH_LONG).show();
	                     }
	               
	              }
			
		});
        getUserWeiBo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = mHandle.obtainMessage();
				msg.what = GETWEIBOINFO;
				mHandle.sendMessage(msg);
				
			}
		});
	}
	
	
    public void initView(){
    	authlogin = (Button) findViewById(R.id.obtain_token_auth);
    	login = (Button) findViewById(R.id.obtain_token_via_sso);
    	sendMessge = (Button) findViewById(R.id.sendMsg);
    	mLoginOut = (Button) findViewById(R.id.logout);
    	getUserInfo = (Button) findViewById(R.id.findUserInfo);
    	mTokenText = (TextView) findViewById(R.id.token_text_view);
    	userinfo =(TextView) findViewById(R.id.userInfo);
    	inviteNumber = (EditText) findViewById(R.id.uid);
    	inviteNumber.setText("2021055521");
    	invteBtn = (Button) findViewById(R.id.invite);
    	getUserWeiBo = (Button) findViewById(R.id.getUserWeibo);
    	
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	  @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        // SSO 授权回调
	        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
	        if (mSsoHandler != null) {
	            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
	    }
	  @Override
	  public Dialog onCreateDialog(int id,Bundle stat) {
			inflater = LayoutInflater.from(this);
			weiBoView = inflater.inflate(R.layout.dialog_show, null);
			mLVWeiBoView = (ListView)weiBoView.findViewById(R.id.showinfo);
			userweiAdapter = new UserWeiBoInfoAdpter(getApplicationContext());
      	    userweiAdapter.setWeiBoList(userweiboList);
      	    mLVWeiBoView.setAdapter(userweiAdapter);
			return new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.weibo_hassend))
				.setView(weiBoView).create();
		}
		
	  /**
	     * 好友邀请按钮的监听器，接收好友邀请处理结果。（API请求结果的监听器）
	     */
	    private class InviteRequestListener implements RequestListener {

	        @Override
	        public void onComplete(String response) {
	               Log.i("ABCDEF","Invite Response : response=="+response);
	            try {
	                Message msg = Message.obtain();
	                if (TextUtils.isEmpty(response) || response.contains("error_code")) {
	                    msg.what = ERROR_RESPONSE;
	                    JSONObject obj = new JSONObject(response);
	                    msg.obj = obj.getString("error");
	                } else {
	                    JSONObject obj = new JSONObject(response);
	                    msg.what = SUCC_RESPONSE;
	                    msg.obj = "邀请成功";
	                    System.out.println(obj);
	                }

	                mHandle.sendMessage(msg);

	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	        }

	        @Override
	        public void onComplete4binary(ByteArrayOutputStream responseOS) {
	        }

	        @Override
	        public void onIOException(IOException e) {
	        }

	        @Override
	        public void onError(WeiboException e) {
	        	//Log.i("ABCD","DASF");
	            Message msg = Message.obtain();
	            msg.what = ERROR_RESPONSE;
	            msg.obj = e.getMessage();
	            mHandle.sendMessage(msg);
	        }
	    }

    class LogoutListener implements RequestListener{

		@Override
		public void onComplete(String response) {
			 if (!TextUtils.isEmpty(response)) {
	                try {
	                    System.out.println("LogOutRequestListener() ---> onComplete --- response = " + response);
	                    JSONObject obj = new JSONObject(response);
	                    String value = obj.getString("result");
	                    if ("true".equals(value)) {
	                        AccessTokenKeeper.clear(MainActivity.this);
	                        Message msg = mHandle.obtainMessage();
	                        System.out.println("cccc");
	                        msg.what = REVOKE_AUTH;
	                        System.out.println("dddd");
	                        mHandle.sendMessage(msg);
	                    }
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            }
		}

		@Override
		public void onComplete4binary(ByteArrayOutputStream arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub
			
		}

    }
	class Authlistener implements WeiboAuthListener{

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			 Toast.makeText(MainActivity.this, 
					 "sso has cancled", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(Bundle values) {
			uid = values.getString("uid");
            userName = values.getString("userName");
            System.out.println("uid = " + uid);
            System.out.println("userName = " + userName);
            access_token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				Log.i(TAG,"success");
                // 显示 Token
                updateTokenView(false);
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                Toast.makeText(MainActivity.this, 
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
			login.setVisibility(View.GONE);
		}
		
		   /**
	     * 显示当前 Token 信息。
	     * 
	     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
	     */
	    private void updateTokenView(boolean hasExisted) {
	        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
	                new java.util.Date(mAccessToken.getExpiresTime()));
	        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
	        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
	        
	        String message = String.format(format, mAccessToken.getToken(), date);
	        if (hasExisted) {
	            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
	        }
	        mTokenText.setText(message);
	    }

		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
			
		}}
	class GetUserWeiboTask extends AsyncTask<String, Void, Boolean> {

	    private String result = "";
	    @Override
	    protected Boolean doInBackground(String... params) {
	        boolean isGetInfo = false;
	        String urlText = params[0];
	        try {
	            URL url = new URL(urlText);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	            byte[] data = new byte[1024];
	            int len = 0;
	            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {//若当前连接成功
	                isGetInfo = true;
	                InputStream inStream = conn.getInputStream();//打开输入流
	                while ((len = inStream.read(data)) != -1) {
	                    outStream.write(data, 0, len);
	                }
	                result = new String(outStream.toByteArray());//新建result变量用于获取服务器端传回的字符串
	                inStream.close();//关闭数据输入流
	            }
	            outStream.close();//关闭数据输出流
	            conn.disconnect();//关闭远程连接
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return isGetInfo;
	    }

	    @Override
	    protected void onPostExecute(Boolean isGetInfo) {
	        super.onPostExecute(isGetInfo);
	        if (isGetInfo) {
	            UserWeiboParser userparser = new UserWeiboParser();
	            try{
	              userweiboList = userparser.userInfoParser(result);
	              Message msg = mHandle.obtainMessage();
	              msg.what = SHOWWEIBO;
	              mHandle.sendMessage(msg);
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            System.out.println("GetUserWeiBo Success");
	        } else {
	        	Message msg = mHandle.obtainMessage();
	            msg.what = SHOWWEIBOFAILURE;
	            System.out.println("Failuer");
	            mHandle.sendMessage(msg);
	        }
	      
	    }

	    @Override
	    protected void onProgressUpdate(Void... values) {
	        super.onProgressUpdate(values);
	    }

	}

}
 
