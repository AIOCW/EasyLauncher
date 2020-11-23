package com.aiocw.aihome.easylauncher.common;



import android.app.AlertDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.aiocw.aihome.easylauncher.common.net.NetThreadRecvFile;
import com.aiocw.aihome.easylauncher.common.net.NetThreadSendFile;
import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHost;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHostSerializable;
import com.aiocw.aihome.easylauncher.extendfun.entity.TextMessageSerializable;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.WaitToDoDB;

import java.util.ArrayList;
import java.util.List;


public class EasyLauncherService extends Service {

    private static final String TAG = "messenger";

    private ClipboardManager clipboard;

    private List<Messenger> messengerInputList = new ArrayList<>();

    private Messenger threadMessenger;

    private Messenger messengerServerConnectionSetting;

    private Messenger shareFileClientMessenger;

    private List<String> sendBigSpiltFileConfirmSuccessList;


    //1、用于接收客户端发送过来的消息，从ShareFileActivity发送来的消息
    Handler mServerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("发送编码是====================e", String.valueOf(msg.what));
            switch (msg.what) {
                case 1:
                    threadMessenger = msg.replyTo;
                    break;
                case 1001:
                    //设备列表
                    messengerInputList.add(msg.replyTo);
                    Message answerToShareFile1001 = Message.obtain();
                    answerToShareFile1001.what = 1001;
                    try {
                        msg.replyTo.send(answerToShareFile1001);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    // 给线程回复
                    Message answerToThread1001 = Message.obtain();
                    answerToThread1001.what = 1001;
                    try {
                        threadMessenger.send(answerToThread1001);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1011:
                    // 文本信息
                    messengerInputList.add(msg.replyTo);
                    Message answerToShareFile1011 = Message.obtain();
                    answerToShareFile1011.what = 1011;
                    try {
                        msg.replyTo.send(answerToShareFile1011);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    // 给线程回复
                    Message answerToThread1011 = Message.obtain();
                    answerToThread1011.what = 1011;
                    answerToThread1011.setData(msg.getData());
                    try {
                        threadMessenger.send(answerToThread1011);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 910110:
                    Message answerToShareFile910110 = Message.obtain();
                    answerToShareFile910110.what = 910110;
                    try {
                        messengerInputList.remove(0).send(answerToShareFile910110);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 91011:
                    Bundle bundle91011 = msg.getData();
                    addTextMessageToWaitToDo((TextMessageSerializable) bundle91011.getSerializable("textMessageSerializable"));
                    break;
                case 91001:
                    // 设备列表
                    Bundle bundle91001 = msg.getData();
                    Message answerToShareFile91001 = Message.obtain();
                    answerToShareFile91001.what = 91001;
                    answerToShareFile91001.setData(bundle91001);
                    try {
                        messengerInputList.remove(0).send(answerToShareFile91001);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1100:
                    // 文件发送标识
                    Message answerToThread1100 = Message.obtain();
                    answerToThread1100.what = 1100;
                    answerToThread1100.setData(msg.getData());
                    try {
                        threadMessenger.send(answerToThread1100);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 430:
                    // 文件发送标识
                    shareFileClientMessenger = msg.replyTo;
                    NetThreadSendFile netThreadSendFile = new NetThreadSendFile(msg.getData(), "", 0);
                    netThreadSendFile.start();

                    break;
                case 530:
                    // 文件发送标识
                    shareFileClientMessenger = msg.replyTo;
                    NetThreadRecvFile netThreadRecvFile = new NetThreadRecvFile(msg.getData(), "", 0);
                    netThreadRecvFile.start();

                    break;
                case 91100:
                    // 接收线程的通知，并发给shareways
                    Message answerToShareWays91100 = Message.obtain();
                    answerToShareWays91100.what = 91100;
                    answerToShareWays91100.setData(msg.getData());
                    try {
                        shareFileClientMessenger.send(answerToShareWays91100);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 911005:
                    // 告诉线程ftp成功发送出文件，你可以发送文件信息了
                    Message answerToThread911005 = Message.obtain();
                    answerToThread911005.what = 1100;
                    answerToThread911005.setData(msg.getData());
                    try {
                        threadMessenger.send(answerToThread911005);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 81001:
                    //获取到客户端的 Messenger，这里可以保存为全局变量，以后就不用每次都去获取了
                    messengerServerConnectionSetting = msg.replyTo;

                    //获取来自文件分享客户端的消息
                    Message message81001 = Message.obtain();
                    message81001.what=81001;
                    Bundle bundle81001 = msg.getData();
                    message81001.setData(bundle81001);
                    try {
                        threadMessenger.send(message81001);
                        Log.e(TAG, "81001" );
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 71001:
                    //获取来自文件分享客户端的消息
                    Message message71001 = Message.obtain();
                    message71001.what = 71001;
                    try {
                        messengerServerConnectionSetting.send(message71001);
                        Log.e(TAG, "71001" );
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 911000:
                    Bundle bundle911000 = msg.getData();
                    boolean successFlag = bundle911000.getBoolean("successFlag", false);

                    if (successFlag) {
                        if (sendBigSpiltFileConfirmSuccessList == null) {
                            sendBigSpiltFileConfirmSuccessList = new ArrayList<>();
                        }
                        sendBigSpiltFileConfirmSuccessList.add(successFlag +"");
                        if (sendBigSpiltFileConfirmSuccessList.size() == 5) {
                            Message message11003 = Message.obtain();
                            message11003.what = 11003;
                            try {
                                threadMessenger.send(message11003);
                                Log.e(TAG, "81001" );
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case 3331:
                    //获取来自服务端的消息，标识码是
                    Bundle bundle3331 = msg.getData();
                    OtherHostSerializable otherHostSerializable = (OtherHostSerializable) bundle3331.getSerializable("otherHostList");
                    List<OtherHost> answerOtherHostList = otherHostSerializable.getOtherHostList();
                    showListDialog(answerOtherHostList);
                    Log.e(TAG, "复制信息获取发送列表");
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };
    Messenger mServerMessenger = new Messenger(mServerHandler);


    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: MyServerService绑定" );
//        netThreadRunnable.start();
        return mServerMessenger.getBinder();


    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: MyServerService创建" );
        super.onCreate();
        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
//        clipboard.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: MyServerService销毁" );
//        clipboard.removePrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    private void addTextMessageToWaitToDo(TextMessageSerializable textMessageSerializable) {
        // 获取系统剪贴板

        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText("EasyLauncher", textMessageSerializable.getTextMessage());

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);

        WaitToDoDB.insertWaitToDo(getBaseContext(), textMessageSerializable.getTextMessage());
        HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
        HomeActivity.updateRecycle();
    }

    private void showListDialog(List list) {
        final String[] items = {"取消"};
        int answerLen = list.size();
        for (int i = 0; i < answerLen; i ++) {
            list.add(list.remove(0));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("我是一个列表Dialog");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始
                // ...To-do
                Toast.makeText(EasyLauncherService.this,
                        "你点击了" + items[which],
                        Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog listDialog = builder.create();
        listDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        listDialog.show();
    }


    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void sendFileMessageToService(String clientName) {
        if (mServerMessenger == null || !CommonDynamicData.IS_CONNECTION_SERVER) {
            Log.i(TAG, "出现网络错误");
            return;
        }
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 1100;
        toServerMsg.replyTo = threadMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        Bundle bundle = new Bundle();
        toServerMsg.setData(bundle);

        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
