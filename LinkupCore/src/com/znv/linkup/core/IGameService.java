package com.znv.linkup.core;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.path.LinkInfo;

public interface IGameService {

	Piece[][] getPieces();

	boolean hasPieces();

	Piece findPiece(float x, float y);

	LinkInfo link(Piece p1, Piece p2);

	void refresh();
}
