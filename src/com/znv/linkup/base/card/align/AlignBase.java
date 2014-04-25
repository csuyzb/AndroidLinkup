package com.znv.linkup.base.card.align;

import com.znv.linkup.base.card.Piece;

public abstract class AlignBase {

    protected Piece[][] pieces;

    public AlignBase(Piece[][] pieces) {
        this.pieces = pieces;
    }

    public abstract void Translate(Piece p1, Piece p2);

    protected void ExchangePiece(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return;
        }

        p1.exchange(p2);
    }
}
