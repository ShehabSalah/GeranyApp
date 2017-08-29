package com.shehabsalah.geranyapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by ShehabSalah on 7/29/17.
 * This class contain common resources used in the application
 */

public class Config {

    //Fragments
    public static final String ADD_NEW_PLACE_FRAGMENT       = "addNewPlaceFragment";
    public static final String MY_NEW_PLACES_LIST           = "myNewPlacesList";
    public static final String POST_FRAGMENT                = "postFragment";

    //Extra
    public static final String MY_PLACES_EXTRA              = "extra_places";
    public static final String CATEGORIES_EXTRA             = "extra_categories";
    public static final String USER_INFO                    = "extra_user_info";
    public static final String PLACES_EXTRA                 = "places_extra";
    public static final String USER_HAS_EMAIL               = "user_has_email";
    public static final String USER_HAS_NUMBER              = "user_has_number";
    public static final String POST_ID_EXTRA                = "post_id";
    public static final String FAKE_IMAGE                   = "http://www.platformhouse.com/fack_image.jpg";
    public static final String ACTIVE_PLACE                 = "active_place";

    //Database roots
    public static final String DB_POSTS                     = "posts";
    public static final String DB_CATEGORIES                = "categories";
    public static final String DB_USERS                     = "users";
    public static final String DB_PLACES                    = "places";
    public static final String DB_DISLIKES                  = "dislikes";
    public static final String DB_VOLUNTEER                 = "volunteer";
    public static final String DB_REPORT_POST               = "post_reports";
    public static final String DB_REPORT_FEEDBACK           = "feedback_reports";
    public static final String DB_FEEDBACK                  = "feedback";
    public static final String DB_DONATIONS                 = "donations";

    //Database Childs
    public static final String PROFILE_ID                   = "profileUid";
    public static final String USER_ID                      = "userId";
    public static final String USER_ID2                     = "user_id";
    public static final String LOCATION                     = "location";
    public static final String CATEGORY                     = "categoryId";

    public static final String DEFAULT_PLACE_NAME           = "Default Place";

    //API Links
    public static final String SCHEME                       = "http";
    public static final String BASE_URL                     = "www.platformhouse.com";
    public static final String LOCATION_URL                 = "/location";
    public static final String FIND_LOCATION                = "finder.php";

    //Params
    public static final String LAT                          = "lat";
    public static final String LON                          = "lon";

    //Posts Indicators
    public static final int HOME_POSTS                      = 0;
    public static final int PROFILE_POSTS                   = 1;

    //Collaborators Section number
    public static final int FEEDBACK_SECTION                = 1;
    public static final int VOLUNTEER_SECTION               = 2;
    public static final int DONATION_SECTION                = 3;

    //Collaborators Fragment Indicators
    public static final int FEEDBACK_INDICATOR              = 0;
    public static final int VOLUNTEER_INDICATOR             = 1;
    public static final int DONATION_INDICATOR              = 2;

    //Collaborators section name
    public static final String FEEDBACK_NAME                = "Feedback";
    public static final String VOLUNTEERS_NAME              = "Volunteers";
    public static final String DONATION_NAME                = "Donation";

    //Providers
    public static final String AUTHORITY            = "com.shehabsalah.geranyapp";
    public static final Uri CONTENT_URI_BASE        = Uri.parse("content://" + AUTHORITY);

    /**
     * This method used to display short toasts.
     * @param context The application context
     * @param message Message to display
     * */
    public static void toastShort(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    /**
     * This method used to display long toasts.
     * @param context The application context
     * @param message Message to display
     * */
    public static void toastLong(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    /***
     * This method responsible on checking if the device connected with the internet or not.
     * @param context The application context.
     * @return (True) if the internet connected and (False) if the internet not connected.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
