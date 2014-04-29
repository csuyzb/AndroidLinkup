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
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;

public class RankPager extends PagerAdapter implements IconPagerAdapter {

//    class RankViewHolder {
//        TextView rankLevels;
//        TextView rankStars;
//    }

    public interface ISelectedLevel {
        void onSelectedLevel(LevelCfg levelCfg);
    }

    private Context context;
    private LayoutInflater inflater;
    private List<RankCfg> rankCfgs = new ArrayList<RankCfg>();
//    private List<RankViewHolder> rankViewHolders = new ArrayList<RankViewHolder>();
    private List<View> grids = new ArrayList<View>();
    private List<RankAdapter> rankAdapters = new ArrayList<RankAdapter>();
    private ISelectedLevel levelListener;

    public RankPager(Context context, List<RankCfg> rankCfgs) {
        this(context, rankCfgs, null);
    }

    public RankPager(Context context, List<RankCfg> rankCfgs, ISelectedLevel levelListener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        this.rankCfgs = rankCfgs;
        this.levelListener = levelListener;

        initRankPages();
    }

    private void initRankPages() {

        int index = 0;
        for (final RankCfg rankCfg : rankCfgs) {
            View rank = inflater.inflate(R.layout.rank, null);

            TextView text = (TextView) rank.findViewById(R.id.rankName);
            text.setText(rankCfg.getRankName());
            text.setTextColor(context.getResources().getColor(ViewSettings.RankTitleBgColor[index]));
            text.setBackgroundResource(R.drawable.title_bg);

            RankAdapter adapter = new RankAdapter(context, rankCfg);
            GridView grid = (GridView) rank.findViewById(R.id.rankGrid);
            grid.setAdapter(adapter);
            grid.setColumnWidth(rankCfg.getLevelInfos().get(0).getPieceWidth() + 20);

            grid.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (levelListener != null) {
                        levelListener.onSelectedLevel(rankCfg.getLevelInfos().get(arg2));
                    }
                }
            });

//            RankViewHolder holder = new RankViewHolder();
//            holder.rankLevels = (TextView) rank.findViewById(R.id.rankLevels);
//            holder.rankStars = (TextView) rank.findViewById(R.id.rankStars);
//            rankViewHolders.add(holder);

            grids.add(rank);

            rankAdapters.add(adapter);
            index++;
        }
    }

    public void updateRankData() {
        for (int i = 0; i < rankCfgs.size(); i++) {
//            RankViewHolder holder = rankViewHolders.get(i);
//
//            int actLevelCount = DbScore.selectLevelByRank(rankCfgs.get(i).getRankId());
//            holder.rankLevels.setText(String.valueOf(actLevelCount) + "/" + rankCfgs.get(i).getLevelInfos().size());
//
//            int actStarCount = DbScore.selectStarByRank(rankCfgs.get(i).getRankId());
//            holder.rankStars.setText(String.valueOf(actStarCount) + "/" + rankCfgs.get(i).getLevelInfos().size() * 5);

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

    @Override
    public int getIconResId(int index) {
        return ViewSettings.IndicatorImageIds[index];
    }

}
