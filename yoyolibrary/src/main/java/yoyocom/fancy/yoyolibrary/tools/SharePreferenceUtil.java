package yoyocom.fancy.yoyolibrary.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fancy on 2015/8/31.
 */
public class SharePreferenceUtil {

    public static final String SHAREKEY = "zrx";

    public static void remove(String key, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREKEY,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean contain(String key, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREKEY,
                0);
        return sharedPreferences.contains(key);
    }

    public static void save(String key, String value, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREKEY,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void save(String key, String value, Context mContext,String tag) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringValue(String key, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREKEY,
                0);
        return sharedPreferences.getString(key, "");
    }

    public static String getStringValue(String key, Context mContext,String tag) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag,
                0);
        return sharedPreferences.getString(key, "");
    }

    public static void saveInt(String key, int value, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREKEY,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveInt(String key, int value, Context mContext,String tag) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(String key, Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREKEY,
                0);
        return sharedPreferences.getInt(key, 0);
    }

    public static int getIntValue(String key, Context mContext,String tag) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag,
                0);
        return sharedPreferences.getInt(key, 0);
    }
}
