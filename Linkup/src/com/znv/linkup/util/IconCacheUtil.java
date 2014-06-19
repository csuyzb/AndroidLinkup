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
     * @param userId
     *            用户id
     * @return 用户图标
     */
    public static Bitmap getIcon(String userId) {
        if (icons.containsKey(userId)) {
            return icons.get(userId);
        }
        return null;
    }

    /**
     * 缓存用户图标
     * 
     * @param userId
     *            用户id
     * @param bm
     *            用户图标
     */
    public static void putIcon(String userId, Bitmap bm) {
        if (!userId.equals("") && bm != null) {
            icons.put(userId, bm);
        }
    }

}
