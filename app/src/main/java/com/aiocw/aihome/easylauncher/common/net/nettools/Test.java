package com.aiocw.aihome.easylauncher.common.net.nettools;

import com.aiocw.aihome.easylauncher.common.CommonDynamicData;
import com.aiocw.aihome.easylauncher.common.CommonTools;
import com.aiocw.aihome.easylauncher.common.net.NetThreadSendFile;
import com.aiocw.aihome.easylauncher.common.tools.filetools.FileReaderTools;
import com.aiocw.aihome.easylauncher.common.tools.filetools.XFileTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {
//        testSecurity();
//        testConnection();
//        testGetFileName();
        sendBigFile("D:/PycharmProjects/development/DeviceCenterController/file/JustSave/1.apk", "just-save");
    }

    public static void testByte22Int() {
        byte[] data = new byte[4];
        data[0] = 0x01;
        data[1] = 0x00;
        data[2] = 0x00;
        data[3] = 0x00;
        System.out.println(CommonTools.bytes2Int(CommonTools.int2Byte(1000)));
        System.out.println(CommonTools.bytes2Int(data));
    }

    public static void testConnection() {
        Socket data = null;
        try {
            data = new Socket("192.168.100.14", 8080);
            OutputStream outputStream = data.getOutputStream();
            InputStream inputStream = data.getInputStream();
            SocketHelp.online(outputStream, inputStream, "0123456789", "test");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testSecurity() {
        int data = 1000;
        byte [] bytes = CommonTools.int2Byte(data);
        CommonTools.packageByteSecurity(bytes);
        int sData = CommonTools.bytes2Int(bytes);
        System.out.println(sData);
        CommonTools.unPackageByteSecurity(bytes);
        int oData = CommonTools.bytes2Int(bytes);
        System.out.println(oData);
    }

    public static void testGetFileName() {
        System.out.println(XFileTools.getFileName("/data/android/wo.png"));
    }

    private static void sendBigFile(String filePath, String aimDevice) {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<String> tempFileList = FileReaderTools.doFileSplitReturnInformation(filePath, 5);
        for (String oneFile : tempFileList) {
//            Log.i("Send Lite File", oneFile);
//            NetThreadSendFile netThreadSendFile = new NetThreadSendFile(oneFile, CommonDynamicData.IP, CommonDynamicData.PORT, aimDevice);
//            pool.execute(netThreadSendFile);
        }
    }
}
