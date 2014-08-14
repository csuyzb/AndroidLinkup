package com.znv.linkup.core.config;

/**
 * 游戏聚集对齐方式
 * 
 * @author yzb
 * 
 */
public enum GameAlign {
    AlignNone(0), AlignDown(1), AlignUp(2), AlignLeft(3), AlignRight(4), AlignRandom(5);

    private int value;

    GameAlign(int value) {
        this.value = value;
    }

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
        case 5:
            return AlignRandom;
        default:
            return AlignNone;
        }
    }

    public int value() {
        return this.value;
    }
}
