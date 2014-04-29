package com.znv.linkup.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.card.path.TwoCorner;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.map.GameMap;

/**
 * 卡片相关的游戏逻辑处理类
 * 
 * @author yzb
 * 
 */
class GameServiceImpl implements IGameService {

    private Piece[][] pieces;
    private LevelCfg levelCfg;

    public GameServiceImpl(LevelCfg levelCfg) {
        this.levelCfg = levelCfg;
        this.pieces = GameMap.createPieces(levelCfg);
    }

    @Override
    /**
     * 获取所有卡片信息
     * 
     * @return 所有卡片信息
     */
    public Piece[][] getPieces() {
        return pieces;
    }

    @Override
    /**
     * 判断当前是否存在游戏卡片
     * 
     * @return 存在游戏卡片时返回true
     */
    public boolean hasPieces() {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (Piece.canSelect(pieces[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    /**
     * 根据坐标点查找卡片
     * 
     * @param x
     *            横坐标
     * @param y
     *            纵坐标
     * @return 卡片信息
     */
    public Piece findPiece(float x, float y) {
        int relativeX = (int) x - levelCfg.getBeginImageX();
        int relativeY = (int) y - levelCfg.getBeginImageY();
        if (relativeX < 0 || relativeY < 0) {
            return null;
        }
        int indexX = getIndex(relativeX, levelCfg.getPieceWidth());
        int indexY = getIndex(relativeY, levelCfg.getPieceHeight());
        if (indexX < 0 || indexY < 0) {
            return null;
        }
        if (indexY >= pieces.length || indexX >= pieces[0].length) {
            return null;
        }
        return pieces[indexY][indexX];
    }

    private int getIndex(int relative, int size) {
        int index = -1;
        if (relative % size == 0) {
            index = relative / size - 1;
        } else {
            index = relative / size;
        }
        return index;
    }

    @Override
    /**
     * 重排卡片
     */
    public void refresh() {
        int index = 0;
        // 获取游戏块的值
        List<Piece> array = new ArrayList<Piece>();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (Piece.canSelect(pieces[i][j])) {
                    array.add(pieces[i][j]);
                }
            }
        }
        // 随机打乱
        Collections.shuffle(array);
        // 重新赋值
        index = 0;
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (Piece.canSelect(pieces[i][j])) {
                    // 交换
                    pieces[i][j].exchange(array.get(index++));
                }
            }
        }
    }

    @Override
    /**
     * 判断两个卡片是否可消除，同时返回连接路径
     * 
     * @param p1
     *            卡片1
     * @param p2
     *            卡片2
     * @return 连接路径信息，不可消除时返回null
     */
    public LinkInfo link(Piece p1, Piece p2) {
        if (p1.equals(p2)) {
            return null;
        }
        if (!p1.isSameImage(p2)) {
            return null;
        }
        if (p2.getIndexX() < p1.getIndexX()) {
            return link(p2, p1);
        }

        LinkInfo linkInfo = null;

        linkInfo = zeroCornerConnect(p1, p2);
        if (linkInfo != null) {
            return linkInfo;
        }

        linkInfo = oneCornerConnect(p1, p2);
        if (linkInfo != null) {
            return linkInfo;
        }

        linkInfo = twoCornerConnect(p1, p2);
        if (linkInfo != null) {
            return linkInfo;
        }

        return null;
    }

    private LinkInfo zeroCornerConnect(Piece p1, Piece p2) {
        if (p1.getIndexY() == p2.getIndexY()) {
            if (isXConnect(p1, p2)) {
                return new LinkInfo(p1, p2);
            }
        } else if (p1.getIndexX() == p2.getIndexX()) {
            if (isYConnect(p1, p2)) {
                return new LinkInfo(p1, p2);
            }
        }
        return null;
    }

    private boolean isXConnect(Piece p1, Piece p2) {
        if (p1.getIndexY() != p2.getIndexY()) {
            return false;
        }
        if (p1.getIndexX() == p2.getIndexX()) {
            return false;
        }

        if (p2.getIndexX() < p1.getIndexX()) {
            return isXConnect(p2, p1);
        }

        for (int i = p1.getIndexX() + 1; i < p2.getIndexX(); i++) {
            if (Piece.hasImage(pieces[p1.getIndexY()][i])) {
                return false;
            }
        }
        return true;
    }

