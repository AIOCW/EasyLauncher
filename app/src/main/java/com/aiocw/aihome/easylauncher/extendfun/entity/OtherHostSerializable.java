package com.aiocw.aihome.easylauncher.extendfun.entity;

import java.io.Serializable;
import java.util.List;

public class OtherHostSerializable implements Serializable {
    private List<OtherHost> otherHostList;

    public OtherHostSerializable() {

    }

    public OtherHostSerializable(List<OtherHost> otherHostList) {
        this.otherHostList = otherHostList;
    }

    public List<OtherHost> getOtherHostList() {
        return otherHostList;
    }

    public void setOtherHostList(List<OtherHost> otherHostList) {
        this.otherHostList = otherHostList;
    }
}
