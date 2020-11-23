package com.aiocw.aihome.easylauncher.extendfun.notifiction;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.CommonDynamicData;
import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.WaitToDoDB;

import static com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity.mServerMessenger;

public class NotificationCollectorService extends NotificationListenerService {
    private String TAG = "NotificationCollectorService";
    // Service
//    Messenger mServerMessenger = HomeActivity.mServerMessenger;

    //来通知时的调用
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        String key = sbn.getKey();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        String content = classificationMessage(extras, sbn);
        sendTextMessageToService(content);
        Log.i("状态栏发发发",content);

//        switch (sbn.getPackageName()){
//            case "com.tencent.mm":
//                sendTextMessageToService(content);
//                Log.i("微信",content);
////                cancelNotification(key);
//                break;
//            case "com.android.mms":
//                sendTextMessageToService(content);
//                Log.i("短信",content);
//                break;
//            case "com.tencent.mobileqq":
//                sendTextMessageToService(content);
//                Log.i("qq",content);
////                cancelNotification(key);
//                break;
//            case "com.tencent.tim":
//                sendTextMessageToService(content);
//                Log.i("tim",content);
////                cancelNotification(key);
//                break;
//            case "com.android.incallui":
//                sendTextMessageToService(content);
//                Log.i("电话",content);
//                break;
//            default:
////                sendTextMessageToService(content);
//                break;
//        }


    }

    //删除通知时的调用
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        Bundle extras = notification.extras;
        String content = "";
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
            Log.i("删包名：",sbn.getPackageName() + "标题:" + title + "内容:" + content);
        }
        switch (sbn.getPackageName()){
            case "com.tencent.mm":
                Log.i("删微信",content);
                break;
            case "com.android.mms":
                Log.i("删短信",content);
                break;
            case "com.tencent.mqq":
                Log.i("删qq",content);
                break;
            case "com.tencent.tim":
                Log.i("删tim",content);
                break;
            case "com.android.incallui":
                Log.i("删电话",content);
                break;
        }

    }


    public String classificationMessage(Bundle extras, StatusBarNotification sbn) {
        String content = "";
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
            Log.i("包名：",sbn.getPackageName() + "标题:" + title + "内容:" + content);
            if (content.contains("【快递超市】")) {
                Log.i("这是快递短信", content);
                WaitToDoDB.insertWaitToDo(getBaseContext(), content);
                HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
                HomeActivity.updateRecycle();
            }else if (content.contains("【菜鸟驿站】")) {
                Log.i("这是快递短信", content);
                WaitToDoDB.insertWaitToDo(getBaseContext(), content);
                HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
                HomeActivity.updateRecycle();
            }else if (content.contains("【百世快递】")) {
                Log.i("这是快递短信", content);
                WaitToDoDB.insertWaitToDo(getBaseContext(), content);
                HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
                HomeActivity.updateRecycle();
            }else if (content.contains("【中国邮政】")) {
                Log.i("这是快递短信", content);
                WaitToDoDB.insertWaitToDo(getBaseContext(), content);
                HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
                HomeActivity.updateRecycle();
            }else if (content.contains("【京东快递】")) {
                Log.i("这是快递短信", content);
                WaitToDoDB.insertWaitToDo(getBaseContext(), content);
                HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
                HomeActivity.updateRecycle();
            }else if (content.contains("【滴滴出行】")) {
                Log.i("这是快递短信", content);
//                cancelNotification(key);
            }else if (content.contains("【天天快递】")) {
                Log.i("这是快递短信", content);
                WaitToDoDB.insertWaitToDo(getBaseContext(), content);
                HomeActivity.waitToDoArrayList = WaitToDoDB.queryWaitToDo(HomeActivity.waitToDoArrayList, getBaseContext());
                HomeActivity.updateRecycle();
            }

        }
        return content;
    }

    //1、创建一个Handler用于接Service的发送来的反馈信息
    Handler mClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 910110:
                    //获取来自服务端的消息
//                    spinKitViewWaitTip.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "handleMessage: 发出文本消息成功：serverMsg=");
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    //2、构建一个客户端的Messenger，方便服务端使用，这个用于告诉服务端，他要发给谁
    Messenger mClientMessenger = new Messenger(mClientHandler);


    // 发送文本信息
    public void sendTextMessageToService(String textMessage) {
        if (mServerMessenger == null || !CommonDynamicData.IS_CONNECTION_SERVER) {
            Log.i(TAG, "出现网络错误");
            return;
        }
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 1011;
        toServerMsg.replyTo = mClientMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        Bundle bundle = new Bundle();
        bundle.putString("client_name", "all");
        bundle.putString("text_message", textMessage);
        toServerMsg.setData(bundle);
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToService: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
