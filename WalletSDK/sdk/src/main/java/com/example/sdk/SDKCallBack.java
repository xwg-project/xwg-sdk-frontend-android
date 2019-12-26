package com.example.sdk;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/21
 * Time: 10:12
 */
public interface SDKCallBack {

    int INIT_SUCCESS = 0;
    int INIT_FAILURE = -1;

    int LOGIN_SUCCESS = 0;
    int LOGIN_FAILURE = -1;
    int LOGIN_CANCEL = -2;

    int PAY_SUCCESS = 0;
    int PAY_FAILURE = -1;
    int PAY_CANCEL = -2;

    int LOGOUT_SUCCESS = 0;
    int LOGOUT_FAILURE = -1;
    int LOGOUT_CANCEL = -2;
    void onInit(int code,String msg);
    void onLogin(int code, String msg);
    void onPay(int code,String msg);
    void onLogout(int code,String msg);
    void onLoginUserName(int code,String msg);
}
