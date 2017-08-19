package com.shehabsalah.geranyapp.controllers;

import android.content.Context;
import android.net.Uri;

import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;

/**
 * Created by ShehabSalah on 8/12/17.
 * PostController class responsible on the communication between server, data model and views.
 * This class contain the business logic of (Posts) tab and its main functionality.
 */

public class PostController {

    public void addNewPost(Context context, String postText, String category, String place, Uri postImage, User user){
        //image uri may come with null value, check on it here
        //ToDo: implement upload post (IMPLEMENTATION: #1)
        Config.toastShort(context, "Post Will be Uploaded");
    }
}
