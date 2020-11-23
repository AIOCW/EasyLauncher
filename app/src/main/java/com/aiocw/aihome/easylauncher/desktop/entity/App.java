package com.aiocw.aihome.easylauncher.desktop.entity;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.aiocw.aihome.easylauncher.common.tools.filetools.XFileTools;
import com.aiocw.aihome.easylauncher.common.tools.imagetools.ImageTools;

public class App {
    private int id;
    private String iconPath;
    private String appName;
    private String packageName;
    private String className;
    private int serialNumber;

    public App() {
    }

    public App(int id, String iconPath, String appName, String packageName, String className, int serialNumber) {
        this.id = id;
        this.iconPath = iconPath;
        this.appName = appName;
        this.packageName = packageName;
        this.className = className;
        this.serialNumber = serialNumber;
    }

    public App(Context context, PackageManager pm, ResolveInfo info, int serialNumber) {
        this.appName = info.loadLabel(pm).toString();
        String iconPath = XFileTools.getSelfDataPath(context) + XFileTools.ICON_PATH + appName + ".png";
        ImageTools.drawableSave(info.loadIcon(pm), iconPath);
        this.iconPath = iconPath;
        this.packageName = info.activityInfo.packageName;
        this.className = info.activityInfo.name;
        this.serialNumber = serialNumber;
    }

    public App(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info;
        try {
            info = pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
            this.appName = info.loadLabel(pm).toString();
            String iconPath = XFileTools.getSelfDataPath(context) + XFileTools.ICON_PATH + appName + ".png";
            ImageTools.drawableSave(info.loadIcon(pm), iconPath);
            this.iconPath = iconPath;
            this.packageName = info.packageName;
            this.className = info.name;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof App) {
            App app = (App) object;
            return packageName.equals(app.packageName);
        } else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getComponentName() {
        return new ComponentName(packageName, className).toString();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
