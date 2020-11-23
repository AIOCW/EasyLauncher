package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aiocw.aihome.easylauncher.desktop.tools.AppWidgetModelOption;

import java.util.List;

public class AppWidgetModelAdapter extends RecyclerView.Adapter<AppWidgetModelAdapter.AppWidgetHolder> {
    private String TAG = "AppWidgetModelAdapter========";
    private Context context;
    private List<AppWidgetHostView> list;


    public AppWidgetModelAdapter(Context context, List list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AppWidgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new LinearLayout(context);
        view.setPadding(2, 2, 2, 2);
        return new AppWidgetModelAdapter.AppWidgetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppWidgetHolder holder, int position) {
        Log.i(TAG, "" + position);
        final AppWidgetHostView hostView = list.get(position);
        if (hostView.getParent() != null) {
            ((ViewGroup)hostView.getParent()).removeView(hostView);
        }
        holder.linearLayout.removeAllViews();
        holder.linearLayout.addView(hostView);
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppWidgetModelOption.deleteAppWidget(hostView.getAppWidgetId(), context);
                list.remove(hostView);
                notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AppWidgetHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public AppWidgetHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView;
        }
    }
}
