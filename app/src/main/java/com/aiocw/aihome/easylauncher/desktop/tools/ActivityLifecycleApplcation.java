package com.aiocw.aihome.easylauncher.desktop.tools;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class ActivityLifecycleApplcation extends Application{
    private final static String TAG = "ActivityLifecycleApplcation";
    private static ActivityLifecycleApplcation mActivityLifecycleApplcation;
    private static int mActivityCount = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        mActivityLifecycleApplcation = new ActivityLifecycleApplcation();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG,"onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG,"onActivityStarted");
                mActivityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG,"onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG,"onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG,"onActivityStopped");
                mActivityCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG,"onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG,"onActivityDestroyed");
            }
        });
    }

    public static ActivityLifecycleApplcation getInstance( ) {
        if (null == mActivityLifecycleApplcation)
            mActivityLifecycleApplcation = new ActivityLifecycleApplcation();
        return mActivityLifecycleApplcation;
    }

    public static int getActivityCount( ) {
        Log.d(TAG, mActivityCount + "");
        return mActivityCount;
    }
}
