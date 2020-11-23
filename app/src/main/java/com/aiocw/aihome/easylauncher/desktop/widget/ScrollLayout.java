package com.aiocw.aihome.easylauncher.desktop.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.aiocw.aihome.easylauncher.desktop.tools.ActivityLifecycleApplcation;

/**
 * 仿Launcher中的WorkSapce，可以左右滑动切换屏幕的类
 * @author Yao.GUET
 * blog: http://blog.csdn.NET/Yao_GUET
 * date: 2011-05-04
 */
public class ScrollLayout extends ViewGroup {
    private static final String TAG = "ScrollLayout";
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mCurScreen;
    private int mDefaultScreen = 0;

    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;

    private static final int SNAP_VELOCITY = 600;

    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ScrollLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        mScroller = new Scroller(context);

        mCurScreen = mDefaultScreen;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        registerHomeKeyReceiver(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
//        if (changed) {
        int childLeft = 0;
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                childView.layout(childLeft, 0,
                        childLeft + childWidth, childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        }

        // The children are given the same width and height as the scrollLayout
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        // Log.e(TAG, "moving to screen "+mCurScreen);
        scrollTo(mCurScreen * width, 0);
    }

    /**
     * According to the position of current layout
     * scroll to the destination page.
     */
    public void snapToDestination() {
        final int screenWidth = getWidth();
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth())) {

            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0,
                    delta, 0, Math.abs(delta) * 1);
            mCurScreen = whichScreen;
            invalidate();       // Redraw the layout
        }
    }

    public void snapToScreenFaster(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth())) {

            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0,
                    delta, 0, Math.abs(delta) / 4);
            mCurScreen = whichScreen;
            invalidate();       // Redraw the layout
        }
    }

    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(whichScreen * getWidth(), 0);
    }

    public int getCurScreen() {
        return mCurScreen;
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "event down!");
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;

                scrollBy(deltaX, 0);
                break;

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "event : up");
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                Log.e(TAG, "velocityX:" + velocityX);

                if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                    // Fling enough to move left
                    Log.e(TAG, "snap left");
                    snapToScreen(mCurScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY
                        && mCurScreen < getChildCount() - 1) {
                    // Fling enough to move right
                    Log.e(TAG, "snap right");
                    snapToScreen(mCurScreen + 1);
                } else {
                    if (true) {
                        if (velocityX > SNAP_VELOCITY && mCurScreen == 0) {
                            Log.e(TAG, "snap left to the last right");
                            setToScreen(getChildCount());
                        }else if (velocityX < -SNAP_VELOCITY && mCurScreen == getChildCount() - 1) {
                            Log.e(TAG, "snap right to the first left");
                            setToScreen(0);
                        }
                    }else{
                        snapToDestination();
                    }
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) &&
                (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;

                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterHomeKeyReceiver(getContext());

    }



    public class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "HomeReceiver";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, "onReceive: action: " + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                Log.i(LOG_TAG, "reason: " + reason);

                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    Log.i(LOG_TAG, "homekey");
//                    setToScreen(0);
                    if (ActivityLifecycleApplcation.getActivityCount() == 1) {
                        if (getCurScreen() == 0) {
                            snapToScreenFaster(1);
                        }else {
                            snapToScreenFaster(0);
                        }
                    }


                } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    Log.i(LOG_TAG, "long press home key or activity switch");

                } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    Log.i(LOG_TAG, "lock");
                } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    Log.i(LOG_TAG, "assist");
                }

            }
        }
    }


    private static HomeWatcherReceiver mHomeKeyReceiver = null;
    private void registerHomeKeyReceiver(Context context) {
        Log.i("REGISTER", "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private void unregisterHomeKeyReceiver(Context context) {
        Log.i("REGISTER", "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }
}