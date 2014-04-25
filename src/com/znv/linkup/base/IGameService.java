package com.znv.linkup.base;

import com.znv.linkup.base.card.Piece;
import com.znv.linkup.base.card.PiecePair;
import com.znv.linkup.base.card.path.LinkInfo;

public interface IGameService {

	Piece[][] getPieces();

	boolean hasPieces();

	Piece findPiece(float x, float y);

	LinkInfo link(Piece p1, Piece p2);

	PiecePair prompt();

	void refresh();
}
