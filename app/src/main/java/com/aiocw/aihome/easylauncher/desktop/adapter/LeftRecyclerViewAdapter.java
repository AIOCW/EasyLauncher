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

import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.tools.imagetools.ImageTools;
import com.aiocw.aihome.easylauncher.desktop.entity.App;

import java.util.List;

public class LeftRecyclerViewAdapter extends RecyclerView.Adapter<LeftRecyclerViewAdapter.ActivityHolder>
        implements RecyclerView.OnClickListener, RecyclerView.OnLongClickListener{
    private Context context;
    private List<App> leftApps;
    private String TAG = "LeftRecyclerViewAdapter";

    private LeftRecyclerOptionOnListener leftRecyclerOptionOnListener;

    public LeftRecyclerViewAdapter(Context context, List<App> leftApps){
        this.context = context;
        this.leftApps = leftApps;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.easy_launcher_recycler_left_view_item, parent,false);
        return new ActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        App app = leftApps.get(position);
        String appName = app.getAppName();
        Drawable appIcon = ImageTools.loadImage(app.getIconPath());
        Log.i(TAG, "========" + appName + "=======");
        holder.mNameTextView.setText(appName);
        holder.mIconImageView.setImageDrawable(appIcon);
        holder.mIconImageView.setTag(position);
        holder.mIconImageView.setOnClickListener(this);
        holder.mIconImageView.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return leftApps.size();
    }

    class ActivityHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private ImageView mIconImageView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.app_name);
            mIconImageView = itemView.findViewById(R.id.app_icon);
        }
    }

    @Override
    public void onClick(View v) {

        /**
         * 创建显式Intent
         */
//            Intent i = new Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
//                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);

//            另一种方法
        int postion = (int) v.getTag();
        App app = leftApps.get(postion);
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
        leftRecyclerOptionOnListener.deleteLeftApp(postion);
        return false;
    }

    public void setLeftRecyclerOptionOnListener(LeftRecyclerOptionOnListener leftRecyclerOptionOnListener) {
        this.leftRecyclerOptionOnListener = leftRecyclerOptionOnListener;
    }

    public interface LeftRecyclerOptionOnListener {
        void deleteLeftApp(int postion);
    }

}
