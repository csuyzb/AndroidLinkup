package com.znv.linkup.base.card.align;

import com.znv.linkup.base.card.Piece;

public class AlignContext {
    private AlignBase gAlign = null;
    private Piece[][] pieces;

    public AlignContext(Piece[][] pieces, GameAlign align) {
        this.pieces = pieces;
        this.gAlign = CreateAlign(align);
    }

    public void Translate(Piece p1, Piece p2) {
        if (gAlign != null) {
            gAlign.Translate(p1, p2);
        }
    }

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
