package com.aiocw.aihome.easylauncher.common;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class CommonTools {

    /**
     * 将uri转换成真实路径
     *
     * @param selectedVideoUri
     * @param contentResolver
     * @return
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath = "";
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn,
                null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(filePathColumn[0]);
                if(id > -1)
                    filePath = cursor.getString(id);
            }
            cursor.close();
        }

        return filePath;
    }


    /**
     * byte数组转int类型的对象
     * @param val
     * @return
     */
    public static byte[] int2Byte(int val){
        byte[] b = new byte[4];
        b[0] = (byte)(val & 0xff);
        b[1] = (byte)((val >> 8) & 0xff);
        b[2] = (byte)((val >> 16) & 0xff);
        b[3] = (byte)((val >> 24) & 0xff);
        return b;
    }
    /**
     * byte数组转int类型的对象
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes) {
        return (bytes[3]&0xff)<<24
                | (bytes[2]&0xff)<<16
                | (bytes[1]&0xff)<<8
                | (bytes[0]&0xff);

    }

    public static void packageByteSecurity(byte[] bytes) {
        byte zeroData = bytes[0];
        for (int i = 1; i < bytes.length; i ++) {
            bytes[i - 1] = bytes[i];
        }
        bytes[bytes.length - 1] = zeroData;
    }

    public static void unPackageByteSecurity(byte[] bytes) {
        byte lastData = bytes[bytes.length - 1];
        for (int i = bytes.length - 1; i > 0; i --) {
            bytes[i] = bytes[i - 1];
        }
        bytes[0] = lastData;
    }
}
