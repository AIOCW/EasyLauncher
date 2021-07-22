package com.aiocw.aihome.easylauncher.desktop.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.StartAppUsually;
import com.aiocw.aihome.easylauncher.common.activity.BaseActivity;
import com.aiocw.aihome.easylauncher.common.CommonStaticData;
import com.aiocw.aihome.easylauncher.common.StartAppFirst;
import com.aiocw.aihome.easylauncher.common.activity.MessageShowWebView;
import com.aiocw.aihome.easylauncher.common.net.NetThreadRunnable;
import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;
import com.aiocw.aihome.easylauncher.desktop.adapter.AppWidgetModelAdapter;
import com.aiocw.aihome.easylauncher.desktop.adapter.BottomRecyclerViewAdapter;
import com.aiocw.aihome.easylauncher.desktop.adapter.FolderFileViewAdapter;
import com.aiocw.aihome.easylauncher.desktop.adapter.LeftRecyclerViewAdapter;
import com.aiocw.aihome.easylauncher.desktop.adapter.MoveDropAdapter;
import com.aiocw.aihome.easylauncher.desktop.adapter.RightAdapter;
import com.aiocw.aihome.easylauncher.desktop.dragpager.RecycleViewLongPressMove;
import com.aiocw.aihome.easylauncher.desktop.entity.App;
import com.aiocw.aihome.easylauncher.desktop.tools.AppDataDB;
import com.aiocw.aihome.easylauncher.common.tools.AppDataTool;
import com.aiocw.aihome.easylauncher.desktop.tools.AppInstallerAndRemove;
import com.aiocw.aihome.easylauncher.desktop.tools.AppWidgetModelOption;
import com.aiocw.aihome.easylauncher.desktop.tools.WeatherTools;
import com.aiocw.aihome.easylauncher.extendfun.activity.OpenWaysActivity;
import com.aiocw.aihome.easylauncher.extendfun.entity.FolderFileAttribute;
import com.aiocw.aihome.easylauncher.extendfun.entity.NetSettingSerializable;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.WaitToDo;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.AddWaitToDoActivity;
import com.aiocw.aihome.easylauncher.extendfun.waittodo.WaitToDoDB;
import com.paul623.wdsyncer.SyncConfig;
import com.paul623.wdsyncer.SyncManager;
import com.paul623.wdsyncer.api.OnListFileListener;
import com.paul623.wdsyncer.model.DavData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends BaseActivity implements RecycleViewLongPressMove.OnLongPressMoveListener,
        BottomRecyclerViewAdapter.BottomRecyclerOptionOnListener,
        LeftRecyclerViewAdapter.LeftRecyclerOptionOnListener{

    //Service
    public static Messenger mServerMessenger = null;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerMessenger = new Messenger(service);
            Log.e(TAG, "onServiceConnected: 绑定服务端成功");
            NetSettingSerializable netSettingSerializable = StartAppUsually.getNetSettingData(HomeActivity.this);
            NetThreadRunnable.getInstance(netSettingSerializable.getServerIp(),
                    netSettingSerializable.getServerPort(), "123456789",
                    netSettingSerializable.getDeviceName()).start();

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {//注：unbindService不会调用此方法
            mServerMessenger = null;
            Log.e(TAG, "onServiceDisconnected: 与服务端断开联系");
        }
    };

    // 第一页
    private RecyclerView appWidgetRecyclerView;
    private List<AppWidgetHostView> appWidgetHostViewList;
    private TextView timeTextView;
    private TextView dateTextView;
    private String DATE_FORMAT = "yyyy-MM-dd E";
    private String TIME_FORMAT = "HH:mm";
    private int flashNumber = 0;

    // 该部分时第一页的时间图标的显示控制
    private final BroadcastReceiver timeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                SimpleDateFormat sdf_time = new SimpleDateFormat(TIME_FORMAT);
                timeTextView.setText(sdf_time.format(new Date()));
                SimpleDateFormat sdf_data = new SimpleDateFormat(DATE_FORMAT);
                dateTextView.setText(sdf_data.format(new Date()));
                flashNumber ++;
                if (flashNumber % 5 == 0) {
                    flashNumber = 0;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            weatherTTextView.setText(WeatherTools.getWeatherDataTemp());
                            clearMem();
                            btnShowMemoryAndClear.setText(getCurrentMemInfo());
                        }
                    });
                    thread.start();
                }
            }
        }
    };

    private TextView weatherTTextView;
    private ImageView weatherStatusImageView;


    // 第二页桌面应用
    private static final String TAG = "HomeActivity";

    private RecyclerView mLeftRecyclerView;
    private RecyclerView mBottomRecyclerView;

    public static LeftRecyclerViewAdapter leftAdapter;
    public static BottomRecyclerViewAdapter bottomAdapter;

    private static List<App> leftApps;
    private static List<App> bottomApps;

    private Button btnWallpaperChange; //改变壁纸
    private Button btnShowMemoryAndClear; //内存清理按钮
    private Button btnMainSetting; //设置页面入口
    private LinearLayout appWidgetLinearLayout; // 添加小部件

    private static final int MY_REQUEST_APPWIDGET = 1;
    private static final int MY_CREATE_APPWIDGET = 2;

    private static final int HOST_ID = 1024 ;

    private AppWidgetHost appWidgetHost = null ;
    private AppWidgetManager appWidgetManager = null;

    // 第二页的提示笔记
    private RecyclerView mRightRecyclerView;
    public static RightAdapter rightAdapter;
    public static ArrayList<WaitToDo> waitToDoArrayList;
    private Button btnAddWaitToDo;


    //DragPager 第三页
    private static MoveDropAdapter moveDropAdapter;
    private RecyclerView recyclerView;
    private static List<App> thirdApps;
    private LinearLayout appOptionLayout;
    //End

    //Fourth
    private TextView tvShowDavFile;
    private Button btnRefresh;
    private FolderFileViewAdapter folderFileViewAdapter;
    private RecyclerView fourthRecyclerView;
    private List<FolderFileAttribute> folderFileAttributeList = new ArrayList<>();
    //end

    //app安装卸载更新广播
    private InstallUninstallBroadcastReceiver receiver;
    private IntentFilter package_removed;
    private IntentFilter package_added;
    private IntentFilter package_changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setStatusBarColor(0x00000000);
        setContentView(R.layout.home_activity);

        if (StartAppUsually.isFirstRun(HomeActivity.this)) {
            // 权限初始化，文件，文件夹， 数据库, 初始化设置
            StartAppFirst.startApp(HomeActivity.this);
            // 权限申请
            Intent intent = new Intent(HomeActivity.this, Welcome.class);
            startActivityForResult(intent, CommonStaticData.PREMISSION_REQUEST_CODE_CONTACT);
        }else {
            onInitApp();
            bind(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unBind(null);
        //        clipboard.removePrimaryClipChangedListener(onPrimaryClipChangedListener);

        // 注销广播
//        unregisterReceiver(receiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public void onInitApp() {
        // 必要的监听支持 数据第一次启动获取
        initAppSupport();

        initFirstLayout();

        initSecondLayout();

        initThirdLayout();

        initFourthLayout();

        //内存管理， 获取当前可用内存余量，建议级别清理，Android系统当前允许的最大清理级别
        initMemoryAndClear();


        //该函数用于设置RecyclerView的Adapter
        setupAdapter();




    }

    public void initAppSupport() {
        startAppsDataLoad();

        // 系统广播==app install remove change
        // 用于更行桌面app图标
        receiver = new InstallUninstallBroadcastReceiver();
        package_removed = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        package_added = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        package_changed = new IntentFilter(Intent.ACTION_PACKAGE_CHANGED);
        package_added.addDataScheme("package");    //必须添加这项，否则拦截不到广播
        package_removed.addDataScheme("package");    //必须添加这项，否则拦截不到广播
        package_changed.addDataScheme("package");    //必须添加这项，否则拦截不到广播
        getBaseContext().registerReceiver(receiver, package_added);
        getBaseContext().registerReceiver(receiver, package_removed);
        getBaseContext().registerReceiver(receiver, package_changed);

    }

    private void initAppWidgetDatas() {
        AppWidgetModelOption.reStoreAppWidget(appWidgetHostViewList, getBaseContext(), appWidgetManager, appWidgetHost);
    }

    private void startAppsDataLoad() {
        leftApps = new ArrayList<>();
        bottomApps = new ArrayList<>();
        thirdApps = new ArrayList<>();

        bottomApps = AppDataDB.queryAppData(bottomApps, getBaseContext(), DBCommonData.TABLE_BOTTOM_APP_DATA);
        leftApps = AppDataDB.queryAppData(leftApps, getBaseContext(), DBCommonData.TABLE_LEFT_APP_DATA);

        StartAppUsually.usuallyStart(bottomApps, leftApps, thirdApps, this, HomeActivity.this);
    }

    private void initFirstLayout() {
        timeTextView = findViewById(R.id.tv_time);
        dateTextView = findViewById(R.id.tv_date);
        initTimeShow();

        weatherTTextView = findViewById(R.id.tv_weather_t);
        weatherStatusImageView = findViewById(R.id.iv_weather_status);
        weatherStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("URL", "https://e.weather.com.cn/d/town/index?lat=41.16964&lon=122.09044&areaid=101090404");
                Intent intent = new Intent(HomeActivity.this, MessageShowWebView.class);
                intent.putExtras(bundle);
                startActivity(intent, bundle);
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                weatherTTextView.setText(WeatherTools.getWeatherDataTemp());
            }
        });
        thread.start();

        //desktop widget桌面小部件的列表注册流程
        appWidgetHostViewList = new ArrayList<>();
        appWidgetRecyclerView = findViewById(R.id.recycler_view_app_widget);
        LinearLayoutManager llmAppwidget = new LinearLayoutManager(this);
        appWidgetRecyclerView.setLayoutManager(llmAppwidget);
        appWidgetRecyclerView.setAdapter(new AppWidgetModelAdapter(this, appWidgetHostViewList));

        appWidgetLinearLayout = findViewById(R.id.linear_layout_app_widget);
        appWidgetLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //显示所有能创建AppWidget的列表 发送此 ACTION_APPWIDGET_PICK 的Action
                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK) ;

                //向系统申请一个新的appWidgetId ，该appWidgetId与我们发送Action为ACTION_APPWIDGET_PICK
                // 后所选择的AppWidget绑定 。 因此，我们可以通过这个appWidgetId获取该AppWidget的信息了

                //为当前所在进程申请一个新的appWidgetId
                int newAppWidgetId = appWidgetHost.allocateAppWidgetId() ;
                Log.i(TAG, "The new allocate appWidgetId is ----> " + newAppWidgetId) ;

                //作为Intent附加值 ， 该appWidgetId将会与选定的AppWidget绑定
                pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, newAppWidgetId) ;

                //选择某项AppWidget后，立即返回，即回调onActivityResult()方法
                startActivityForResult(pickIntent , MY_REQUEST_APPWIDGET);

                return false;
            }
        });
        //其参数hostid大意是指定该AppWidgetHost 即本Activity的标记Id， 直接设置为一个整数值吧 。
        appWidgetHost = new AppWidgetHost(this, HOST_ID) ;
        //为了保证AppWidget的及时更新 ， 必须在Activity的onCreate/onStar方法调用该方法
        // 当然可以在onStop方法中，调用mAppWidgetHost.stopListenering() 停止AppWidget更新
        appWidgetHost.startListening();
        //获得AppWidgetManager对象
        appWidgetManager = AppWidgetManager.getInstance(this);

        // 小部件数据初始化
        initAppWidgetDatas();
        // 刷新布局文件
        appWidgetRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void initSecondLayout() {
        // 查询数据，获取当前数据库中的待做项
        waitToDoArrayList = WaitToDoDB.queryWaitToDo(waitToDoArrayList, getBaseContext());

        // 设置的入口按钮
        btnMainSetting = findViewById(R.id.btn_main_setting);
        btnMainSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainSetting.class);
                startActivityForResult(intent, 22);
            }
        });

        // wait to add entrance(入口)
        btnAddWaitToDo = findViewById(R.id.btn_add_wait_to_do);
        btnAddWaitToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddWaitToDoActivity.class);
                startActivityForResult(intent, 11);
            }
        });

        // wallpaper setting
        btnWallpaperChange = findViewById(R.id.btn_wallpaper_change);
        btnWallpaperChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetWallpaper(v);
            }
        });

        // left app set
        mLeftRecyclerView = findViewById(R.id.left_recycler_view);
        mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        // dock app set
        mBottomRecyclerView = findViewById(R.id.bottom_recycler_view);
        LinearLayoutManager bottomLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        bottomLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBottomRecyclerView.setLayoutManager(bottomLinearLayoutManager);

        // wait to do set
        mRightRecyclerView = findViewById(R.id.right_recycler_view);
        LinearLayoutManager rightLinearLayoutManager = new LinearLayoutManager(this);
        rightLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRightRecyclerView.setLayoutManager(rightLinearLayoutManager);
    }

    private void initThirdLayout() {
        // DragPager
        moveDropAdapter = new MoveDropAdapter(this, thirdApps); //目前共享一个list
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(moveDropAdapter);
        moveDropAdapter.notifyDataSetChanged();

        appOptionLayout = findViewById(R.id.app_option_layout);
        RecycleViewLongPressMove recycleViewLongPressMove = new RecycleViewLongPressMove(recyclerView, thirdApps, appOptionLayout);
        recycleViewLongPressMove.setOnLongPressMoveListener(this);
        appOptionLayout.setVisibility(View.GONE);
    }

    // Fourth
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                // fourth
                fourthRecyclerView = findViewById(R.id.dav_recycler_view);
                LinearLayoutManager fourthLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                fourthRecyclerView.setLayoutManager(fourthLinearLayoutManager);
                folderFileViewAdapter = new FolderFileViewAdapter(HomeActivity.this, folderFileAttributeList); //目前共享一个list
                fourthRecyclerView.setAdapter(folderFileViewAdapter);
