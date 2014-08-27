package com.znv.linkup;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.znv.linkup.core.Game;
import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.IGameAction;
import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.config.GameAlign;
import com.znv.linkup.core.config.GameMode;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.util.AnimatorUtil;
import com.znv.linkup.util.Stopwatch;
import com.znv.linkup.util.StringUtil;
import com.znv.linkup.util.ToastUtil;
import com.znv.linkup.view.CardsView;
import com.znv.linkup.view.PathView;
import com.znv.linkup.view.dialog.FailDialog;
import com.znv.linkup.view.dialog.ResultInfo;
import com.znv.linkup.view.dialog.SuccessDialog;
import com.znv.linkup.view.dialog.TaskDialog;
import com.znv.linkup.view.dialog.TimeDialog;
import com.znv.linkup.view.handler.GameMsgHandler;

/**
 * 游戏主界面活动处理类
 */
public class GameActivity extends BaseActivity implements IGameAction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_linkup);

        Stopwatch sw = new Stopwatch();
        sw.start();

        Display mDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        curLevelCfg = levelCfgs.get(getIntent().getIntExtra("levelIndex", 0));

        holder.tvLevel = (TextView) findViewById(R.id.tvLevel);
        holder.tvRecord = (TextView) findViewById(R.id.maxScore);
        holder.pbTime = (ProgressBar) findViewById(R.id.pbTime);
        holder.tsScore = (TextSwitcher) findViewById(R.id.scoreText);
        holder.flBackground = (FrameLayout) findViewById(R.id.rootFrame);
        holder.startCoin = (ImageView) findViewById(R.id.startCoin);
        holder.endCoin = (ImageView) findViewById(R.id.endCoin);
        holder.tvCombo = (TextView) findViewById(R.id.tvCombo);
        holder.tvAnimMsg = (TextView) findViewById(R.id.tvAnimMsg);
        holder.screenWidth = size.x;
        holder.screenHeight = size.y;
        holder.screenCenter = new Point((int) (size.x * 0.5), (int) (size.y * 0.5));
        holder.tools = findViewById(R.id.tools);
        holder.btnPrompt = (Button) findViewById(R.id.prompt);
        holder.btnRefresh = (Button) findViewById(R.id.refresh);
        holder.btnAddTime = (Button) findViewById(R.id.addTime);
        holder.tsScore.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                TextView tv = new TextView(GameActivity.this);
                tv.setTextSize(30);
                tv.setTextColor(0xffff6347);
                tv.setGravity(Gravity.CENTER);
                return tv;
            }
        });
        if (curLevelCfg.getLevelMode() == GameMode.Level || curLevelCfg.getLevelMode() == GameMode.ScoreTask) {
            holder.tsScore.setInAnimation(this, R.anim.slide_in_up);
            holder.tsScore.setOutAnimation(this, R.anim.slide_out_up);
        }

        pathView = new PathView(this);
        holder.flBackground.addView(pathView, -1, -1);
        cardsView = (CardsView) findViewById(R.id.cardsView);
        failDialog = new FailDialog(this);
        successDialog = new SuccessDialog(this);
        timeDialog = new TimeDialog(this);
        taskDialog = new TaskDialog(this);

        sw.stop();
        Log.e("game load1", String.valueOf(sw.getElapsedTime()));

        sw.start();
        start();
        sw.stop();
        Log.e("game load2", String.valueOf(sw.getElapsedTime()));
    }

    /**
     * 随机播放背景音乐
     */
    @Override
    protected void playMusic() {
        if (musicMgr != null) {
            musicMgr.setBgMusicRes(R.raw.bgmusic);
            musicMgr.play();
        }
    }

    /**
     * 开始游戏
     */
    public void start() {
        if (game != null) {
            game.finish();
        }

        if (!curLevelCfg.isAdjust()) {
            // 根据屏幕动态调整卡片大小及位置
            adjustLevelCfg();
        }

        holder.btnPrompt.setText(String.valueOf(LevelCfg.globalCfg.getPromptNum()));
        holder.btnRefresh.setText(String.valueOf(LevelCfg.globalCfg.getRefreshNum()));
        holder.btnAddTime.setText(String.valueOf(LevelCfg.globalCfg.getAddTimeNum()));
        holder.tvLevel.setText(curLevelCfg.getRankName() + "-" + curLevelCfg.getLevelName());
        holder.pbTime.setMax(curLevelCfg.getLevelTime());
        holder.flBackground.setBackgroundResource(ViewSettings.GameBgImageIds[curLevelCfg.getLevelBackground()]);
        if (curLevelCfg.getLevelMode() == GameMode.Level) {
            holder.tsScore.setText("0");
            holder.tvRecord.setText(getString(R.string.game_level_record) + String.valueOf(curLevelCfg.getMaxScore()));
        } else if (curLevelCfg.getLevelMode() == GameMode.Time) {
            holder.pbTime.setVisibility(View.GONE);
            holder.btnAddTime.setVisibility(View.GONE);
            holder.tsScore.setText("00:00");
            if (curLevelCfg.getMaxScore() == 0) {
                holder.tvRecord.setText(getString(R.string.game_level_norecord));
            } else {
                holder.tvRecord.setText(getString(R.string.game_level_record) + StringUtil.secondToString(curLevelCfg.getMinTime()));
            }
        } else if (curLevelCfg.getLevelMode() == GameMode.ScoreTask) {
            holder.pbTime.setVisibility(View.GONE);
            holder.btnAddTime.setVisibility(View.GONE);
            holder.tsScore.setText("0");
            holder.tvRecord.setText(getString(R.string.game_level_task) + curLevelCfg.getScoreTask());
        } else if (curLevelCfg.getLevelMode() == GameMode.TimeTask) {
            holder.pbTime.setVisibility(View.GONE);
            holder.btnAddTime.setVisibility(View.GONE);
            holder.tsScore.setText("00:00");
            holder.tvRecord.setText(getString(R.string.game_level_task) + StringUtil.secondToString(curLevelCfg.getTimeTask()));
        } else if (curLevelCfg.getLevelMode() == GameMode.Star) {
            holder.pbTime.setVisibility(View.GONE);
            holder.btnAddTime.setVisibility(View.GONE);
            holder.tsScore.setText(String.format("%d/%d", 0, curLevelCfg.getStars()));
            if (curLevelCfg.getMaxScore() == 0) {
                holder.tvRecord.setText(getString(R.string.game_level_norecord));
            } else {
                holder.tvRecord.setText(getString(R.string.game_level_record) + StringUtil.secondToString(curLevelCfg.getMinTime()));
            }
        }

        game = new Game(curLevelCfg, this);
        cardsView.setGame(game);

        initAnimation();
        game.start();
    }

    /**
     * 处理开始动画
     */
    private void initAnimation() {
        // 从上面落下
        // cardsView.setVisibility(View.GONE);
        // Animator cardsAnim = ObjectAnimator.ofFloat(cardsView, "translationY", -holder.screenHeight, 0);
        // cardsAnim.setDuration(750);
        // cardsAnim.setStartDelay(50);
        // cardsAnim.addListener(new AppearAnimator(cardsView));
        // cardsAnim.start();

        // 播放声音和动画
        showAnimMsg(getString(R.string.game_ready_go), 30, 20);
        soundMgr.readyGo();

        // 工具条动画
        // holder.tools.setVisibility(View.GONE);
        // Animator toolsAnim = ObjectAnimator.ofFloat(holder.tools, "translationY", 100, 0);
        // toolsAnim.setDuration(500);
        // toolsAnim.setStartDelay(500);
        // toolsAnim.addListener(new AppearAnimator(holder.tools));
        // toolsAnim.start();

    }

    /**
     * 根据屏幕动态调整卡片大小及位置
     */
    private void adjustLevelCfg() {
        // 上下保留一部分空间
        int heightReserve = 60;
        // 默认保留半个图标的边距
        int levelWidth = holder.screenWidth / (curLevelCfg.getXSize() - 1);
        // 每行大于6个游戏图标时不保留边距
        if (curLevelCfg.getXSize() > 8) {
            levelWidth = (int) (holder.screenWidth / (curLevelCfg.getXSize() - 1.6));
        }
        int levelHeight = (holder.screenHeight - heightReserve) / curLevelCfg.getYSize();
        // 像素调整为2的倍数
        int newSize = (Math.min(levelWidth, levelHeight) / 4) * 4;
        int beginX = (holder.screenWidth - newSize * curLevelCfg.getXSize()) / 2;
        int beginY = (holder.screenHeight - newSize * curLevelCfg.getYSize()) / 2;
        curLevelCfg.setPieceWidth(newSize);
        curLevelCfg.setPieceHeight(newSize);
        curLevelCfg.setBeginImageX(beginX);
        curLevelCfg.setBeginImageY(beginY);
        curLevelCfg.setAdjust(true);
    }

    /**
     * 下一关
     */
    public void next() {
        game.finish();

        int nextLevelId = curLevelCfg.getLevelId() + 1;
        curLevelCfg = levelCfgs.get(nextLevelId);
        if (curLevelCfg != null) {
            start();
        } else {
            // 通过所有关卡
            showAnimMsg(getString(R.string.game_success), 30, 0);
            onBackPressed();
        }
    }

    /**
     * 游戏失败时的处理
     */
    @Override
    public void onGameFail() {
        handler.sendEmptyMessage(ViewSettings.FailMessage);
    }

    /**
     * 游戏胜利时的处理
     */
    @Override
    public void onGameWin() {
        // 激活下一关
        int nextLevelId = curLevelCfg.getLevelId() + 1;
        LevelCfg nextCfg = levelCfgs.get(nextLevelId);
        if (nextCfg != null) {
            nextCfg.setActive(true);

            LevelScore nls = new LevelScore(nextLevelId);
            nls.setIsActive(1);
            DbScore.updateActive(nls);
        }

        int isRecord = 0;
        int stars = 0;
        if (curLevelCfg.getLevelMode() == GameMode.Level) {
            // 保存记录和星级
            if (game.getTotalScore() > curLevelCfg.getMaxScore()) {
                // 新纪录
                LevelScore cls = new LevelScore(curLevelCfg.getLevelId());
                cls.setMaxScore(game.getTotalScore());
                cls.setMinTime(game.getGameTime());
                cls.setStar(curLevelCfg.getStar(game.getTotalScore()));
                DbScore.updateScore(cls);

                // 更新缓存
                curLevelCfg.setLevelStar(cls.getStar());
                curLevelCfg.setMinTime(cls.getMinTime());
                curLevelCfg.setMaxScore(cls.getMaxScore());
                isRecord = 1;
            }
            stars = curLevelCfg.getStar(game.getTotalScore());
        } else if (curLevelCfg.getLevelMode() == GameMode.Time || curLevelCfg.getLevelMode() == GameMode.Star) {
            if (curLevelCfg.getMinTime() == 0 || game.getGameTime() < curLevelCfg.getMinTime()) {
                // 更新时间记录
                LevelScore cls = new LevelScore(curLevelCfg.getLevelId());
                cls.setMaxScore(game.getGameScore());
                cls.setMinTime(game.getGameTime());
                DbScore.updateScore(cls);

                // 更新缓存
                curLevelCfg.setMaxScore(cls.getMaxScore());
                curLevelCfg.setMinTime(cls.getMinTime());
                isRecord = 1;
            }
            stars = curLevelCfg.getStar(game.getGameScore());
        } else if (curLevelCfg.getLevelMode() == GameMode.ScoreTask) {
            if (game.getGameScore() >= curLevelCfg.getScoreTask()) {
                if (game.getGameScore() > curLevelCfg.getMaxScore()) {
                    // 更新任务完成记录
                    LevelScore cls = new LevelScore(curLevelCfg.getLevelId());
                    cls.setMaxScore(game.getGameScore());
                    cls.setMinTime(game.getGameTime());
                    DbScore.updateScore(cls);
                }

                // 更新缓存
                curLevelCfg.setMaxScore(game.getGameScore());
                curLevelCfg.setMinTime(game.getGameTime());

                // 是否完成任务
                isRecord = 1;
            }

            stars = curLevelCfg.getStar(game.getGameScore());
        } else if (curLevelCfg.getLevelMode() == GameMode.TimeTask) {
            if (game.getGameTime() <= curLevelCfg.getTimeTask()) {
                if (curLevelCfg.getMinTime() == 0 || game.getGameTime() < curLevelCfg.getMinTime()) {
                    // 更新任务完成记录
                    LevelScore cls = new LevelScore(curLevelCfg.getLevelId());
                    cls.setMaxScore(game.getGameScore());
                    cls.setMinTime(game.getGameTime());
                    DbScore.updateScore(cls);
                }

                // 更新缓存
                curLevelCfg.setMaxScore(game.getGameScore());
                curLevelCfg.setMinTime(game.getGameTime());
                // 是否完成任务
                isRecord = 1;
            }
            stars = curLevelCfg.getStar(game.getGameScore());
        }

        handler.sendMessage(handler.obtainMessage(ViewSettings.WinMessage, isRecord, stars));
    }

    /**
     * 游戏提示
     */
    public void onPrompt(PiecePair pair) {
        if (pair == null) {
            // 没有可消除时给出提示
            Toast toast = ToastUtil.getToast(this, R.string.game_prompt_none);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160);
            toast.show();
        } else {
            cardsView.prompt(pair);
            // 减少提示一次
            LevelCfg.globalCfg.setPromptNum(LevelCfg.globalCfg.getPromptNum() - 1);
            setGlobalCfg();
            handler.sendEmptyMessage(ViewSettings.PromptMessage);
            soundMgr.prompt();
        }
    }

    /**
     * 取消游戏提示
     */
    public void onUnPrompt(PiecePair pair) {
        if (pair != null) {
            cardsView.unPrompt(pair);
        }
    }

    /**
     * 连击时的处理
     */
    @Override
    public void onCombo(int combo) {
        if (combo % GameSettings.ComboMod == 0) {
            String msg = String.valueOf(combo);
            holder.tvCombo.setText(msg);
            int msgWidth = msg.length() * holder.screenWidth / 50 + getResources().getDrawable(R.drawable.combo).getMinimumWidth() / 2;
            Point startPoint = new Point(holder.screenCenter.x - msgWidth, (int) holder.tsScore.getY() + 100);
            Point endPoint = new Point(startPoint.x, startPoint.y - 50);
            animTranslate(holder.tvCombo, startPoint, endPoint, 1500, 0);

            soundMgr.combo();
        }
    }

    /**
     * 屏幕中心显示文字动画
     * 
     * @param msg
     *            文字信息
     * @param textSize
     *            字体大小
     */
    private void showAnimMsg(String msg, int textSize, int delay) {
        setAnimMsgStyle(msg, textSize);
        int msgWidth = msg.length() * holder.screenWidth / 50;
        Point startPoint = new Point(holder.screenCenter.x - msgWidth, holder.screenCenter.y);
        Point endPoint = new Point(startPoint.x, startPoint.y - 50);
        animTranslate(holder.tvAnimMsg, startPoint, endPoint, 1500, delay);
    }

    /**
     * 显示增加分数动画
     * 
     * @param msg
     *            文字信息
     * @param textSize
     *            字体大小
     */
    private void showAddScore(String msg, int textSize) {
        setAnimMsgStyle(msg, textSize);
        int msgWidth = msg.length() * holder.screenWidth / 60;
        Point startPoint = new Point(holder.screenCenter.x - msgWidth, (int) holder.tsScore.getY() + 100);
        Point endPoint = new Point(startPoint.x, startPoint.y - 40);
        animTranslate(holder.tvAnimMsg, startPoint, endPoint, 500, 0);
    }

    /**
     * 设置动画文字的样式
     * 
     * @param msg
     *            文字信息
     * @param textSize
     *            字体大小
     */
    private void setAnimMsgStyle(String msg, int textSize) {
        holder.tvAnimMsg.setText(msg);
        holder.tvAnimMsg.setTextSize(textSize);
    }

    /**
     * 重排时的处理
     */
    @Override
    public void onRefresh() {
        cardsView.createCards(false);

        soundMgr.refresh();
        // 减少重排一次
        LevelCfg.globalCfg.setRefreshNum(LevelCfg.globalCfg.getRefreshNum() - 1);
        setGlobalCfg();
        handler.sendEmptyMessage(ViewSettings.RefreshMessage);
    }

    /**
     * 选中时的处理
     */
    public void onCheck(Piece piece) {
        if (piece != null) {
            cardsView.check(piece);
            soundMgr.select();
        }
    }

    /**
     * 取消选中时的处理
     */
    public void onUnCheck(Piece piece) {
        if (piece != null) {
            cardsView.unCheck(piece);
        }
    }

    /**
     * 变换时的处理
     */
    @Override
    public void onTranslate() {
        if (curLevelCfg.getLevelAlign() != GameAlign.AlignNone) {
            cardsView.createCards(false);
        }
    }

    /**
     * 处理游戏消除路径
     */
    @Override
    public void onLinkPath(LinkInfo linkInfo) {
        pathView.showLines(linkInfo.getLinkPieces());

        if (curLevelCfg.getLevelMode() == GameMode.Level || curLevelCfg.getLevelMode() == GameMode.ScoreTask || curLevelCfg.getLevelMode() == GameMode.Star) {
            // 动画
            Point startPoint1 = linkInfo.getLinkPieces().get(0).getCenter();
            Point endPoint = new Point((int) (holder.tsScore.getLeft() + holder.tsScore.getWidth() * 0.5),
                    (int) (holder.tsScore.getTop() + holder.tsScore.getHeight() * 0.5));
            Point startPoint2 = linkInfo.getLinkPieces().get(linkInfo.getLinkPieces().size() - 1).getCenter();
            if (curLevelCfg.getLevelMode() == GameMode.Star) {
                // 收集星星
                holder.startCoin.setImageResource(R.drawable.star_32);
                holder.endCoin.setImageResource(R.drawable.star_32);

                if (linkInfo.getLinkPieces().get(0).isStar()) {
                    animTranslate(holder.startCoin, startPoint1, endPoint, 400, 0);
                }
                if (linkInfo.getLinkPieces().get(linkInfo.getLinkPieces().size() - 1).isStar()) {
                    animTranslate(holder.endCoin, startPoint2, endPoint, 400, 0);
                }
            } else {
                // 收集金币
                holder.startCoin.setImageResource(R.drawable.coin);
                holder.endCoin.setImageResource(R.drawable.coin);

                animTranslate(holder.startCoin, startPoint1, endPoint, 400, 0);
                animTranslate(holder.endCoin, startPoint2, endPoint, 400, 0);
            }
        }

        // 消除卡片
        cardsView.erase(linkInfo.getLinkPieces().get(0));
        cardsView.erase(linkInfo.getLinkPieces().get(linkInfo.getLinkPieces().size() - 1));

        soundMgr.erase();
    }

    @Override
    public void onErase() {
        // 判断是否需要自动重排,改为异步任务执行
        if (checkDeadLockTask != null) {
            checkDeadLockTask.cancel(true);
            checkDeadLockTask = null;
        }
        checkDeadLockTask = new CheckDeadLockTask();
        checkDeadLockTask.execute();
    }

    /**
     * 视图移动的动画
     * 
     * @param view
     *            要移动的视图
     * @param start
     *            起点
     * @param end
     *            终点
     * @param duration
     *            动画时长
     */
    private void animTranslate(View view, Point start, Point end, int duration, int delay) {
        AnimatorUtil.animTranslate(view, start.x, end.x, start.y, end.y, duration, delay, true);
    }

    /**
     * 时间改变时的处理
     */
    @Override
    public void onTimeChanged(int time) {
        handler.sendMessage(handler.obtainMessage(ViewSettings.TimeMessage, time, 0));
    }

    /**
     * 分数改变时的处理
     */
    @Override
    public void onScoreChanged(int score) {
        handler.sendMessage(handler.obtainMessage(ViewSettings.ScoreMessage, score, 0));
    }

    /**
     * 暂停游戏时的处理
     */
    @Override
    public void onGamePause() {

    }

    /**
     * 恢复游戏时的处理
     */
    @Override
    public void onGameResume() {

    }

    /**
     * 处理back键
     */
    @Override
    public void onBackPressed() {
        game.finish();
        super.onBackPressed();
    }

    /**
     * 暂停游戏
     */
    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
    }

    /**
     * 恢复游戏
     */
    @Override
    protected void onResume() {
        game.resume();
        super.onResume();
    }

    /**
     * 提示按钮按下时的处理
     * 
     * @param v
     */
    public void prompt(View v) {
        if (LevelCfg.globalCfg.getPromptNum() > 0) {
            game.prompt();
        } else {
            // 没有可用道具时给出提示
            Toast toast = ToastUtil.getToast(this, R.string.tool_no_prompt);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160);
            toast.show();
        }
    }

    /**
     * 重排按钮按下时的处理
     * 
     * @param v
     */
    public void refresh(View v) {
        if (LevelCfg.globalCfg.getRefreshNum() > 0) {
            game.refresh();
        } else {
            // 没有可用道具时给出提示
            Toast toast = ToastUtil.getToast(this, R.string.tool_no_refresh);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160);
            toast.show();
        }
    }

    /**
     * 重排按钮按下时的处理
     * 
     * @param v
     */
    public void addTime(View v) {
        if (LevelCfg.globalCfg.getAddTimeNum() > 0) {
            // 减少加时间一次
            LevelCfg.globalCfg.setAddTimeNum(LevelCfg.globalCfg.getAddTimeNum() - 1);
            setGlobalCfg();
            game.addGameTime(ViewSettings.AddTimeSeconds);

            showAddTime();
        } else {
            // 没有可用道具时给出提示
            Toast toast = ToastUtil.getToast(this, R.string.tool_no_addtime);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160);
            toast.show();
        }
    }

    /**
     * 重排按钮按下时的处理
     * 
     * @param v
     */
    public void stop(View v) {
        game.stop();
    }

    /**
     * Handler的消息处理--显示时间
     * 
     * @param seconds
     *            显示的时间秒数
     */
    public void showTime(int seconds) {
        if (curLevelCfg.getLevelMode() == GameMode.Level) {
            holder.pbTime.setProgress(seconds);
        } else if (curLevelCfg.getLevelMode() == GameMode.Time || curLevelCfg.getLevelMode() == GameMode.TimeTask) {
            holder.tsScore.setText(StringUtil.secondToString(seconds));
        }
    }

    /**
     * Handler的消息处理--显示分数
     */
    public void showScore(int score) {
        if (curLevelCfg.getLevelMode() == GameMode.Level || curLevelCfg.getLevelMode() == GameMode.ScoreTask) {
            if (score > 0) {
                // 显示增加分数动画
                int lastScore = Integer.parseInt((String) ((TextView) holder.tsScore.getCurrentView()).getText());
                String msg = "+" + String.valueOf(score - lastScore);
                showAddScore(msg, 20);
            }

            holder.tsScore.setText(String.valueOf(score));
        }
    }

    /**
     * Handler的消息处理--显示失败
     */
    public void showFail() {
        if (curLevelCfg.getLevelMode() == GameMode.Level) {
            failDialog.showDialog(game.getGameScore());
            soundMgr.fail();
        }
    }

    /**
     * Handler的消息处理--显示胜利
     */
    public void showSuccess(int isRecord, int stars) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setNewRecord(isRecord == 1);
        resultInfo.setMaxScore(curLevelCfg.getMaxScore());
        resultInfo.setMinTime(curLevelCfg.getMinTime());
        resultInfo.setUpload(curLevelCfg.isUpload());
        resultInfo.setLevel(curLevelCfg.getLevelId());
        resultInfo.setScore(game.getGameScore());
        resultInfo.setTime(game.getGameTime());
        if (userInfo != null) {
            resultInfo.setUserId(userInfo.getUserId());
        }
        resultInfo.setStars(stars);
        if (curLevelCfg.getLevelMode() == GameMode.Level) {
            resultInfo.setScore(game.getTotalScore());
            successDialog.showDialog(resultInfo);
        } else if (curLevelCfg.getLevelMode() == GameMode.Time || curLevelCfg.getLevelMode() == GameMode.Star) {
            timeDialog.showDialog(resultInfo);
        } else if (curLevelCfg.getLevelMode() == GameMode.ScoreTask) {
            taskDialog.showDialog(resultInfo);
        } else if (curLevelCfg.getLevelMode() == GameMode.TimeTask) {
            taskDialog.showDialog(resultInfo);
        }
        soundMgr.win();
    }

    /**
     * Handler的消息处理--显示提示数
     */
    public void showPrompt() {
        holder.btnPrompt.setText(String.valueOf(LevelCfg.globalCfg.getPromptNum()));
    }

    /**
     * Handler的消息处理--显示重排数
     */
    public void showRefresh() {
        holder.btnRefresh.setText(String.valueOf(LevelCfg.globalCfg.getRefreshNum()));
    }

    /**
     * Handler的消息处理--显示加时间数
     */
    public void showAddTime() {
        if (curLevelCfg.getLevelMode() == GameMode.Level) {
            holder.btnAddTime.setText(String.valueOf(LevelCfg.globalCfg.getAddTimeNum()));
        }
    }

    /**
     * 获取当前游戏关卡配置
     * 
     * @return 当前游戏关卡配置
     */
    public LevelCfg getLevelCfg() {
        return curLevelCfg;
    }

    private Game game;
    private CardsView cardsView;
    private FailDialog failDialog;
    private SuccessDialog successDialog;
    private TimeDialog timeDialog;
    private TaskDialog taskDialog;
    private LevelCfg curLevelCfg = null;
    private LevelHolder holder = new LevelHolder();
    private Handler handler = new GameMsgHandler(this);
    private PathView pathView = null;
    private CheckDeadLockTask checkDeadLockTask = null;

    /**
     * 界面信息缓存类
     * 
     * @author yzb
     * 
     */
    class LevelHolder {
        TextView tvLevel;
        TextView tvRecord;
        ProgressBar pbTime;
        TextSwitcher tsScore;
        FrameLayout flBackground;
        ImageView startCoin;
        ImageView endCoin;
        TextView tvCombo;
        TextView tvAnimMsg;
        int screenWidth;
        int screenHeight;
        Point screenCenter;
        View tools;
        Button btnPrompt;
        Button btnRefresh;
        Button btnAddTime;
    }

    /**
     * 判断死锁的异步任务
     * 
     * @author yzb
     * 
     */
    private class CheckDeadLockTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // 判断是否需要重排
            return game.hasPieces() && game.isDeadLock();
        }

        @Override
        protected void onPostExecute(Boolean needRefresh) {
            super.onPostExecute(needRefresh);

            if (needRefresh) {
                refresh(null);
            }
        }
    }

    @Override
    public void onStepChanged(int step) {
        if (curLevelCfg.getLevelMode() == GameMode.Endless) {
            holder.tsScore.setText(String.valueOf(step));
        }
    }

    @Override
    public void onStarChanged(int star) {
        if (curLevelCfg.getLevelMode() == GameMode.Star) {
            holder.tsScore.setText(String.format("%d/%d", star, curLevelCfg.getStars()));
        }
    }
}
