package com.znv.linkup.core;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.status.IGameStatus;

/**
 * 游戏操作接口，继承自游戏状态接口，界面实现此接口以响应相应逻辑
 * 
 * @author yzb
 * 
 */
public interface IGameAction extends IGameStatus {

    /**
     * 选择卡片时的处理
     * 
     * @param piece
     *            选择的卡片信息
     */
    void onCheck(Piece piece);

    /**
     * 取消选择时的处理
     * 
     * @param piece
     *            取消选择的卡片信息
     */
    void onUnCheck(Piece piece);

    /**
     * 游戏卡片变换时的处理
     */
    void onTranslate();

    /**
     * 对连接路径的处理
     * 
     * @param linkInfo
     */
    void onLinkPath(LinkInfo linkInfo);

    /**
     * 消除后的处理，用于自动检测死锁
     */
    void onErase();
}
