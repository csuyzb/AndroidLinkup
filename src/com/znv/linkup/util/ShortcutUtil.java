package com.znv.linkup.util;

import com.znv.linkup.R;
import com.znv.linkup.WelcomeActivity;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;

public class ShortcutUtil {

    private Context ctx = null;

    public ShortcutUtil(Context ctx) {
        this.ctx = ctx;
    }

    public boolean hasShortcut() {
        boolean isInstallShortcut = false;
        final ContentResolver cr = ctx.getContentResolver();
        final String AUTHORITY = "com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "", new String[] {  },
                null);
        if (c != null && c.getCount() > 0) {
            if(c.getString(0).equals(ctx.getResources().getString(R.string.app_name))) {
                isInstallShortcut = true;
            }
        }

        return isInstallShortcut;
    }

    /**
     * 为程序创建桌面快捷方式
     */
    public void addShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.app_name));
        // 不允许重复创建
        shortcut.putExtra("duplicate", false); 

        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClass(ctx, WelcomeActivity.class);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        // 快捷方式的图标
        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(ctx, R.drawable.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        ctx.sendBroadcast(shortcut);
    }

    /**
     * 删除程序的快捷方式
     */
    public void delShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.app_name));
        String appClass = ctx.getClass().getName();
        ComponentName comp = new ComponentName(ctx.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        ctx.sendBroadcast(shortcut);
    }
}
