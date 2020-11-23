package com.aiocw.aihome.easylauncher.common.tools.toolsdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCommonData extends SQLiteOpenHelper {

    public static final String DB_NAME = "easy_launcher.db";

    public static final int DB_VERSION = 1;

    // wait to do
    public static final String TABLE_WAIT_TO_DO = "waittodo";

    // appList data
    public static final String TABLE_LEFT_APP_DATA = "leftappdata";
    public static final String TABLE_BOTTOM_APP_DATA = "bottomappdata";
    public static final String TABLE_THIRD_APP_DATA = "thirdappdata";

    // app widget data
    public static final String TABLE_APP_WIDGET_DATA = "appwidgetdata";

    //创建 students 表的 sql 语句
    private static final String WAITTOD_CREATE_TABLE_SQL = "create table " + DBCommonData.TABLE_WAIT_TO_DO + "("
            + "id integer primary key autoincrement,"
            + "content text not null,"
            + "lastTime text not null,"
            + "createTime text not null,"
            + "isFinish boolean not null"
            + ");";

    //创建 Left app data 表的 sql 语句
    private static final String LEFT_APP_DATA_CREATE_TABLE_SQL = "create table " + DBCommonData.TABLE_LEFT_APP_DATA + "("
            + "id integer primary key autoincrement,"
            + "app_name text not null,"
            + "class_name text not null,"
            + "icon_path text not null,"
            + "package_name text not null,"
            + "serial_number integer not null"
            + ");";

    //创建 Left app data 表的 sql 语句
    private static final String BOTTOM_APP_DATA_CREATE_TABLE_SQL = "create table " + DBCommonData.TABLE_BOTTOM_APP_DATA + "("
            + "id integer primary key autoincrement,"
            + "app_name text not null,"
            + "class_name text not null,"
            + "icon_path text not null,"
            + "package_name text not null,"
            + "serial_number integer not null"
            + ");";
    //创建 Left app data 表的 sql 语句
    private static final String THIRD_APP_DATA_CREATE_TABLE_SQL = "create table " + DBCommonData.TABLE_THIRD_APP_DATA + "("
            + "id integer primary key autoincrement,"
            + "app_name text not null,"
            + "class_name text not null,"
            + "icon_path text not null,"
            + "package_name text not null,"
            + "serial_number integer not null"
            + ");";

    private static final String APP_WIDGET_DATA_TABLE_SQL = "create table " + DBCommonData.TABLE_APP_WIDGET_DATA + "("
            + "id integer primary key autoincrement,"
            + "app_widget_id integer not null"
            + ");";

    public DBCommonData(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DBCommonData.DB_NAME, null, DBCommonData.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建 students 表
        db.execSQL(WAITTOD_CREATE_TABLE_SQL);
        db.execSQL(LEFT_APP_DATA_CREATE_TABLE_SQL);
        db.execSQL(BOTTOM_APP_DATA_CREATE_TABLE_SQL);
        db.execSQL(THIRD_APP_DATA_CREATE_TABLE_SQL);
        db.execSQL(APP_WIDGET_DATA_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
        switch (oldVersion) {
            case 1:
                // do something
                break;

            default:
                break;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // 启动外键
            db.execSQL("PRAGMA foreign_keys = 1;");

            //或者这样写
            String query = String.format("PRAGMA foreign_keys = %s", "ON");
            db.execSQL(query);
        }
    }


}