//                folderFileViewAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });
    // Fourth
    private void initFourthLayout() {
        btnRefresh = findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncConfig config = new SyncConfig(HomeActivity.this);
                config.setPassWord("12345678");
                config.setUserAccount("test");
                config.setServerUrl("http://192.1.1.23:8080");
                SyncManager syncManager = new SyncManager(HomeActivity.this);

                syncManager.listAllFile("", new OnListFileListener() {
                    @Override
                    public void listAll(List<DavData> davResourceList) {

                        for(DavData i:davResourceList){
                            Log.i("FourthLayout", i.toString() + i.getDisplayName());
                            folderFileAttributeList.add(new FolderFileAttribute(i.getDisplayName(),
                                    "folder",
                                    i.getModified().toString(),
                                    "Yaque"));
                        }
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                        System.out.println("================");
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Log.d("MainActivity","请求失败:"+errorMsg);
                    }
                });
            }
        });

    }

    // 第二页的RecyclerView设置Adapter
    private void setupAdapter(){
        leftAdapter = new LeftRecyclerViewAdapter(getBaseContext(), leftApps);
        leftAdapter.setLeftRecyclerOptionOnListener(this);
        mLeftRecyclerView.setAdapter(leftAdapter);
        bottomAdapter = new BottomRecyclerViewAdapter(getBaseContext(), bottomApps);
        bottomAdapter.setRecyclerOptionOnListener(this);
        mBottomRecyclerView.setAdapter(bottomAdapter);
        rightAdapter = new RightAdapter(getBaseContext(), waitToDoArrayList);
        mRightRecyclerView.setAdapter(rightAdapter);
    }

    // 初始化时间方法
    public void initTimeShow() {
        // 新时间
        SimpleDateFormat sdf_time = new SimpleDateFormat(TIME_FORMAT);
        timeTextView.setText(sdf_time.format(new Date()));
        SimpleDateFormat sdf_data = new SimpleDateFormat(DATE_FORMAT);
        dateTextView.setText(sdf_data.format(new Date()));
        // 更新时间的广播
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeBroadcastReceiver,filter);
    }


    // 内存清理按钮的初始化与监听
    private void initMemoryAndClear() {
        btnShowMemoryAndClear = findViewById(R.id.btn_show_memory);
        btnShowMemoryAndClear.setText(getCurrentMemInfo());
        btnShowMemoryAndClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShowMemoryAndClear.setText("清理中...");
                clearMem();
                btnShowMemoryAndClear.setText(getCurrentMemInfo());
            }
        });
    }


    public static void updateRecycle() {
        rightAdapter.notifyDataSetChanged();
    }

    public static void updateLeftRecycle() {
        leftAdapter.notifyDataSetChanged();
    }

    public static void updateBottomRecycle() {
        bottomAdapter.notifyDataSetChanged();
    }

    public static void updateAllRecycle() {
        moveDropAdapter.notifyDataSetChanged();
    }


    // 以下是实现第三页app长按操作得 接口
    @Override
    public void onNomalView() {
        Log.d(TAG, "onNomalView");
        appOptionLayout.setVisibility(View.GONE);
        moveDropAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoveView(boolean isTouchPointInView) {
        Log.d(TAG, "onMoveView");
        if(isTouchPointInView){
            appOptionLayout.setVisibility(View.VISIBLE);
            appOptionLayout.setBackgroundColor(Color.GREEN);
        }else{
            appOptionLayout.setVisibility(View.VISIBLE);
            appOptionLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onOperation(int position, int code) {
//        deleteAppTextView.setBackgroundColor(Color.WHITE);
//        deleteAppTextView.setTextColor(Color.BLUE);
        appOptionLayout.setVisibility(View.GONE);
        Log.d(TAG, "onOperation" + ">" + position + ">>>>>>>>>>    " + code);
        AppDataDB.deleteAppData(thirdApps.get(position).getId(), getBaseContext(), DBCommonData.TABLE_THIRD_APP_DATA);
        App app = thirdApps.remove(position);
        switch (code) {
            case 0:
                moveDropAdapter.notifyDataSetChanged();
                AppInstallerAndRemove.uninstallApk(HomeActivity.this, app.getPackageName());
                break;
            case 1:
                moveDropAdapter.notifyDataSetChanged();
                bottomApps.add(app);
                AppDataDB.insertAppData(getBaseContext(), app, DBCommonData.TABLE_BOTTOM_APP_DATA);
                bottomAdapter.notifyDataSetChanged();
                break;
            case 2:
                moveDropAdapter.notifyDataSetChanged();
                leftApps.add(app);
                AppDataDB.insertAppData(getBaseContext(), app, DBCommonData.TABLE_LEFT_APP_DATA);
                leftAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void deleteBottomApp(int postion) {
        App app = bottomApps.get(postion);
        AppDataDB.deleteAppData(app.getId(), this, DBCommonData.TABLE_BOTTOM_APP_DATA);
        bottomApps.remove(postion);
        bottomAdapter.notifyDataSetChanged();

        thirdApps.add(app);
        AppDataDB.insertAppData(this, app, DBCommonData.TABLE_THIRD_APP_DATA);
        moveDropAdapter.notifyDataSetChanged();
        Toast.makeText(this, "dock中的应用移除成功", Toast.LENGTH_SHORT);

    }

    @Override
    public void deleteLeftApp(int postion) {
        App app = leftApps.get(postion);
        AppDataDB.deleteAppData(app.getId(), this, DBCommonData.TABLE_BOTTOM_APP_DATA);
        leftApps.remove(postion);
        leftAdapter.notifyDataSetChanged();

        thirdApps.add(app);
        AppDataDB.insertAppData(this, app, DBCommonData.TABLE_THIRD_APP_DATA);
        moveDropAdapter.notifyDataSetChanged();
        Toast.makeText(this, "应用移除成功", Toast.LENGTH_SHORT);
    }


    public void onSetWallpaper(View view) {
        //生成一个设置壁纸的请求
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
        Intent chooser = Intent.createChooser(pickWallpaper,"chooser_wallpaper");
        //发送设置壁纸的请求
        startActivity(chooser);
    }


    // 如果
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //直接返回，没有选择任何一项 ，例如按Back键
        if(resultCode == RESULT_CANCELED)
            return ;

        switch(requestCode){
            case MY_REQUEST_APPWIDGET :
                Log.i(TAG, "MY_REQUEST_APPWIDGET intent info is -----> "+data ) ;
                int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , AppWidgetManager.INVALID_APPWIDGET_ID) ;

                Log.i(TAG, "MY_REQUEST_APPWIDGET : appWidgetId is ----> " + appWidgetId) ;

                //得到的为有效的id
                if(appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
                    //查询指定appWidgetId的 AppWidgetProviderInfo对象 ， 即在xml文件配置的<appwidget-provider />节点信息
                    AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId) ;

                    //如果配置了configure属性 ， 即android:configure = "" ，需要再次启动该configure指定的类文件,通常为一个Activity
                    if(appWidgetProviderInfo.configure != null){

                        Log.i(TAG, "The AppWidgetProviderInfo configure info -----> " + appWidgetProviderInfo.configure ) ;

                        //配置此Action
                        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
                        intent.setComponent(appWidgetProviderInfo.configure) ;
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);


                        startActivityForResult(intent , MY_CREATE_APPWIDGET) ;
                    }
                    else //直接创建一个AppWidget
                        onActivityResult(MY_CREATE_APPWIDGET , RESULT_OK , data) ; //参数不同，简单回调而已
                }
                break ;
            case MY_CREATE_APPWIDGET:
                AppWidgetHostView appWidgetHostView = AppWidgetModelOption.completeAddAppWidget(this, data, appWidgetManager, appWidgetHost);
                appWidgetHostViewList.add(appWidgetHostView);
                appWidgetRecyclerView.getAdapter().notifyDataSetChanged();
                break;

            case 11 : {
                Log.i(TAG, "this is use?" + resultCode);
                if (resultCode == RESULT_OK) {
                    waitToDoArrayList = WaitToDoDB.queryWaitToDo(waitToDoArrayList, getBaseContext());
                    rightAdapter.setWaitToDoList(waitToDoArrayList);
                    rightAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CommonStaticData.PREMISSION_REQUEST_CODE_CONTACT:
                if (resultCode == 3) {
                    onInitApp();
                    bind(null);
                }
                break;
        }

    }

    /**
     * （安装/替换/卸载）接收者，可以接收三个广播
     * 当其他应用被（安装/替换/卸载）后，Android操作系统会自动检测到，系统会自动的发出以下三种广播
     *  1安装
     *  2替换
     *  3卸载
     */
    public class InstallUninstallBroadcastReceiver extends BroadcastReceiver {

        private final String TAG = InstallUninstallBroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Info", "检测到了");

            /**
             * 获取（安装/替换/卸载）应用的 信息
             */
            String packages = intent.getDataString();
            packages = packages.split(":")[1];


            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_PACKAGE_ADDED:
                    Log.d(TAG, packages + "=================了");
                    break;
            }
            if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                App app = new App(getBaseContext(), packages);
                AppDataTool.addAppToThird(app, bottomApps, leftApps, thirdApps, HomeActivity.this);
                Log.d(TAG, packages + "新应用程序安装了，需要进行该应用安全扫描吗");
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                if (AppDataTool.deleteApp(thirdApps, packages, DBCommonData.TABLE_THIRD_APP_DATA, HomeActivity.this)) {
                    Log.d(TAG, "delete third app");
                }else if (AppDataTool.deleteApp(leftApps, packages, DBCommonData.TABLE_LEFT_APP_DATA, HomeActivity.this)) {
                    Log.d(TAG, "delete left app");
                }else if (AppDataTool.deleteApp(bottomApps, packages, DBCommonData.TABLE_BOTTOM_APP_DATA, HomeActivity.this)) {
                    Log.d(TAG, "delete bottom app");
                }
                Log.d(TAG, packages + "应用程序卸载了");
            } else if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
                Log.d(TAG, packages + "应用程序覆盖了");
            } else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
                Log.d(TAG, packages + "应用程序改变了");
            }
            updateAllRecycle();
            updateLeftRecycle();
            updateBottomRecycle();
        }
    }

    /**
     * 获取内存信息
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String getCurrentMemInfo() {
        StringBuffer sb = new StringBuffer();
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        sb.append("可用="+(mi.availMem/1024/1024)+"MB");
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            sb.append("A： " + (mi.totalMem/1024/1024) + "MB\n");
//        }
//        sb.append("内存是否过低：" + mi.lowMemory);
        return sb.toString();
    }

    /**
     * 清理内存
     */
    private void clearMem() {
        ActivityManager activityManger = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = activityManger.getRunningAppProcesses();
        if (appList != null) {
            for (int i = 0; i < appList.size(); i++) {
                ActivityManager.RunningAppProcessInfo appInfo = appList.get(i);

                Log.v("sha bi", "pid: " + appInfo.pid);
                Log.v("sha bi", "processName: " + appInfo.processName);
                Log.v("sha bi", "importance: " + appInfo.importance);

                String[] pkgList = appInfo.pkgList;
                if (appInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    for (int j = 0; j < pkgList.length; j++) {
                        activityManger.killBackgroundProcesses(pkgList[j]);
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            Toast.makeText(this, "按下了back键   onKeyDown()", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //Service
    //3、绑定服务端并在回调方法onServiceConnected中保存下服务端的Messenger
    public void bind(View view) {
        Intent intent = new Intent();
        intent.setPackage("com.aiocw.aihome.easylauncher");
        intent.setAction("com.aiocw.aihome.easylauncher.server.action");
        boolean bindService = bindService(intent, mConnection, BIND_AUTO_CREATE);
        Log.e(TAG, "bind: bindService=" + bindService);
    }

    public void unBind(View view) {
        if (mServerMessenger != null) {
            unbindService(mConnection);
            mServerMessenger = null;
        }
        Log.e(TAG, "unBind: ");
    }

    private void registerClipEvents() {

        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {

                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();

                    if (addedText != null) {
                        Log.d(TAG, "copied text: " + addedText);
                        Intent intent = new Intent(HomeActivity.this, OpenWaysActivity.class);
                        intent.putExtra("clip_data", addedText);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                registerClipEvents();
//                mBinding.clipText.setText(getClipData(MainActivity.this));
            }
        });
    }
}