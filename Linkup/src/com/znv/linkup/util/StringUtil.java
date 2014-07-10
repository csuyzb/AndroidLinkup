package com.znv.linkup.util;

import android.util.Log;

/**
 * 字符串处理帮助类
 * 
 * @author yzb
 * 
 */
public class StringUtil {

    /**
     * 转为UTF-8编码
     * 
     * @param str
     *            字符串
     * @return utf-8编码字符串
     */
    public static String toUtf8(String str) {
        try {
            return new String(str.getBytes("ISO8859_1"), "UTF-8");
        } catch (Exception e) {
            Log.d("StringUtil-toUtf8", e.getMessage());
            return str;
        }
    }

    /**
     * 将时间秒数转化为格式mm:ss
     * 
     * @param seconds
     *            时间秒数
     * @return 时间格式字符串
     */
    public static String secondToString(int seconds) {
        int min = seconds / 60;
        int second = seconds % 60;

        StringBuffer sb = new StringBuffer();
        if (min > 9) {
            sb.append(String.valueOf(min));
        } else {
            sb.append("0" + String.valueOf(min));
        }

        if (second > 9) {
            sb.append(":" + String.valueOf(second));
        } else {
            sb.append(":0" + String.valueOf(second));
        }

        return sb.toString();
    }

    public static String substring(String str, int toCount) {
        return substring(str, toCount, "..");
    }

    /**
     * 获取符合长度的字符串
     * 
     * @param str
     *            需要控制长度的字符串
     * @param toCount
     *            长度
     * @param more
     *            超过长度字符
     * @return 符合长度的字符串
     */
    public static String substring(String str, int len, String more) {
        if (str == null || "".equals(str) || len < 1) {
            return "";
        }
        char[] chars = str.toCharArray();
        int count = 0;
        int charIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            int charLength = getCharLen(chars[i]);
            if (count <= len - charLength) {
                count += charLength;
                charIndex++;
            } else {
                break;
            }
        }
        if (charIndex == chars.length) {
            return new String(chars, 0, charIndex);
        } else {
            return new String(chars, 0, charIndex) + more;
        }
    }

    /**
     * 获取字符长度
     * 
     * @param c
     *            字符
     * @return 长度
     */
    private static int getCharLen(char c) {
        int k = 0x80;
        return c / k == 0 ? 1 : 2;
    }
}
