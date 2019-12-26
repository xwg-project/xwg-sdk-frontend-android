package com.example.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.WindowManager;

import com.example.sdk.SDKCache;
import com.example.sdk.SDKCallBack;
import com.example.sdk.dsbridge.DWebView;
import com.example.sdk.dsbridge.JsApi;
import com.example.sdk.dsbridge.JsEchoApi;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WindowUtils {
    private static DWebView web = null;
    private static WindowManager mWindowManager = null;
    private static SDKCache mCache = SDKCache.getInstance();

    public static void showInitWindow(final Context context) {
        LogUtils.e("开始初始化WebView" + new Date());
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams winParams = new WindowManager.LayoutParams();
        winParams.height = 2;
        winParams.width = 2;
        winParams.gravity = Gravity.CENTER;
        //控制布局区域外是否可以点击
//        winParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        winParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        web = new DWebView(context);
//        web.getSettings().setBlockNetworkImage(true);
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染的优先级

        web.addJavascriptObject(new JsApi(), null);
//        web.addJavascriptObject(new JsEchoApi(),"echo");
        web.loadUrl(UrlParameter.getInstance().SDK_BASE_URL);//外网
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                LogUtils.e("onPageStarted" + new Date());
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                LogUtils.e("onPageFinished:" + new Date());

            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                LogUtils.e("初始化失败了，可能存在网络问题");
                mCache.isIniting = false;
                mCache.mCallBack.onInit(SDKCallBack.INIT_FAILURE, errorCode + ":" + description);
                LogUtils.e("初始化失败了" + errorCode + ":" + description);
                removeWindow();
            }
        });
        mWindowManager.addView(web, winParams);

        JSONObject json=new JSONObject();
        try {
            json.put("appid",mCache.appid);
        } catch (JSONException e) {
            LogUtils.e("JSONException:"+e);
        }
        web.callHandler("init", new Object[]{ json,1});
        LogUtils.e("WebView初始化完毕，并添加到窗口" + new Date());

    }

    public static void removeWindow() {
        mWindowManager.removeView(web);
    }

}