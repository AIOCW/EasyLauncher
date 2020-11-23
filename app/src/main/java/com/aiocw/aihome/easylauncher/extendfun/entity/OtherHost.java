package com.aiocw.aihome.easylauncher.extendfun.entity;

public class OtherHost {
    private int id;
    private String hostName;
    private String hostIp;
    private int hostPort;


    public OtherHost() {

    }

    public OtherHost(String hostName, String hostIp, int hostPort) {
        this.hostName = hostName;
        this.hostIp = hostIp;
        this.hostPort = hostPort;
    }

    public OtherHost(int id, String  hostName, String hostIp,int hostPort) {
        this.id = id;
        this.hostName = hostName;
        this.hostIp = hostIp;
        this.hostPort = hostPort;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }
}
