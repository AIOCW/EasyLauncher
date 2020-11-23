package com.aiocw.aihome.easylauncher.extendfun.entity;

import java.io.Serializable;

public class NetSettingSerializable implements Serializable {
    private String deviceName;
    private String serverIp;
    private String remoteServerIp;
    private int serverPort;

    public NetSettingSerializable() {

    }

    public NetSettingSerializable(String deviceName, String serverIp, String remoteServerIp, int serverPort) {
        this.deviceName = deviceName;
        this.serverIp = serverIp;
        this.remoteServerIp = remoteServerIp;
        this.serverPort = serverPort;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getRemoteServerIp() {
        return remoteServerIp;
    }

    public void setRemoteServerIp(String remoteServerIp) {
        this.remoteServerIp = remoteServerIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
