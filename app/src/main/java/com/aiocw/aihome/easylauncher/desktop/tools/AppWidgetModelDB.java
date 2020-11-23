package com.aiocw.aihome.easylauncher.desktop.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;
import com.aiocw.aihome.easylauncher.desktop.entity.AppWidgetModel;

import java.util.ArrayList;
import java.util.List;

public class AppWidgetModelDB {
    private static String TAG = "AppWidgetModelDB";


    //  数据库数据插入
    public static void insertAppWidgetModel(Context context, AppWidgetModel appWidgetModel) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        ContentValues values = appToContentValues(appWidgetModel);

        sqLiteDatabase.insert(DBCommonData.TABLE_APP_WIDGET_DATA, null, values);
        sqLiteDatabase.close();
    }

    // 将 App 对象的值存储到 ContentValues 中
    private static ContentValues appToContentValues(AppWidgetModel appWidgetModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_widget_id", appWidgetModel.getAppWidgetId());
        return contentValues;
    }

    //数据的查询
    public static List<AppWidgetModel> queryAppData(Context context) {
        List<AppWidgetModel> appWidgetModelList = new ArrayList<>();
        // 相当于 select * from students 语句
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBCommonData.TABLE_APP_WIDGET_DATA, null,null,null,null,null,null);

        // 不断移动光标获取值
        while (cursor.moveToNext()) {
            AppWidgetModel appWidgetModel = new AppWidgetModel();
            int id = cursor.getInt(0);
            // 先获取 name 的索引值，然后再通过索引获取字段值
            int appWidgetId = cursor.getInt(cursor.getColumnIndex("app_widget_id"));
            appWidgetModel.setId(id);
            appWidgetModel.setAppWidgetId(appWidgetId);
            appWidgetModelList.add(appWidgetModel);

            Log.i(TAG, String.valueOf(appWidgetModelList.size()));

        }
        // 关闭光标
        cursor.close();
        sqLiteDatabase.close();
        return appWidgetModelList;
    }

    //数据删除
    public static void deleteAppData(int id, Context context) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        //删除条件

        //删除SQL语句
        String sql = "delete from " + DBCommonData.TABLE_APP_WIDGET_DATA + " where id = " + id;
        //执行SQL语句
        sqLiteDatabase.execSQL(sql);
    }

}
