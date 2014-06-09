package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.config.GameAlign;

/**
 * 游戏卡片聚集的处理类
 * 
 * @author yzb
 * 
 */
public class AlignContext {
    private AlignBase gAlign = null;
    private Piece[][] pieces;

    public AlignContext(Piece[][] pieces, GameAlign align) {
        this.pieces = pieces;
        this.gAlign = CreateAlign(align);
    }

    /**
     * 根据消除的卡片实现变换
     * 
     * @param p1
     *            消除的卡片1
     * @param p2
     *            消除的卡片2
     */
    public void Translate(Piece p1, Piece p2) {
        if (gAlign != null) {
            gAlign.Translate(p1, p2);
        }
    }

    /**
     * 根据游戏对齐方式创建聚集类对象
     * 
     * @param align
     *            对齐方式
     * @return 聚集基类对象
     */
    private AlignBase CreateAlign(GameAlign align) {
        AlignBase gAlign = null;
        switch (align) {
        case AlignDown:
            gAlign = new AlignDown(pieces);
            break;
        case AlignLeft:
            gAlign = new AlignLeft(pieces);
            break;
        case AlignUp:
            gAlign = new AlignUp(pieces);
            break;
        case AlignRight:
            gAlign = new AlignRight(pieces);
            break;
        default:
            gAlign = new AlignNone(pieces);
            break;
        }
        return gAlign;
    }
}
