package com.aiocw.aihome.easylauncher.common.tools;

import android.util.Log;

import com.aiocw.aihome.easylauncher.extendfun.entity.TextMessageSerializable;

import org.json.JSONException;
import org.json.JSONObject;

public class StringTools {
    public static TextMessageSerializable getDataFromJSONObject(String textMessage) {
        TextMessageSerializable textMessageSerializable = new TextMessageSerializable();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(textMessage);
            String clietName = jsonObject.getString("client_name");
            String securityMd5Text = jsonObject.getString("security_md5_text");
            textMessageSerializable.setClientName(clietName);
            textMessageSerializable.setTextMessage(securityMd5Text);
            Log.i("收到的textMessage","lient_name" + clietName + ";security_md5_text " + securityMd5Text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return textMessageSerializable;
    }

    public static String substringForWidth(String str, int length){
        if (str.length() > length) {
            return str.substring(0, length) + "...";
        }else {
            return str + "...";
        }
    }
}
