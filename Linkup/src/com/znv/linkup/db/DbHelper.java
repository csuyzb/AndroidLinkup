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
        super(context, ViewSettings.DbName, null, ViewSettings.DbVersion);
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
                if (index < ViewSettings.DefaultActiveLevels) {
                    db.execSQL(sql, new Object[] { index++, r, 0, 1, 0 });
                } else {
                    db.execSQL(sql, new Object[] { index++, r, 0, 0, 0 });
                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
