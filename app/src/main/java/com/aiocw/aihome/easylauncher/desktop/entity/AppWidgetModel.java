package com.aiocw.aihome.easylauncher.desktop.entity;

public class AppWidgetModel {
    private int id;
    private int appWidgetId;

    public AppWidgetModel() {

    }

    public AppWidgetModel(int appWidgetId) {
        this.appWidgetId = appWidgetId;
    }

    public AppWidgetModel(int id, int appWidgetId) {
        this.id = id;
        this.appWidgetId = appWidgetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppWidgetId() {
        return appWidgetId;
    }

    public void setAppWidgetId(int appWidgetId) {
        this.appWidgetId = appWidgetId;
    }
}
