package com.znv.linkup.view.indicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;

/**
 * Rank数据的分页适配类
 * 
 * @author yzb
 * 
 */
public class Ranks extends PagerAdapter {

    /**
     * 选择关卡时的处理接口
     * 
     * @author yzb
     * 
     */
    public interface ISelectedLevel {
        void onSelectedLevel(LevelCfg levelCfg);
    }

    public Ranks(Context context, List<RankCfg> rankCfgs) {
        this(context, rankCfgs, null);
    }

    public Ranks(Context context, List<RankCfg> rankCfgs, ISelectedLevel levelListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        this.rankCfgs = rankCfgs;
        this.levelListener = levelListener;

        // 初始化游戏等级页面
        initRankPages();
    }

    /**
     * 初始化游戏等级页面
     */
    private void initRankPages() {

        for (final RankCfg rankCfg : rankCfgs) {
            View rank = inflater.inflate(R.layout.rank, null);

            TextView text = (TextView) rank.findViewById(R.id.rankName);
            text.setText(rankPrefix[Integer.parseInt(rankCfg.getRankId())] + rankCfg.getRankName());

            Levels adapter = new Levels(context, rankCfg);
            GridView grid = (GridView) rank.findViewById(R.id.rankGrid);
            grid.setAdapter(adapter);
//            grid.setColumnWidth(rankCfg.getLevelInfos().get(0).getPieceWidth() + 20);

            grid.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (levelListener != null) {
                        levelListener.onSelectedLevel(rankCfg.getLevelInfos().get(arg2));
                    }
                }
            });

            grids.add(rank);

            rankAdapters.add(adapter);
        }
    }

    /**
     * 更新每个关卡的数据
     */
    public void updateRankData() {
        for (int i = 0; i < rankCfgs.size(); i++) {
            rankAdapters.get(i).updateLevelData();
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
        View grid = grids.get(arg1);
        if (grid.getParent() != null) {
            ((ViewPager) grid.getParent()).removeView(grid);
        }
        ((ViewPager) arg0).addView(grid);
        return grid;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(grids.get(arg1));
    }

    private Context context;
    private LayoutInflater inflater;
    private List<RankCfg> rankCfgs = new ArrayList<RankCfg>();
    private List<View> grids = new ArrayList<View>();
    private List<Levels> rankAdapters = new ArrayList<Levels>();
    private ISelectedLevel levelListener;
    private static String[] rankPrefix = new String[] { "一.", "二.", "三.", "四.", "五.", "六.", "七.", "八.", "九.", "十." };

}
