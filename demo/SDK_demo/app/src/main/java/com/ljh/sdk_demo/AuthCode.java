package com.ljh.sdk_demo;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/11/20
 * Time: 10:58
 */
public class AuthCode {

    /**
     * code : 0
     * message : success
     * path :
     * data : O9BUYjGO
     * extra : {}
     * timestamp : 1574218769592
     */

    private int code;
    private String message;
    private String path;
    private String data;
    private ExtraBean extra;
    private String timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class ExtraBean {
    }
}
