package com.aiocw.aihome.easylauncher.common;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;

import java.io.Serializable;

public class CommunicationTools {
    public static String TAG = "CommunicationTools";

    static Messenger mServerMessenger = HomeActivity.mServerMessenger;

    public static boolean sendNetSettingToService(Messenger mClientMessenger, Serializable serializableData, int code) {
        if (mServerMessenger == null) {
            // 向服务端发送消息
            Log.e("==========================", "出现错误");
            return false;
        }
        Message toServerMsg = Message.obtain();
        toServerMsg.what = code;
        toServerMsg.replyTo = mClientMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        Bundle bundle = new Bundle();
        bundle.putSerializable("serializableData", serializableData);
        toServerMsg.setData(bundle);
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServer: ");
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
