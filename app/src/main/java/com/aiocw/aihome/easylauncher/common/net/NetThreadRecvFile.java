package com.aiocw.aihome.easylauncher.common.net;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.net.nettools.FTPTools;
import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;

public class NetThreadRecvFile extends Thread {
    private static final String TAG = "NetThreadRecvFile";
    private String ip;
    private int port;
    private String filePath;
    private Bundle data;

    // Service
    Messenger mServerMessenger = HomeActivity.mServerMessenger;

    // 返回文件发送成功
    public void recvFileSuccess() {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.setData(data);
        toServerMsg.what = 1100;
        try {
            mServerMessenger.send(toServerMsg);
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public NetThreadRecvFile(Bundle data, String ip, int port) {
        this.data = data;
        String filePath = data.getString("filename");
        this.ip = ip;
        this.port = port;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        recvFile();
        recvFileSuccess();
    }

    private void recvFile() {
        FTPTools ftpTools = FTPTools.getInstance();
        ftpTools.initFtpClient("192.168.100.18", 7070, "user", "12345");
        ftpTools.downloadFile("D:/Easy/Temp/", filePath, Environment.getExternalStorageDirectory() + "/Download/EasyLauncher");
//                ftpTools.downloadFile("./","filezilla.exe", Environment.getExternalStorageDirectory() + "/Download/");
        Log.i(TAG, "文件接收完成");
    }
}
