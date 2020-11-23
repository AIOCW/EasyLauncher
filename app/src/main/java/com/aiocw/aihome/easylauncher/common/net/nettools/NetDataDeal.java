package com.aiocw.aihome.easylauncher.common.net.nettools;

import com.aiocw.aihome.easylauncher.common.CommonTools;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class NetDataDeal {
    public static void packageDataSend(byte[] bufferFast, int dataNumber, OutputStream outputStream) throws IOException {
        byte [] sendPackageBuffer = Arrays.copyOf(bufferFast, 1028);
        byte [] dataNumberBuffer = CommonTools.int2Byte(dataNumber);
        sendPackageBuffer[1024] = dataNumberBuffer[0];
        sendPackageBuffer[1025] = dataNumberBuffer[1];
        sendPackageBuffer[1026] = dataNumberBuffer[2];
        sendPackageBuffer[1027] = dataNumberBuffer[3];
        CommonTools.packageByteSecurity(sendPackageBuffer);
//        Log.i("DataNumberBufferData", dataNumberBuffer[0] + dataNumberBuffer[1] + dataNumberBuffer[2] + dataNumberBuffer[3] +"====DataLen" + sendPackageBuffer.length);
        outputStream.write(sendPackageBuffer);
        outputStream.flush();
    }
}
