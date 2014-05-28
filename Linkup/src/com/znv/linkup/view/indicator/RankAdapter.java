package com.znv.linkup.view.indicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;

/**
 * Rank下所有关卡的数据适配类
 * 
 * @author yzb
 * 
 */
public class RankAdapter extends BaseAdapter {

    /**
     * 利用ViewHolder提升adapter效率
     * 
     * @author yzb
     * 
     */
    class LevelViewHolder {
        ImageView tvLevel;
        RatingBar rbStar;
    }

    public RankAdapter(Context context, RankCfg rankCfg) {
        inflater = LayoutInflater.from(context);
        this.rankCfg = rankCfg;
    }

    @Override
    public int getCount() {
        return rankCfg.getLevelInfos().size();
    }

    @Override
    public LevelCfg getItem(int position) {
        return rankCfg.getLevelInfos().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取每一个关卡的视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LevelViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.level, null);

            holder = new LevelViewHolder();
            holder.tvLevel = (ImageView) convertView.findViewById(R.id.tvLevel);
            holder.rbStar = (RatingBar) convertView.findViewById(R.id.rbStar);
            // 缓存holder
            convertView.setTag(holder);

            levels.add(holder);
        } else {
            holder = (LevelViewHolder) convertView.getTag();
        }

        // 更新holder
        updateLevelView(holder, position);

        return convertView;
    }

    /**
     * 更新关卡数据，包括星级和是否解锁
     */
    public void updateLevelData() {
        int levelCount = levels.size();
        if (levelCount > rankCfg.getLevelInfos().size()) {
            levelCount = rankCfg.getLevelInfos().size();
        }
        for (int i = 0; i < levelCount; i++) {
            updateLevelView(levels.get(i), i);
        }
    }

    /**
     * 更新一个关卡数据
     * 
     * @param holder
     *            当前关卡的holder
     * @param position
     *            关卡索引
     */
    private void updateLevelView(LevelViewHolder holder, int position) {
        LevelCfg levelCfg = getItem(position);
        if (levelCfg.isActive()) {
            holder.tvLevel.setBackgroundResource(R.drawable.levelbg);
            int level = Integer.parseInt(levelCfg.getLevelName());
            holder.tvLevel.setImageResource(ViewSettings.Numbers[level]);
            holder.rbStar.setRating(getItem(position).getLevelStar());
        } else {
            holder.tvLevel.setBackgroundResource(R.drawable.locked);
            holder.rbStar.setVisibility(View.INVISIBLE);
        }

    }

    private LayoutInflater inflater;
    private RankCfg rankCfg;
    private List<LevelViewHolder> levels = new ArrayList<LevelViewHolder>();
}
