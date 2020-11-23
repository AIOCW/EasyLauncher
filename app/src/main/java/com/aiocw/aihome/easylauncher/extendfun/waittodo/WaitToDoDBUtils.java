package com.aiocw.aihome.easylauncher.extendfun.waittodo;

import android.content.ContentValues;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WaitToDoDBUtils {
    private static String TAG = "WaitToDoDBUtils";

    // 构建 WaitToDo 对象
    public static WaitToDo mockWaitToDo(String content, String limit_date, String limit_time) {
        WaitToDo waitToDo = new WaitToDo();
        Date createDate = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        waitToDo.setContent(content);
        waitToDo.setLastTime(limit_date + " " + limit_time);
        waitToDo.setCreateTime(ft.format(createDate));
        waitToDo.setFinish(false);
        return waitToDo;
    }

    // 构建 WaitToDo 对象
    public static WaitToDo mockWaitToDo(String content) {
        WaitToDo waitToDo = new WaitToDo();
        Date createDate = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat ft_ = new SimpleDateFormat("yyyy-MM-dd");
        waitToDo.setContent(content);
        waitToDo.setLastTime(ft_.format(createDate) + " 18:00:00");
        waitToDo.setCreateTime(ft.format(createDate));
        waitToDo.setFinish(false);
        return waitToDo;
    }

    // 将 WaitToDo 对象的值存储到 ContentValues 中
    public static ContentValues waitToDoToContentValues(WaitToDo waitToDo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", waitToDo.getContent());
        contentValues.put("lastTime", waitToDo.getLastTime());
        contentValues.put("createTime", waitToDo.getCreateTime());
        contentValues.put("isFinish", waitToDo.isFinish());
        return contentValues;
    }

}
