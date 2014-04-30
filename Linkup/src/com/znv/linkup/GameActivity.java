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
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
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
import com.znv.linkup.view.GameView;
import com.znv.linkup.view.animation.ViewPathAnimator;
import com.znv.linkup.view.animation.view.AnimatorImage;
import com.znv.linkup.view.animation.view.AnimatorText;
import com.znv.linkup.view.animation.view.IAnimatorView;
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
        ViewPathAnimator startCoin;
        ViewPathAnimator endCoin;
        ViewPathAnimator tvComb;
        int screenWidth;
        int screenHeight;
        Point screenCenter;
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
    private static FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

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
        holder.startCoin = new ViewPathAnimator(genAnimatorCoin());
        holder.endCoin = new ViewPathAnimator(genAnimatorCoin());
        holder.tvComb = new ViewPathAnimator(genAnimatorText());
        holder.tvComb.setDuration(1500);
        holder.screenWidth = size.x;
        holder.screenHeight = size.y;
        holder.screenCenter = new Point((int) (size.x * 0.5), (int) (size.y * 0.5));
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

        start();
    }

    private IAnimatorView genAnimatorCoin() {
        AnimatorImage view = new AnimatorImage(this);
        view.setImageResource(R.drawable.coin);
        view.setLayoutParams(params);
        view.setAlpha(0f);
        getRoot().addView(view);
        return view;
    }

    private IAnimatorView genAnimatorText() {
        AnimatorText view = new AnimatorText(this);
        view.setLayoutParams(params);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(0x00000000);
        view.setTextColor(0xFFFF3399);
        view.setTextSize(30);
        view.setText("test");
        view.setAlpha(0f);
        getRoot().addView(view);
        return view;
    }

    public void start() {
        if (game != null) {
            game.finish();
        }

        adjustLevelCfg(curLevelCfg);

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
                msg += getString(R.string.game_prompt_add);
            }
        } else if (game.getGameCombo() == ViewSettings.CombAddRefresh) {
            if (LevelCfg.globalCfg.getRefreshNum() < ViewSettings.RefreshMaxNum) {
                // refresh 增加一次
                LevelCfg.globalCfg.setRefreshNum(LevelCfg.globalCfg.getRefreshNum() + 1);
                msg += getString(R.string.game_refresh_add);
            }
        }
        showCenterToast(msg);
    }

    private void showCenterToast(String msg) {
        AnimatorText text = (AnimatorText) holder.tvComb.getView();
        text.setText(msg);
        holder.tvComb.onAnimationCancel(null);
        Point endPoint = new Point(holder.screenCenter.x - (int) (text.getWidth() * 0.5), holder.screenCenter.y - (int) (text.getHeight() * 0.5));
        Point startPoint = new Point(endPoint.x, endPoint.y + 100);
        holder.tvComb.animatePath(startPoint, endPoint);
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
        holder.startCoin.onAnimationCancel(null);
        holder.startCoin.animatePath(startPoint, endPoint);
        startPoint = linkInfo.getLinkPieces().get(linkInfo.getLinkPieces().size() - 1).getCenter();
        holder.endCoin.onAnimationCancel(null);
        holder.endCoin.animatePath(startPoint, endPoint);
        musicServer.erase();
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onGameResume() {
        // TODO Auto-generated method stub

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
        game.prompt();
    }

    public void refresh(View v) {
        game.refresh();
        musicServer.refresh();
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
