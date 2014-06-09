package com.znv.linkup.core.config;

/**
 * 游戏聚集对齐方式
 * 
 * @author yzb
 * 
 */
public enum GameAlign {
    AlignNone, AlignDown, AlignUp, AlignLeft, AlignRight, AlignRandom;

    public static GameAlign valueOf(int align) {
        switch (align) {
        case 0:
            return AlignNone;
        case 1:
            return AlignDown;
        case 2:
            return AlignUp;
        case 3:
            return AlignLeft;
        case 4:
            return AlignRight;
        default:
            return AlignNone;
        }
    }
}
