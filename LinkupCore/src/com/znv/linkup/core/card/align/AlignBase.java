package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;

/**
 * 卡片聚集的基类
 * 
 * @author yzb
 * 
 */
abstract class AlignBase {

    protected Piece[][] pieces;

    public AlignBase(Piece[][] pieces) {
        this.pieces = pieces;
    }

    /**
     * 消除卡片后的变换
     * 
     * @param p1
     *            消除的卡片1
     * @param p2
     *            消除的卡片2
     */
    public abstract void Translate(Piece p1, Piece p2);

    /**
     * 交换卡片信息
     * 
     * @param p1
     *            卡片1
     * @param p2
     *            卡片2
     */
    protected void ExchangePiece(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return;
        }

        p1.exchange(p2);
    }
}
