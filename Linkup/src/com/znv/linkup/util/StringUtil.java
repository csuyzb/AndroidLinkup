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
}
