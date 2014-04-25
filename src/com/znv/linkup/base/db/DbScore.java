package com.znv.linkup.base.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.znv.linkup.base.config.RankCfg;

public class DbScore {

    private static DbHelper database = null;

    public static void init(Context context, List<RankCfg> rankCfgs) {
        database = new DbHelper(context, rankCfgs);
    }

    public static void insert(LevelScore levelScore) {
        SQLiteDatabase db = database.getWritableDatabase();
        String sql = "insert into scores(level, rank, maxscore, isactive, star) values(?,?,?,?,?)";
        db.execSQL(sql, new Object[] { levelScore.getLevel(), levelScore.getRank(), levelScore.getMaxScore(), levelScore.getIsActive(), levelScore.getStar() });
        db.close();
    }

    public static void batchInsert(List<LevelScore> levelScores) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        for (LevelScore score : levelScores) {
            insert(score);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static void updateScore(LevelScore levelScore) {
        SQLiteDatabase db = database.getWritableDatabase();
        String sql = "update scores set maxscore=?, star=? where level=?";
        db.execSQL(sql, new Object[] { levelScore.getMaxScore(), levelScore.getStar(), levelScore.getLevel() });
        db.close();
    }

    public static void updateActive(LevelScore levelScore) {
        SQLiteDatabase db = database.getWritableDatabase();
        String sql = "update scores set isactive=? where level=?";
        db.execSQL(sql, new Object[] { levelScore.getIsActive(), levelScore.getLevel() });
        db.close();
    }

    public static void delete(String level) {
        SQLiteDatabase db = database.getWritableDatabase();
        String sql = "delete from scores where level=?";
        db.execSQL(sql, new Object[] { level });
        db.close();
    }

    public static int selectMaxScore(String level) {
        SQLiteDatabase db = database.getReadableDatabase();
        String sql = "select maxscore from scores where level=?";
        try {
            Cursor cursor = db.rawQuery(sql, new String[] { level });
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("maxscore"));
            }
            return 0;
        } finally {
            db.close();
        }
    }

    public static List<LevelScore> selectAll() {
        SQLiteDatabase db = database.getReadableDatabase();
        String sql = "select * from scores order by level";
        try {
            Cursor cursor = db.rawQuery(sql, new String[] {});
            List<LevelScore> levelScores = new ArrayList<LevelScore>();
            while (cursor.moveToNext()) {
                levelScores.add(new LevelScore(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
            }
            return levelScores;
        } finally {
            db.close();
        }
    }

    public static int selectLevelByRank(String rank) {
        SQLiteDatabase db = database.getReadableDatabase();
        String sql = "select count(*) from scores where isactive=1 and rank=?";
        try {
            Cursor cursor = db.rawQuery(sql, new String[] {rank});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            db.close();
        }
    }

    public static int selectStarByRank(String rank) {
        SQLiteDatabase db = database.getReadableDatabase();
        String sql = "select sum(star) from scores where isactive=1 and rank=?";
        try {
            Cursor cursor = db.rawQuery(sql, new String[] {rank});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            db.close();
        }
    }
}
