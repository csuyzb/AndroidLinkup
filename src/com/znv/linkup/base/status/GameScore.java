package com.znv.linkup.base.status;

import java.util.List;

import com.znv.linkup.base.GameSettings;
import com.znv.linkup.base.card.Piece;
import com.znv.linkup.base.card.path.LinkInfo;

public class GameScore {

	public GameScore() {
		this(null);
	}

	public GameScore(IGameStatus listener) {
		gameScore = 0;
		this.listener = listener;
		if (listener != null) {
			listener.onScoreChanged(gameScore);
		}
	}

	public void addScore(int score) {
		this.gameScore += score;
		if (listener != null) {
			listener.onScoreChanged(gameScore);
		}
	}

//	public void addRewardScore(int time) {
//		rewardScore += time * GameSettings.TimeScore;
//	}

//	public void addRewardScore(LinkInfo linkInfo) {
//		rewardScore += getCornerScore(linkInfo);
//	}

	public int getCornerScore(LinkInfo linkInfo) {
		List<Piece> linkPieces = linkInfo.getLinkPieces();
		return (linkPieces.size() - 2) * GameSettings.CornerScore;
	}

	public int getRewardScore(int gameTime) {
		int rewardTimeScore = gameTime * GameSettings.TimeScore;
		return rewardScore + rewardTimeScore;
	}

//	public int getTotalScore(int gameTime) {
//		return gameScore + getRewardScore(gameTime);
//	}

	public int getGameScore() {
		return gameScore;
	}

	private int gameScore;
	private int rewardScore;
	private IGameStatus listener;
}
