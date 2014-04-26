package com.znv.linkup.core.status;

import java.util.Timer;
import java.util.TimerTask;

public class GameTime {

	public GameTime(int totalTime) {
		this(totalTime, null);
	}

	public GameTime(int totalTime, IGameStatus listener) {
		this.totalTime = totalTime;
		this.gameTime = totalTime + 1;
		this.listener = listener;
	}

	public void start() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				timerTick();
			}
		}, 0, 1000);
	}

	private void timerTick() {
		if (gameTime < 1) {
			gameTime = 0;
			stop();
			if (listener != null) {
				listener.onGameFail();
			}
		} else {
			gameTime -= 1;
			if (listener != null) {
				listener.onTimeChanged(gameTime);
			}
		}
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void addTime(int seconds) {
		gameTime += seconds;
		if (gameTime > totalTime) {
			gameTime = totalTime;
		}
		if (listener != null) {
			listener.onTimeChanged(gameTime);
		}
	}

	public int getGameTime() {
		return gameTime;
	}

	private Timer timer = null;
	private int gameTime;
	private int totalTime;
	private IGameStatus listener;
}
