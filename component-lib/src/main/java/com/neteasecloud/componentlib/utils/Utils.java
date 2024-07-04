package com.neteasecloud.componentlib.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public final class Utils {
    public static final String TAG = Utils.class.getSimpleName();
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param context context
     */
    public static void init(final Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());
    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        }
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * 根据值的类型调用对应的方法保存数据
     *
     * @param key key
     * @param obj value
     *            <p>
     *            /data/system/users/0/settings_system.xml
     *            adb shell settings get system xxx
     */
    public static void put(String key, Object obj) {
        ContentResolver resolver = Utils.getApp().getContentResolver();
        if (obj instanceof String) {
            Settings.System.putString(resolver, key, (String) obj);
        } else if (obj instanceof Integer) {
            Settings.System.putInt(resolver, key, (Integer) obj);
        } else if (obj instanceof Boolean) {
            Settings.System.putInt(resolver, key, (Boolean) obj ? TRUE : FALSE);
        } else if (obj instanceof Float) {
            Settings.System.putFloat(resolver, key, (Float) obj);
        } else if (obj instanceof Long) {
            Settings.System.putLong(resolver, key, (Long) obj);
        } else {
            Log.w(TAG, "put key = " + key + " value = " + obj + " not save");
        }
    }

    /**
     * 根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return value
     */
    public static Object get(String key, Object defaultValue) {
        Object value = defaultValue;
        ContentResolver resolver = Utils.getApp().getContentResolver();
        try {
            if (defaultValue instanceof String) {
                String getValue = Settings.System.getString(resolver, key);
                if (!TextUtils.isEmpty(getValue)) {
                    value = getValue;
                }
            } else if (defaultValue instanceof Integer) {
                value = Settings.System.getInt(resolver, key, (Integer) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                value = Settings.System.getInt(resolver, key) == TRUE;
            } else if (defaultValue instanceof Float) {
                value = Settings.System.getFloat(resolver, key, (Float) defaultValue);
            } else if (defaultValue instanceof Long) {
                value = Settings.System.getLong(resolver, key, (Long) defaultValue);
            } else {
                Log.w(TAG, "not get key = " + key + " defaultValue = " + defaultValue);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return value;
    }

}
