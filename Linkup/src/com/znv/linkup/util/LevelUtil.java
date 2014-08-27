package com.znv.linkup.util;

/**
 * 与关卡相关的帮助类
 * 
 * @author yzb
 * 
 */
public class LevelUtil {

    /**
     * 是否是时间模式
     * 
     * @param level
     *            关卡序号
     * @return 时间模式:true
     */
    public static boolean isTimeMode(int level) {
        return (level >= 120 && level < 192) || (level >= 264 && level < 408);
    }
}
