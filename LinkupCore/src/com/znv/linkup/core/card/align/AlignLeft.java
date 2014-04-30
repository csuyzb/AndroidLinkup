package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;

/**
 * 向左聚集
 * 
 * @author yzb
 * 
 */
class AlignLeft extends AlignBase {

    public AlignLeft(Piece[][] pieces) {
        super(pieces);
    }

    @Override
    /**
     * 向左聚集的变换
     */
    public void Translate(Piece p1, Piece p2) {
        TranslatePiece(p1.getIndexY());

        if (p2.getIndexY() != p1.getIndexY()) {
            TranslatePiece(p2.getIndexY());
        }
    }

    private void TranslatePiece(int indexY) {
        int curIndexX = 1;
        int nextIndexX = curIndexX + 1;
        while (curIndexX < pieces[indexY].length - 1) {
            Piece curPiece = pieces[indexY][curIndexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexX = Math.max(nextIndexX, curIndexX + 1);
                while (nextIndexX < pieces[indexY].length - 1) {
                    Piece nextPiece = pieces[indexY][nextIndexX];
                    if (Piece.hasImage(nextPiece)) {
                        ExchangePiece(curPiece, nextPiece);
                        nextIndexX++;
                        break;
                    }
                    nextIndexX++;
                }
                if (nextIndexX == pieces[indexY].length - 1) {
                    break;
                }
            }
            curIndexX++;
        }
    }
}
