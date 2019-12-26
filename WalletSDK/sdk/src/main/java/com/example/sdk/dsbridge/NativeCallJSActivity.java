package com.example.sdk.dsbridge;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.sdk.SDK;
import com.example.sdk.SDKCache;
import com.example.sdk.SDKCallBack;
import com.example.sdk.utils.ActivityManagerUtils;
import com.example.sdk.utils.LogUtils;
import com.example.sdk.utils.ResUtil;
import com.example.sdk.utils.UrlParameter;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/21
 * Time: 11:34
 */
public class NativeCallJSActivity extends Activity {

    private DWebView mDWebView;
    public static NativeCallJSActivity instance = null;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        SDKCache.getInstance().activity=this;
        setContentView(ResUtil.getLayout(this, "activity_js_call_native"));
        mAction = getIntent().getAction();
        SDKCache.getInstance().currentAction=mAction;
        // set debug mode
        ActivityManagerUtils.getAppManager().addActivity(this);
        initWebView();
    }

    private void initWebView(){
        mDWebView.setWebContentsDebuggingEnabled(SDK.getInstance().isDebug);
        mDWebView = (DWebView) findViewById(ResUtil.getId(this, "webview"));
        mDWebView.getSettings().setUseWideViewPort(true);
        mDWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mDWebView.getSettings().setLoadWithOverviewMode(true);
        mDWebView.addJavascriptObject(new JsApi(), null);
        mDWebView.addJavascriptObject(new JsEchoApi(), "echo");
        mDWebView.getSettings().setDomStorageEnabled(true);
        mDWebView.loadUrl(UrlParameter.getInstance().SDK_BASE_URL);//外网
//        mDWebView.loadUrl("http://soft.imtt.qq.com/browser/tes/feedback.html");//检测是否使用了x5
//        mDWebView.loadUrl("http://debugtbs.qq.com");//tbs调试界面
        mDWebView.setWebViewClient(new WebViewEmptyViewClient(this));
    }



    /**
     * android 调用Javascript API
     *
     * @param action
     */
    public void callJS(String action) {
        switch (action) {
            case "init":
                mDWebView.callHandler(action, new Object[]{});
                break;
            case "getLoginCode":
                mDWebView.callHandler(action, new Object[]{"1561527878813", "e31ac1bb84934aaa9e46d5daf08301d9",SDKCache.getInstance().username});
                break;
            case "login":
                mDWebView.callHandler(action, new Object[]{});
                break;
            case "goLogin":
                LogUtils.e("callJS    goLogin +1-----"+SDKCache.getInstance().authcode);
                mDWebView.callHandler(action, new Object[]{SDKCache.getInstance().authcode});
                LogUtils.e("callJS    goLogin +2-------"+SDKCache.getInstance().authcode);
                break;
            case "pay":
                mDWebView.callHandler(action, new Object[]{SDKCache.getInstance().orderid});
                break;
            case "logout":
                mDWebView.callHandler(action,new Object[]{});
                break;
            case "openWindow":
                mDWebView.callHandler(action, new Object[]{});
                break;
        }
    }


    @Override
    protected void onResume() {
        if (mDWebView != null) {
            //恢复pauseTimers状态
            mDWebView.resumeTimers();
            //激活WebView为活跃状态，能正常执行网页的响应
            mDWebView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mDWebView != null) {
            //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
            mDWebView.pauseTimers();
            //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
            //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
            mDWebView.onPause();
        }
        super.onPause();
    }

    /**
     * WebView内存泄漏修复，如果用户关闭了webview界面，js 不会给到结果，需要自己判断
     */
    @Override
    protected void onDestroy() {

        if (SDKCache.getInstance().isIniting) {
            SDKCache.getInstance().isIniting = false;
            SDKCache.getInstance().mCallBack.onInit(SDKCallBack.INIT_FAILURE, "用户取消了初始化");
        }
        if (SDKCache.getInstance().isLogining) {
            SDKCache.getInstance().isLogining = false;
            SDKCache.getInstance().mCallBack.onLogin(SDKCallBack.LOGIN_CANCEL, "用户取消了登录");
        }
        if (SDKCache.getInstance().isPaying) {
            SDKCache.getInstance().isPaying = false;
            SDKCache.getInstance().mCallBack.onPay(SDKCallBack.PAY_CANCEL, "用户取消了支付");
        }


        if (mDWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mDWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mDWebView);
            }

            mDWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mDWebView.getSettings().setJavaScriptEnabled(false);
            mDWebView.clearHistory();
            mDWebView.clearView();
            mDWebView.removeAllViews();
            try {
                mDWebView.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }
}
