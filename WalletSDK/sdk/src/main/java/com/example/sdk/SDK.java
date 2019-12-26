package com.example.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.sdk.dsbridge.JsApi;
import com.example.sdk.dsbridge.NativeCallJSActivity;

import com.example.sdk.floatwindows.FloatBallManager;
import com.example.sdk.floatwindows.floatball.FloatBallCfg;
import com.example.sdk.utils.ActivityManagerUtils;
import com.example.sdk.utils.BackGroudSeletor;
import com.example.sdk.utils.DensityUtil;
import com.example.sdk.utils.LogUtils;
import com.example.sdk.utils.SPUtils;
import com.example.sdk.utils.ResUtil;
import com.example.sdk.utils.WindowUtils;
import com.example.sdk.utils.permission.FloatPermissionManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Date;

import com.example.sdk.dsbridge.DWebView;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/21
 * Time: 10:11
 */
public class SDK {
    private Activity mContext;
    private FloatBallManager mFloatballManager;
    private static volatile SDK mInstance;
    public boolean isDebug = false;
    private SDKCallBack mCallBack;
    private SDKCache mCache = SDKCache.getInstance();
    private WindowManager windowManager;
    private DWebView web;
    private FloatPermissionManager mFloatPermissionManager;
    private ActivityLifeCycleListener mActivityLifeCycleListener = new ActivityLifeCycleListener();
    private int resumed;

    public static SDK getInstance() {
        if (null == mInstance) {
            synchronized (SDK.class) {
                if (null == mInstance) {
                    mInstance = new SDK();
                }
            }
        }
        return mInstance;
    }

    public void init(Activity context,String appid ,SDKCallBack callBack) {

        if (mCache.isInit) {
            LogUtils.e("已经初始化，请不要重复操作");
            show("已经初始化，请不要重复操作");
            return;
        }
        if (mCache.isIniting) {
            show("正在初始化，请不要重复操作");
            LogUtils.e("正在初始化，请不要重复操作");
            return;
        }
        LogUtils.e("SDK初始化开始");
        mCache.isIniting = true;
        mContext = context;
        mCache.appid=appid;
        mCallBack = callBack;
        mCache.mCallBack = callBack;

        ActivityManagerUtils.getAppManager().addActivity(context);
        //初始化ResUtils，用来在SDK中使用布局和id
        initFloatWindow();
        initConfig();
        WindowUtils.showInitWindow(mContext);
    }


