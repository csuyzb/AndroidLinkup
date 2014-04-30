package com.znv.linkup.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * 获取Application元数据信息的帮助类
 * 
 * @author yzb
 * 
 */
public class AppMetaUtil {
    /**
     * 获取Application中的meta-data，用于获取ApiKey
     * 
     * @param context
     *            context
     * @param metaKey
     *            metakey
     * @return metaValue
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return metaValue;
    }
}
