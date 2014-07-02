package com.znv.linkup.view.indicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.view.indicator.Rank.RankHolder;

/**
 * Rank数据的分页适配类
 * 
 * @author yzb
 * 
 */
public class RankAdapter extends PagerAdapter {

    public RankAdapter(Context context, List<RankCfg> rankCfgs, Rank.ISelectedLevel iSelectedLevel) {
        this.rankCfgs = rankCfgs;
        this.ranks = new Rank[rankCfgs.size()];
        for (int i = 0; i < ranks.length; i++) {
            ranks[i] = new Rank(context, rankCfgs.get(i), iSelectedLevel);
            // 设置rank名称，更新时不会改变
            updateRankInfo(i);
        }
    }

    /**
     * 更新每个关卡的数据
     */
    public void updateRankData() {
        for (int i = 0; i < rankCfgs.size(); i++) {
            ranks[i].getLevelAdapter().updateLevelData(false);
        }
    }

    @Override
    public int getCount() {
        return rankCfgs.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        View rankView = ranks[arg1].getRankView();
        if (rankView.getParent() != null) {
            ((ViewPager) rankView.getParent()).removeView(rankView);
        }
        ((ViewPager) arg0).addView(rankView);
        return rankView;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(ranks[arg1].getRankHolder().rankGrid);
    }

    /**
     * 更新rank信息
     * 
     * @param i
     *            页码
     */
    private void updateRankInfo(int i) {
        RankHolder holder = ranks[i].getRankHolder();
        holder.tvTitle.setText(rankPrefix[i] + rankCfgs.get(i).getRankName());
    }

    private Rank[] ranks = null;
    private List<RankCfg> rankCfgs = new ArrayList<RankCfg>();
    private static String[] rankPrefix = new String[] { "一.", "二.", "三.", "四.", "五.", "六.", "七.", "八.", "九.", "十." };

}
