package com.znv.linkup.util;

/**
 * 字符串处理帮助类
 * 
 * @author yzb
 * 
 */
public class StringUtil {

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
        if (str == null) {
            return "";
        }
        byte[] strByte = str.getBytes();
        int strLen = strByte.length;
        if (len >= strLen || len < 1) {
            return str;
        }
        int count = 0;
        for (int i = 0; i < len; i++) {
            int value = (int) strByte[i];
            if (value < 0) {
                count++;
            }
        }
        if (count % 2 != 0) {
            len = (len == 1) ? len + 1 : len - 1;
        }
        return new String(strByte, 0, len) + more.trim();
    }
}
