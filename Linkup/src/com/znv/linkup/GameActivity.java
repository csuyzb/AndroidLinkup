package com.znv.linkup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.znv.linkup.core.Game;
import com.znv.linkup.core.IGameOp;
import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.util.ImageUtil;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.util.AnimatorUtil;
import com.znv.linkup.view.GameView;
import com.znv.linkup.view.dialog.GameResultDialogs;
import com.znv.linkup.view.handler.GameMsgHandler;

/**
 * Game Activity
 */
public class GameActivity extends FullScreenActivity implements IGameOp {

    class LevelHolder {
        TextView tvLevel;
        TextView tvMaxScore;
        ProgressBar pbTime;
        TextSwitcher tsScore;
        FrameLayout flBackground;
        Bitmap bmSelected;
        ImageView startCoin;
        ImageView endCoin;
        TextView tvComb;
        int screenWidth;
        int screenHeight;
        Point screenCenter;
        Button tvPrompt;
        Button tvRefresh;
    }

    class ScreenInfo {

    }

    // private GameMenu gMenu;
    private Game game;
    private GameView gameView;
    private GameResultDialogs resultDialog;
    private LevelCfg curLevelCfg = null;
    private LevelHolder holder = new LevelHolder();
    private Handler handler = new GameMsgHandler(this);

    public void updateTime() {
        holder.pbTime.setProgress(game.getGameTime());
    }

    public void updateScore() {
        holder.tsScore.setText(String.valueOf(game.getGameScore()));
    }

    public void showFail() {
        resultDialog.lost();
    }

    public void showSuccess() {
        int stars = curLevelCfg.getStar(game.getTotalScore());
        curLevelCfg.setLevelStar(stars);
        boolean isNewRecord = false;
        if (game.getTotalScore() > curLevelCfg.getMaxScore()) {
            isNewRecord = true;
            curLevelCfg.setMaxScore(game.getTotalScore());
        }
        resultDialog.success(stars, isNewRecord);
    }

    public void showPrompt() {
        holder.tvPrompt.setText(String.valueOf(LevelCfg.globalCfg.getPromptNum()));
    }

