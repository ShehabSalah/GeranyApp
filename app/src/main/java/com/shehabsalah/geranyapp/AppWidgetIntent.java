package com.shehabsalah.geranyapp;


import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ShehabSalah on 8/28/17.
 *
 */

public class AppWidgetIntent extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}