    private void initFloatWindow() {
        // 1.初始化悬浮窗
        LogUtils.e("开始初始化悬浮球");
        int ballSize = DensityUtil.dip2px(mContext, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", mContext);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_CENTER);
        mFloatballManager = new FloatBallManager(mContext.getApplication(), ballCfg);
//        setFloatPermission();
        if (mFloatballManager.getMenuItemSize() == 0) {
            mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {
                    openWebView("openWindow");
                    LogUtils.e("悬浮球被点击了");
                }
            });
        }
        mContext.getApplication().registerActivityLifecycleCallbacks(mActivityLifeCycleListener);
        LogUtils.e("悬浮球初始化完毕");
    }

    private void setFloatPermission() {
        //设置悬浮球权限，用于申请悬浮球权限的，这里用的是别人写好的库，可以自己选择
        //如果不设置permission，则不会弹出悬浮球
        mFloatPermissionManager = new FloatPermissionManager();
        mFloatballManager.setPermission(new FloatBallManager.IFloatBallPermission() {
            @Override
            public boolean onRequestFloatBallPermission() {
                LogUtils.e("onRequestFloatBallPermission -----------");
                requestFloatBallPermission(mContext);
                return true;
            }

            @Override
            public boolean hasFloatBallPermission(Context context) {
                LogUtils.e("hasFloatBallPermission -----------");
                return mFloatPermissionManager.checkPermission(context);
            }

            @Override
            public void requestFloatBallPermission(Activity activity) {
                LogUtils.e("requestFloatBallPermission -----------");
                mFloatPermissionManager.applyPermission(activity);
            }

        });
    }

    private void initConfig() {
        LogUtils.e("开始初始化 initConfig");
        ResUtil.init(mContext);
        SPUtils.getInstance().init(mContext);
        LogUtils.e("initConfig 初始化完毕 ");
    }

    public void initOver() {
        LogUtils.e("关闭WebView");
        WindowUtils.removeWindow();
    }

    public void login(Activity context) {
        mContext = context;
        if (!mCache.isInit || mCache.isIniting) {
            show("SDK未初始化或正在初始化，请在初始化成功后再进行登录");
            LogUtils.e("SDK未初始化或正在初始化，请在初始化成功后再进行登录");
            return;
        }
        if (mCache.isLogining) {
            //未初始化
//            mCallBack.onLogin(SDKCallBack.LOGIN_FAILURE,"SDK未初始化，请在初始化成功后再进行登录");
            show("SDK正在登录，请不要重复登录");
            LogUtils.e("SDK正在登录，请不要重复登录");
            return;
        }
        LogUtils.e("SDK开始登录");
        mCache.isLogining = true;
        openWebView("login");
    }

    public void loginAuth(Activity context,String authCode) {
        LogUtils.e("SDK开始登录----loginAuth");
        mContext = context;
        mCache.authcode=authCode;
//        openWebView("goLogin");
        mCache.activity.callJS("goLogin");
    }

    public void pay(Activity context, String orderID) {
        mCache.orderid = orderID;
        mContext = context;
        if (!mCache.isInit || mCache.isIniting) {
            //未初始化
//            mCallBack.onPay(SDKCallBack.PAY_FAILURE,"SDK未初始化");
            show("SDK未初始化或正在初始化，无法进行支付操作");
            LogUtils.e("SDK未初始化或正在初始化，无法进行支付操作");
            return;
        }
        if (mCache.isPaying) {
            //正在支付
            show("SDK正在支付，请不要重复操作");
            LogUtils.e("SDK正在支付，请不要重复操作");
            return;
        }
        LogUtils.e("SDK开始支付");
        mCache.isPaying = true;
        openWebView("pay");
    }

    public void logout(Activity context) {
        mContext = context;
        if (!mCache.isInit || mCache.isIniting) {
            //
//            mCallBack.onPay(SDKCallBack.PAY_FAILURE,"SDK未初始化");
            show("SDK未初始化或正在初始化，无法进行退出操作");
            return;
        }
        if (!mCache.isLogin || mCache.isLogining) {
            //未登录
//            mCallBack.onPay(SDKCallBack.PAY_FAILURE,"SDK未登录");
            show("SDK未登录或正在登录，无法进行退出操作");
            return;
        }
        LogUtils.e("SDK开始退出");
        openWebView("logout");
    }


    private void openWebView(String tag) {
        LogUtils.e("准备跳转WebView界面，" + tag);
        Intent webIntent = new Intent(mContext, NativeCallJSActivity.class);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webIntent.setAction(tag);
        mContext.startActivity(webIntent);
        LogUtils.e("跳转WebView界面完毕，" + tag);
    }


    public void showFloatWindow() {
        if (!mCache.isShowFloat) return;

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("显示悬浮窗调用 +" + Thread.currentThread());
                mFloatballManager.show();
                LogUtils.e("显示悬浮窗调用");
            }
        });

    }

    public void hideFloatWindow() {
        if (!mCache.isShowFloat) return;
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("隐藏悬浮窗调用" + Thread.currentThread());
                mFloatballManager.hide();
                LogUtils.e("隐藏悬浮窗调用");
            }
        });


    }


    public void show(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public boolean isApplicationInForeground() {
        return resumed > 0;
    }

    public class ActivityLifeCycleListener implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            ++resumed;
            LogUtils.e("onActivityResumed +"+activity);
            if (activity.getClass().equals(NativeCallJSActivity.class)){
                return;
            }
            showFloatWindow();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            --resumed;
            LogUtils.e("onActivityPaused +"+activity);
            if (activity.getClass().equals(NativeCallJSActivity.class)){
                return;
            }
            if (!isApplicationInForeground()) {
                hideFloatWindow();
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtils.e("onActivityDestroyed +" + (activity == mContext) + "/");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
    }
}
