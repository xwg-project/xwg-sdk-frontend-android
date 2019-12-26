package com.example.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/10/23
 * Time: 9:41
 */
public class SPUtils {
    private static SPUtils _spUtil;
    private SharedPreferences sp;
    private static final String key = "GameSDK";
    private SPUtils(){};

    public static SPUtils getInstance() {
        if(_spUtil == null) {
            _spUtil = new SPUtils();
        }

        return _spUtil;
    }

    public void init(Context context) {
        sp = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);
    }
    public void put(String key, Object object) {
    if (null == sp) return;
    SharedPreferences.Editor editor = sp.edit();

    if (object instanceof String) {
        editor.putString(key, (String) object);
    } else if (object instanceof Integer) {
        editor.putInt(key, (Integer) object);
    } else if (object instanceof Boolean) {
        editor.putBoolean(key, (Boolean) object);
    } else if (object instanceof Float) {
        editor.putFloat(key, (Float) object);
    } else if (object instanceof Long) {
        editor.putLong(key, (Long) object);
    } else {
        editor.putString(key, object.toString());
    }

    editor.commit();
}
    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */

    public Object get(String key, Object defaultObject) {
        if (null == sp) return null;
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }
    /**
     * 移除某个key值已经对应的值
     */

    public void remove(String key) {
        if (null == sp) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */

    public void clear() {
        if (null == sp) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
