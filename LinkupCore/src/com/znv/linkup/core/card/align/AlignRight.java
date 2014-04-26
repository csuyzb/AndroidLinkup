package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;

public class AlignRight extends AlignBase {

    public AlignRight(Piece[][] pieces) {
        super(pieces);
    }

    @Override
    public void Translate(Piece p1, Piece p2) {
        TranslatePiece(p1.getIndexY());

        if (p2.getIndexY() != p1.getIndexY()) {
            TranslatePiece(p2.getIndexY());
        }
    }

    private void TranslatePiece(int indexY) {
        int curIndexX = pieces[indexY].length - 2;
        int nextIndexX = curIndexX - 1;
        while (curIndexX > 0) {
            Piece curPiece = pieces[indexY][curIndexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexX = Math.min(nextIndexX, curIndexX - 1);
                while (nextIndexX > 0) {
                    Piece nextPiece = pieces[indexY][nextIndexX];
                    if (Piece.hasImage(nextPiece)) {
                        ExchangePiece(curPiece, nextPiece);
                        nextIndexX--;
                        break;
                    }
                    nextIndexX--;
                }
                if (nextIndexX == 0) {
                    break;
                }
            }
            curIndexX--;
        }
    }

}
