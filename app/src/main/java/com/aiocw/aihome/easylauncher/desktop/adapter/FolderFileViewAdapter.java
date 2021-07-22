package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import com.aiocw.aihome.easylauncher.extendfun.entity.FolderFileAttribute;

import java.util.List;

public class FolderFileViewAdapter extends RecyclerView.Adapter<FolderFileViewAdapter.ActivityHolder> {
//        implements RecyclerView.OnClickListener, RecyclerView.OnLongClickListener {
    private Context context;
    private List<FolderFileAttribute> folderFileAttributeList;
//    private FolderFileViewAdapter.ForderFileOptionOnListener forderFileOptionOnListener;
    private String TAG = "forderFileRecyclerViewAdapter";

    public FolderFileViewAdapter(Context context, List<FolderFileAttribute> folderFileAttributeList) {
        this.context = context;
        this.folderFileAttributeList = folderFileAttributeList;
    }

    @NonNull
    @Override
    public FolderFileViewAdapter.ActivityHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.folder_file_view_item, viewGroup,false);
        return new FolderFileViewAdapter.ActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderFileViewAdapter.ActivityHolder activityHolder, int position) {
        FolderFileAttribute folderFileAttribute = folderFileAttributeList.get(position);

        String name = folderFileAttribute.getName();
        String modifyTime = folderFileAttribute.getModifyTime();
        Drawable kindIconDrawable = null;
        Resources resources = context.getResources();
        switch (folderFileAttribute.getKind()) {
            case "folder":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "txt":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "mp4":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "mp3":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "doc":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "docx":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "ppt":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "pptx":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "xls":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
            case "xlsx":
                kindIconDrawable = resources.getDrawable(R.drawable.forder);
                break;
        }

        Log.i(TAG, "========" + position + "=======");
        activityHolder.tvName.setText(name);
        activityHolder.tvModifyTime.setText(modifyTime);
        activityHolder.ivKindIcon.setImageDrawable(kindIconDrawable);
        activityHolder.ivKindIcon.setTag(position);
//        activityHolder.mIconImageView.setOnClickListener(this);
//        activityHolder.mIconImageView.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return folderFileAttributeList.size();
    }

//    @Override
//    public void onClick(View v) {
//        int postion = (int) v.getTag();
//        FolderFileAttribute folderFileAttribute = folderFileAttributeList.get(postion);
//        Log.i("vvv" + postion, folderFileAttribute.getName());
//        /**
//         * 创建显式Intent
//         */
//    }

//    @Override
//    public boolean onLongClick(View v) {
//        int postion = (int) v.getTag();
//        forderFileOptionOnListener.deleteBottomApp(postion);
//        return false;
//    }

    class ActivityHolder extends RecyclerView.ViewHolder {


        private TextView tvName;
        private TextView tvModifyTime;
        private ImageView ivKindIcon;

        public ActivityHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvModifyTime = itemView.findViewById(R.id.modify_time);
            ivKindIcon = itemView.findViewById(R.id.kind_icon);
        }
    }

//    public void setRecyclerOptionOnListener(FolderFileViewAdapter.ForderFileOptionOnListener forderFileOptionOnListener) {
//        this.forderFileOptionOnListener = forderFileOptionOnListener;
//    }

//    public interface ForderFileOptionOnListener {
//        void deleteBottomApp(int code);
//    }
}
