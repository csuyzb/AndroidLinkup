package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;

/**
 * 不聚集
 * 
 * @author yzb
 * 
 */
class AlignNone extends AlignBase {

    public AlignNone(Piece[][] pieces) {
        super(pieces);
    }

    @Override
    public void Translate(Piece p1, Piece p2) {

    }

}