    private boolean isYConnect(Piece p1, Piece p2) {
        if (p1.getIndexX() != p2.getIndexX()) {
            return false;
        }
        if (p1.getIndexY() == p2.getIndexY()) {
            return false;
        }

        if (p2.getIndexY() < p1.getIndexY()) {
            return isYConnect(p2, p1);
        }

        for (int i = p1.getIndexY() + 1; i < p2.getIndexY(); i++) {
            if (Piece.hasImage(pieces[i][p1.getIndexX()])) {
                return false;
            }
        }
        return true;
    }

    private LinkInfo oneCornerConnect(Piece p1, Piece p2) {
        Piece cornerPiece1 = pieces[p2.getIndexY()][p1.getIndexX()];
        Piece cornerPiece2 = pieces[p1.getIndexY()][p2.getIndexX()];
        if (isOneCornerConnect(p1, p2, cornerPiece1)) {
            return new LinkInfo(p1, cornerPiece1, p2);
        }

        if (isOneCornerConnect(p1, p2, cornerPiece2)) {
            return new LinkInfo(p1, cornerPiece2, p2);
        }
        return null;
    }

    private boolean isOneCornerConnect(Piece p1, Piece p2, Piece corner) {
        if (Piece.hasImage(corner)) {
            return false;
        }
        if (p2.getIndexX() < p1.getIndexX()) {
            return isOneCornerConnect(p2, p1, corner);
        }

        if (p1.getIndexX() == corner.getIndexX()) {
            if (isYConnect(p1, corner) && isXConnect(p2, corner)) {
                return true;
            }
        } else {
            if (isXConnect(p1, corner) && isYConnect(p2, corner)) {
                return true;
            }
        }
        return false;
    }

    private LinkInfo twoCornerConnect(Piece p1, Piece p2) {
        TwoCorner shortestCorner = null;
        TwoCorner twoCorner = null;
        twoCorner = chanelConnect(p1, p2, getLeftChanel(p1));
        if (twoCorner != null) {
            if (twoCorner.isShorterThan(shortestCorner)) {
                shortestCorner = twoCorner;
            }
        }

        twoCorner = chanelConnect(p1, p2, getUpChanel(p1));
        if (twoCorner != null) {
            if (twoCorner.isShorterThan(shortestCorner)) {
                shortestCorner = twoCorner;
            }
        }

        twoCorner = chanelConnect(p1, p2, getRightChanel(p1));
        if (twoCorner != null) {
            if (twoCorner.isShorterThan(shortestCorner)) {
                shortestCorner = twoCorner;
            }
        }

        twoCorner = chanelConnect(p1, p2, getDownChanel(p1));
        if (twoCorner != null) {
            if (twoCorner.isShorterThan(shortestCorner)) {
                shortestCorner = twoCorner;
            }
        }

        if (shortestCorner != null) {
            return new LinkInfo(p1, shortestCorner.getCornerOne(), shortestCorner.getCornerTwo(), p2);
        }
        return null;
    }

    private List<Piece> getLeftChanel(Piece p) {
        List<Piece> results = new ArrayList<Piece>();
        for (int i = p.getIndexX() - 1; i >= 0; i--) {
            if (Piece.hasImage(pieces[p.getIndexY()][i])) {
                return results;
            }
            results.add(pieces[p.getIndexY()][i]);
        }
        return results;
    }

    private List<Piece> getRightChanel(Piece p) {
        List<Piece> results = new ArrayList<Piece>();
        for (int i = p.getIndexX() + 1; i < levelCfg.getXSize(); i++) {
            if (Piece.hasImage(pieces[p.getIndexY()][i])) {
                return results;
            }
            results.add(pieces[p.getIndexY()][i]);
        }
        return results;
    }

    private List<Piece> getUpChanel(Piece p) {
        List<Piece> results = new ArrayList<Piece>();
        for (int i = p.getIndexY() - 1; i >= 0; i--) {
            if (Piece.hasImage(pieces[i][p.getIndexX()])) {
                return results;
            }
            results.add(pieces[i][p.getIndexX()]);
        }
        return results;
    }

    private List<Piece> getDownChanel(Piece p) {
        List<Piece> results = new ArrayList<Piece>();
        for (int i = p.getIndexY() + 1; i < levelCfg.getYSize(); i++) {
            if (Piece.hasImage(pieces[i][p.getIndexX()])) {
                return results;
            }
            results.add(pieces[i][p.getIndexX()]);
        }
        return results;
    }

    private TwoCorner chanelConnect(Piece p1, Piece p2, List<Piece> chanelPieces) {
        for (Piece p : chanelPieces) {
            LinkInfo linkInfo = oneCornerConnect(p, p2);
            if (linkInfo != null) {
                return new TwoCorner(p1, p, linkInfo.getLinkPieces().get(1), p2);
            }
        }
        return null;
    }
}
