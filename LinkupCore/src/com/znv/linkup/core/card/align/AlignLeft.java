package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;

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
        PiecePair pair = new PiecePair(p1, p2);
        pair.sort();

        // 先变换p2，再变换p1
        TranslatePiece(p2);

        TranslatePiece(p1);
    }

    private void TranslatePiece(Piece p) {
        int indexY = p.getIndexY();
        int curIndexX = p.getIndexX();
        int nextIndexX = curIndexX + 1;
        while (curIndexX < pieces[indexY].length - 1) {
            Piece curPiece = pieces[indexY][curIndexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexX = Math.max(nextIndexX, curIndexX + 1);
                while (nextIndexX < pieces[indexY].length - 1) {
                    Piece nextPiece = pieces[indexY][nextIndexX];
                    if (Piece.hasImage(nextPiece)) {
                        if (Piece.canSelect(nextPiece)) {
                            // 游戏卡片则交换
                            ExchangePiece(curPiece, nextPiece);
                            nextIndexX++;
                            break;
                        } else {
                            // 遇到障碍则停止
                            nextIndexX = pieces[indexY].length - 1;
                        }
                    } else {
                        nextIndexX++;
                    }
                }
                if (nextIndexX == pieces[indexY].length - 1) {
                    break;
                }
            }
            curIndexX++;
        }
    }
}
