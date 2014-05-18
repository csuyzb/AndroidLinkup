package com.znv.linkup.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.RankCfg;

/**
 * SQLite数据库处理帮助类，存储关卡最高分，是否激活，游戏星级
 * 
 * @author yzb
 * 
 */
class DbHelper extends SQLiteOpenHelper {

    private List<RankCfg> rankCfgs;

    public DbHelper(Context context, List<RankCfg> config) {
        super(context, ViewSettings.DbFileName, null, ViewSettings.DbVersion);
        this.rankCfgs = config;
    }

    /**
     * 根据游戏配置信息创建表并初始化
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table scores(level int primary key, rank int, maxscore int, isactive int, star int);";
        db.execSQL(sql);

        int index = 0;
        db.beginTransaction();
        for (int r = 0; r < rankCfgs.size(); r++) {
            for (int l = 0; l < rankCfgs.get(r).getLevelInfos().size(); l++) {
                sql = "insert into scores(level, rank, maxscore, isactive, star) values(?,?,?,?,?)";
                // 控制默认激活的关卡数
                if (isActive(index)) {
                    db.execSQL(sql, new Object[] { index++, r, 0, 1, 0 });
                } else {
                    db.execSQL(sql, new Object[] { index++, r, 0, 0, 0 });
                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 判断是否默认激活
     * 
     * @param index
     *            要判断的关卡id
     * @return 激活true
     */
    private boolean isActive(int index) {
        for (int i : ViewSettings.DefaultActiveLevels) {
            if (i < index) {
                continue;
            } else if (i == index) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
