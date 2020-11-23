package com.aiocw.aihome.easylauncher.desktop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.tools.PermissionTools;

public class Welcome extends AppCompatActivity implements View.OnClickListener{
    private Button permission1Button;
    private Button permission2Button;
    private Button permissionFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        permission1Button = findViewById(R.id.btn_permission_1);
        permission2Button = findViewById(R.id.btn_permission_2);
        permissionFinishButton = findViewById(R.id.btn_permission_finish);
        permission1Button.setOnClickListener(this);
        permission2Button.setOnClickListener(this);
        permissionFinishButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            setResult(3, intent);
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_permission_1:
                PermissionTools.getEasyPermission(Welcome.this);
                break;
            case R.id.btn_permission_2:
                PermissionTools.getComplexPermission(Welcome.this);
                break;
            case R.id.btn_permission_finish:
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
        }
    }
}
