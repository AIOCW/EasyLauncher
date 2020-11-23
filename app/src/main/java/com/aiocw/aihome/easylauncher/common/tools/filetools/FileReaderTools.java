package com.aiocw.aihome.easylauncher.common.tools.filetools;

import com.aiocw.aihome.easylauncher.common.CommonStaticData;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileReaderTools {

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
                tempFilePathList.add(CommonStaticData.TEMP_FILE_PATH + i + ".elt");
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
                tempFile = new RandomAccessFile(CommonStaticData.TEMP_FILE_PATH + i + ".elt", "r");
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
