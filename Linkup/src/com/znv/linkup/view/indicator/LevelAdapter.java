package com.znv.linkup.view.indicator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.GameMode;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.util.StringUtil;
import com.znv.linkup.view.indicator.Level.LevelHolder;

/**
 * Rank下所有关卡的数据适配类
 * 
 * @author yzb
 * 
 */
public class LevelAdapter extends BaseAdapter {

    public LevelAdapter(Context context, RankCfg rankCfg) {
        this.context = context;
        this.rankCfg = rankCfg;
        this.levels = new Level[rankCfg.getLevelInfos().size()];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = new Level(context);
        }
    }

    @Override
    public int getCount() {
        return levels.length;
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
        if (position >= 0 && position < levels.length) {
            // 更新holder
            updateLevelView(levels[position].getLevelHolder(), position);

            return levels[position].getLevelView();
        }
        return null;
    }

    /**
     * 更新关卡数据，包括星级和是否解锁
     */
    public void updateLevelData(boolean isInit) {
        for (int i = 0; i < levels.length; i++) {
            if (isInit || getItem(i).isActive()) {
                updateLevelView(levels[i].getLevelHolder(), i);
            }
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
    private void updateLevelView(LevelHolder holder, int position) {
        LevelCfg levelCfg = getItem(position);
        if (levelCfg.isActive()) {
            holder.tvLevel.setBackgroundResource(R.drawable.levelbg);
            int level = Integer.parseInt(levelCfg.getLevelName());
            holder.tvLevel.setImageResource(ViewSettings.Numbers[level]);
            if (levelCfg.getLevelMode() == GameMode.Level) {
                holder.rbStar.setVisibility(View.VISIBLE);
                holder.tvTime.setVisibility(View.GONE);
                holder.tvTask.setVisibility(View.GONE);
                holder.rbStar.setRating(getItem(position).getLevelStar());
            } else if (levelCfg.getLevelMode() == GameMode.Time) {
                holder.rbStar.setVisibility(View.GONE);
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvTask.setVisibility(View.GONE);
                if (getItem(position).getMaxScore() > 0) {
                    holder.tvTime.setText(StringUtil.secondToString(getItem(position).getMaxScore()));
                } else {
                    holder.tvTime.setText("");
                }
            } else if (levelCfg.getLevelMode() == GameMode.ScoreTask) {
                holder.rbStar.setVisibility(View.GONE);
                holder.tvTime.setVisibility(View.GONE);
                holder.tvTask.setVisibility(View.VISIBLE);
                holder.tvTask.setText(String.valueOf(getItem(position).getTask()));
                if (getItem(position).getMaxScore() > 0) {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.pass);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tvTask.setCompoundDrawables(null, null, drawable, null);
                }
            }
        }
    }

    private Context context = null;
    private RankCfg rankCfg;
    private Level[] levels = null;
}
