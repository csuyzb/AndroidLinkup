package com.znv.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.view.indicator.IconPageIndicator;
import com.znv.linkup.view.indicator.RankPager;

public class RankActivity extends FullScreenActivity implements OnPageChangeListener {

    private static RankPager rankPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_rank);

        initRank();
    }

    private void initRank() {

        if (rankPager == null) {
            rankPager = new RankPager(this, rankCfgs, new RankPager.ISelectedLevel() {

                @Override
                public void onSelectedLevel(LevelCfg levelCfg) {
                    if (levelCfg.isActive()) {
                        startLevel(levelCfg);
                    }
                }
            });
        }

        rankPager.updateRankData();

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(rankPager);

        IconPageIndicator mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);
        // mIndicator.setPadding(10, 10, 10, 10);
        mIndicator.setOnPageChangeListener(this);
    }

    private void startLevel(LevelCfg levelCfg) {
        Intent intent = new Intent(RankActivity.this, GameActivity.class);
        intent.putExtra("levelIndex", levelCfg.getLevelId());
        startActivity(intent);
    }

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
        if (rankPager != null) {
            rankPager.updateRankData();
        }
        super.onResume();
    }
}
