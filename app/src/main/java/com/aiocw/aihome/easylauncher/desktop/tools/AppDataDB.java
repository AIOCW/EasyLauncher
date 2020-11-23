package com.aiocw.aihome.easylauncher.desktop.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;
import com.aiocw.aihome.easylauncher.desktop.entity.App;

import java.util.List;

public class AppDataDB {

    //  数据库数据插入
    public static void insertAppData(Context context, App app, String tableName) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        ContentValues values = appToContentValues(app);

        sqLiteDatabase.insert(tableName, null, values);
        sqLiteDatabase.close();
    }

    // 将 App 对象的值存储到 ContentValues 中
    private static ContentValues appToContentValues(App app) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_name", app.getAppName());
        contentValues.put("class_name", app.getClassName());
        contentValues.put("icon_path", app.getIconPath());
        contentValues.put("package_name", app.getPackageName());
        contentValues.put("serial_number", app.getSerialNumber());
        return contentValues;
    }

    //数据的查询
    public static List<App> queryAppData(List<App> appList, Context context, String tableName) {
        // 相当于 select * from students 语句
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, null,null,null,null,null,null);

        // 不断移动光标获取值
        while (cursor.moveToNext()) {
            App app = new App();
            int id = cursor.getInt(0);
            // 先获取 name 的索引值，然后再通过索引获取字段值
            String appName = cursor.getString(cursor.getColumnIndex("app_name"));
            String className = cursor.getString(cursor.getColumnIndex("class_name"));
            String iconPath = cursor.getString(cursor.getColumnIndex("icon_path"));
            String packageName = cursor.getString(cursor.getColumnIndex("package_name"));
            int serialNumber = cursor.getInt(cursor.getColumnIndex("serial_number"));
            Log.e("Mout", appName);
            app.setId(id);
            app.setAppName(appName);
            app.setClassName(className);
            app.setIconPath(iconPath);
            app.setPackageName(packageName);
            app.setSerialNumber(serialNumber);
            appList.add(app);

            Log.i("AppDataDB    ", String.valueOf(appList.size()));

        }
        // 关闭光标
        cursor.close();
        sqLiteDatabase.close();
        return appList;
    }

    //数据删除
    public static void deleteAppData(int id, Context context, String tableName) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        //删除条件

        //删除SQL语句
        String sql = "delete from " + tableName + " where id = " + id;
        //执行SQL语句
        sqLiteDatabase.execSQL(sql);
    }

    public static void deleteAppDataTable(Context context, String tableName) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        String sql = "update sqlite_sequence set seq = 0 where name = '" + tableName + "'";
        String sql_ = "delete from " + tableName;

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql_);
    }

}
