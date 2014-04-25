package com.znv.linkup.base.card;

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
