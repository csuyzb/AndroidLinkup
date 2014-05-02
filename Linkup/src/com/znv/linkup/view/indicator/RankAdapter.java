package com.znv.linkup.view.indicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;

public class RankAdapter extends BaseAdapter {

    /**
     * 利用ViewHolder提升adapter效率
     * 
     * @author yzb
     * 
     */
    class LevelViewHolder {
        TextView textView;
        RatingBar rating;
    }

    private LayoutInflater inflater;
    private RankCfg rankCfg;
    private List<LevelViewHolder> levels = new ArrayList<LevelViewHolder>();

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LevelViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.level, null);

            holder = new LevelViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
            convertView.setTag(holder);

            levels.add(holder);
        } else {
            holder = (LevelViewHolder) convertView.getTag();
        }

        updateLevelView(holder, position);

        return convertView;
    }

    public void updateLevelData() {
        int levelCount = levels.size();
        if (levelCount > rankCfg.getLevelInfos().size()) {
            levelCount = rankCfg.getLevelInfos().size();
        }
        for (int i = 0; i < levelCount; i++) {
            updateLevelView(levels.get(i), i);
        }
    }

    private void updateLevelView(LevelViewHolder holder, int position) {
        LevelCfg levelCfg = getItem(position);
        if (levelCfg.isActive()) {
            holder.textView.setBackgroundResource(ViewSettings.RankLevelBgImages[Integer.parseInt(rankCfg.getRankId())]);
            holder.textView.setText(levelCfg.getLevelName());
        } else {
            holder.textView.setBackgroundResource(R.drawable.locked);
        }

        holder.rating.setRating(getItem(position).getLevelStar());
    }
}
