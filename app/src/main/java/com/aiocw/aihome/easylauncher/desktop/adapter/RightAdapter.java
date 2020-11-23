package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.WaitToDo;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.WaitToDoDB;

import java.util.List;

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.WaitToDoViewHolder> {
    private Context context;
    private List<WaitToDo> waitToDoList;
    private static String TAG = "RightAdapter";

    public RightAdapter(Context context, List<WaitToDo> waitToDoList) {
        this.context = context;
        this.waitToDoList = waitToDoList;

    }

    public void setWaitToDoList(List<WaitToDo> waitToDoList) {
        this.waitToDoList = waitToDoList;
    }

    @Override
    public WaitToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
//            MyViewHolder holder = new MyViewHolder(layoutInflater.inflate(R.layout.easy_launcher_recycler_right_view_item_message, parent, false));
        WaitToDoViewHolder waitToDoViewHolder = new WaitToDoViewHolder(layoutInflater.inflate(R.layout.easy_launcher_recycler_right_view_item_wait_to_do,
                parent, false));
        return waitToDoViewHolder;
    }

    @Override
    public void onBindViewHolder(WaitToDoViewHolder waitToDoViewHolder, int position) {
        WaitToDo waitToDo = waitToDoList.get(position);
        waitToDoViewHolder.bindActivity(waitToDo);
    }

    @Override
    public int getItemCount() {
        return waitToDoList.size();
    }


    class WaitToDoViewHolder extends RecyclerView.ViewHolder {

        WaitToDo waitToDo;
        TextView textView;
        CheckBox checkBox;
        int id = 0;
        int i = 0;

        public WaitToDoViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_view_wait_to_do);
            checkBox = view.findViewById(R.id.check_box_wait_to_do);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = waitToDo.getId();
                    for (i = 0; i < waitToDoList.size(); i ++) {
                        if (waitToDoList.get(i).getId() == id) {
                            waitToDoList.remove(i);
                            checkBox.setChecked(false);
                            RightAdapter.this.notifyDataSetChanged();
                            WaitToDoDB.deleteToDo(id, context);
//                            AlertDialog alertDialog = new AlertDialog.Builder(context)
//                                    .setTitle("删除提示")
//                                    .setMessage("是否删除该条提示内容？")
//                                    .setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int e) {
//
//                                        }
//                                    })
//                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            checkBox.setChecked(false);
//                                            return;
//                                        }
//                                    }).create();
//                            alertDialog.show();
                            break;
                        }
                    }
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
        }

        public void bindActivity(WaitToDo waitToDo) {
            this.waitToDo = waitToDo;
            String content = this.waitToDo.getContent();
            String lastTime = this.waitToDo.getLastTime();
            Log.i(TAG, content + "========" + lastTime + "=======");
            textView.setText(content + "\n最晚执行时间" + lastTime);
        }
    }
}
