package com.example.sdk.utils;

import com.example.sdk.SDKCache;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/12/2
 * Time: 14:38
 */
public class UrlParameter {
    //测试用
//    public static String  SDK_BASE_URL = "http://192.168.56.101/wallet/index.html";
//    public static String  SDK_FINISHED_URL = "http://192.168.56.101/wallet/index.html#/";

    //正式用
    public  String  SDK_BASE_URL = "http://192.168.1.115:3000/index.html";
    public  String  SDK_FINISHED_URL = "http://192.168.1.115:3000/index.html#/";
    private volatile static UrlParameter mInstance;
    public static UrlParameter getInstance(){
        if(null == mInstance){
            synchronized (SDKCache.class){
                if(null == mInstance){
                    mInstance = new UrlParameter();
                }
            }
        }
        return mInstance;
    }
}
