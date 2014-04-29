package com.znv.linkup.core.card;

/**
 * 卡片对，用于游戏提示
 * 
 * @author yzb
 * 
 */
public class PiecePair {
    public PiecePair(Piece p1, Piece p2) {
        pieceOne = p1;
        pieceTwo = p2;
    }

    public Piece getPieceOne() {
        return pieceOne;
    }

    public void setPieceOne(Piece pieceOne) {
        this.pieceOne = pieceOne;
    }

    public Piece getPieceTwo() {
        return pieceTwo;
    }

    public void setPieceTwo(Piece pieceTwo) {
        this.pieceTwo = pieceTwo;
    }

    private Piece pieceOne;
    private Piece pieceTwo;
}
