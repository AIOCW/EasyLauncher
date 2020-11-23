package com.aiocw.aihome.easylauncher.common.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.StartAppUsually;
import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;
import com.aiocw.aihome.easylauncher.desktop.entity.App;
import com.aiocw.aihome.easylauncher.desktop.tools.AppDataDB;

import java.util.List;

public class AppDataTool {
    private static String TAG = "AppDataTool";

    // 删除某个list中的指定包名的app
    public static boolean deleteApp(List<App> apps, String packages, String deleteTable, Context context) {
        for (int i = 0; i < apps.size(); i ++) {
            if (apps.get(i).getPackageName().equals(packages)) {
                AppDataDB.deleteAppData(apps.get(i).getId(), context, deleteTable);
                apps.remove(i);
                return true;
            }
        }
        return false;
    }

    // 根据设置内容划分 APP所在位置
    public static boolean addAppToThird(App app, List<App> bottomApps, List<App> leftApps, List<App> thirdApps, Context context) {
        String packageName = app.getPackageName();
        for (int j = 0; j < bottomApps.size(); j ++) {
            if (bottomApps.get(j).getPackageName().equals(packageName)) {
                return false;
            }
        }

        for (int j = 0; j < leftApps.size(); j ++) {
            if (leftApps.get(j).getPackageName().equals(packageName)) {
                return false;
            }
        }

        for (int j = 0; j < thirdApps.size(); j ++) {
            if (thirdApps.get(j).getPackageName().equals(packageName)) {
                return false;
            }
        }
        AppDataDB.insertAppData(context, app, DBCommonData.TABLE_THIRD_APP_DATA);
        thirdApps.add(app);
        return true;
    }

    //加载系统已安装的所有 app
    public static void firstInitAppDatas(Context context) {
        AppDataDB.deleteAppDataTable(context, DBCommonData.TABLE_THIRD_APP_DATA);

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(startupIntent,0);

        for (int i = 0; i < activities.size(); i ++) {
            Log.i(TAG, "Size=========== " + activities.size());
            ResolveInfo resolveInfo = activities.get(i);
            App app = new App(context, packageManager, resolveInfo, i);
            // 加入数据库
            AppDataDB.insertAppData(context, app, DBCommonData.TABLE_THIRD_APP_DATA);
        }
    }


    //根据设置参数将APP写入对应的 数据库 无论怎么样第三页都会有所有应用
    public static boolean usuallyInitAppDatas(List<App> bottomApps, List<App> leftApps, List<App> thirdApps, Context context) {
        AppDataDB.deleteAppDataTable(context, DBCommonData.TABLE_THIRD_APP_DATA);

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(startupIntent,0);

        for (int i = 0; i < activities.size(); i ++) {
            Log.i(TAG, "Size=========== " + activities.size());
            ResolveInfo resolveInfo = activities.get(i);
            App app = new App(context, packageManager, resolveInfo, i);
            // 加入数据库
            addAppToThird(app, bottomApps, leftApps, thirdApps, context);
        }
        return true;
    }

}
