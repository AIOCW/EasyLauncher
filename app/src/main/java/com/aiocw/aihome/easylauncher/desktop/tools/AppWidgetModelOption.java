package com.aiocw.aihome.easylauncher.desktop.tools;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.aiocw.aihome.easylauncher.desktop.entity.AppWidgetModel;

import java.util.List;

public class AppWidgetModelOption {
    static String TAG = "AppWidgetAddTool";


    // 向当前视图添加一个用户选择的
    public static AppWidgetHostView completeAddAppWidget(Context context, Intent data, AppWidgetManager appWidgetManager, AppWidgetHost appWidgetHost){

        Bundle extra = data.getExtras() ;
        int appWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID , -1) ;

        AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;

        AppWidgetHostView hostView = appWidgetHost.createView(context, appWidgetId, appWidgetProviderInfo);

        Log.i(TAG, "completeAddAppWidget : appWidgetId is ----> " + appWidgetId) ;
        Toast.makeText(context, "添加窗口小部件OK", Toast.LENGTH_SHORT).show();

        if(appWidgetId == -1){
            Toast.makeText(context, "添加窗口小部件有误", Toast.LENGTH_SHORT).show();
            return null;
        }
        AppWidgetModelDB.insertAppWidgetModel(context, new AppWidgetModel(appWidgetId));

        return hostView;

//        int widget_minWidht = appWidgetProviderInfo.minWidth ;
//        int widget_minHeight = appWidgetProviderInfo.minHeight ;
//        //设置长宽 appWidgetProviderInfo 对象的 minWidth 和 minHeight 属性
//        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(widget_minWidht, widget_minHeight);
//        //添加至LinearLayout父视图中
//        linearLayout.addView(hostView, linearLayoutParams);
//        linearLayout.removeAllViews();
//        linearLayout.updateViewLayout(hostView, linearLayoutParams);
    }


    public static List<AppWidgetHostView> reStoreAppWidget(List<AppWidgetHostView> appWidgetHostViewList, Context context, AppWidgetManager appWidgetManager, AppWidgetHost appWidgetHost) {
        List<AppWidgetModel> appWidgetModelList = AppWidgetModelDB.queryAppData(context);
        for (int i = 0; i < appWidgetModelList.size(); i ++) {
            int appWidgetId = appWidgetModelList.get(i).getAppWidgetId();
            //查询指定appWidgetId的 AppWidgetProviderInfo对象 ， 即在xml文件配置的<appwidget-provider />节点信息
            AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;
            String a = appWidgetProviderInfo.provider.getClassName();
            String b = appWidgetProviderInfo.provider.getPackageName();
            Log.e(TAG, a  + "           " + b);

            AppWidgetHostView hostView = appWidgetHost.createView(context, appWidgetId, appWidgetProviderInfo);
            appWidgetHostViewList.add(hostView);
        }
        return appWidgetHostViewList;
    }

    public static void deleteAppWidget(int appWidgetId, Context context) {
        List<AppWidgetModel> appWidgetModelList = AppWidgetModelDB.queryAppData(context);
        for (int i = 0; i < appWidgetModelList.size(); i ++) {
            if (appWidgetId == appWidgetModelList.get(i).getAppWidgetId()) {
                AppWidgetModelDB.deleteAppData(appWidgetModelList.get(i).getId(), context);
            }
        }
    }



}
