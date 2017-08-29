package com.shehabsalah.geranyapp.localDatabase;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ShehabSalah on 8/29/17.
 *
 */

public class PostContentValues {
    public ContentValues contentValues_NewPost(@NonNull String username, @Nullable String profile_picture, @NonNull String text, @NonNull String location) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PostModel.COL_USERNAME,    username);
        contentValues.put(PostModel.COL_PROFILE_PIC, profile_picture);
        contentValues.put(PostModel.COL_TEXT,        text);
        contentValues.put(PostModel.COL_LOCATION,    location);
        return contentValues;
    }
}
