package com.neteasecloud.componentlib.utils;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * SystemPropertiesProxy
 */
public class SystemPropertiesProxy {

    public static String get(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, defaultValue));
            Log.e("SystemPropertiesProxy", "get key =" + key + " value =" + value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SystemPropertiesProxy", "get==" + e);
        } finally {

        }
        return value;
    }

    public static void set(String key, String value) {
        try {
            Log.e("SystemPropertiesProxy", "set key =" + key + " value =" + value);
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SystemPropertiesProxy", "set==" + e);
        }
    }

    public static int getInt(String key, int defaultValue) {
        int value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("getInt", String.class, Integer.class);
            value = (Integer) (get.invoke(c, key, defaultValue));
            Log.e("SystemPropertiesProxy", "getInt key =" + key + " value =" + value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SystemPropertiesProxy", "getInt ==" + e);
        } finally {

        }
        return value;
    }

    public static long getLong(String key, long defaultValue) {
        long value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("getLong", String.class, long.class);
            value = (Long) (get.invoke(c, key, defaultValue));
            Log.e("SystemPropertiesProxy", "getLong key =" + key + " value =" + value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SystemPropertiesProxy", "getLong ==" + e);
        } finally {

        }
        return value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("getLong", String.class, Boolean.class);
            value = (Boolean) (get.invoke(c, key, defaultValue));
            Log.e("SystemPropertiesProxy", "getBoolean key =" + key + " value =" + value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SystemPropertiesProxy", "getBoolean ==" + e);
        } finally {

        }
        return value;
    }
}
