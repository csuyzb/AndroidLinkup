package com.znv.linkup.core.status;

import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.config.LevelCfg;

public class GameStatus {

	public enum GameState {
		None, Playing, Prompt, Pause
	}

	public GameStatus(LevelCfg levelCfg) {
		this(levelCfg, null);
	}

	public GameStatus(LevelCfg levelCfg, IGameStatus listener) {
		this.listener = listener;
		gameTime = new GameTime(levelCfg.getLevelTime(), listener);
		gameScore = new GameScore(listener);
		gameCombo = new GameCombo(listener);
	}

	public void start() {
		gameTime.stop();
		gameState = GameState.Playing;
		gameTime.start();
	}

	public void refresh() {
		gameCombo.clearCombo();
		if (listener != null) {
			listener.onRefresh();
		}
	}

	public void pause() {
		gameTime.stop();
		gameState = GameState.Pause;
		if (listener != null) {
			listener.onGamePause();
		}
	}

	public void resume() {
		if (gameState == GameState.Pause) {
			if (gameTime.getGameTime() > 0) {
				gameTime.start();
				if (listener != null) {
					listener.onGameResume();
				}
			}
		}
	}

	public void stop() {
		gameState = GameState.None;
		gameTime.stop();
	}

	public void fail() {
		stop();
		if (listener != null) {
			listener.onGameFail();
		}
	}

	public void win() {
		stop();
		
		if (listener != null) {
			listener.onGameWin();
		}
	}

	public void prompt(PiecePair pair) {
		gameState = GameState.Prompt;
		if (listener != null) {
			listener.onPrompt(pair);
		}
	}

	public void unPrompt() {
		if (gameState == GameState.Prompt) {
			gameState = GameState.Playing;
			if (listener != null) {
				listener.onUnPrompt();
			}
		}
	}

	public void matchSuccess(LinkInfo linkInfo) {
		gameTime.addTime(GameSettings.RewardTime);
		gameScore.addScore(GameSettings.CardScore * 2 + gameScore.getCornerScore(linkInfo) + gameCombo.getComboScore());
	}

	public GameTime getTime() {
		return gameTime;
	}

	public GameScore getScore() {
		return gameScore;
	}

	public GameCombo getCombo() {
		return gameCombo;
	}

	public int getRewardScore() {
		return gameScore.getRewardScore(gameTime.getGameTime());
	}

	private GameTime gameTime;
	private GameCombo gameCombo;
	private GameScore gameScore;
	private IGameStatus listener;
	private GameState gameState;
}
