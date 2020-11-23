package com.aiocw.aihome.easylauncher.extendfun.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.CommonDynamicData;
import com.aiocw.aihome.easylauncher.common.CommonTools;
import com.aiocw.aihome.easylauncher.common.tools.StringTools;
import com.aiocw.aihome.easylauncher.common.tools.filetools.FileTools;
import com.aiocw.aihome.easylauncher.common.tools.ProviderTools;
import com.aiocw.aihome.easylauncher.common.tools.filetools.XFileTools;
import com.aiocw.aihome.easylauncher.desktop.activity.HomeActivity;
import com.aiocw.aihome.easylauncher.extendfun.adapter.OtherHostRecyclerViewAdapter;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHost;
import com.aiocw.aihome.easylauncher.extendfun.callback.OnSendMessageListener;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHostSerializable;
import com.github.ybq.android.spinkit.SpinKitView;


import java.util.ArrayList;
import java.util.List;


public class ShareWaysActivity extends WaysActivity implements OnSendMessageListener {

    private TextView textViewMessage;
    private SpinKitView spinKitViewWaitTip;

    private static final String TAG = "messenger";


    // 待发送信息
    private String filePath;
    private String newFilePath;

    private String textMessage;

    private RecyclerView otherHostRecyclerView;
    private List<OtherHost> otherHostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 实现悬浮activity 需要继承自Activity
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_share_file);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.height = (int) (display.getHeight() * 0.3);
//        params.width = (int) (display.getWidth() * 0.8);
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);

        otherHostList = new ArrayList<>();
        OtherHost otherHost = new OtherHost("本地转存", "0.0.0.0", 0);
        otherHostList.add(otherHost);


        otherHostRecyclerView = findViewById(R.id.rv_other_host);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShareWaysActivity.this);
        otherHostRecyclerView.setLayoutManager(linearLayoutManager);
        otherHostRecyclerView.setAdapter(new OtherHostRecyclerViewAdapter(ShareWaysActivity.this, otherHostList, this));

        textViewMessage = findViewById(R.id.tv_message);
        spinKitViewWaitTip = findViewById(R.id.spin_kit);

        //share lisener
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type !=null) {
            if ("text/plain".equals(type)) {
                textMessage = dealTextMessage(intent);
//                showMessage(textMessage);
                sendMessageToServiceForGetDevicesList();
                CommonDynamicData.SEND_TYPE = 1011;
            }else {
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                filePath = uri.getPath();

                if (String.valueOf(uri) != null && String.valueOf(uri).contains("content")) {
                    boolean kkk = false;
                    try {
                        String newFilename = CommonTools.getFilePathFromContentUri(uri, this.getContentResolver());
                        if (FileTools.isFileEmpty(newFilename)) {
                            kkk = true;
                            Log.i("文件路径对吗？", "不对，初始的路径是：" + filePath + "查看的根目录：" + Environment.getExternalStorageDirectory().getPath());
                        } else {
                            filePath = newFilename;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        kkk = true;
                        Log.e("基本路径检测出错", "出现错误");
                    }
                    if (kkk) {
                        String[] filepathArray = filePath.split(":");
                        if (filepathArray.length == 2) {
                            filePath = Environment.getExternalStorageDirectory().getPath() + "/" + filepathArray[1];
                        } else {
                            filePath = ProviderTools.getFPUriToPath(this, uri);
                        }
                    }
                }
                FileTools.addFolder(Environment.getExternalStorageDirectory().getPath(), "AISaveFolder");

                String fileName = XFileTools.getFileName(filePath);
                newFilePath = Environment.getExternalStorageDirectory().getPath() + "/AISaveFolder/" + fileName;
                Log.i("文件名为", filePath);
                showMessage("初始获取的文件路径：" + fileName);

                // 在屏幕显示可传送列表


                sendMessageToServiceForGetDevicesList();
                CommonDynamicData.SEND_TYPE = 1100;

            }


        }else if (Intent.ACTION_SEND_MULTIPLE.equals(action)&&type!=null){
            // 多选分享
            if (type.startsWith("image/")){
                dealMultiplePicStream(intent);
            }
        }
        Log.i(TAG + "onCreate success", "right");

    }

    public void onWindowFocusChanged(boolean hasFocus){
        // activity 完成既定页面加载生命周期后执行的函数
    }

    private String dealTextMessage(Intent intent){
        String share = intent.getStringExtra(Intent.EXTRA_TEXT);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);
        Log.i(TAG + "share + title", share + ":" + title);
        return share;
    }

    void dealPicStream(Intent intent){
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    }

    void dealMultiplePicStream(Intent intent){
        ArrayList<Uri> arrayList = intent.getParcelableArrayListExtra(intent.EXTRA_STREAM);
    }

    private void showMessage(String message) {
        textViewMessage.setText(StringTools.substringForWidth(message, 4));
    }

    //1、创建一个Handler用于接Service的发送来的反馈信息
    Handler mClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data;
            String serverMsg;
            switch (msg.what) {
                case 1001:
                    Log.e(TAG, "已连接设备请求已发出: 收到来自服务端的消息：确认");
                    break;
                case 91001:
                    //获取来自服务端的消息，标识码是
                    data = msg.getData();
                    OtherHostSerializable otherHostSerializable = (OtherHostSerializable) data.getSerializable("otherHostList");
                    List<OtherHost> answerOtherHostList = otherHostSerializable.getOtherHostList();
                    int answerLen = answerOtherHostList.size();
                    for (int i = 0; i < answerLen; i ++) {
                        otherHostList.add(answerOtherHostList.remove(0));
                    }
                    otherHostRecyclerView.getAdapter().notifyDataSetChanged();
                    spinKitViewWaitTip.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "handleMessage: 收到来自服务端的消息：serverMsg=");
                    break;
                case 910110:
                    //获取来自服务端的消息
                    spinKitViewWaitTip.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "handleMessage: 收到来自服务端的消息：serverMsg=");

                    ShareWaysActivity.this.finish();
                    break;
                case 91100:
                    //获取来自服务端的消息，标识码是1100
                    data = msg.getData();
                    spinKitViewWaitTip.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "handleMessage: 收到来自服务端的消息  文件传输完成");
                    ShareWaysActivity.this.finish();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    //2、构建一个客户端的Messenger，方便服务端使用，这个用于告诉服务端，他要发给谁
    Messenger mClientMessenger = new Messenger(mClientHandler);

    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void sendFileMessageToService(String clientName) {
        if (mServerMessenger == null || !CommonDynamicData.IS_CONNECTION_SERVER) {
            showMessage("出现网络错误");
            return;
        }
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 430;
        toServerMsg.replyTo = mClientMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        Bundle bundle = new Bundle();
        bundle.putString("filename", filePath);
        bundle.putString("aim_device", clientName);
        toServerMsg.setData(bundle);
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServer: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //4、使用服务端的Messenger发送消息给服务端，并携带上客户端的Messenger。
    public void sendMessageToServiceForGetDevicesList() {
        if (mServerMessenger == null || !CommonDynamicData.IS_CONNECTION_SERVER) {
            showMessage("出现网络错误");
            return;
        }
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 1001;
        toServerMsg.replyTo = mClientMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToServerForGetDevicesList: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 发送文本信息
    public void sendTextMessageToService(String clientName) {
        if (mServerMessenger == null || !CommonDynamicData.IS_CONNECTION_SERVER) {
            showMessage("出现网络错误");
            return;
        }
        // 向服务端发送消息
        Log.e("==========================", "向服务端发送消息");
        Message toServerMsg = Message.obtain();
        toServerMsg.what = 1011;
        toServerMsg.replyTo = mClientMessenger;//将客户端的Messenger携带上，服务端可以用来回复客户端的消息
        Bundle bundle = new Bundle();
        bundle.putString("client_name", clientName);
        bundle.putString("text_message", textMessage);
        toServerMsg.setData(bundle);
        try {
            mServerMessenger.send(toServerMsg);//使用服务端的Messenger发送消息给服务端
            Log.e(TAG, "sendMessageToService: ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFile(String clientName) {
        sendFileMessageToService(clientName);
        spinKitViewWaitTip.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean saveOtherFolder() {
        XFileTools.copyFile(filePath, newFilePath);
        this.finish();
        return false;
    }

    @Override
    public void sendTextMessage(String clientName) {
        sendTextMessageToService(clientName);
        spinKitViewWaitTip.setVisibility(View.VISIBLE);
    }
}
