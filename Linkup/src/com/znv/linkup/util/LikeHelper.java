package com.znv.linkup.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.znv.linkup.ViewSettings;

import android.content.Context;

/**
 * 点赞管理
 * 
 * @author yzb
 * 
 */
public class LikeHelper {

    /**
     * 对用户点赞
     * 
     * @param userId
     *            用户ID
     */
    public static void likeUser(Context ctx, String userId) {
        boolean isModify = likeUsers.add(userId);
        if (isModify) {
            // 保存
            saveLikeUsers(ctx);
        }
    }

    /**
     * 对用户取消点赞
     * 
     * @param userId
     *            用户ID
     */
    public static void unlikeUser(Context ctx, String userId) {
        boolean isModify = likeUsers.remove(userId);
        if (isModify) {
            // 保存
            saveLikeUsers(ctx);
        }
    }

    /**
     * 是否点赞用户
     * 
     * @param userId
     *            用户ID
     * @return 是否点赞
     */
    public static boolean isLikeUser(String userId) {
        return likeUsers.contains(userId);
    }

    /**
     * 保存点赞用户
     * 
     * @param ctx
     *            上下文信息
     */
    private static void saveLikeUsers(Context ctx) {
        String likeUserStr = likeUserToString();
        if (!StringUtil.isNullOrEmpty(likeUserStr)) {
            CacheUtil.setBindStr(ctx, ViewSettings.LikeUsersStr, likeUserStr);
        }
    }

    /**
     * 从配置加载点赞用户集合
     * 
     * @param ctx
     *            上下文信息
     */
    public static void loadLikeUsers(Context ctx) {
        if (likeUsers.size() == 0) {
            String likeUserStr = CacheUtil.getBindStr(ctx, ViewSettings.LikeUsersStr);
            if (StringUtil.isNullOrEmpty(likeUserStr)) {
                return;
            }

            String[] userStrs = likeUserStr.split(seperator);
            for (String userId : userStrs) {
                if (!StringUtil.isNullOrEmpty(userId)) {
                    likeUsers.add(userId);
                }
            }
        }
    }

    /**
     * 将点赞用户集合转化为字符串
     * 
     * @return 点赞用户字符串
     */
    private static String likeUserToString() {
        Iterator<String> itr = likeUsers.iterator();
        StringBuffer sb = new StringBuffer();
        while (itr.hasNext()) {
            sb.append(itr.next() + seperator);
        }
        return sb.toString();
    }

    /**
     * 点赞用户分隔符
     */
    private static String seperator = ";";
    /**
     * 点赞用户集合
     */
    private static Set<String> likeUsers = new HashSet<String>();
}
