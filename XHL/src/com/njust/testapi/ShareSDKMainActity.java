package com.njust.testapi;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class ShareSDKMainActity extends Activity {
	
	private Button authLoginBtn,shareGuiBtn,shareBtn,getInfoBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharesdk_main);
		//SDK
//		ShareSDK.initSDK(this);
		initView();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		ShareSDK.stopSDK(this);
	}
	public void initView(){
		authLoginBtn = (Button) findViewById(R.id.btnLogin);
		shareGuiBtn = (Button) findViewById(R.id.btnShareAllGui);
		shareBtn = (Button) findViewById(R.id.btnShareAll);
		getInfoBtn = (Button) findViewById(R.id.btnUserInfo);
	}

}
