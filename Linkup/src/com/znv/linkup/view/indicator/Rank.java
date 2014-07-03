package com.znv.linkup.view.indicator;

import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 等级视图
 * 
 * @author yzb
 * 
 */
public class Rank {

    private RankCfg rankCfg = null;
    private View rankView = null;
    private LevelAdapter levels = null;
    private RankHolder holder = new RankHolder();

    public Rank(Context ctx, final RankCfg rankCfg) {
        this.rankCfg = rankCfg;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        rankView = inflater.inflate(R.layout.rank, null);

        holder.tvTitle = (TextView) rankView.findViewById(R.id.rankName);
        holder.rankGrid = (GridView) rankView.findViewById(R.id.rankGrid);

        levels = new LevelAdapter(ctx, rankCfg.getLevelInfos());
        holder.rankGrid.setAdapter(levels);
    }

    /**
     * 根据等级配置更新
     * 
     * @param rankCfg
     *            等级配置
     */
    public void changeRankCfg(RankCfg rankCfg, boolean isInit) {
        this.rankCfg = rankCfg;
        levels.changeLevelCfgs(rankCfg.getLevelInfos(), isInit);
    }

    /**
     * 设置关卡选择的监听器
     * 
     * @param levelListener
     *            选择关卡操作接口
     */
    public void setLevelLister(final ISelectedLevel levelListener) {
        holder.rankGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (levelListener != null) {
                    levelListener.onSelectedLevel(rankCfg.getLevelInfos().get(arg2));
                }
            }
        });
    }

    /**
     * 获取rank的视图
     * 
     * @return
     */
    public View getRankView() {
        return rankView;
    }

    /**
     * 获取rank的缓存控件，用于更新，提高速度
     * 
     * @return
     */
    public RankHolder getRankHolder() {
        return holder;
    }

    /**
     * 获取与等级相关的levelAdapter
     * 
     * @return
     */
    public LevelAdapter getLevelAdapter() {
        return levels;
    }

    /**
     * 选择关卡时的处理接口
     * 
     * @author yzb
     * 
     */
    public interface ISelectedLevel {
        void onSelectedLevel(LevelCfg levelCfg);
    }

    /**
     * Rank控件缓存
     * 
     * @author yzb
     * 
     */
    public class RankHolder {
        TextView tvTitle;
        GridView rankGrid;
    }
}
