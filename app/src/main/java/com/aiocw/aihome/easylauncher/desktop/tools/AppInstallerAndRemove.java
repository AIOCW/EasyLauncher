package com.aiocw.aihome.easylauncher.desktop.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AppInstallerAndRemove {

    /* 安装apk */
    public static void installApk(Context context, String fileName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + fileName),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /* 卸载apk */
    public static void uninstallApk(Context context, String packageName) {
        Log.i("应用卸载", packageName);
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }
}
