package com.znv.linkup.base;

import com.znv.linkup.base.card.Piece;
import com.znv.linkup.base.card.PiecePair;
import com.znv.linkup.base.card.align.AlignContext;
import com.znv.linkup.base.card.path.LinkInfo;
import com.znv.linkup.base.config.LevelCfg;
import com.znv.linkup.base.sound.GameMusic;
import com.znv.linkup.base.status.GameStatus;

public class Game {

    public Game(LevelCfg levelCfg, IGameOp listener) {
        this.levelCfg = levelCfg;
        this.listener = listener;
        gameStatus = new GameStatus(levelCfg, listener);
        sound = GameMusic.getInstance();
        gameService = new GameServiceImpl(levelCfg);
        alignContext = new AlignContext(gameService.getPieces(), levelCfg.getLevelAlign());
    }

    public void start() {
        unCheck();
        gameStatus.start();

        refreshView();
    }

    public void finish() {
        gameStatus.stop();
    }

    public void stop() {
        // sound.stop();
        finish();

        refreshView();
        if (gameService.hasPieces()) {
            gameStatus.fail();
        } else {
            gameStatus.win();
        }
    }

    public void pause() {
        gameStatus.pause();
        refreshView();
    }

    public void resume() {
        gameStatus.resume();
        refreshView();
    }

    public void touch(float x, float y) {
        Piece[][] pieces = gameService.getPieces();
        Piece curPiece = gameService.findPiece(x, y);
        if (curPiece == null) {
            return;
        }

        unPrompt();
        if (selected == curPiece || !Piece.canSelect(curPiece)) {
            if (selected != null) {
                unCheck();
            }
            return;
        }

        if (selected == null) {
            check(curPiece);
        } else {
            LinkInfo linkInfo = gameService.link(selected, curPiece);
            if (linkInfo == null) {
                check(curPiece);
            } else {
                handleSuccess(linkInfo, selected, curPiece, pieces);
            }
        }

        refreshView();
    }

    private void handleSuccess(LinkInfo linkInfo, Piece prePiece, Piece curPiece, Piece[][] pieces) {
        unCheck();
        pieces[prePiece.getIndexY()][prePiece.getIndexX()].setEmpty(true);
        pieces[curPiece.getIndexY()][curPiece.getIndexX()].setEmpty(true);
        onLinkPath(linkInfo);

        if (alignContext != null) {
            alignContext.Translate(prePiece, curPiece);
        }

        refreshView();
        if (!gameService.hasPieces()) {
            gameStatus.win();
        }
    }

    public void check(Piece piece) {
        sound.select();
        selected = piece;
        if (listener != null) {
            listener.onCheck(piece);
        }
        refreshView();
    }

    public void unCheck() {
        selected = null;
        if (listener != null) {
            listener.onUnCheck();
        }
        refreshView();
    }

    public void onLinkPath(LinkInfo linkInfo) {
        sound.erase();
        gameStatus.matchSuccess(linkInfo);
        if (listener != null) {
            listener.onLinkPath(linkInfo);
        }
    }

    public void prompt() {
        pair = gameService.prompt();
        if (pair != null) {
            gameStatus.prompt(pair);
        }
        refreshView();
    }

    public void unPrompt() {
        if (pair != null) {
            gameStatus.unPrompt();
            pair = null;
        }
        refreshView();
    }

    public void refresh() {
        sound.refresh();
        unPrompt();
        gameService.refresh();
        gameStatus.refresh();
        refreshView();
    }

    public void refreshView() {
        if (listener != null) {
            listener.onRefreshView();
        }
    }

    public IGameService getGameService() {
        return gameService;
    }

    public LevelCfg getLevelCfg() {
        return levelCfg;
    }

    public int getGameTime() {
        return gameStatus.getTime().getGameTime();
    }

    public int getGameScore() {
        return gameStatus.getScore().getGameScore();
    }

    public int getRewardScore() {
        return gameStatus.getRewardScore();
    }

    public int getTotalScore() {
        return getGameScore() + getRewardScore();
    }

    public int getGameCombo() {
        return gameStatus.getCombo().getGameCombo();
    }

    private LevelCfg levelCfg;
    private GameStatus gameStatus;
    private IGameOp listener;
    private GameMusic sound;
    private Piece selected = null;
    private IGameService gameService;
    private AlignContext alignContext = null;
    private PiecePair pair = null;
}
