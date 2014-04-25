package com.znv.linkup.base.card.path;

import java.util.ArrayList;
import java.util.List;

import com.znv.linkup.base.card.Piece;

public class LinkInfo {
	private List<Piece> linkPieces = new ArrayList<Piece>();

	public LinkInfo(Piece p1, Piece p2) {
		linkPieces.add(p1);
		linkPieces.add(p2);
	}

	public LinkInfo(Piece p1, Piece p2, Piece p3) {
		this(p1, p2);
		linkPieces.add(p3);
	}

	public LinkInfo(Piece p1, Piece p2, Piece p3, Piece p4) {
		this(p1, p2, p3);
		linkPieces.add(p4);
	}

	public List<Piece> getLinkPieces() {
		return linkPieces;
	}
}
