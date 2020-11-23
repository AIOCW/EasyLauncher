package com.aiocw.aihome.easylauncher.common.net;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.net.nettools.FTPTools;
import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;

public class NetThreadSendFile extends Thread {
    private static final String TAG = "NetThreadSendFile";
    private String ip;
    private int port;
    private String filePath;
    private Bundle data;

    // Service
    Messenger mServerMessenger = HomeActivity.mServerMessenger;

    // 返回文件发送成功
    public void sendFileSuccess() {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.setData(data);
        toServerMsg.what = 911005;
        try {
            mServerMessenger.send(toServerMsg);
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public NetThreadSendFile(Bundle data, String ip, int port) {
        this.data = data;
        String filePath = data.getString("filename");
        this.ip = ip;
        this.port = port;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        sendFile();
        sendFileSuccess();
    }

    private void sendFile() {
        String pathList[] = filePath.split("/");
        FTPTools ftpTools = FTPTools.getInstance();
        ftpTools.initFtpClient("192.168.100.18", 7070, "user", "12345");
        ftpTools.uploadFile("E:Easy/Temp/", pathList[pathList.length -1], filePath);
//                ftpTools.downloadFile("./","filezilla.exe", Environment.getExternalStorageDirectory() + "/Download/");
        Log.i(TAG, "文件分片发送完成");
    }
}
