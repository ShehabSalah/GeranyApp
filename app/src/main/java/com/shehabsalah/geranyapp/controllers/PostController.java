package com.shehabsalah.geranyapp.controllers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.model.Post;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ShehabSalah on 8/12/17.
 * PostController class responsible on the communication between server, data model and views.
 * This class contain the business logic of (Posts) tab and its main functionality.
 */

public abstract class PostController {
    private Context context;
    private ArrayList<Post> posts;
    private FirebaseDatabase database;
    private DatabaseReference postRef;


    public PostController(Context context) {
        this.context = context;
        posts = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        postRef = database.getReference(Config.DB_POSTS);
        postRef.keepSynced(true);
    }

    public void addNewPost(String postText, String category, String place, String postImage, User user){

        String currentDate = getCurrentDate();
        //date will calculate here
        //image will convert and sent from here also
        Post post = new Post(user.getProfileDisplayName(), user.getProfileUid(), user.getProfilePhotoUrl(), place, postText, postImage, currentDate, 0, 0, 0, category);
        addPostToServer(post);
    }
    public void updateNewPost(Post post){
        postRef.child(post.getDate()).setValue(post);
    }


    public String getCurrentDate(){
        Date d = new Date();
        CharSequence dateSeq = DateFormat.format("yyyy-MM-dd-hh-mm-ss", d.getTime());
        return dateSeq.toString();
    }

    public String getPath(Uri uri) {
        String res = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor!=null && cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    private void addPostToServer(Post post){
        postRef.child(post.getDate()).setValue(post);
        posts.add(0,post);
        onLoadFinish();
    }

    /**
     * This method allows to delete post at given position.
     * @param context the application context.
     * @param position post index that want to delete
     * @return boolean contain true if the post deleted, false if the post doesn't deleted.
     * */
    public boolean deletePostAtPosition(Context context, int position){
        if (Config.isNetworkConnected(context)){
            Post post = posts.get(position);
            postRef.child(post.getDate()).removeValue();
            posts.remove(position);
            return true;
        }else{
            return false;
        }
    }

    public void getHomePostsList(final String activePlace){
        Query query = postRef.orderByChild(Config.LOCATION)
                .equalTo(activePlace);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                posts = new ArrayList<>();
                for (DataSnapshot postSnap: snapshot.getChildren()) {
                    Post post = postSnap.getValue(Post.class);
                    posts.add(0,post);
                }
                onLoadFinish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onLoadFinish();
            }
        });
    }

    public void getProfilePostsList(User userProfile){
        Query query = postRef.orderByChild(Config.USER_ID2)
                .equalTo(userProfile.getProfileUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                posts = new ArrayList<>();
                for (DataSnapshot postSnap: snapshot.getChildren()) {
                    Post post = postSnap.getValue(Post.class);
                    posts.add(0,post);
                }
                onLoadFinish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                onLoadFinish();
            }
        });
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public abstract void onLoadFinish();

}
