package com.znv.linkup;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.view.indicator.CirclePageIndicator;
import com.znv.linkup.view.indicator.Ranks;

/**
 * 关卡选择界面活动处理类
 * 
 * @author yzb
 * 
 */
public class RankActivity extends BaseActivity implements OnPageChangeListener {

    private static int modeIndex = -1;
    private static Ranks rankPager = null;
    private static List<RankCfg> rankCfgs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_rank);

        // 初始化游戏等级
        initRank();
    }

    @Override
    protected void playMusic() {
        // if (musicMgr != null) {
        // musicMgr.setBgMusicRes(R.raw.bgmusic2);
        // musicMgr.play();
        // }
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
            modeIndex = index;
            rankCfgs = modeCfgs.get(index).getRankInfos();
            rankPager = new Ranks(this, rankCfgs, new Ranks.ISelectedLevel() {

                @Override
                public void onSelectedLevel(LevelCfg levelCfg) {
                    if (levelCfg.isActive()) {
                        soundMgr.select();
                        Intent intent = new Intent(RankActivity.this, GameActivity.class);
                        intent.putExtra("levelIndex", levelCfg.getLevelId());
                        startActivity(intent);
                    }
                }
            });
        }
        root.setBackgroundResource(ViewSettings.RankBgImageIds[rankCfgs.get(0).getRankBackground()]);

        // 更新等级数据
        rankPager.updateRankData();

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(rankPager);

        // 设置Indicator
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);
        mIndicator.setOnPageChangeListener(this);
    }

    /**
     * 切换背景图片
     */
    @Override
    public void onPageSelected(int arg0) {
        soundMgr.pageChanged();
        LinearLayout root = (LinearLayout) RankActivity.this.findViewById(R.id.rankBg);
        root.setBackgroundResource(ViewSettings.RankBgImageIds[rankCfgs.get(arg0).getRankBackground()]);
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
        if (rankPager != null) {
            // 更新等级数据
            rankPager.updateRankData();
        }
    }
}
