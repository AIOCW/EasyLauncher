package com.aiocw.aihome.easylauncher.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.aiocw.aihome.easylauncher.common.tools.AppDataTool;
import com.aiocw.aihome.easylauncher.common.tools.PermissionTools;
import com.aiocw.aihome.easylauncher.extendfun.entity.NetSettingSerializable;

import java.util.List;

public class StartAppUsually {
    private static String TAG = "StartAppUsually    ";

    public static void usuallyStart(List bottomApps, List leftApps, List thirdApps, Context context, Activity activity) {
        // 检测权限是否完整
        PermissionTools.getEasyPermission(activity);
        PermissionTools.getComplexPermission(context);

        dynamicDataInit();

        AppDataTool.usuallyInitAppDatas(bottomApps, leftApps, thirdApps, context);
    }

    public static boolean isFirstRun(Context context) {
        //判断app启动是否是第一次
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonStaticData.APP_SETTING_FILE_NAME,0);
        Boolean firstRun = sharedPreferences.getBoolean("firstRun", true);
        if (firstRun){
            //进入引导页
            Toast.makeText(context,"第一次",Toast.LENGTH_LONG).show();
            return true;
        } else {
            //进入首页
            Toast.makeText(context,"不是第一次",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static NetSettingSerializable getNetSettingData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonStaticData.APP_SETTING_FILE_NAME,0);
        String deviceName = sharedPreferences.getString("deviceName", "");
        String serverIp = sharedPreferences.getString("serverIp", "");
        String remoteServerIp = sharedPreferences.getString("remoteServerIp", "");
        int serverPort = sharedPreferences.getInt("serverPort", 0);
        CommonDynamicData.IP = serverIp;
        CommonDynamicData.PORT = serverPort;
        return new NetSettingSerializable(deviceName, serverIp, remoteServerIp, serverPort);
    }

    private static void dynamicDataInit(){
        CommonDynamicData.WIFI_CONNECTED = true;
    }


}
