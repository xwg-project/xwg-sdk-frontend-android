package com.example.sdk.dsbridge;

import android.webkit.JavascriptInterface;

import org.json.JSONException;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/21
 * Time: 11:35
 */
public class JsEchoApi {

    @JavascriptInterface
    public Object syn(Object args) throws JSONException {
        return  args;
    }

    @JavascriptInterface
    public void asyn(Object args, CompletionHandler handler){
        handler.complete(args);
    }
}