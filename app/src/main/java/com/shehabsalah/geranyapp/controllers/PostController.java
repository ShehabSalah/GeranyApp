package com.shehabsalah.geranyapp.controllers;

import android.content.Context;
import android.net.Uri;

import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;

/**
 * Created by ShehabSalah on 8/12/17.
 *
 */

public class PostController {

    public void addNewPost(Context context, String postText, String category, String place, Uri postImage, User user){
        //image uri may come with null value, check on it here
        //ToDo: implement upload post (IMPLEMENTATION: #1)
        Config.toastShort(context, "Post Will be Uploaded");
    }
}
