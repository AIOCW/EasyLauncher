package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.desktop.activity.ServerConnectionSetting;
import com.aiocw.aihome.easylauncher.desktop.activity.WebDavActivity;

import java.util.List;

public class SettingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> list;
    private Context context;

    public SettingListAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(context).inflate(R.layout.setting_list_item, null, false);
        RecyclerView.ViewHolder holder = null;
        holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).settingItemName.setText(list.get(position));
        ((MyViewHolder) holder).settingItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingDetial(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void goToSettingDetial(int number) {
        switch (number) {
            case 0:
                break;
            case 1:
                Intent serverCon = new Intent(context, ServerConnectionSetting.class);
                context.startActivity(serverCon);
                break;
            case 2:
                Intent webdav = new Intent(context, WebDavActivity.class);
                context.startActivity(webdav);
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView settingItemName;

        public MyViewHolder(View itemView) {
            super(itemView);
            settingItemName = itemView.findViewById(R.id.setting_item_name);
        }
    }

}