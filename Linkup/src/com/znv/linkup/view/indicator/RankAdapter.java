package com.znv.linkup.view.indicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.view.indicator.Rank.RankHolder;

/**
 * Rank数据的分页适配类
 * 
 * @author yzb
 * 
 */
public class RankAdapter extends PagerAdapter {

    public RankAdapter(Context context, List<RankCfg> rankCfgs) {
        this.rankCfgs = rankCfgs;
        this.ranks = new Rank[rankCfgs.size()];
        for (int i = 0; i < ranks.length; i++) {
            ranks[i] = new Rank(context, rankCfgs.get(i));
            // 设置rank名称，更新时不会改变
            updateRankInfo(i);
        }
    }

    /**
     * 根据rankCfgs更新RankCfg
     * 
     * @param rankCfgs
     *            等级配置信息
     */
    public void changeRankCfgs(List<RankCfg> rankCfgs, boolean isInit) {
        this.rankCfgs = rankCfgs;
        for (int i = 0; i < ranks.length; i++) {
            ranks[i].changeRankCfg(rankCfgs.get(i), isInit);
            if (levelListener != null) {
                ranks[i].setLevelLister(levelListener);
            }
            // 设置rank名称，更新时不会改变
            updateRankInfo(i);
        }
    }

    /**
     * 设置选择关卡的监听器
     * 
     * @param iSelectedLevel
     *            选择关卡操作接口
     */
    public void setLevelListener(Rank.ISelectedLevel levelListener) {
        this.levelListener = levelListener;
        for (int i = 0; i < ranks.length; i++) {
            ranks[i].setLevelLister(levelListener);
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
        holder.tvTitle.setText(ViewSettings.RankPrefix[i] + rankCfgs.get(i).getRankName());
    }

    /**
     * 获取rank集合信息
     * 
     * @return rank集合信息
     */
    public Rank[] getRanks() {
        return ranks;
    }

    private Rank[] ranks = null;
    private Rank.ISelectedLevel levelListener = null;
    private List<RankCfg> rankCfgs = new ArrayList<RankCfg>();
}
