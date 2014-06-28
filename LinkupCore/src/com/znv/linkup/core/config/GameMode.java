package com.znv.linkup.core.config;

/**
 * 游戏模式
 * 
 * @author yzb
 * 
 */
public enum GameMode {
    Level, Time, ScoreTask, TimeTask, Star, Endless;

    public static GameMode valueOf(int mode) {
        switch (mode) {
        case 1:
            return Time;
        case 2:
            return ScoreTask;
        case 3:
            return TimeTask;
        case 4:
            return Star;
        case 5:
            return Endless;
        default:
            return Level;
        }
    }
}
