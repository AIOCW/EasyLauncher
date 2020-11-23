package com.aiocw.aihome.easylauncher.common.net.nettools;

import android.os.Environment;
import android.util.Log;

import com.aiocw.aihome.easylauncher.common.CommonDynamicData;
import com.aiocw.aihome.easylauncher.common.CommonTools;
import com.aiocw.aihome.easylauncher.common.FileInformation;
import com.aiocw.aihome.easylauncher.common.tools.StringTools;
import com.aiocw.aihome.easylauncher.common.tools.filetools.FileTools;
import com.aiocw.aihome.easylauncher.common.tools.filetools.SecurityEncryption;
import com.aiocw.aihome.easylauncher.extendfun.entity.OtherHost;
import com.aiocw.aihome.easylauncher.extendfun.entity.TextMessageSerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SocketHelp {
    private static String TAG = "与服务器交流出错";


    // 发送登录请求
    public static boolean online(OutputStream outputStream, InputStream inputStream, String securityMD5, String clientName){
        try {
            byte [] flagCode = CommonTools.int2Byte(1000);
            CommonTools.packageByteSecurity(flagCode);
            outputStream.write(flagCode);
            JSONObject  jsonObject = new JSONObject();
            jsonObject.put("client_name", clientName);
            jsonObject.put("security_md5", securityMD5);

            byte [] sendData = jsonObject.toString().getBytes("UTF-8");
            CommonTools.packageByteSecurity(sendData);
            byte [] sendDataLength = CommonTools.int2Byte(sendData.length);
            CommonTools.packageByteSecurity(sendDataLength);

            outputStream.write(sendDataLength);
            outputStream.write(sendData);

            byte [] successBuffer = new byte[4];
            inputStream.read(successBuffer);
            CommonTools.unPackageByteSecurity(successBuffer);
            if (CommonTools.bytes2Int(successBuffer) == 1000) {
                return true;
            }
        } catch (SocketTimeoutException e) {
            CommonDynamicData.IS_TIME_OUT = true;
            Log.e("SocketTimeoutException online ", "网络超时");
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, " online 与服务器交流IO出错");
        }finally {

        }
        return false;
    }

    // 心跳包
    public static int heartbeat(OutputStream outputStream, InputStream inputStream){
        try {
            // 表明当前为心跳包
            byte[] flagCode = CommonTools.int2Byte(1010);
            CommonTools.packageByteSecurity(flagCode);
            outputStream.write(flagCode);

            byte[] successBuffer = new byte[4];
            inputStream.read(successBuffer);
            CommonTools.unPackageByteSecurity(successBuffer);
            int receCode = CommonTools.bytes2Int(successBuffer);
            Log.e("wwwwww", receCode + "");
            if (receCode == 1010) {
                return 1010;
            }else {
                return receCode;
            }
        }catch (SocketTimeoutException e) {
            CommonDynamicData.IS_TIME_OUT = true;
            Log.e("SocketTimeoutException heartbeat", "网络超时");
            e.printStackTrace();
        }catch (Exception e) {
            Log.e(TAG, "heartbeat 2与服务器交流出错");
        }finally {

        }
        return -1;
    }

    //获取设备数
    public static List<OtherHost> getConnectDevices(OutputStream outputStream, InputStream inputStream) {
        String message = "";
        List<OtherHost> otherHostList = new ArrayList<>();
        try {
            byte[] flagCode = CommonTools.int2Byte(1001);
            CommonTools.packageByteSecurity(flagCode);
            outputStream.write(flagCode);

            byte[] successBuffer = new byte[4];
            inputStream.read(successBuffer);
            CommonTools.unPackageByteSecurity(successBuffer);
//            try {
//                Thread.sleep(500);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
            if (CommonTools.bytes2Int(successBuffer) == 1001) {
                byte[] messageLengthBuffer = new byte[4];
                inputStream.read(messageLengthBuffer);
                CommonTools.unPackageByteSecurity(messageLengthBuffer);
                int messageLength = CommonTools.bytes2Int(messageLengthBuffer);
                byte[] messageBuffer = new byte[messageLength];
                inputStream.read(messageBuffer);
                CommonTools.unPackageByteSecurity(messageBuffer);
                message = new String(messageBuffer);
                JSONArray jsonArray = new JSONArray(message);
                for (int i=0; i < jsonArray.length(); i++) {
                    String oneDeviceInfo = jsonArray.getString(i);
                    JSONObject jsonObject = new JSONObject(oneDeviceInfo);
                    String name = jsonObject.getString("name");
                    String ip = jsonObject.getString("ip");
                    int port = jsonObject.getInt("port");
                    OtherHost otherHost = new OtherHost(name, ip, port);
                    otherHostList.add(otherHost);
                    Log.i("JsonData" + i, "name" + name + ";ip" + ip + ";port" + port);
                }
                // 表明接收成功
                outputStream.write(flagCode);
                return otherHostList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            CommonDynamicData.IS_TIME_OUT = true;
            Log.e("SocketTimeoutException getConnectDevices", "网络超时");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getConnectDevices与服务器交流IO出错");
        }finally {

        }
        return null;
    }

    // 发送文本消息
    public static boolean sendTextMessage(OutputStream outputStream, InputStream inputStream, String securityMD5Text, String clientName){
        try {
            byte [] flagCode = CommonTools.int2Byte(1011);
            CommonTools.packageByteSecurity(flagCode);
            outputStream.write(flagCode);
            JSONObject  jsonObject = new JSONObject();
            jsonObject.put("client_name", clientName);
            jsonObject.put("security_md5_text", securityMD5Text);

            byte [] sendData = jsonObject.toString().getBytes("UTF-8");
            CommonTools.packageByteSecurity(sendData);
            byte [] sendDataLength = CommonTools.int2Byte(sendData.length);
            CommonTools.packageByteSecurity(sendDataLength);

            outputStream.write(sendDataLength);
            outputStream.write(sendData);

            byte [] successBuffer = new byte[4];
            inputStream.read(successBuffer);
            CommonTools.unPackageByteSecurity(successBuffer);
            if (CommonTools.bytes2Int(successBuffer) == 1011) {
                return true;
            }
        } catch (SocketTimeoutException e) {
            CommonDynamicData.IS_TIME_OUT = true;
            Log.e("SocketTimeoutException sendTextMessage", "网络超时");
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendTextMessage 与服务器交流IO出错");
        }finally {

        }
        return false;
    }

    //被动接收文本信息
    public static TextMessageSerializable getTextMessage(OutputStream outputStream, InputStream inputStream) {
        try {
            byte[] successBuffer = new byte[4];
            inputStream.read(successBuffer);
            CommonTools.unPackageByteSecurity(successBuffer);
            if (CommonTools.bytes2Int(successBuffer) == 1011) {
                byte[] messageLengthBuffer = new byte[4];
                inputStream.read(messageLengthBuffer);
                CommonTools.unPackageByteSecurity(messageLengthBuffer);
                int messageLength = CommonTools.bytes2Int(messageLengthBuffer);
                byte[] messageBuffer = new byte[messageLength];
                inputStream.read(messageBuffer);
                CommonTools.unPackageByteSecurity(messageBuffer);
                String textMessage = new String(messageBuffer);
                Log.i(TAG, textMessage);
                return StringTools.getDataFromJSONObject(textMessage);
            }
        } catch (SocketTimeoutException e) {
            CommonDynamicData.IS_TIME_OUT = true;
            Log.e("SocketTimeoutException sendTextMessage", "网络超时");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "sendTextMessage 与服务器交流IO出错");
        }finally {

        }
        return null;
    }

    // 发送文件
    public static boolean SendFile(String filePath, String aimDevice, OutputStream outputStream, InputStream inputStream){
        long startTime = System.currentTimeMillis();
        try {
            JSONObject jsonObject = FileTools.sendFileJsonMessage(filePath, aimDevice);

            byte [] flagCode = CommonTools.int2Byte(1100);
            CommonTools.packageByteSecurity(flagCode);
            outputStream.write(flagCode);

            byte [] confirmBuffer = new byte[4];
            inputStream.read(confirmBuffer);
            CommonTools.unPackageByteSecurity(confirmBuffer);
            if (CommonTools.bytes2Int(confirmBuffer) == 91100) {
                byte [] sendData = jsonObject.toString().getBytes("UTF-8");
                CommonTools.packageByteSecurity(sendData);
                byte [] sendDataLength = CommonTools.int2Byte(sendData.length);
                CommonTools.packageByteSecurity(sendDataLength);

                outputStream.write(sendDataLength);
                outputStream.write(sendData);

                byte [] successBuffer = new byte[4];
                inputStream.read(successBuffer);
                CommonTools.unPackageByteSecurity(successBuffer);
                if (CommonTools.bytes2Int(successBuffer) == 91100) {
                    Log.i("SocketHelp", "文件在云端存储成功,一共耗时" + (System.currentTimeMillis() - startTime) / 1000);
                    return true;
                }
            }else {
                return false;
            }
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "与服务器交流出错");
        } finally {
        }
        return false;
    }

    private static byte [] packageDataRecv(InputStream inputStream) throws IOException {
        byte[] headData = new byte[1028];
        inputStream.read(headData);
        CommonTools.unPackageByteSecurity(headData);
        return headData;
    }

