package com.aiocw.aihome.easylauncher.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.aiocw.aihome.easylauncher.common.tools.AppDataTool;
import com.aiocw.aihome.easylauncher.common.tools.filetools.XFileTools;
import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;


public class StartAppFirst {


    public static void startApp(Context context) {

        //下载路径
        XFileTools.addFolder(Environment.getExternalStorageDirectory() + "/Download/", "EasyLauncher");
        XFileTools.addFolder(Environment.getExternalStorageDirectory() + "/Download/EasyLauncher/", "temp");

        // 图标文件夹初始化
        String selfDataPath = XFileTools.getSelfDataPath(context);
        XFileTools.addFolder(selfDataPath, XFileTools.ICON_PATH);

        // 数据库文件初始化
        new DBCommonData(context);

        // app设置初始化
        initAppSettingData(context);

        // 第一次加载app
        AppDataTool.firstInitAppDatas(context);


    }

    private static void initAppSettingData(Context context) {
        //判断app启动是否是第一次
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonStaticData.APP_SETTING_FILE_NAME, 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("firstRun", false);

        editor.putString("deviceName", "device");

        editor.putString("serverIp", "192.168.0.100");
        editor.putString("remoteServerIp", "23.182.46.25");
        editor.putInt("serverPort", 8080);
        editor.commit();

    }

}
