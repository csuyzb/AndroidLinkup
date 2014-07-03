package com.znv.linkup.view.indicator;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.znv.linkup.core.config.LevelCfg;

/**
 * Rank下所有关卡的数据适配类
 * 
 * @author yzb
 * 
 */
public class LevelAdapter extends BaseAdapter {

    public LevelAdapter(Context context, List<LevelCfg> levelCfgs) {
        this.levelCfgs = levelCfgs;
        this.levels = new Level[levelCfgs.size()];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = new Level(context, levelCfgs.get(i));
        }
    }
    
    /**
     * 根据关卡配置集合更新
     * @param levelCfgs 关卡配置集合
     */
    public void changeLevelCfgs(List<LevelCfg> levelCfgs, boolean isInit) {
        for (int i = 0; i < levels.length; i++) {
            levels[i].changeLevelCfg(levelCfgs.get(i), isInit);
        }
    }

    @Override
    public int getCount() {
        return levels.length;
    }

    @Override
    public LevelCfg getItem(int position) {
        return levelCfgs.get(position);
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
            return levels[position].getLevelView();
        }
        return null;
    }

    private List<LevelCfg> levelCfgs = null;
    private Level[] levels = null;
}
