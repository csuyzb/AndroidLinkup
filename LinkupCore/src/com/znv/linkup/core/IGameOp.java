package com.znv.linkup.core;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.status.IGameStatus;

public interface IGameOp extends IGameStatus {
	void onCheck(Piece piece);

	void onUnCheck();
	
	void onTranslate();

	void onLinkPath(LinkInfo linkInfo);

	void onRefreshView();
}
