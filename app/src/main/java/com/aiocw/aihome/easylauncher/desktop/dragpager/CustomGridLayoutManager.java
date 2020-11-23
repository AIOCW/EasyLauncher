package com.aiocw.aihome.easylauncher.desktop.dragpager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 *  Android 禁止RecycleView的滑动
 */
public class CustomGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}