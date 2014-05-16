package com.znv.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.view.indicator.CirclePageIndicator;
import com.znv.linkup.view.indicator.RankPager;

/**
 * 关卡选择界面活动处理类
 * 
 * @author yzb
 * 
 */
public class RankActivity extends BaseActivity implements OnPageChangeListener {

    private static RankPager rankPager = null;

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
        root.setBackgroundResource(ViewSettings.RankBgImageIds[0]);

        if (rankPager == null) {
            rankPager = new RankPager(this, rankCfgs, new RankPager.ISelectedLevel() {

                @Override
                public void onSelectedLevel(LevelCfg levelCfg) {
                    if (levelCfg.isActive()) {
                        Intent intent = new Intent(RankActivity.this, GameActivity.class);
                        intent.putExtra("levelIndex", levelCfg.getLevelId());
                        startActivity(intent);
                    }
                }
            });
        }

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
        LinearLayout root = (LinearLayout) RankActivity.this.findViewById(R.id.rankBg);
        root.setBackgroundResource(ViewSettings.RankBgImageIds[arg0]);
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
