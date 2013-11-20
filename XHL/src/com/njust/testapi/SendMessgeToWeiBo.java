package com.njust.testapi;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessgeToWeiBo extends Activity implements
		IWeiboHandler.Response {
	private final String TAG = "SendMessgeToWeiBo";
	private Button sendMsg;
	private TextView send;
	private ImageView mImage;
	private IWeiboShareAPI weiboAPI;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmsgdemo);
		initData();
		// 创建微博对外接口实例
		weiboAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
		Log.i(TAG,"SUCCESS");
		if(savedInstanceState!=null){
		    weiboAPI.handleWeiboResponse(getIntent(), this);
		     }
		if(weiboAPI.checkEnvironment(true)){
			weiboAPI.registerApp();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		weiboAPI.handleWeiboResponse(intent, this);
	}

	public void initData() {
		sendMsg = (Button) findViewById(R.id.sendMsg);
		send = (TextView) findViewById(R.id.sendText);
		mImage = (ImageView) findViewById(R.id.image);
		sendMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (weiboAPI.isWeiboAppSupportAPI()) {
					sendOneWeiBoText();
				} else {
					Toast.makeText(getApplicationContext(),
							"您的手机并未安装新浪微博客户端，故不能发布微博，请下载！。。。",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	public void sendOneWeiBoText() {
		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.textObject = getTextObj();
		weiboMessage.imageObject = getImageObj();
		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.multiMessage = weiboMessage;
		// 3. 发送请求消息到微博，唤起微博分享界面s
		weiboAPI.sendRequest(req);
	}

	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = send.getText().toString();
		return textObject;
	}

	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) mImage.getDrawable();
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		System.out.println("onResponse() --> baseResp.toString() = " + baseResp.toString()); 
        System.out.println("onResponse() --> baseResp.errCode = " + baseResp.errCode); 
        System.out.println("onResponse() --> baseResp.errMsg = " + baseResp.errMsg); 
        System.out.println("onResponse() --> baseResp.reqPackageName = " + baseResp.reqPackageName); 
        System.out.println("onResponse() --> baseResp.transaction = " + baseResp.transaction); 
        switch (baseResp.errCode) {
        case WBConstants.ErrorCode.ERR_OK:
            Toast.makeText(this, "成功！！", Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_CANCEL:
            Toast.makeText(this, "用户取消！！", Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_FAIL:
            Toast.makeText(this, baseResp.errMsg + ":失败！！", Toast.LENGTH_LONG).show();
            break;
        }

	}

}
