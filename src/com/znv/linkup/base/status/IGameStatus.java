package com.znv.linkup.base.status;

import com.znv.linkup.base.card.PiecePair;

public interface IGameStatus {

	void onCombo();

	void onPrompt(PiecePair pair);

	void onUnPrompt();

	void onRefresh();

	void onScoreChanged(int gameScore);

	void onTimeChanged(int gameTime);

	void onGameFail();

	void onGamePause();

	void onGameResume();

	void onGameWin();
}
