package com.aiocw.aihome.easylauncher.desktop.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.CommonStaticData;
import com.aiocw.aihome.easylauncher.common.CommunicationTools;
import com.aiocw.aihome.easylauncher.common.StartAppUsually;
import com.aiocw.aihome.easylauncher.extendfun.entity.NetSettingSerializable;

public class ServerConnectionSetting extends AppCompatActivity {
    private String TAG = "ServerConnectionSetting";

    private EditText editTextDeviceName;
    private EditText editTextIp;
    private EditText editTextRemoteIp;
    private EditText editTextPort;
    private Button btnApply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_connection_setting);

        final NetSettingSerializable netSettingSerializable = StartAppUsually.getNetSettingData(this);

        editTextDeviceName = findViewById(R.id.et_device_name);
        editTextIp = findViewById(R.id.et_ip);
        editTextRemoteIp = findViewById(R.id.et_remote_ip);
        editTextPort = findViewById(R.id.et_port);
        btnApply = findViewById(R.id.btn_apply);

        editTextDeviceName.setText(netSettingSerializable.getDeviceName());
        editTextIp.setText(netSettingSerializable.getServerIp());
        editTextRemoteIp.setText(netSettingSerializable.getRemoteServerIp());
        editTextPort.setText(netSettingSerializable.getServerPort() + "");

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 写入sharedpreference
                SharedPreferences sharedPreferences = ServerConnectionSetting.this.getSharedPreferences(CommonStaticData.APP_SETTING_FILE_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String deviceName = editTextDeviceName.getText().toString();
                String ip = editTextIp.getText().toString();
                String remoteIp = editTextRemoteIp.getText().toString();
                int port = Integer.valueOf(editTextPort.getText().toString());

                editor.putString("deviceName", deviceName);
                editor.putString("serverIp", ip);
                editor.putString("remoteServerIp", remoteIp);
                editor.putInt("serverPort", port);
                editor.commit();

                CommunicationTools.sendNetSettingToService(mClientMessenger, netSettingSerializable, 81001);
            }
        });

    }

    //1、创建一个Handler用于接Service的发送来的反馈信息
    Handler mClientHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 71001:
                    Log.e(TAG, "确认");
//                    Toast.makeText(ServerConnectionSetting.this, R.string.net_setting_change_success, Toast.LENGTH_LONG);
                    ServerConnectionSetting.this.finish();
                    break;
                default:
//                    Toast.makeText(ServerConnectionSetting.this, "error", Toast.LENGTH_SHORT);
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    //2、构建一个客户端的Messenger，方便服务端使用，这个用于告诉服务端，他要发给谁
    Messenger mClientMessenger = new Messenger(mClientHandler);
}
