package com.example.sdk.dsbridge;

import android.os.CountDownTimer;
import android.webkit.JavascriptInterface;

import com.example.sdk.SDK;
import com.example.sdk.SDKCache;

import com.example.sdk.utils.ActivityManagerUtils;
import com.example.sdk.utils.LogUtils;
import com.example.sdk.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/21
 * Time: 11:29
 */
public class JsApi {

    private SDKCache mCache=SDKCache.getInstance();
    private JSONObject jsonObject;
    //所有Java API 必须有"@JavascriptInterface" 标注。
    /**
     *
     * 测试用
     * @param msg
     * @param handler
     */
    @JavascriptInterface
    public void testAsyn(Object msg, CompletionHandler<String> handler) {
        ActivityManagerUtils.getAppManager().finishActivity();
    }

    //所有Java API 必须有"@JavascriptInterface" 标注。
    /**
     *
     * js 通知原生 初始化结果
     * @param object
     */
    @JavascriptInterface
    public void onInit(Object object)  {
        mCache.isIniting=false;
        jsonObject= (JSONObject) object;
        LogUtils.e("*****************"+jsonObject.toString());
        int code = -1;
        String msg = null;
        try {
             code=jsonObject.getInt("code");
             msg=jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (code==0){ //初始化成功，
                mCache.isInit=true;
            }//isInit 默认为false，此处不需要修改

        if (code==0){ //服务器返回悬浮球开关状态为开
            mCache.isShowFloat=true;
        }
        LogUtils.e("JsApi调用"+ Thread.currentThread());
        SDK.getInstance().showFloatWindow();

        mCache.mCallBack.onInit(code,msg);
     SDK.getInstance().initOver();
    }


    //所有Java API 必须有"@JavascriptInterface" 标注。
    /**
     *
     * js 通知原生 登录结果
     * @param object
     */
    @JavascriptInterface
    public void getAuthCode(Object object)  {
        jsonObject= (JSONObject) object;
        LogUtils.e("*******getAuthCode**********"+jsonObject.toString());
        int code = -1;
        String msg = null;
        try {
            code=jsonObject.getInt("code");
            msg=jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        LogUtils.e("*******onLoginUserName**********1");
        mCache.mCallBack.onLoginUserName(code,msg);
//        LogUtils.e("*******onLoginUserName**********2");

//        mCache.username=msg;
//        mCache.activity.callJS("getLoginCode");
    }

//    @JavascriptInterface
//    public void onGetLoginCode(Object object)  {
//        jsonObject= (JSONObject) object;
//        LogUtils.e("*******onGetLoginCode**********"+jsonObject.toString());
//        int code = -1;
//        String msg = null;
//        try {
//            code=jsonObject.getInt("code");
//            msg=jsonObject.getString("msg");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        mCache.authcode=msg;
//        mCache.activity.callJS("goLogin");
//    }


       //所有Java API 必须有"@JavascriptInterface" 标注。
    /**
     *
     * js 通知原生 登录结果
     * @param object
     */
    @JavascriptInterface
    public void onLogin(Object object)  {
        mCache.isLogining=false;
        jsonObject= (JSONObject) object;
        int code = -1;
        String msg = null;
        try {
            code=jsonObject.getInt("code");
            msg=jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (code==0){
            //登录成功
            mCache.isLogin=true;
        }// isLogin  默认为false，此处不需要修改
        mCache.mCallBack.onLogin(code,msg);

    }

    //所有Java API 必须有"@JavascriptInterface" 标注。
    /**
     * js 通知原生 支付结果
     * @param object
     */
    @JavascriptInterface
    public void onPay(Object object)  {
        mCache.isPaying=false;
        jsonObject= (JSONObject) object;
        int code = -1;
        String msg = null;
        try {
            code=jsonObject.getInt("code");
            msg=jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCache.mCallBack.onPay(code,msg);
    }

    //所有Java API 必须有"@JavascriptInterface" 标注。
    /**
     * js 通知原生 退出结果
     * @param object
     */
    @JavascriptInterface
    public void onLogout(Object object)
    {
        jsonObject= (JSONObject) object;
        int code = -1;
        String msg = null;
        try {
            code=jsonObject.getInt("code");
            msg=jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();}
        if (code==0){
            mCache.isLogin=false;
        }
        mCache.mCallBack.onLogout(code,msg);
    }


    //所有Java API 必须有"@JavascriptInterface" 标注。


    /**
     * js 通知原生 关闭webview
     */
    @JavascriptInterface
    public void onCloseWindows(Object object) {
        LogUtils.e("*****************onCloseWindows 调用了");
        ActivityManagerUtils.getAppManager().finishActivity();
    }

    /**
     * js 通知原生 保存键值信息
     */
    @JavascriptInterface
    public void onSetKV( Object object) {
        jsonObject= (JSONObject) object;
        String key = "";
        Object defaultObject = null;
        try {
            key=jsonObject.getString("key");
            defaultObject=jsonObject.get("defaultObject");
        } catch (JSONException e) {
            e.printStackTrace();}

        LogUtils.e("key:"+key +"  defaultObject:"+defaultObject);
        SPUtils.getInstance().put(key,defaultObject);
    }

    /**
     * js 通知原生 读取键值信息
     */
    @JavascriptInterface
    public Object onGetKV(Object object) {
        jsonObject= (JSONObject) object;
        LogUtils.e("JSONObject:"+jsonObject.toString());
        String key = "";
        Object dftObj= null;
        Object rtnObj = null;
        try {
            key=jsonObject.getString("key");
            LogUtils.e("getString:"+key);
            if (jsonObject.has("defaultObject")){
                dftObj=jsonObject.getString("defaultObject");
                rtnObj= SPUtils.getInstance().get(key,dftObj);
            }else{
                rtnObj= SPUtils.getInstance().get(key,"");
            }
        } catch (JSONException e) {
            e.printStackTrace();}
        LogUtils.e("onGetKV:"+rtnObj);
        return rtnObj;
    }


    //所有Java API 必须有"@JavascriptInterface" 标注。
    @JavascriptInterface
    public void callProgress(Object args, final CompletionHandler<Integer> handler) {

        new CountDownTimer(11000, 1000) {
            int i=10;
            @Override
            public void onTick(long millisUntilFinished) {
                //setProgressData can be called many times util complete be called.
                handler.setProgressData((i--));
            }
            @Override
            public void onFinish() {
                //complete the js invocation with data; handler will be invalid when complete is called
                handler.complete(0);
            }
        }.start();
    }
}
