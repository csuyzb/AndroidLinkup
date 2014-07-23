package com.znv.linkup.view.indicator;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.GameMode;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.util.StringUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

    private Context ctx = null;
    private View levelView = null;
    private LevelHolder levelHolder = new LevelHolder();

    public Level(Context ctx, LevelCfg levelCfg) {

        this.ctx = ctx;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        levelView = inflater.inflate(R.layout.level, null);

        levelHolder.tvLevel = (ImageView) levelView.findViewById(R.id.tvLevel);
        levelHolder.rbStar = (RatingBar) levelView.findViewById(R.id.rbStar);
        levelHolder.tvTime = (TextView) levelView.findViewById(R.id.tvTime);
        levelHolder.tvTask = (TextView) levelView.findViewById(R.id.tvTask);

        updateLevelView(levelCfg);
    }

    /**
     * 更新关卡配置
     * 
     * @param levelCfg
     *            关卡配置
     */
    public void changeLevelCfg(LevelCfg levelCfg, boolean isInit) {
        if (isInit || levelCfg.isActive()) {
            updateLevelView(levelCfg);
        }
    }

    /**
     * 获取关卡视图
     * 
     * @return 关卡视图
     */
    public View getLevelView() {
        return levelView;
    }

    /**
     * 获取关卡控件缓存
     * 
     * @return 关卡控件缓存
     */
    public LevelHolder getLevelHolder() {
        return levelHolder;
    }

    /**
     * 根据关卡配置更新一个关卡数据
     * 
     * @param levelCfg
     *            关卡配置
     */
    private void updateLevelView(LevelCfg levelCfg) {
        if (levelCfg.isActive()) {
            levelHolder.tvLevel.setBackgroundResource(R.drawable.levelbg);
            int level = Integer.parseInt(levelCfg.getLevelName());
            levelHolder.tvLevel.setImageResource(ViewSettings.Numbers[level]);
            if (levelCfg.getLevelMode() == GameMode.Level) {
                levelHolder.rbStar.setVisibility(View.VISIBLE);
                levelHolder.tvTime.setVisibility(View.GONE);
                levelHolder.tvTask.setVisibility(View.GONE);
                levelHolder.rbStar.setRating(levelCfg.getLevelStar());
            } else if (levelCfg.getLevelMode() == GameMode.Time) {
                levelHolder.rbStar.setVisibility(View.GONE);
                levelHolder.tvTime.setVisibility(View.VISIBLE);
                levelHolder.tvTask.setVisibility(View.GONE);
                levelHolder.tvTime.setText("");
                if (levelCfg.getMinTime() > 0) {
                    levelHolder.tvTime.setText(StringUtil.secondToString(levelCfg.getMinTime()));
                }
            } else if (levelCfg.getLevelMode() == GameMode.ScoreTask) {
                levelHolder.rbStar.setVisibility(View.GONE);
                levelHolder.tvTime.setVisibility(View.GONE);
                levelHolder.tvTask.setVisibility(View.VISIBLE);
                levelHolder.tvTask.setText(String.valueOf(levelCfg.getScoreTask()));
                if (levelCfg.getMaxScore() > 0) {
                    Drawable drawable = ctx.getResources().getDrawable(R.drawable.pass);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    levelHolder.tvTask.setCompoundDrawables(null, null, drawable, null);
                }
            } else if (levelCfg.getLevelMode() == GameMode.TimeTask) {
                levelHolder.rbStar.setVisibility(View.GONE);
                levelHolder.tvTime.setVisibility(View.GONE);
                levelHolder.tvTask.setVisibility(View.VISIBLE);
                levelHolder.tvTask.setText(StringUtil.secondToString(levelCfg.getTimeTask()));
                if (levelCfg.getMinTime() > 0) {
                    Drawable drawable = ctx.getResources().getDrawable(R.drawable.pass);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    levelHolder.tvTask.setCompoundDrawables(null, null, drawable, null);
                }
            }
        } else {
            levelHolder.tvLevel.setBackgroundResource(R.drawable.locked);
            levelHolder.rbStar.setVisibility(View.INVISIBLE);
            levelHolder.tvTime.setVisibility(View.GONE);
            levelHolder.tvTask.setVisibility(View.GONE);
        }
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
