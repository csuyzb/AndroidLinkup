package com.znv.linkup.core.config;

/**
 * 游戏模式
 * 
 * @author yzb
 * 
 */
public enum GameMode {
    Level, Time, Task;

    public static GameMode valueOf(int mode) {
        switch (mode) {
        case 1:
            return Time;
        case 2:
            return Task;
        default:
            return Level;
        }
    }
}
