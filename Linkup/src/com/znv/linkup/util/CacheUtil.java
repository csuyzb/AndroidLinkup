package com.znv.linkup.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 处理SharedPreferences的缓存帮助类
 * 
 * @author yzb
 * 
 */
public class CacheUtil {

    /**
     * 用share preference来实现是否绑定的开关
     * 
     * @param context
     * @return
     */
    public static boolean hasBind(Context context) {
        return hasBind(context, "push_flag");
    }

    /**
     * 设置是否绑定
     * 
     * @param context
     * @param flag
     *            是否绑定
     */
    public static void setBind(Context context, boolean flag) {
        setBind(context, "push_flag", flag);
    }

    /**
     * 获取是否绑定特定字符串
     * 
     * @param context
     * @param bindStr
     *            绑定的字符串key
     * @return 是否绑定
     */
    public static boolean hasBind(Context context, String bindStr) {
        String strValue = getBindStr(context, bindStr);
        if ("ok".equalsIgnoreCase(strValue)) {
            return true;
        }
        return false;
    }

    /**
     * 设置字符串是否绑定
     * 
     * @param context
     * @param bindStr
     *            绑定字符串key
     * @param flag
     *            是否绑定
     */
    public static void setBind(Context context, String bindStr, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        setBindStr(context, bindStr, flagStr);
    }

    /**
     * 获取绑定字符串的值
     * 
     * @param context
     * @param bindStr
     *            绑定字符串
     * @return 绑定的字符串值
     */
    public static String getBindStr(Context context, String bindStr) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(bindStr, "");
    }

    /**
     * 设置绑定的字符串键值对
     * 
     * @param context
     * @param bindStr
     *            绑定的字符串key
     * @param strValue
     *            绑定的字符串value
     */
    public static void setBindStr(Context context, String bindStr, String strValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(bindStr, strValue);
        editor.commit();
    }
}
