package com.shehabsalah.geranyapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by ShehabSalah on 7/29/17.
 * This class contain common resources used in the application
 */

public class Config {

    //Fragments
    public static final String ADD_NEW_PLACE_FRAGMENT       = "addNewPlaceFragment";
    public static final String MY_NEW_PLACES_LIST           = "myNewPlacesList";

    //Extra
    public static final String MY_PLACES_EXTRA              = "extra_places";
    public static final String CATEGORIES_EXTRA             = "extra_categories";
    public static final String USER_INFO                    = "extra_user_info";
    public static final String LOGOUT_EXTRA                 = "logout_extra";
    public static final String USER_HAS_EMAIL               = "user_has_email";
    public static final String USER_HAS_NUMBER              = "user_has_number";


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
