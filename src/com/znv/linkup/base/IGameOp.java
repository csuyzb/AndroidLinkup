package com.znv.linkup.base;

import com.znv.linkup.base.card.Piece;
import com.znv.linkup.base.card.path.LinkInfo;
import com.znv.linkup.base.status.IGameStatus;

public interface IGameOp extends IGameStatus {
	void onCheck(Piece piece);

	void onUnCheck();

	void onLinkPath(LinkInfo linkInfo);

	void onRefreshView();
}
