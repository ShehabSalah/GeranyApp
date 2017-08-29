package com.shehabsalah.geranyapp;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shehabsalah.geranyapp.controllers.GetWidgetData;

import java.util.ArrayList;

/**
 * Created by ShehabSalah on 8/29/17.
 *
 */


class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private GetWidgetData getWidgetData;
    private ArrayList<RemoteViews> remoteViewsArrayList;

    ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        getWidgetData = new GetWidgetData();
        remoteViewsArrayList = getWidgetData.getRemoteViews(context);
    }

    @Override
    public void onDestroy() {
        getWidgetData = null;
        remoteViewsArrayList = null;
    }

    @Override
    public int getCount() {
        if (remoteViewsArrayList == null) return 0;
        return remoteViewsArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (remoteViewsArrayList == null) return null;
        return remoteViewsArrayList.get(position);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
