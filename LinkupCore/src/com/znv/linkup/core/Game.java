package com.znv.linkup.core;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.align.AlignContext;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.status.GameStatus;

public class Game implements IGameService {

    public Game(LevelCfg levelCfg, IGameOp listener) {
        this.levelCfg = levelCfg;
        this.listener = listener;
        gameStatus = new GameStatus(levelCfg, listener);
        // sound = new MusicServer(levelCfg.getContext());
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
            if (listener != null) {
                listener.onTranslate();
            }
        }

        refreshView();
        if (!gameService.hasPieces()) {
            gameStatus.win();
        }
    }

    public void check(Piece piece) {
        // sound.select();
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
        // sound.erase();
        gameStatus.matchSuccess(linkInfo);
        if (listener != null) {
            listener.onLinkPath(linkInfo);
        }
    }

    public void prompt() {
        pair = promptPair();
        if (pair != null) {
            gameStatus.prompt(pair);
        }
        refreshView();
    }
    
    private PiecePair promptPair() {
        Piece[][] pieces = gameService.getPieces();
        for (int i = 0; i < pieces.length * pieces[0].length; i++) {
            Piece p1 = pieces[i / pieces[0].length][i % pieces[0].length];
            if (Piece.canSelect(p1)) {
                for (int j = i + 1; j < pieces.length * pieces[0].length; j++) {
                    Piece p2 = pieces[j / pieces[0].length][j % pieces[0].length];
                    if (Piece.canSelect(p2) && p1.isSameImage(p2) && link(p1, p2) != null) {
                        return new PiecePair(p1, p2);
                    }
                }
            }
        }
        return null;
    }

    public void unPrompt() {
        if (pair != null) {
            gameStatus.unPrompt();
            pair = null;
        }
        refreshView();
    }

    public void refresh() {
        // sound.refresh();
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
    // private MusicServer sound;
    private Piece selected = null;
    private IGameService gameService;
    private AlignContext alignContext = null;
    private PiecePair pair = null;
    
    @Override
    public Piece[][] getPieces() {
        return gameService.getPieces();
    }

    @Override
    public boolean hasPieces() {
        return gameService.hasPieces();
    }

    @Override
    public Piece findPiece(float x, float y) {
        return gameService.findPiece(x, y);
    }

    @Override
    public LinkInfo link(Piece p1, Piece p2) {
        return gameService.link(p1, p2);
    }
}
