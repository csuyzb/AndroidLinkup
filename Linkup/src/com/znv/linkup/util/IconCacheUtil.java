package com.znv.linkup.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * 缓存用户的图标
 * 
 * @author yzb
 * 
 */
public class IconCacheUtil {

    public static Map<String, Bitmap> icons = new HashMap<String, Bitmap>();

    /**
     * 获取图标
     * 
     * @param url
     *            icon的url
     * @return 用户图标
     */
    public static Bitmap getIcon(String url) {
        if (icons.containsKey(url)) {
            return icons.get(url);
        }
        return null;
    }

    /**
     * 缓存用户图标
     * 
     * @param url
     *            icon的url
     * @param bm
     *            用户图标
     */
    public static void putIcon(String url, Bitmap bm) {
        if (!url.equals("") && bm != null) {
            icons.put(url, bm);
        }
    }

}
