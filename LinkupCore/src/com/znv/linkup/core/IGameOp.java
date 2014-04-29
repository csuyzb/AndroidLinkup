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
public interface IGameOp extends IGameStatus {

    /**
     * 选择卡片时的处理
     * 
     * @param piece
     *            选择的卡片信息
     */
    void onCheck(Piece piece);

    /**
     * 取消选择时的处理
     */
    void onUnCheck();

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
     * 界面刷新处理
     */
    void onRefreshView();
}
