package com.aiocw.aihome.easylauncher.desktop.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.desktop.adapter.SettingListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainSetting extends AppCompatActivity {
    private RecyclerView rv;
    private String [] setting = {"本机设置", "服务器连接设置"};
    private ArrayList selectList;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_setting);

        rv = findViewById(R.id.setting_recyclerview);

        //LinearLayoutManager是用来做列表布局，也就是单列的列表
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        //默认就是垂直方向的
        ((LinearLayoutManager) layoutManager).setOrientation(OrientationHelper.VERTICAL);
        //谷歌提供了一个默认的item删除添加的动画
        rv.setItemAnimator(new DefaultItemAnimator());

        //谷歌提供了一个DividerItemDecoration的实现类来实现分割线
        //往往我们需要自定义分割线的效果，需要自己实现ItemDecoration接口
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);

        //当item改变不会重新计算item的宽高
        //调用adapter的增删改差方法的时候就不会重新计算，但是调用nofityDataSetChange的时候还是会
        //所以往往是直接先设置这个为true，当需要布局重新计算宽高的时候才调用nofityDataSetChange
        rv.setHasFixedSize(true);

        //模拟列表数据
        selectList = new ArrayList<>();
        for (int i = 0; i < setting.length; i++) {
            selectList.add(setting[i]);
        }
        // 设置适配器
        SettingListAdapter settingListAdapter = new SettingListAdapter(selectList, MainSetting.this);
        rv.setAdapter(settingListAdapter);
    }
}
