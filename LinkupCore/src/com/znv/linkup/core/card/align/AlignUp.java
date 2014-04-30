package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;

/**
 * 向上聚集
 * 
 * @author yzb
 * 
 */
class AlignUp extends AlignBase {

    public AlignUp(Piece[][] pieces) {
        super(pieces);
    }

    @Override
    /**
     * 向上聚集变换
     */
    public void Translate(Piece p1, Piece p2) {
        TranslatePiece(p1.getIndexX());

        if (p2.getIndexX() != p1.getIndexX()) {
            TranslatePiece(p2.getIndexX());
        }
    }

    private void TranslatePiece(int indexX) {
        int curIndexY = 1;
        int nextIndexY = curIndexY + 1;
        while (curIndexY < pieces.length - 1) {
            Piece curPiece = pieces[curIndexY][indexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexY = Math.max(nextIndexY, curIndexY + 1);
                while (nextIndexY < pieces.length - 1) {
                    Piece nextPiece = pieces[nextIndexY][indexX];
                    if (Piece.hasImage(nextPiece)) {
                        ExchangePiece(curPiece, nextPiece);
                        nextIndexY++;
                        break;
                    }
                    nextIndexY++;
                }
                if (nextIndexY == pieces.length - 1) {
                    break;
                }
            }
            curIndexY++;
        }
    }
}
