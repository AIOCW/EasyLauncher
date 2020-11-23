package com.aiocw.aihome.easylauncher.extendfun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.CommonDynamicData;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHost;
import com.aiocw.aihome.easylauncher.extendfun.callback.OnSendMessageListener;

import java.util.List;

public class OtherHostRecyclerViewAdapter extends RecyclerView.Adapter<OtherHostRecyclerViewAdapter.OtherHostRVHolder> {
    private String TAG = "OtherHostRecyclerViewAdapter ====    ";

    private List<OtherHost> list;
    private Context context;
    private OnSendMessageListener onSendMessageListener;

    public OtherHostRecyclerViewAdapter(Context context, List<OtherHost> list, OnSendMessageListener onSendMessageListener) {
        this.list = list;
        this.context = context;
        this.onSendMessageListener = onSendMessageListener;
    }

    @NonNull
    @Override
    public OtherHostRecyclerViewAdapter.OtherHostRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orther_host, parent, false);
        return new OtherHostRecyclerViewAdapter.OtherHostRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OtherHostRecyclerViewAdapter.OtherHostRVHolder holder, final int position) {
        holder.nameTextView.setText(list.get(position).getHostName());
        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.nameTextView.setBackgroundColor(R.color.gray);
                // 向线后台服务发送文件路径
                if (list.get(position).getHostName().equals("本地转存")) {
                    switch (CommonDynamicData.SEND_TYPE) {
                        case 1100:
                            onSendMessageListener.saveOtherFolder();
                            break;
                        case 1011:
                            // 等待完善
                            break;
                    }
                }else {
                    switch (CommonDynamicData.SEND_TYPE) {
                        case 1100:
                            onSendMessageListener.sendFile(list.get(position).getHostName());
                            Log.i(TAG, "准备将小文件发送到" + list.get(position).getHostName());
                            break;
                        case 1011:
                            onSendMessageListener.sendTextMessage(list.get(position).getHostName());
                            Log.i(TAG, "准备将文本消息发送到" + list.get(position).getHostName());
                            break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OtherHostRVHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public OtherHostRVHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
        }
    }
}