// 典韦 刘婵 刘备 小乔 大桥 钟馗 李元芳 老夫子 沈梦西 百里守约 娜可露露 明世隐 嫦娥

    // 文件接收
    public static JSONObject ReceiveFile(OutputStream outputStream, InputStream inputStream) {
        try {
            byte [] flagCode = CommonTools.int2Byte(91100);
            CommonTools.packageByteSecurity(flagCode);
            outputStream.write(flagCode);

            byte[] headLength = new byte[4];
            inputStream.read(headLength);
            CommonTools.unPackageByteSecurity(headLength);
            int headLen = CommonTools.bytes2Int(headLength);

            byte[] headData = new byte[headLen];
            inputStream.read(headData);
            CommonTools.unPackageByteSecurity(headData);

            String headMessage = new String(headData);
            JSONObject jsonObject = new JSONObject(headMessage);

            String filename = jsonObject.getString("filename");
            long filesize = jsonObject.getLong("filesize");
            String md5 = jsonObject.getString("md5");

            return jsonObject;

//            FileInformation fileInformation = new FileInformation();
//            fileInformation.setFilename(filename);
//            fileInformation.setFilesize(filesize);
//
//            String nowMD5 = SecurityEncryption.getFileMD5(savePath);
//            if (nowMD5.equals(md5)) {
//                Log.i("size的大学是", ":" + allSize + ":" + filesize);
//                Log.i("文件是否是成功接收，如果是8就是成功接收", "XXXX:" + filename);
//            }
        }catch (Exception e) {
            Log.e("文件接收出现错误", e.getMessage());
        }finally {
        }
        return null;
    }
}