package com.aiocw.aihome.easylauncher.common.tools.filetools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class XFileTools {
    public static int FILE_SLIP = 52428800;

    public static String TAG = "==========:    XFileTools    ";

    // 文件类型
    public static String [] fileClassStrings = {"png", "jpg", "mp4", "pdf", "zip", "rar", "mp3"};

    // 公共文件夹
    public static String DIRECTORY_MUSIC = "Music";
    public static String DIRECTORY_ALARMS = "Alarms";
    public static String DIRECTORY_NOTIFICATIONS = "Notifications";
    public static String DIRECTORY_PICTURES = "Pictures";
    public static String DIRECTORY_MOVIES = "Movies";
    public static String DIRECTORY_DOWNLOADS = "Download";
    public static String DIRECTORY_DCIM = "DCIM";
    public static String DIRECTORY_DOCUMENTS = "Documents";

    public static String EMPTY = "";

    // icon 存储路径
    public static String ICON_PATH = "/AppIcon/";
    public static String MY_SAVE_PATH = "/A_SAVE_PATH/";


    // 获取当前应用的默认文件存取路径 data/package/files
    public static String getSelfDataPath(Context context) {
        String files_path = context.getExternalFilesDir(EMPTY).getAbsolutePath();
        Log.i(TAG, files_path);
        return files_path;
    }

    /**【创建一个新的文件夹】**/
    public static void addFolder(String path, String folderName){
        try {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File newFolder = new File(path + File.separator + folderName);
                if(!newFolder.exists()){
                    boolean isSuccess = newFolder.mkdirs();
                    Log.i("TAG:","文件夹创建状态--->" + isSuccess);
                }
                Log.i("TAG:","文件夹所在目录：" + newFolder.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getFileClass(String filePath) {
        filePath = "ioio.png";
        String [] fileClasses = filePath.split("\\.");
        String fileClass = fileClasses[fileClasses.length - 1];
        for (int i = 0; i < fileClassStrings.length; i++) {
            if (fileClass.equals(fileClassStrings[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String getFileName(String filePath) {
        String [] fileSideArray = filePath.split("/");
        return fileSideArray[fileSideArray.length - 1];

    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     *         <code>false</code> otherwise
     */
    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制文件夹及其中的文件
     *
     * @param oldPath String 原文件夹路径 如：data/user/0/com.test/files
     * @param newPath String 复制后的路径 如：data/user/0/com.test/cache
     * @return <code>true</code> if and only if the directory and files were copied;
     *         <code>false</code> otherwise
     */
    public boolean copyFolder(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                if (!newFile.mkdirs()) {
                    Log.e("--Method--", "copyFolder: cannot create directory.");
                    return false;
                }
            }
            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }

                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (!temp.exists()) {
                    Log.e("--Method--", "copyFolder:  oldFile not exist.");
                    return false;
                } else if (!temp.isFile()) {
                    Log.e("--Method--", "copyFolder:  oldFile not file.");
                    return false;
                } else if (!temp.canRead()) {
                    Log.e("--Method--", "copyFolder:  oldFile cannot read.");
                    return false;
                } else {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }

                /* 如果不需要打log，可以使用下面的语句
                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (temp.exists() && temp.isFile() && temp.canRead()) {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                 */
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> doFileSplitReturnInformation(String filePath, int spiltNumber) {
        List<String> tempFilePathList = new ArrayList<>();
        long splitLong = 0;
        long fileLength = 0;
        RandomAccessFile originalFile = null;
        RandomAccessFile newFile;
        byte [] buffer = new byte[1024];
        int readSize = 1024;
        long oneFileSize = 0;
        long startTime = 0;
        long endTime = 0;
        try {
            originalFile = new RandomAccessFile(filePath, "r");
            fileLength = originalFile.length();
            splitLong = fileLength / spiltNumber;
            for (int i = 0; i < spiltNumber; i ++) {
                startTime = System.currentTimeMillis();
                tempFilePathList.add("temp/" + i + ".elt");
                newFile = new RandomAccessFile(tempFilePathList.get(i), "rw");
                while (-1 != (readSize = originalFile.read(buffer))) {
                    oneFileSize += readSize;
                    newFile.write(buffer, 0, readSize);
                    if (oneFileSize > splitLong && (i < spiltNumber)) {
                        break;
                    }
                }
                endTime = System.currentTimeMillis();
                System.out.println("SplitOne Use Time" + (endTime - startTime) + "ms");
                oneFileSize = 0;
                newFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                originalFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempFilePathList;
    }

    public static void compositeFile(List<String> tempFilePathList, String filePath) {
        RandomAccessFile receFile;
        RandomAccessFile tempFile;
        byte [] buffer = new byte[1024];
        int readSize;
        long startTime = 0;
        long endTime = 0;
        try {
            receFile = new RandomAccessFile(filePath, "rw");
            for (int i = 0; i < tempFilePathList.size(); i ++) {
                startTime = System.currentTimeMillis();
                tempFile = new RandomAccessFile("temp/" + i + ".elt", "r");
                while (-1 != (readSize = tempFile.read(buffer))) {
                    receFile.write(buffer, 0, readSize);
                }
                endTime = System.currentTimeMillis();
                System.out.println("CompositeOne Use Time" + (endTime - startTime) + "ms");
                tempFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
