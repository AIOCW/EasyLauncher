package com.aiocw.aihome.easylauncher.desktop.tools;


import android.util.Log;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherTools {
    private static String TAG = "WeatherTools      ";

    public static String getWeatherDataTemp() {
//        /**HtmlUnit请求web页面*/
//        WebClient wc = new WebClient();
//        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
//        wc.getOptions().setCssEnabled(false); //禁用css支持
//        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
//        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
//        HtmlPage page = null;
//        try {
//            page = wc.getPage("http://www.weather.com.cn/weather1dn/101250111.shtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String pageXml = page.asXml(); //以xml的形式获取响应文本
//
//        /**jsoup解析文档*/
//        Document doc = Jsoup.parse(pageXml);
//        Elements elements = doc.getElementsByClass("tempDiv");
//        String temp = doc.getElementsByClass("temp").text();
        return "14℃";
    }
}
