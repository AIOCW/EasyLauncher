package com.aiocw.aihome.easylauncher.extendfun.entity;

public class FolderFileAttribute {
    private String name;
    private String kind;
    private String modifyTime;
    private String author;

    public FolderFileAttribute(String name, String kind, String modifyTime, String author) {
        this.name = name;
        this.kind = kind;
        this.modifyTime = modifyTime;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
