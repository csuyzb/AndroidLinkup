package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;

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
        PiecePair pair = new PiecePair(p1, p2);
        pair.sort();

        // 先变换p1，再变换p2
        TranslatePiece(p1);

        TranslatePiece(p2);
    }

    private void TranslatePiece(Piece p) {
        int indexX = p.getIndexX();
        int curIndexY = p.getIndexY();
        int nextIndexY = curIndexY - 1;
        // 填补游戏块前面的空块
        while (curIndexY + 1 < pieces.length - 1) {
            Piece lastPiece = pieces[curIndexY + 1][indexX];
            if (!Piece.hasImage(lastPiece)) {
                curIndexY += 1;
            } else {
                break;
            }
        }
        while (curIndexY > 0) {
            Piece curPiece = pieces[curIndexY][indexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexY = Math.min(nextIndexY, curIndexY - 1);
                while (nextIndexY > 0) {
                    Piece nextPiece = pieces[nextIndexY][indexX];
                    if (Piece.hasImage(nextPiece)) {
                        // 游戏卡片则交换
                        if (Piece.canSelect(nextPiece)) {
                            ExchangePiece(curPiece, nextPiece);
                            nextIndexY--;
                            break;
                        } else {
                            // 障碍卡片则停止交换
                            nextIndexY = 0;
                        }
                    } else {
                        nextIndexY--;
                    }
                }
                if (nextIndexY == 0) {
                    break;
                }
            }
            curIndexY--;
        }
    }
}
