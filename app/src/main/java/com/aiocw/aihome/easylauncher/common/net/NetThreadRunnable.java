package com.aiocw.aihome.easylauncher.common.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.CommonDynamicData;
import com.aiocw.aihome.easylauncher.common.CommonStaticData;
import com.aiocw.aihome.easylauncher.common.FileInformation;
import com.aiocw.aihome.easylauncher.common.net.nettools.SocketHelp;
import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;
import com.aiocw.aihome.easylauncher.extendfun.entity.NetSettingSerializable;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHost;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHostSerializable;
import com.aiocw.aihome.easylauncher.extendfun.entity.TextMessageSerializable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class NetThreadRunnable extends Thread{
    private String TAG = "NetThreadRunnable";

    private static NetThreadRunnable netThreadRunnable;

    private String securityMD5;
    private String ipAddress;
    private int port;
    private String clientName;


    private int sendCode = -1;
    private int recvCode = -1;

    private OutputStream outputData = null;
    private InputStream inputData = null;
    private Socket socket = null;

    private boolean netChanged;
    private int heartbeatCount;
    private int getOtherHostListCount;

    private String localFilepath;

    // 发送出去的数据
    private String textMessage;
    private String sendClientName;
    private String aimDevice;

    private NetSettingSerializable netSettingSerializable;


    // Service
    Messenger mServerMessenger = HomeActivity.mServerMessenger;


    private NetThreadRunnable(String ipAddress, int port, String securityMD5, String clientName) {
        super("Controller-Thread");
        this.ipAddress = ipAddress;
        this.port = port;
        this.securityMD5 = securityMD5;
        this.clientName = clientName;

        CommonDynamicData.IS_TIME_OUT = false;
        heartbeatCount = 0;
        getOtherHostListCount = 0;

        netChanged = false;

        sendInitMessageToService();

    }

    public static synchronized NetThreadRunnable getInstance(String ipAddress, int port, String securityMD5, String clientName) {
        try {
            if (netThreadRunnable == null) {
                netThreadRunnable = new NetThreadRunnable(ipAddress, port, securityMD5, clientName);
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("getInstance Error", "Error");
        }
        return netThreadRunnable;
    }

    @Override
    public void run() {
        sendCode = 1010;
        while (true) {
            if (socket == null || CommonDynamicData.IS_TIME_OUT || netChanged) {
                if (CommonDynamicData.WIFI_CONNECTED) {
                    try {
                        outputData.close();
                        inputData.close();
                        socket.close();
                        socket = null;
                        outputData = null;
                        inputData = null;
                    }catch (Exception e) {
                        socket = null;
                        outputData = null;
                        inputData = null;
                        Log.e("关闭socket连接出错", "socket关闭出错");
                    }
                    try {
                        // 不可以放在后面
                        if (netChanged) {
                            netChanged = false;
                            ipAddress = netSettingSerializable.getServerIp();
                            port = netSettingSerializable.getServerPort();
                            clientName = netSettingSerializable.getDeviceName();

                            socket = new Socket(ipAddress, port);
                            socket.setSoTimeout(CommonStaticData.NET_TIME_OUT);
                            outputData = socket.getOutputStream();
                            inputData = socket.getInputStream();
                            CommonDynamicData.IS_TIME_OUT = false;
                            SocketHelp.online(outputData, inputData, securityMD5, clientName);
                            CommonDynamicData.IS_CONNECTION_SERVER = true;
                            reConnectionSuccess();
                        }else {

                            socket = new Socket(ipAddress, port);
                            socket.setSoTimeout(CommonStaticData.NET_TIME_OUT);
                            outputData = socket.getOutputStream();
                            inputData = socket.getInputStream();
                            CommonDynamicData.IS_TIME_OUT = false;
                            SocketHelp.online(outputData, inputData, securityMD5, clientName);
                            CommonDynamicData.IS_CONNECTION_SERVER = true;
                        }
                    } catch (IOException e) {
                        Log.e("初始化服务器链接出错", "将自动重连");
                        CommonDynamicData.IS_CONNECTION_SERVER = false;
                        e.printStackTrace();
                    } finally {
                        if (socket == null) {
                            CommonDynamicData.IS_CONNECTION_SERVER = false;
                            Log.e("当前WiFi为非服务器WiFi无法链接到服务器", "稍后将自动重试");
                            //应当是网络状态出现变化之后再尝试了
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                switch (sendCode) {
                    case 1010:
                        if (heartbeatCount == 3) {
                            heartbeatCount = 0;
                            switch (SocketHelp.heartbeat(outputData, inputData)){
                                case 1010:
                                    Log.i("OK", "目前在线");
                                    break;
                                case 91011:
                                    sendCode = -1;
                                    recvCode = 91011;
                                    break;
                                // 小文件接收
                                case 91100:
                                    sendCode = -1;
                                    recvCode = 91100;
                                    break;
                                case 9110040:
                                    Log.i(TAG, "服务端成功接收大文件，计算MD5相同");
                                    break;
                                case 9110041:
                                    Log.i(TAG, "准备接收其它客户端的大文件");
                                    break;
                                case -1:
                                    CommonDynamicData.IS_TIME_OUT = true;
                                    CommonDynamicData.IS_CONNECTION_SERVER = false;
                                    Log.i("OK", "目前离线");
                                    break;
                            }
                        }
                        try {
                            this.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        heartbeatCount ++;
                        break;
                    case 1001:
                        try {
                            if (getOtherHostListCount != 3) {
                                List<OtherHost> otherHostList = SocketHelp.getConnectDevices(outputData, inputData);
                                if (otherHostList != null) {
                                    sendDevicesListMessageToService(otherHostList);
                                    sendCode = 1010;
                                } else {
                                    Thread.sleep(500);
                                    getOtherHostListCount ++;
                                }
                            }else {
                                OtherHost otherHost = new OtherHost();
                                otherHost.setHostName("网络错误");
                                List<OtherHost> otherHostList = new ArrayList<OtherHost>();
                                otherHostList.add(otherHost);
                                sendDevicesListMessageToService(otherHostList);
                                getOtherHostListCount = 0;
                            }

                        } catch (Exception e) {
                            Log.e("EndSend", "发送错误1:\n" + e.getMessage());
                        } finally {
                            break;
                        }
                    case 1011:
                        try {
                            if (SocketHelp.sendTextMessage(outputData, inputData, textMessage, sendClientName)) {
                                sendTextMessageCompleteToService();
                            }else {
                                sendTextMessageCompleteToService();
                            }
                            sendCode = 1010;
                        } catch (Exception e) {
                            Log.e("EndSend", "发送错误1:\n" + e.getMessage());
                        } finally {
                            break;
                        }
                    case 1100:
                        try {
                            SocketHelp.SendFile(localFilepath, aimDevice, outputData, inputData);
                            sendFileSuccessToServer();
                        } catch (Exception e) {
                            Log.e("EndSend", "发送错误1:\n" + e.getMessage());
                        } finally {
                            sendCode = 1010;
                            break;
                        }
                }
                switch (recvCode) {
                    case 91011:
                        TextMessageSerializable textMessageSerializable = SocketHelp.getTextMessage(outputData, inputData);
                        Log.i(TAG, textMessageSerializable.getTextMessage());
                        recvCode = -1;
                        sendCode = 1010;
                        sendTextMessageToService(textMessageSerializable);
                        break;
                    case 91100:
                        JSONObject jsonObject = SocketHelp.ReceiveFile(outputData, inputData);
                        try {
                            recvFileSuccessMessage(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sendCode = 1010;
                        recvCode = -1;
                        break;
                    case 2:
//                        serverMessage = SocketHelp.getConnectDevices(outputData, inputData);
                        sendCode = 2;
                        recvCode = -1;
                        break;
                }
            }
        }
    }

    //1、创建一个Handler用于接Service的发送来的反馈信息
    Handler mClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    sendCode = 1001;
                    break;
                case 1011:
                    //获取来自服务端的消息，标识码是1100
                    Bundle bundle1011 = msg.getData();
                    textMessage = bundle1011.getString("text_message") + "----" + clientName;
                    sendClientName = bundle1011.getString("client_name");
                    sendCode = 1011;
                    Log.e(TAG, "handleMessage: 收到来自服务端的消息：serverMsg=" + textMessage + sendClientName);
                    break;
                case 1100:
                    // 文件发送
                    Bundle bundle1100 = msg.getData();
                    localFilepath = bundle1100.getString("filename");
                    aimDevice = bundle1100.getString("aim_device");
                    sendCode = 1100;
                    Log.e(TAG, "准备发送文件");
                    break;
                case 81001:
                    // 服务器切换
                    Bundle bundle81001 = msg.getData();
                    netSettingSerializable = (NetSettingSerializable) bundle81001.getSerializable("serializableData");
                    netChanged = true;
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    //2、构建一个客户端的Messenger，方便服务端使用，这个用于告诉服务端，他要发给谁
    Messenger mClientMessenger = new Messenger(mClientHandler);

    // 第一次启动时使用的提交函数
    public void sendInitMessageToService() {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 1;
        toServerMsg.replyTo = mClientMessenger;
        try {
            mServerMessenger.send(toServerMsg);
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void sendDevicesListMessageToService(List<OtherHost> otherHostList) {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向网络管理服务发送获取已连接设备");

        OtherHostSerializable otherHostSerializable = new OtherHostSerializable(otherHostList);

        Message message91001 = Message.obtain();
        message91001.what=91001;
        Bundle bundle91001 = new Bundle();
        bundle91001.putSerializable("otherHostList", otherHostSerializable);
        message91001.setData(bundle91001);
        try {
            mServerMessenger.send(message91001);//回复客户端
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 第一次启动时使用的提交函数
    public void sendTextMessageCompleteToService() {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message message910110 = Message.obtain();
        message910110.what = 910110;
        message910110.replyTo = mClientMessenger;
        try {
            mServerMessenger.send(message910110);
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //
    public void sendTextMessageToService(TextMessageSerializable textMessageSerializable) {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Message message91011 = Message.obtain();
        message91011.what = 91011;
        message91011.replyTo = mClientMessenger;
        Bundle bundle91011 = new Bundle();
        bundle91011.putSerializable("textMessageSerializable", textMessageSerializable);
        message91011.setData(bundle91011);
        try {
            mServerMessenger.send(message91011);
            Log.e(TAG, "把服务端发送来的数据发给service");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void reConnectionSuccess() {
        if (mServerMessenger == null) return;
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 71001;
        toServerMsg.replyTo = mClientMessenger;
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void sendFileSuccessToServer() {
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 91100;
        toServerMsg.replyTo = mClientMessenger;
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void recvFileSuccessMessage(JSONObject jsonObject) throws JSONException {
        String filename = jsonObject.getString("filename");
        long filesize = jsonObject.getLong("filesize");
        String md5 = jsonObject.getString("md5");
        FileInformation fileInformation = new FileInformation();
        fileInformation.setFilename(filename);
        fileInformation.setFilesize(filesize);
        if (mServerMessenger == null) return;
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 530;
        toServerMsg.replyTo = mClientMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        Bundle bundle = new Bundle();
        bundle.putString("filename", filename);
        toServerMsg.setData(bundle);
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}