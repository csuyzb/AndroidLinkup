package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;

/**
 * 向右聚集
 * 
 * @author yzb
 * 
 */
class AlignRight extends AlignBase {

    public AlignRight(Piece[][] pieces) {
        super(pieces);
    }

    @Override
    /**
     * 向右聚集变换
     */
    public void Translate(Piece p1, Piece p2) {
        PiecePair pair = new PiecePair(p1, p2);
        pair.sort();

        // 先变换p1，再变换p2
        TranslatePiece(p1);

        TranslatePiece(p2);
    }

    private void TranslatePiece(Piece p) {
        int indexY = p.getIndexY();
        int curIndexX = p.getIndexX();
        int nextIndexX = curIndexX - 1;
        // 填补游戏块前面的空块
        while (curIndexX + 1 < pieces[indexY].length - 1) {
            Piece lastPiece = pieces[indexY][curIndexX + 1];
            if (!Piece.hasImage(lastPiece)) {
                curIndexX += 1;
            } else {
                break;
            }
        }
        while (curIndexX > 0) {
            Piece curPiece = pieces[indexY][curIndexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexX = Math.min(nextIndexX, curIndexX - 1);
                while (nextIndexX > 0) {
                    Piece nextPiece = pieces[indexY][nextIndexX];
                    if (Piece.hasImage(nextPiece)) {
                        if (Piece.canSelect(nextPiece)) {
                            // 游戏卡片则交换
                            ExchangePiece(curPiece, nextPiece);
                            nextIndexX--;
                            break;
                        } else {
                            // 遇到障碍则结束
                            nextIndexX = 0;
                        }
                    } else {
                        nextIndexX--;
                    }
                }
                if (nextIndexX == 0) {
                    break;
                }
            }
            curIndexX--;
        }
    }

}
