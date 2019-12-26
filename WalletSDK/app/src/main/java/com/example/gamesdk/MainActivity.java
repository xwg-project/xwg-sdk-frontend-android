package com.example.gamesdk;




import com.example.sdk.floatwindows.FloatBallManager;
import com.example.sdk.utils.LogUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdk.SDK;
import com.example.sdk.SDKCallBack;
import com.example.sdk.utils.UrlParameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

public class MainActivity extends Activity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private EditText editText;
    private EditText editText1;
    private EditText editText2;
    private String  orderid;
    private long touchTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SDK.getInstance().isDebug=true;
        LogUtils.e("初始化开始时间"+new Date());
//        SDK.getInstance().init(MainActivity.this, "1561527878813", new SDKCallBack() {//测试

        initView();
        initClickListener();

    }

    private void initClickListener() {

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlParameter.getInstance().SDK_BASE_URL=editText1.getText().toString();
                UrlParameter.getInstance().SDK_FINISHED_URL=editText1.getText().toString();
                SDK.getInstance().init(MainActivity.this, "1561527878813", new SDKCallBack() {//棋牌游戏参数

                    @Override
                    public void onInit(int code, String errmsg) {
                        show("onInit",code,errmsg);
                        LogUtils.e("onInit 执行了--"+code+ ":"+errmsg);
                    }

                    @Override
                    public void onLogin(int code, String accessToken) {
                        show("onLogin",code,accessToken);
                        LogUtils.e("onInit 执行了--"+code +":"+accessToken);
                    }

                    @Override
                    public void onPay(int code, String errmsg) {
                        show("onPay",code,errmsg);
                        LogUtils.e("onInit 执行了--"+code+ ":"+errmsg);
                    }

                    @Override
                    public void onLogout(int code, String errmsg) {
                        show("onLogout",code,errmsg);
                        LogUtils.e("onInit 执行了--"+code+ ":"+errmsg);
                    }

                    @Override
                    public void onLoginUserName(int code, String errmsg) {
                        //根据用户名和appid 去申请授权码，
                        LogUtils.e("onLoginUserName 执行了--"+code+ ":"+errmsg);
                SDK.getInstance().loginAuth(MainActivity.this,getAuthCode("1561527878813","e31ac1bb84934aaa9e46d5daf08301d9",errmsg));//测试
//                        SDK.getInstance().loginAuth(MainActivity.this,getAuthCode("1573459165647","6c94a0d846384ecabc0c464647f0f529",errmsg));//棋牌游戏参数
                    }
                });
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.getInstance().login(MainActivity.this);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("输入框 "+editText.getText().toString());
                SDK.getInstance().pay(MainActivity.this,editText.getText().toString());
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.getInstance().logout(MainActivity.this);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.getInstance().showFloatWindow();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDK.getInstance().hideFloatWindow();
            }
        });

    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        editText = findViewById(R.id.text);
        editText1 = findViewById(R.id.text1);
        editText2 = findViewById(R.id.text2);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //只有activity被添加到windowmanager上以后才可以调用show方法。
       SDK.getInstance().showFloatWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
       SDK.getInstance().hideFloatWindow();
    }

    public void show(String func,int code,String string){
        Toast.makeText(this,func+"回调结果 code:"+code+"---msg:"+ string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if((currentTime-touchTime)>=1200) {
            //让Toast的显示时间和等待时间相同
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        }else {
            System.exit(0);
        }
    }

    public String getAuthCode(String client_id,String client_secret,String username){
        //此处功能，游戏自己实现
//        String path = "http://18.138.229.12:8888/appauth/login/thirdAuthCode";
        String path = editText2.getText().toString();
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data = "client_id="+client_id+"&client_secret="+client_secret+"&username="+username;

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if(responseCode ==200){
                //请求成功
                InputStream is = connection.getInputStream();
                String  str=InputStream2String(is);
                Log.e("1", "getAuthCode: " +str);
                JSONObject jsonObject = new JSONObject(str);
                Log.e("1", "getAuthCode:jsonObject " +jsonObject);
                return jsonObject.getString("data");
            }else {
                //请求失败
                Log.e("1", "getAuthCode: null" );
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("1", "getAuthCode: MalformedURLException" +e.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e("1", "getAuthCode: ProtocolException" +e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("1", "getAuthCode: IOException" +e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("1", "getAuthCode: JSONException" +e.toString());
        }
        return null;
    }

    public String InputStream2String(InputStream in) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
