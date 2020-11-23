package com.aiocw.aihome.easylauncher.common.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;

import java.util.ArrayList;

public class MessageShowWebView extends AppCompatActivity {

    private WebView weatherWebView;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_message_show);
        initWebView();
//        String url = "file:///android_asset/weather.html";
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("URL");
        weatherWebView.loadUrl(url);
    }

    private void initWebView() {

        weatherWebView = findViewById(R.id.wv_message_show);
        WebSettings settings = weatherWebView.getSettings();
        settings.setDomStorageEnabled(true);
        //解决一些图片加载问题
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        weatherWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("webview","url: "+url);
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void setting() {
        WebSettings settings = weatherWebView.getSettings();

        // webview启用javascript支持 用于访问页面中的javascript
        settings.setJavaScriptEnabled(true);

        //设置WebView缓存模式 默认断网情况下不缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);


        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        //断网情况下加载本地缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //让WebView支持DOM storage API
        settings.setDomStorageEnabled(true);

        //让WebView支持缩放
        settings.setSupportZoom(true);

        //启用WebView内置缩放功能
        settings.setBuiltInZoomControls(true);

        //让WebView支持可任意比例缩放
        settings.setUseWideViewPort(true);

        //让WebView支持播放插件
        settings.setPluginState(WebSettings.PluginState.ON);

        //设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上
        settings.setDisplayZoomControls(false);

        //设置在WebView内部是否允许访问文件
        settings.setAllowFileAccess(true);

        //设置WebView的访问UserAgent
        //settings.setUserAgentString(String string);

        //设置脚本是否允许自动打开弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
                settings.setLoadsImagesAutomatically(true);
            } else {
                settings.setLoadsImagesAutomatically(false);
            }

        // 开启Application H5 Caches 功能
        settings.setAppCacheEnabled(true);

        // 设置编码格式
        settings.setDefaultTextEncodingName("utf-8");

    }

//    come here
    /**
   Bundle bundle = new Bundle();
   bundle.putString("URL",url);
   Intent intent = new Intent(getActivity(),WebDetailActivity.class);
   intent.putExtras(bundle);
   startActivity(intent,bundle);
    * */
}
