package com.asyn;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.bean.UserBean;
import com.parser.Userparser;

import android.os.AsyncTask;
import android.widget.TextView;



public class GetUserInfoTask extends AsyncTask<String, Void, Boolean> {

	private HttpClient httpClient;
    private TextView userInfo;
    private String result = "";

    public GetUserInfoTask(TextView userInfo) {
        this.userInfo = userInfo;
    }

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
                System.out.println("result = " + result); 
              
                inStream.close();//关闭数据输入流
            }
            outStream.close();//关闭数据输出流
            conn.disconnect();//关闭远程连接
            httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[1]);
            List<NameValuePair> par = new ArrayList<NameValuePair>();
            par.add(new BasicNameValuePair("status", result));
            /* 添加请求参数到请求对象 */
            post.setEntity(new UrlEncodedFormEntity(par, HTTP.UTF_8));
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = httpClient.execute(post);
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /* 读返回数据 */
                System.out.println("Success");

            } else {
                System.out.println("Failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGetInfo;
    }

    @Override
    protected void onPostExecute(Boolean isGetInfo) {
        super.onPostExecute(isGetInfo);
        if (isGetInfo) {
            Userparser userparser = new Userparser();
            UserBean bean = userparser.userInfoParser(result);
            userInfo.setText(bean.toString());
        } else {
            userInfo.setText("false --- "); 
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}