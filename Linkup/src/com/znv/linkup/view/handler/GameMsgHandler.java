package com.znv.linkup.view.handler;

import java.lang.ref.WeakReference;

import com.znv.linkup.GameActivity;
import com.znv.linkup.ViewSettings;

import android.os.Handler;
import android.os.Message;

public class GameMsgHandler extends Handler {

    private WeakReference<GameActivity> gameActs;

    public GameMsgHandler(GameActivity act) {
        gameActs = new WeakReference<GameActivity>(act);
    }

    @Override
    public void handleMessage(Message msg) {
        GameActivity gameAct = gameActs.get();
        switch (msg.what) {
        case ViewSettings.GameTimeMessage:
            gameAct.updateTime();
            break;
        case ViewSettings.GameScoreMessage:
            gameAct.updateScore();
            break;
        case ViewSettings.FailMessage:
            gameAct.showFail();
            break;
        case ViewSettings.WinMessage:
            gameAct.showSuccess();
            break;
        case ViewSettings.PromptMessage:
            gameAct.showPrompt();
            break;
        case ViewSettings.RefreshMessage:
            gameAct.showRefresh();
            break;
        }
    }

}
