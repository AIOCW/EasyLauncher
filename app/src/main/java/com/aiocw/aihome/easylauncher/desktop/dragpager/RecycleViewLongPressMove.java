package com.aiocw.aihome.easylauncher.desktop.dragpager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;

import java.util.Collections;
import java.util.List;

/**
 * 移动拖拽ItemView 到指定区域操作
 *
 * @author xulin
 * @email xllovey@163.com
 */
public class RecycleViewLongPressMove implements View.OnTouchListener {

    private CustomGridLayoutManager customGridLayoutManager;
    private List<?> list;
    private View removeView;
    private View tvDeleteApp;
    private View tvMoveToBottom;
    private View tvMoveToLeft;
    private int positionTag;
    private boolean isLongPress;

    /**
     * 移动拖拽ItemView 到指定区域操作
     *
     * @param recyclerView 当前集合RecyclerView
     * @param list         当前Adater的数据源 移动时排序使用
     * @param removeView   指定的操作View区域
     */
    public RecycleViewLongPressMove(RecyclerView recyclerView, List<?> list, View removeView) {
        this.list = list;
        this.removeView = removeView;
        tvDeleteApp = removeView.findViewById(R.id.tv_delete_app);
        tvMoveToBottom = removeView.findViewById(R.id.tv_move_to_bottom);
        tvMoveToLeft = removeView.findViewById(R.id.tv_move_to_left);

        customGridLayoutManager = new CustomGridLayoutManager(recyclerView.getContext(), 4);
        recyclerView.setLayoutManager(customGridLayoutManager);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnTouchListener(this);
    }


    //为RecycleView绑定触摸事件
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //首先回调的方法 返回int表示是否监听该方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;//拖拽

            positionTag = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);//位置变化
            positionTag = toPosition;
            Log.i("RecycleViewLongPressMove", toPosition + "p");
            //数据源位置更换
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(list, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        }

        @Override
        public boolean isLongPressDragEnabled() {
            customGridLayoutManager.setScrollEnabled(true);//禁止滑动为false
            isLongPress = true;
            onLongPressMoveListener.onMoveView(false);
            return true;
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (onLongPressMoveListener == null) {
            new IllegalArgumentException("请在 RecycleViewLongPressMove 调用 setOnLongPressMoveLisener !!！");
            return true;
        }
        float xDown = event.getX();
        float yDown = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isLongPress) {
                if (isTouchPointInView(tvDeleteApp, xDown, yDown)) {
                    onLongPressMoveListener.onOperation(positionTag, 0);
                }else if (isTouchPointInView(tvMoveToBottom, xDown, yDown)) {
                    onLongPressMoveListener.onOperation(positionTag, 1);
                }else if (isTouchPointInView(tvMoveToLeft, xDown, yDown)) {
                    onLongPressMoveListener.onOperation(positionTag, 2);
                }
            }
            onLongPressMoveListener.onNomalView();
            isLongPress = false;
            customGridLayoutManager.setScrollEnabled(true);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isLongPress) {
                onLongPressMoveListener.onMoveView(isTouchPointInView(removeView, xDown, yDown));
            }
        }
        return false;
    }

    private boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    private OnLongPressMoveListener onLongPressMoveListener;

    public void setOnLongPressMoveListener(OnLongPressMoveListener onLongPressMoveListener) {
        this.onLongPressMoveListener = onLongPressMoveListener;
    }

    public interface OnLongPressMoveListener {
        void onNomalView();//正常
        void onMoveView(boolean isTouchPointInView);//移动
        void onOperation(int position, int code);//操作
    }

}

