package com.shehabsalah.geranyapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.localDatabase.PostModel;
import com.shehabsalah.geranyapp.model.Post;
import com.shehabsalah.geranyapp.util.Config;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ShehabSalah on 8/29/17.
 *
 */

public class GetWidgetData {
    private Cursor postCursor;

    private ArrayList<Post> getArrayList(Context context) {
        ArrayList<Post> postHolders = new ArrayList<>();
        if (postCursor != null) postCursor.close();
        postCursor = context.getContentResolver().query(
                PostModel.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (postCursor != null && postCursor.moveToFirst()){
            do {
                String postText = postCursor.getString(postCursor.getColumnIndex(PostModel.COL_TEXT));
                String profilePic = postCursor.getString(postCursor.getColumnIndex(PostModel.COL_PROFILE_PIC));
                String username = postCursor.getString(postCursor.getColumnIndex(PostModel.COL_USERNAME));
                String activeLocation = postCursor.getString(postCursor.getColumnIndex(PostModel.COL_LOCATION));
                Log.e("geranyapp", postText);
                postHolders.add(new Post(username, null, profilePic, activeLocation, postText, null, null,0,0,0,null));
            }while (postCursor.moveToNext());
            postCursor.close();
        }else
            return null;
        return postHolders;
    }

    public ArrayList<RemoteViews> getRemoteViews(final Context context){

        ArrayList<Post> posts = getArrayList(context);
        Log.e("posts size: ", posts.size()+"");
        ArrayList<RemoteViews> remoteViewsArrayList = new ArrayList<>();

        if (posts != null){
            for (int position = 0; position < posts.size(); position++){
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
                remoteViews.removeAllViews(R.id.parent_layout);

                RemoteViews listView = new RemoteViews(context.getPackageName(), R.layout.app_widget_small);
                listView.setTextViewText(R.id.post_text, posts.get(position).getText());
                listView.setTextViewText(R.id.username, posts.get(position).getUsername());
                Log.v("geranyapp", posts.get(position).getText());
                try {
                    URL url = new URL(posts.get(position).getProfile_picture());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    listView.setImageViewBitmap(R.id.profile_picture, bmp);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Intent appIntent = new Intent();
                remoteViews.setOnClickFillInIntent(R.id.parent_layout, appIntent);
                remoteViews.setOnClickFillInIntent(R.id.parent_layout, appIntent);
                remoteViews.addView(R.id.parent_layout, listView);
                remoteViewsArrayList.add(remoteViews);
            }
            return remoteViewsArrayList;
        }
        return null;
    }
}
