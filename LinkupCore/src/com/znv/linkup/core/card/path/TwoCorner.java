package com.znv.linkup.core.card.path;

import com.znv.linkup.core.card.Piece;

/**
 * 两个弯的路径卡片信息，包括起点，转弯点1，转弯点2和终点，便于对比路径长度
 * 
 * @author yzb
 * 
 */
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

    /**
     * 判断当期路径与待比较的路径哪个更短
     * 
     * @param other
     *            待比较的路径
     * @return 当期路径短则返回true
     */
    public boolean isShorterThan(TwoCorner other) {
        if (other == null) {
            return true;
        }
        int dist1 = this.getDistance();
        int dist2 = other.getDistance();
        return dist1 < dist2;
    }

    /**
     * 获取路径距离
     * 
     * @return 路径距离
     */
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
