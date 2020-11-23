package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

public class BottomRecyclerViewAdapter extends RecyclerView.Adapter<BottomRecyclerViewAdapter.ActivityHolder>
        implements RecyclerView.OnClickListener, RecyclerView.OnLongClickListener {
    private Context context;
    private List<App> bottomApps;
    private BottomRecyclerOptionOnListener bottomRecyclerOptionOnListener;
    private String TAG = "BottomRecyclerViewAdapter";

    public BottomRecyclerViewAdapter(Context context, List<App> bottomApps) {
        this.context = context;
        this.bottomApps = bottomApps;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.easy_launcher_recycler_bottom_view_item, viewGroup,false);
        return new ActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder activityHolder, int position) {
        App app = bottomApps.get(position);

        String appName = app.getAppName();
        Drawable appIcon = ImageTools.loadImage(app.getIconPath());
        Log.i(TAG, "========" + appName + "=======");
//        activityHolder.mNameTextView.setText(appName);
        activityHolder.mIconImageView.setImageDrawable(appIcon);
        activityHolder.mIconImageView.setTag(position);
        activityHolder.mIconImageView.setOnClickListener(this);
        activityHolder.mIconImageView.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return bottomApps.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        App app = bottomApps.get(postion);
        /**
         * 创建显式Intent
         */
//            Intent i = new Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
//                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);

//            另一种方法
        String packagename = app.getPackageName();
        Intent intent2 = context.getPackageManager()
                .getLaunchIntentForPackage(packagename);
        String classNameString = intent2.getComponent().getClassName();//得到app类名
        Intent intent  = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(new ComponentName(packagename, classNameString));
        context.startActivity(intent);

//            再一种

//            Intent intent_ = getPackageManager().getLaunchIntentForPackage(activityInfo.applicationInfo.packageName);
//            startActivity(intent_);
    }

    @Override
    public boolean onLongClick(View v) {
        int postion = (int) v.getTag();
        bottomRecyclerOptionOnListener.deleteBottomApp(postion);
        return false;
    }

    class ActivityHolder extends RecyclerView.ViewHolder {


//        private TextView mNameTextView;
        private ImageView mIconImageView;

        public ActivityHolder(View itemView) {
            super(itemView);
//            mNameTextView = itemView.findViewById(R.id.app_name);
            mIconImageView = itemView.findViewById(R.id.app_icon);
        }
    }

    public void setRecyclerOptionOnListener(BottomRecyclerOptionOnListener bottomRecyclerOptionOnListener) {
        this.bottomRecyclerOptionOnListener = bottomRecyclerOptionOnListener;
    }

    public interface BottomRecyclerOptionOnListener {
        void deleteBottomApp(int code);
    }
}