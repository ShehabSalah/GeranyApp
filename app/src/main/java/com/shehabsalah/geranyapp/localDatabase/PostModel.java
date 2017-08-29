package com.shehabsalah.geranyapp.localDatabase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.shehabsalah.geranyapp.model.Post;
import com.shehabsalah.geranyapp.util.Config;

import java.util.ArrayList;

/**
 * Created by ShehabSalah on 8/29/17.
 *
 */

public class PostModel {
    public static final String TABLE_NAME = "post";
    //General Content URI
    public static final Uri CONTENT_URI = Config.CONTENT_URI_BASE.buildUpon().appendPath(TABLE_NAME).build();
    //Table Columns
    public static final String COL_USERNAME         = "username";
    public static final String COL_PROFILE_PIC      = "profile_picture";
    public static final String COL_TEXT             = "text";
    public static final String COL_LOCATION         = "location";

    public static void insert(Context context, ArrayList<Post> items) {
        PostContentValues recipeContentValues = new PostContentValues();
        for (int i = 0; i < items.size(); i++) {
            context.getContentResolver().insert(
                    CONTENT_URI,
                    recipeContentValues.contentValues_NewPost(items.get(i).getUsername(), items.get(i).getProfile_picture(), items.get(i).getText(), items.get(i).getLocation())
            );
            Log.e("gerany_db", items.get(i).getText());
        }
    }

    public static void delete(Context context){
        context.getContentResolver().delete(CONTENT_URI,null,null);
    }
}
