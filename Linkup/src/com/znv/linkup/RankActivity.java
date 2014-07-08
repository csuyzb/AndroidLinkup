package com.znv.linkup;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.view.dialog.ConfirmDialog;
import com.znv.linkup.view.indicator.CirclePageIndicator;
import com.znv.linkup.view.indicator.Rank;
import com.znv.linkup.view.indicator.RankAdapter;

/**
 * 关卡选择界面活动处理类
 * 
 * @author yzb
 * 
 */
public class RankActivity extends BaseActivity implements OnPageChangeListener {

    private int modeIndex = -1;
    private RankAdapter rankAdapter = null;
    private List<RankCfg> rankCfgs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_rank);

        // 初始化游戏等级
        initRank();

        new RankAsyncTask().execute();
    }

    @Override
    protected void playMusic() {

    }

    @Override
    protected void stopMusic() {

    }

    /**
     * 初始化游戏等级
     */
    private void initRank() {

        LinearLayout root = (LinearLayout) RankActivity.this.findViewById(R.id.rankBg);

        int index = getIntent().getIntExtra("modeIndex", 0);
        if (index != modeIndex) {
            rankCfgs = modeCfgs.get(index).getRankInfos();
        }
        root.setBackgroundResource(ViewSettings.GameBgImageIds[rankCfgs.get(0).getRankBackground()]);
    }

    /**
     * 切换背景图片
     */
    @Override
    public void onPageSelected(int arg0) {
        soundMgr.pageChanged();
        LinearLayout root = (LinearLayout) RankActivity.this.findViewById(R.id.rankBg);
        root.setBackgroundResource(ViewSettings.GameBgImageIds[rankCfgs.get(arg0).getRankBackground()]);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (rankAdapter != null) {
            rankAdapter.changeRankCfgs(rankCfgs, false);
        }
    }

    /**
     * 处理网络消息回调的handler
     */
    @SuppressLint("HandlerLeak")
    public Handler netMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ViewSettings.MSG_UPDATE_GOLD: {
                onUpdateAward();
            }
                break;
            case ViewSettings.MSG_NETWORK_EXCEPTION: {
                Toast.makeText(RankActivity.this, R.string.network_exception, Toast.LENGTH_SHORT).show();
            }
                break;
            }
        }
    };

    public void onUpdateAward() {

    }

    /**
     * 异步加载关卡
     * 
     * @author yzb
     * 
     */
    private class RankAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int index = getIntent().getIntExtra("modeIndex", 0);
            if (index != modeIndex) {
                modeIndex = index;
                // 等待线程创建完成
                while (rankAdapters == null || rankAdapters.length <= modeIndex || rankAdapters[modeIndex] == null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 使用缓存数据
                rankAdapter = rankAdapters[modeIndex];

                rankAdapter.setLevelListener(new Rank.ISelectedLevel() {

                    @Override
                    public void onSelectedLevel(final LevelCfg levelCfg) {
                        soundMgr.select();
                        if (levelCfg.isActive()) {
                            Intent intent = new Intent(RankActivity.this, GameActivity.class);
                            intent.putExtra("levelIndex", levelCfg.getLevelId());
                            startActivity(intent);
                        } else {
                            if (userInfo != null && userInfo.getDiamond(RankActivity.this) >= 5) {
                                ConfirmDialog dialog = new ConfirmDialog(RankActivity.this);
                                dialog.setTitle(RankActivity.this.getString(R.string.unlock_title));
                                dialog.setMessage(RankActivity.this.getString(R.string.unlock_msg, levelCfg.getRankName() + "-" + levelCfg.getLevelName()));
                                dialog.setPositiveButton(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // 消耗钻石
                                        userInfo.addDiamond(RankActivity.this, -5);
                                        // 更新网络钻石数
                                        UserScore.updateAward(userInfo.getUserId(), -5, 0, RankActivity.this.netMsgHandler);

                                        // 解锁关卡
                                        levelCfg.setActive(true);
                                        LevelScore nls = new LevelScore(levelCfg.getLevelId());
                                        nls.setIsActive(1);
                                        DbScore.updateActive(nls);

                                        // 进入关卡
                                        Intent intent = new Intent(RankActivity.this, GameActivity.class);
                                        intent.putExtra("levelIndex", levelCfg.getLevelId());
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(rankAdapter);

            // 设置Indicator
            CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            mIndicator.setViewPager(pager);
            mIndicator.setOnPageChangeListener(RankActivity.this);

            // 更新
            rankAdapter.changeRankCfgs(rankCfgs, false);
        }

    }
}
