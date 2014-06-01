package com.znv.linkup.core;

import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.align.AlignContext;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.status.GameStatus;

/**
 * 游戏逻辑处理实现类，界面通过实现IGameOp接口完成界面调整
 * 
 * @author yzb
 * 
 */
public class Game {

    public Game(LevelCfg levelCfg, IGameAction listener) {
        this.levelCfg = levelCfg;
        this.listener = listener;
        gameStatus = new GameStatus(levelCfg, listener);
        gameService = new GameService(levelCfg);
        alignContext = new AlignContext(gameService.getPieces(), levelCfg.getLevelAlign());
    }

    /**
     * 开始游戏
     */
    public void start() {
        unCheck();
        gameStatus.start();
    }

    /**
     * 放弃游戏，不处理游戏结果
     */
    public void finish() {
        gameStatus.stop();
    }

    /**
     * 结束游戏，处理游戏结果（暂时未用）
     */
    public void stop() {
        finish();

        if (gameService.hasPieces()) {
            gameStatus.fail();
        } else {
            gameStatus.win();
        }
    }

    /**
     * 暂停游戏
     */
    public void pause() {
        gameStatus.pause();
    }

    /**
     * 重新开始游戏
     */
    public void resume() {
        gameStatus.resume();
    }

    /**
     * 处理玩家触摸点击
     * 
     * @param x
     *            横坐标
     * @param y
     *            纵坐标
     */
    public void click(float x, float y) {
        Piece piece = gameService.findPiece(x, y);

        // 点击相应卡片
        click(piece);
    }

    /**
     * 点击卡片时的处理
     * 
     * @param piece
     *            点击的卡片
     */
    public void click(Piece piece) {
        Piece curPiece = piece;
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
                unCheck();
                check(curPiece);
            } else {
                handleSuccess(linkInfo, selected, curPiece);
            }
        }
    }

    /**
     * 成功配对时的处理
     * 
     * @param linkInfo
     *            连接路径信息
     * @param prePiece
     *            前一个卡片信息
     * @param curPiece
     *            当前卡片信息
     */
    private void handleSuccess(LinkInfo linkInfo, Piece prePiece, Piece curPiece) {
        unCheck();
        Piece[][] pieces = gameService.getPieces();
        pieces[prePiece.getIndexY()][prePiece.getIndexX()].setEmpty(true);
        pieces[curPiece.getIndexY()][curPiece.getIndexX()].setEmpty(true);
        onLinkPath(linkInfo);

        if (alignContext != null) {
            alignContext.Translate(prePiece, curPiece);
            if (listener != null) {
                listener.onTranslate();
            }
        }

        if (!gameService.hasPieces()) {
            gameStatus.win();
        }
    }

    /**
     * 选择卡片
     * 
     * @param piece
     *            选中的卡片
     */
    public void check(Piece piece) {
        selected = piece;
        if (listener != null) {
            listener.onCheck(piece);
        }
    }

    /**
     * 取消卡片选择
     */
    public void unCheck() {
        if (listener != null) {
            listener.onUnCheck(selected);
        }
        selected = null;
    }

    /**
     * 连线路径处理
     * 
     * @param linkInfo
     *            连接路径信息
     */
    public void onLinkPath(LinkInfo linkInfo) {
        gameStatus.matchSuccess(linkInfo);
        if (listener != null) {
            listener.onLinkPath(linkInfo);
        }
    }

    /**
     * 游戏提示
     */
    public void prompt() {
        pair = promptPair();
        gameStatus.prompt(pair);
    }

    /**
     * 获取当前提示的卡片对
     * 
     * @return 返回提示的卡片对，没有时返回null
     */
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

    /**
     * 取消游戏提示
     */
    public void unPrompt() {
        if (pair != null) {
            gameStatus.unPrompt(pair);
            pair = null;
        }
    }

    /**
     * 游戏重排
     */
    public void refresh() {
        unPrompt();
        // 刷新后保证有消除,尝试固定次数，避免死循环
        for (int i = 0; i < GameSettings.RefreshTryCount; i++) {
            gameService.refresh();
            if (promptPair() != null) {
                // 有可以消除时不继续重排
                break;
            }
        }
        gameStatus.refresh();
    }
    
    public void addGameTime(int seconds) {
        gameStatus.addGameTime(seconds);
    }

    /**
     * 获取当前关卡配置信息
     * 
     * @return 当前关卡配置信息
     */
    public LevelCfg getLevelCfg() {
        return levelCfg;
    }

    public int getGameTime() {
        return gameStatus.getGameTime();
    }

    /**
     * 获取游戏得分
     * 
     * @return 游戏得分
     */
    public int getGameScore() {
        return gameStatus.getGameScore();
    }

    /**
     * 获取连击奖励分数
     * 
     * @return 连击奖励分数
     */
    public int getComboScore() {
        return gameStatus.getComboScore();
    }

    /**
     * 获取时间奖励得分
     * 
     * @return 时间奖励得分
     */
    public int getRewardScore() {
        return gameStatus.getRewardScore();
    }

    /**
     * 获取游戏总得分
     * 
     * @return 游戏总得分
     */
    public int getTotalScore() {
        return getGameScore() + getRewardScore();
    }

    /**
     * 获取游戏连击数
     * 
     * @return 游戏连击数
     */
    public int getGameCombo() {
        return gameStatus.getGameCombo();
    }

    /**
     * 获取所有卡片信息
     */
    public Piece[][] getPieces() {
        return gameService.getPieces();
    }

    /**
     * 判断当前是否存在游戏卡片
     */
    public boolean hasPieces() {
        return gameService.hasPieces();
    }

    /**
     * 找到当前坐标点的卡片
     */
    public Piece findPiece(float x, float y) {
        return gameService.findPiece(x, y);
    }

    /**
     * 获取两个卡片间的路径信息，若不能消除，返回null
     */
    public LinkInfo link(Piece p1, Piece p2) {
        return gameService.link(p1, p2);
    }

    private LevelCfg levelCfg;
    private GameStatus gameStatus;
    private IGameAction listener;
    private Piece selected = null;
    private GameService gameService;
    private AlignContext alignContext = null;
    private PiecePair pair = null;
}
