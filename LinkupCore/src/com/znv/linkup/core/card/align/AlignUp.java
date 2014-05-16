package com.znv.linkup.core.card.align;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;

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
        PiecePair pair = new PiecePair(p1, p2);
        pair.sort();

        // 先变换p2，再变换p1
        TranslatePiece(p2);

        TranslatePiece(p1);
    }

    private void TranslatePiece(Piece p) {
        int indexX = p.getIndexX();
        int curIndexY = p.getIndexY();
        int nextIndexY = curIndexY + 1;
        while (curIndexY < pieces.length - 1) {
            Piece curPiece = pieces[curIndexY][indexX];
            if (!Piece.hasImage(curPiece)) {
                nextIndexY = Math.max(nextIndexY, curIndexY + 1);
                while (nextIndexY < pieces.length - 1) {
                    Piece nextPiece = pieces[nextIndexY][indexX];
                    if (Piece.hasImage(nextPiece)) {
                        if (Piece.canSelect(nextPiece)) {
                            // 游戏卡片则交换
                            ExchangePiece(curPiece, nextPiece);
                            nextIndexY++;
                            break;
                        } else {
                            // 遇到障碍则停止变换
                            nextIndexY = pieces.length - 1;
                        }
                    } else {
                        nextIndexY++;
                    }
                }
                if (nextIndexY == pieces.length - 1) {
                    break;
                }
            }
            curIndexY++;
        }
    }
}
