package com.znv.linkup.view.indicator;

import com.znv.linkup.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 关卡视图
 * 
 * @author yzb
 * 
 */
public class Level {

    private View levelView = null;
    private LevelHolder levelHolder = new LevelHolder();

    public Level(Context ctx) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        levelView = inflater.inflate(R.layout.level, null);

        levelHolder.tvLevel = (ImageView) levelView.findViewById(R.id.tvLevel);
        levelHolder.rbStar = (RatingBar) levelView.findViewById(R.id.rbStar);
        levelHolder.tvTime = (TextView) levelView.findViewById(R.id.tvTime);
        levelHolder.tvTask = (TextView) levelView.findViewById(R.id.tvTask);

        levelHolder.tvLevel.setBackgroundResource(R.drawable.locked);
        levelHolder.rbStar.setVisibility(View.INVISIBLE);
        levelHolder.tvTime.setVisibility(View.GONE);
        levelHolder.tvTask.setVisibility(View.GONE);
    }

    public View getLevelView() {
        return levelView;
    }

    public LevelHolder getLevelHolder() {
        return levelHolder;
    }

    /**
     * 利用ViewHolder提升adapter效率
     * 
     * @author yzb
     * 
     */
    public class LevelHolder {
        ImageView tvLevel;
        RatingBar rbStar;
        TextView tvTime;
        TextView tvTask;
    }
}
