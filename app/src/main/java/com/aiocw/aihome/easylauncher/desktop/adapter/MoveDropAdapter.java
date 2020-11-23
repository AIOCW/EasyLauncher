package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.tools.imagetools.ImageTools;
import com.aiocw.aihome.easylauncher.desktop.entity.App;

import java.util.List;

public class MoveDropAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private static final int mLayout = R.layout.item_app;

    private Context context;
    private List<App> list;


    public MoveDropAdapter(Context context, List<App> appList) {
        this.list = appList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(mLayout, null);
        return new MoveDropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MoveDropViewHolder) {
            MoveDropViewHolder holder = (MoveDropViewHolder) viewHolder;
            App app = list.get(position);
            holder.tvName.setText(app.getAppName());
            holder.ivIcon.setImageDrawable(ImageTools.loadImage(app.getIconPath()));

            holder.itemView.setTag(mLayout, position);
//            holder.itemView.setOnLongClickListener(this);
            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

//    @Override
//    public boolean onLongClick(View view) {
//        int position = (int) view.getTag(mLayout);
//        if (onAdapterListener != null) {
//            onAdapterListener.onItemLongClick(view, position);
//        }
//        return false;
//    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag(mLayout);

//            另一种方法
        String packageName = list.get(position).getPackageName();
        Intent intent2 = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        String classNameString = intent2.getComponent().getClassName();//得到app类名
        Intent intent  = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(new ComponentName(packageName, classNameString));
        context.startActivity(intent);
    }


    public class MoveDropViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivIcon;

        public MoveDropViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.app_name);
            ivIcon = itemView.findViewById(R.id.app_icon);
        }
    }

    private OnAdapterListener onAdapterListener;

    public void setOnAdapterListener(OnAdapterListener onAdapterListener) {
        this.onAdapterListener = onAdapterListener;
    }

    public interface OnAdapterListener {
        void onItemLongClick(View view, int position);
    }
}
