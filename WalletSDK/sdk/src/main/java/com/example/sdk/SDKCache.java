package com.example.sdk;

import com.example.sdk.dsbridge.NativeCallJSActivity;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/21
 * Time: 13:28
 */
public class SDKCache {
    public SDKCallBack mCallBack;

    public boolean isInit = false;//是否初始化
    public boolean isIniting = false;//是否正在初始化
    public boolean isLogin = false;//是否已登录
    public boolean isLogining = false;//是否正在登录
    public boolean isPaying = false;//是否正在支付
    public boolean isShowFloat=false;//是否显示悬浮球

    public String appid = "";//APPID
    public String orderid = "";//ORDERID
    public String username = "";//用户名
    public String authcode = "";//授权码
    public String currentAction = "";//当前状态
    private volatile static SDKCache mInstance;
    public NativeCallJSActivity  activity;
    public static SDKCache getInstance(){
        if(null == mInstance){
            synchronized (SDKCache.class){
                if(null == mInstance){
                    mInstance = new SDKCache();
                }
            }
        }
        return mInstance;
    }
}