    public void showRefresh() {
        holder.tvRefresh.setText(String.valueOf(LevelCfg.globalCfg.getRefreshNum()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_linkup);

        Display mDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        curLevelCfg = levelCfgs.get(getIntent().getStringExtra("levelIndex"));

        holder.tvLevel = (TextView) findViewById(R.id.tvLevel);
        holder.tvMaxScore = (TextView) findViewById(R.id.maxScore);
        holder.pbTime = (ProgressBar) findViewById(R.id.pbTime);
        holder.tsScore = (TextSwitcher) findViewById(R.id.scoreText);
        holder.flBackground = (FrameLayout) findViewById(R.id.rootFrame);
        holder.bmSelected = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
        holder.startCoin = (ImageView) findViewById(R.id.startCoin);
        holder.endCoin = (ImageView) findViewById(R.id.endCoin);
        holder.tvComb = (TextView) findViewById(R.id.comboText);
        holder.screenWidth = size.x;
        holder.screenHeight = size.y;
        holder.screenCenter = new Point((int) (size.x * 0.5), (int) (size.y * 0.5));
        holder.tvPrompt = (Button) findViewById(R.id.prompt);
        holder.tvRefresh = (Button) findViewById(R.id.refresh);
        holder.tsScore.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView tv = new TextView(GameActivity.this);
                tv.setTextSize(30);
                tv.setTextColor(0xFFccff33);
                tv.setGravity(Gravity.CENTER);
                return tv;
            }
        });

        // gMenu = new GameMenu(this);
        gameView = (GameView) findViewById(R.id.gameView);
        resultDialog = new GameResultDialogs(this);

        // 工具条动画
        View tools = findViewById(R.id.tools);
        AnimatorUtil.animTranslate(tools, tools.getX(), tools.getX(), tools.getY() + 100, tools.getY());

        start();
    }

    public void start() {
        if (game != null) {
            game.finish();
        }

        adjustLevelCfg(curLevelCfg);

        holder.tvPrompt.setText(String.valueOf(LevelCfg.globalCfg.getPromptNum()));
        holder.tvRefresh.setText(String.valueOf(LevelCfg.globalCfg.getRefreshNum()));
        holder.tvLevel.setText(curLevelCfg.getRankName() + "-" + curLevelCfg.getLevelName());
        holder.pbTime.setMax(curLevelCfg.getLevelTime());
        holder.tsScore.setText("0");
        holder.tvMaxScore.setText(getString(R.string.max_score) + String.valueOf(curLevelCfg.getMaxScore()));
        holder.flBackground.setBackgroundResource(ViewSettings.RankBgImageIds[curLevelCfg.getLevelBackground()]);
        gameView.setSelectedImage(ImageUtil.scaleBitmap(holder.bmSelected, curLevelCfg.getPieceWidth(), curLevelCfg.getPieceHeight()));
        gameView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    game.touch(event.getX(), event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gameView.postInvalidate();
                }
                return true;
            }
        });

        game = new Game(curLevelCfg, this);
        gameView.setGameService(game);

        // gMenu.show();

        game.start();
    }

    public void next() {
        game.finish();

        String nextLevelId = String.valueOf(Integer.parseInt(curLevelCfg.getLevelId()) + 1);
        if (levelCfgs.containsKey(nextLevelId)) {
            curLevelCfg = levelCfgs.get(nextLevelId);
            start();
        } else {
            showCenterToast(getString(R.string.game_success));
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onBackPressed();
        }
    }

    public void adjustLevelCfg(LevelCfg levelCfg) {
        int levelWidth = holder.screenWidth / (levelCfg.getXSize() - 1);
        int levelHeight = holder.screenHeight / levelCfg.getYSize();
        int newSize = Math.min(levelWidth, levelHeight);
        int beginX = (holder.screenWidth - newSize * levelCfg.getXSize()) / 2;
        int beginY = (holder.screenHeight - newSize * levelCfg.getYSize()) / 2;
        levelCfg.setPieceWidth(newSize);
        levelCfg.setPieceHeight(newSize);
        levelCfg.setBeginImageX(beginX);
        levelCfg.setBeginImageY(beginY);
        levelCfg.setContext(this);
    }

    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        game.resume();
        super.onResume();
    }

    @Override
    public void onGameFail() {
        handler.sendEmptyMessage(ViewSettings.FailMessage);
    }

    @Override
    public void onGameWin() {
        // LevelCfg levelCfg = levelCfgList.getCurLevelCfg();
        LevelScore ls = new LevelScore(Integer.parseInt(curLevelCfg.getLevelId()) + 1);
        ls.setIsActive(1);
        DbScore.updateActive(ls);

        ls = new LevelScore(Integer.parseInt(curLevelCfg.getLevelId()));
        if (game.getTotalScore() > curLevelCfg.getMaxScore()) {
            // 新纪录
            ls = new LevelScore(Integer.parseInt(curLevelCfg.getLevelId()));
            ls.setMaxScore(game.getTotalScore());
            ls.setStar(curLevelCfg.getStar(game.getTotalScore()));
            DbScore.updateScore(ls);
        }

        handler.sendEmptyMessage(ViewSettings.WinMessage);
    }

    @Override
    public void onPrompt(PiecePair pair) {
        gameView.setPromptPieces(pair);
        // 减少提示一次
        LevelCfg.globalCfg.setPromptNum(LevelCfg.globalCfg.getPromptNum() - 1);
        setGlobalCfg();
        handler.sendEmptyMessage(ViewSettings.PromptMessage);
        musicServer.select();
    }

    @Override
    public void onUnPrompt() {
        gameView.setPromptPieces(null);
    }

    @Override
    public void onCombo() {
        String msgFmt = "%s" + getResources().getString(R.string.game_combo_info) + ", +%s";
        String msg = String.format(msgFmt, game.getGameCombo(), game.getComboScore());
        if (game.getGameCombo() == ViewSettings.CombAddPrompt) {
            if (LevelCfg.globalCfg.getPromptNum() < ViewSettings.PromptMaxNum) {
                // promt 增加一次
                LevelCfg.globalCfg.setPromptNum(LevelCfg.globalCfg.getPromptNum() + 1);
                setGlobalCfg();
                handler.sendEmptyMessage(ViewSettings.PromptMessage);
                msg += getString(R.string.game_prompt_add);
            }
        } else if (game.getGameCombo() == ViewSettings.CombAddRefresh) {
            if (LevelCfg.globalCfg.getRefreshNum() < ViewSettings.RefreshMaxNum) {
                // refresh 增加一次
                LevelCfg.globalCfg.setRefreshNum(LevelCfg.globalCfg.getRefreshNum() + 1);
                setGlobalCfg();
                handler.sendEmptyMessage(ViewSettings.RefreshMessage);
                msg += getString(R.string.game_refresh_add);
            }
        }
        showCenterToast(msg);

    }

    private void showCenterToast(String msg) {
        holder.tvComb.setText(msg);
        int msgWidth = msg.length() * 10;
        Point endPoint = new Point(holder.screenCenter.x - msgWidth, holder.screenCenter.y - (int) (holder.tvComb.getHeight() * 0.5));
        Point startPoint = new Point(endPoint.x, endPoint.y + 100);
        animTranslate(holder.tvComb, startPoint, endPoint, 1500);
    }

    public String getGameResult(boolean isSuccess) {
        if (isSuccess) {
            return String.format("%s%s", getString(R.string.game_score), String.valueOf(game.getGameScore() + game.getRewardScore()));
        } else {
            return getString(R.string.game_score) + String.valueOf(game.getGameScore());
        }
    }

    @Override
    public void onRefresh() {
        // 减少重排一次
        LevelCfg.globalCfg.setRefreshNum(LevelCfg.globalCfg.getRefreshNum() - 1);
        setGlobalCfg();
        handler.sendEmptyMessage(ViewSettings.RefreshMessage);
    }

    @Override
    public void onCheck(Piece piece) {
        gameView.setSelectedPiece(piece);
        musicServer.select();
    }

    @Override
    public void onUnCheck() {
        gameView.setSelectedPiece(null);
    }

    @Override
    public void onTranslate() {
        musicServer.translate();
    }

    @Override
    public void onLinkPath(LinkInfo linkInfo) {
        gameView.setLinkInfo(linkInfo);
        Point startPoint = linkInfo.getLinkPieces().get(0).getCenter();
        Point endPoint = new Point((int) (holder.tsScore.getLeft() + holder.tsScore.getWidth() * 0.5),
                (int) (holder.tsScore.getTop() + holder.tsScore.getHeight() * 0.5));
        animTranslate(holder.startCoin, startPoint, endPoint, AnimatorUtil.defaultDuration);
        startPoint = linkInfo.getLinkPieces().get(linkInfo.getLinkPieces().size() - 1).getCenter();
        animTranslate(holder.endCoin, startPoint, endPoint, AnimatorUtil.defaultDuration);
        musicServer.erase();
    }

    private void animTranslate(View view, Point start, Point end, int duration) {
        AnimatorUtil.animAlpha(view, 0f, 1f, 10);
        AnimatorUtil.animTranslate(view, start.x, end.x, start.y, end.y, duration);
        AnimatorUtil.animAlpha(view, 1f, 0f, 10, duration - 10);
    }

    @Override
    public void onTimeChanged(int time) {
        handler.sendEmptyMessage(ViewSettings.GameTimeMessage);
    }

    @Override
    public void onScoreChanged(int score) {
        handler.sendEmptyMessage(ViewSettings.GameScoreMessage);
    }

    @Override
    public void onGamePause() {

    }

    @Override
    public void onGameResume() {

    }

    @Override
    public void onRefreshView() {
        gameView.postInvalidate();
    }

    @Override
    public void onBackPressed() {
        game.finish();
        super.onBackPressed();
    }

    public Game getGame() {
        return game;
    }

    public FrameLayout getRoot() {
        return holder.flBackground;
    }

    public void prompt(View v) {
        if (LevelCfg.globalCfg.getPromptNum() > 0) {
            game.prompt();
        }
    }

    public void refresh(View v) {
        if (LevelCfg.globalCfg.getRefreshNum() > 0) {
            game.refresh();
            musicServer.refresh();
        }
    }

    public void restart(View v) {
        start();
    }

    public void stop(View v) {
        game.stop();
    }

    public void back(View v) {
        onBackPressed();
    }
}
