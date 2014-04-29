package com.znv.linkup.core;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.path.LinkInfo;

/**
 * 卡片相关的游戏逻辑处理接口
 * 
 * @author yzb
 * 
 */
public interface IGameService {

    /**
     * 获取所有卡片信息
     * 
     * @return 所有卡片信息
     */
    Piece[][] getPieces();

    /**
     * 判断当前是否存在游戏卡片
     * 
     * @return 存在游戏卡片时返回true
     */
    boolean hasPieces();

    /**
     * 根据坐标点查找卡片
     * 
     * @param x
     *            横坐标
     * @param y
     *            纵坐标
     * @return 卡片信息
     */
    Piece findPiece(float x, float y);

    /**
     * 重排卡片
     */
    void refresh();

    /**
     * 判断两个卡片是否可消除，同时返回连接路径
     * 
     * @param p1
     *            卡片1
     * @param p2
     *            卡片2
     * @return 连接路径信息，不可消除时返回null
     */
    LinkInfo link(Piece p1, Piece p2);
}
