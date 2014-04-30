package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;

/**
 * 向下聚集
 * 
 * @author yzb
 * 
 */
class AlignDown extends AlignBase {

    public AlignDown(Piece[][] pieces) {
        super(pieces);
    }

    @Override
    /**
     * 向下聚集的变换
     */
    public void Translate(Piece p1, Piece p2) {

        TranslatePiece(p1.getIndexX());

        if (p2.getIndexX() != p1.getIndexX()) {
            TranslatePiece(p2.getIndexX());
        }
    }

    private void TranslatePiece(int indexX) {
        int curIndexY = pieces.length - 2;
        int nextIndexY = curIndexY - 1;
        while (curIndexY > 0) {
            Piece curPiece = pieces[curIndexY][indexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexY = Math.min(nextIndexY, curIndexY - 1);
                while (nextIndexY > 0) {
                    Piece nextPiece = pieces[nextIndexY][indexX];
                    if (Piece.hasImage(nextPiece)) {
                        ExchangePiece(curPiece, nextPiece);
                        nextIndexY--;
                        break;
                    }
                    nextIndexY--;
                }
                if (nextIndexY == 0) {
                    break;
                }
            }
            curIndexY--;
        }
    }
}
