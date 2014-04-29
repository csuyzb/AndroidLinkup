package com.znv.linkup.core.card.path;

import com.znv.linkup.core.card.Piece;

public class TwoCorner {
	private Piece start;
	private Piece cornerOne;
	private Piece cornerTwo;
	private Piece stop;
	
	public TwoCorner(Piece start, Piece one, Piece two, Piece stop) {
		this.start = start;
		this.cornerOne = one;
		this.cornerTwo = two;
		this.stop = stop;
	}
	
	public boolean isShorterThan(TwoCorner other) {
		if(other == null) {
			return true;
		}
		int dist1 = this.getDistance();
		int dist2 = other.getDistance();
		return dist1 < dist2;
	}
	
	public int getDistance() {
		return Piece.getDistance(start, cornerOne) + Piece.getDistance(cornerOne, cornerTwo) + Piece.getDistance(cornerTwo, stop);
	}

	public Piece getCornerOne() {
		return cornerOne;
	}

	public void setCornerOne(Piece cornerOne) {
		this.cornerOne = cornerOne;
	}

	public Piece getCornerTwo() {
		return cornerTwo;
	}

	public void setCornerTwo(Piece cornerTwo) {
		this.cornerTwo = cornerTwo;
	}
}
