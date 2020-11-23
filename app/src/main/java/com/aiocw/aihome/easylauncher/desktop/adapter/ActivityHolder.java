package com.aiocw.aihome.easylauncher.desktop.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aiocw.aihome.easylauncher.R;
import com.aiocw.aihome.easylauncher.common.tools.imagetools.ImageTools;
import com.aiocw.aihome.easylauncher.common.tools.toolsdb.DBCommonData;
import com.aiocw.aihome.easylauncher.desktop.adapter.BottomRecyclerViewAdapter;
import com.aiocw.aihome.easylauncher.desktop.entity.App;
import com.aiocw.aihome.easylauncher.desktop.tools.AppDataDB;
import com.aiocw.aihome.easylauncher.desktop.tools.AppInstallerAndRemove;
