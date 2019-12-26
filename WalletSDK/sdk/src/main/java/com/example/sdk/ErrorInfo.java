package com.example.sdk;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/11/11
 * Time: 10:19
 */
public class ErrorInfo {

    public static String SDK_NO_INIT = "SDK未初始化或正在初始化";
    public static String SDK_INITING = "SDK正在初始化";
    public static String SDK_INITED = "SDK已经初始化，不要重复操作";
    public static String SDK_NO_LOGIN = "SDK未登录或正在登录";
    public static String SDK_LOGINING = "SDK正在登录";
    public static String SDK_LOGINED = "SDK已经登录，不要重复操作";
    public static String SDK_PAYING = "SDK正在支付，不要重复操作";

    // 成功
    public final static int SUCCESS = 0;

    // 未初始化
    public final static int NO_INIT = -1040;

    // 未登录
    public final static int NO_LOGIN = -1041;

    // 不可重复初始化
    public final static int REPETITION_INIT = -1042;

    // 不可重复登录
    public final static int REPETITION_LOGIN = -1043;

    // 网络异常
    public final static int NET_ERROR = -1044;

    // 取消
    public final static int CANCEL = -1045;

    // 失败
    public final static int FAIL = -1046;

}
