package com.znv.linkup.core.status;

import com.znv.linkup.core.card.PiecePair;

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
