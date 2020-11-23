package com.aiocw.aihome.easylauncher.extendfun.waittodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WaitToDoDB {

    private static String TAG = "WaitToDoDB";

    //  数据库数据插入
    public static void insertWaitToDo(Context context, String content, String limit_date, String limit_time) {
        System.out.println(context);
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        ContentValues values = WaitToDoDBUtils.waitToDoToContentValues(WaitToDoDBUtils.mockWaitToDo(content, limit_date, limit_time));

        sqLiteDatabase.insert(DBCommonData.TABLE_WAIT_TO_DO, null, values);
        sqLiteDatabase.close();
    }

    //  数据库数据插入
    public static void insertWaitToDo(Context context, String content) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        ContentValues values = WaitToDoDBUtils.waitToDoToContentValues(WaitToDoDBUtils.mockWaitToDo(content));

        sqLiteDatabase.insert(DBCommonData.TABLE_WAIT_TO_DO, null, values);
        sqLiteDatabase.close();
    }

    //数据的查询
    public static ArrayList<WaitToDo> queryWaitToDo(ArrayList<WaitToDo> waitToDoArrayList, Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1;
        Date date2;
        waitToDoArrayList = new ArrayList<>();
        // 相当于 select * from students 语句
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DBCommonData.TABLE_WAIT_TO_DO, null,null,null,null,null,null);

        // 不断移动光标获取值
        while (cursor.moveToNext()) {
            WaitToDo waitToDo = new WaitToDo();
            // 直接通过索引获取字段值
            int id = cursor.getInt(0);

            // 先获取 name 的索引值，然后再通过索引获取字段值
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String lastTime = cursor.getString(cursor.getColumnIndex("lastTime"));
            String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
            String isFinish = cursor.getString(cursor.getColumnIndex("isFinish"));
            Log.e("Mout", "id: " + id + " content: " + content + "lastTime: " + lastTime
                    + "createTime: " + createTime + "isFinish: " +isFinish);
            waitToDo.setId(id);
            waitToDo.setContent(content);
            waitToDo.setLastTime(lastTime);
            waitToDo.setCreateTime(createTime);
            waitToDo.setFinish(isFinish.equals('0') ? false : true);
            int thingLength = waitToDoArrayList.size();
            if (thingLength == 0) {
                waitToDoArrayList.add(waitToDo);
            }
            for (int i = 0; i < thingLength; i ++) {
                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                date1 = sdf.parse(waitToDo.getLastTime(), pos1);
                date2 = sdf.parse(waitToDoArrayList.get(i).getLastTime(), pos2);
                if (date1.before(date2)) {
                    waitToDoArrayList.add(i, waitToDo);
                    break;
                }
                if (i == waitToDoArrayList.size() - 1){
                    waitToDoArrayList.add(waitToDo);
                }
            }
            Log.i(TAG, String.valueOf(waitToDoArrayList.size()));

        }
        // 关闭光标
        cursor.close();
        sqLiteDatabase.close();
        return waitToDoArrayList;
    }

    //数据删除
    public static void deleteToDo(int id, Context context) {
        DBCommonData dbCommonData = new DBCommonData(context);
        SQLiteDatabase sqLiteDatabase = dbCommonData.getWritableDatabase();
        //删除条件

        //删除SQL语句
        String sql = "delete from " + DBCommonData.TABLE_WAIT_TO_DO + " where id = " + id;
        //执行SQL语句
        sqLiteDatabase.execSQL(sql);
    }
}
