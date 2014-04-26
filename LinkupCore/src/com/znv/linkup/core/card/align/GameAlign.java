package com.znv.linkup.core.card.align;

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
