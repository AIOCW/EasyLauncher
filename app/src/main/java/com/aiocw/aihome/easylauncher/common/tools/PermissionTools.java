package com.aiocw.aihome.easylauncher.common.tools;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.aiocw.aihome.easylauncher.common.CommonStaticData;
import com.aiocw.aihome.easylauncher.extendfun.notifiction.NotificationCollectorService;

import java.util.Set;

public class PermissionTools {
    public static String TAG = "PermissionTools";

    public static void getEasyPermission(Activity activity) {
        // 权限申请， 存储权限
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW};
        //验证是否许可权限
        for (String str : permissions) {
            if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                activity.requestPermissions(permissions, CommonStaticData.PREMISSION_REQUEST_CODE_CONTACT);
            } else {
                //这里就是权限打开之后自己要操作的逻辑
                Log.i(TAG + "本应用已有的权限有", str);
            }
        }
        if (! Settings.canDrawOverlays(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent,10);
        }
    }

    public static void getComplexPermission(Context context) {
        // 状态栏数据获取，通过注册该监听方式
        if (!isNotificationListenerEnabled(context)){
            openNotificationListenSettings(context);
        }
        toggleNotificationListenerService(context);
    }

    //检测通知监听服务是否被授权
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    //打开通知监听设置页面
    public static void openNotificationListenSettings(Context context) {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作
    private static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(context, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
