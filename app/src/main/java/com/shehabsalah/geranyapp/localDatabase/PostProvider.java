package com.shehabsalah.geranyapp.localDatabase;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.shehabsalah.geranyapp.util.Config;

/**
 * Created by ShehabSalah on 8/29/17.
 *
 */

public class PostProvider extends ContentProvider {

    private SQLiteDatabase db;
    //Post Uri Number
    private static final int TYPE_POST = 0;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //Match each previous number with the corresponding URI
    static {
        URI_MATCHER.addURI(Config.AUTHORITY, PostModel.TABLE_NAME, TYPE_POST);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(@Nullable Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Switch the coming Uri with its corresponding number
        int match = URI_MATCHER.match(uri);
        //Return the cursor result based on the uri number
        switch (match) {
            case TYPE_POST:
                return db.query(
                        PostModel.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
    @Override
    public String getType(@Nullable Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case TYPE_POST:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + PostModel.TABLE_NAME;
        }
        return null;
    }
    @Override
    public Uri insert(@Nullable Uri uri, ContentValues values) {
        //Switch the coming Uri with its corresponding number
        int match = URI_MATCHER.match(uri);
        //Database row id
        long rowID = 0;
        //This Uri is changed based on the inserted Database Table
        Uri _uri = null;
        switch (match) {
            case TYPE_POST:
                rowID = db.insert(PostModel.TABLE_NAME, "", values);
                _uri = PostModel.CONTENT_URI;
                break;
        }
        if (rowID > 0) {
            Uri _uri2 = ContentUris.withAppendedId(_uri, rowID);
            getContext().getContentResolver().notifyChange(_uri2, null);
            return _uri;
        } else {
            return null;
        }
    }
    @Override
    public int delete(@Nullable Uri uri, String selection, String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int count = 0;
        switch (match) {
            case TYPE_POST:
                count = db.delete(PostModel.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int update(@Nullable Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int match = URI_MATCHER.match(uri);
        int count = 0;
        switch (match) {
            case TYPE_POST:
                count = db.update(PostModel.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
