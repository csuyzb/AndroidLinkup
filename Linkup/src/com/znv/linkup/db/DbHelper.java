package com.znv.linkup.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.config.ModeCfg;

/**
 * SQLite数据库处理帮助类，存储关卡最高分，是否激活，游戏星级
 * 
 * @author yzb
 * 
 */
class DbHelper extends SQLiteOpenHelper {

    private List<ModeCfg> modeCfgs;

    public DbHelper(Context context, List<ModeCfg> config) {
        super(context, ViewSettings.DbFileName, null, ViewSettings.DbVersion);
        this.modeCfgs = config;
    }

    /**
     * 根据游戏配置信息创建表并初始化
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table scores(level int primary key, rank int, maxscore int, mintime int, isactive int, star int, isupload int);";
        db.execSQL(sql);

        initDb(db);
    }

    /**
     * 判断是否默认激活
     * 
     * @param index
     *            要判断的关卡id
     * @return 激活true
     */
    private boolean isActive(int index) {
        if (ViewSettings.DefaultActiveLevels.length == 0) {
            return true;
        }

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

    private void initDb(SQLiteDatabase db) {
        String sql;
        int index = 0;
        db.beginTransaction();
        for (int m = 0; m < modeCfgs.size(); m++) {
            for (int r = 0; r < modeCfgs.get(m).getRankInfos().size(); r++) {
                for (int l = 0; l < modeCfgs.get(m).getRankInfos().get(r).getLevelInfos().size(); l++) {
                    sql = "insert into scores(level, rank, maxscore, mintime, isactive, star, isupload) values(?,?,?,?,?,?,?)";
                    // 控制默认激活的关卡数
                    if (isActive(index)) {
                        try {
                            db.execSQL(sql, new Object[] { index++, r, 0, 0, 1, 0, 0 });
                        } catch (Exception ex) {
                            Log.d("sqlite", ex.getMessage());
                        }
                    } else {
                        try {
                            db.execSQL(sql, new Object[] { index++, r, 0, 0, 0, 0, 0 });
                        } catch (Exception ex) {
                            Log.d("sqlite", ex.getMessage());
                        }
                    }
                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // private void alterTable(SQLiteDatabase db) {
    // String sql = "alter table scores add column isupload integer DEFAULT 0";
    // try {
    // db.execSQL(sql);
    // } catch (Exception ex) {
    // Log.d("sqlite", ex.getMessage());
    // }
    // }

    private void initNewLevels(SQLiteDatabase db) {
        String sql;
        int index = 0;
        db.beginTransaction();
        // 新增模式
        int m = 4;
        if (m < modeCfgs.size()) {
            for (int r = 0; r < modeCfgs.get(m).getRankInfos().size(); r++) {
                for (int l = 0; l < modeCfgs.get(m).getRankInfos().get(r).getLevelInfos().size(); l++) {
                    sql = "insert into scores(level, rank, maxscore, mintime, isactive, star, isupload) values(?,?,?,?,?,?,?)";
                    // 控制默认激活的关卡数
                    if (isActive(index)) {
                        try {
                            db.execSQL(sql, new Object[] { index++, r, 0, 0, 1, 0, 0 });
                        } catch (Exception ex) {
                            Log.d("sqlite", ex.getMessage());
                        }
                    } else {
                        try {
                            db.execSQL(sql, new Object[] { index++, r, 0, 0, 0, 0, 0 });
                        } catch (Exception ex) {
                            Log.d("sqlite", ex.getMessage());
                        }
                    }
                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果level存在，则insert不成功，不存在，则新增
        initDb(db);

        // 在表中增加一列,确定是否上传
        // alterTable(db);

        initNewLevels(db);
    }

}
