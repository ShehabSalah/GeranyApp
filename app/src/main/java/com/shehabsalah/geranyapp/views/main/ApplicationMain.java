package com.shehabsalah.geranyapp.views.main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.shehabsalah.geranyapp.R;
import com.firebase.ui.auth.AuthUI;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;

import java.util.Arrays;

/**
 * Created by ShehabSalah on 7/26/17.
 * This class contain the main configurations of the parent of Activities
 */

public class ApplicationMain extends AppCompatActivity {
    private final String LOG_TAG = ApplicationMain.class.getSimpleName();
    protected static int RC_SIGN_IN = 0;
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected User user;

    /**
     * This method check if the user exists or not
     * @return true if the user exists and false if the user not exist
     * */
    protected boolean isNotLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user==null;
    }
    /**
     * This method responsible on creating the login layout of the Login page.
     * Login page support Facebook & google authentications only.
     * */
    protected void signIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .setLogo(R.mipmap.gerany_logo_png)
                        .setTheme(R.style.LoginStyle)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * This method is connecting tha application with Firebase Authentication
     * */
    protected void setFirebaseAuthListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Get the user basic information
                    getUserData(getUserInfo());

                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    /**
     * This method check if the user exists in the DB or its first time to login.
     * If the user exists? get the user info from the DB. If not, get the user info
     * from the social network and then check if the all the user data needed exists.
     * @param profile social network profile
     * */
    private void getUserData(UserInfo profile){
        boolean userDataAvailability = getUserDataFromDB(profile.getUid());
        if (!userDataAvailability){
            addUserToDB(profile);
        }
        checkUserInfo();
        getUserSettings();
    }

    private void getUserSettings(){
        //ToDo: get the user information from firebase server and fill the settings (IMPLEMENTATION #1)
        //the next lines is temporary code to be replaced with firebase connection
        user.setAllowDisplayingEmail(true);
        user.setAllowDisplayingMobileNumber(true);
    }


    /**
     * getUserInfo is getting the current connecting user information
     * @return UserInfo Object which contain the basic information of the user
     * */
    protected UserInfo getUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user!=null?user.getProviderData().get(0):null;
    }

    /**
     * This method responsible on handling the sign in states such as: sign in cancellation,
     * no network to sign in and unknown error happen. Each state will produce different message
     * to the user.
     * @param response The sign in response to identify the state
     * @param resultCode The onActivityResult passed code to make sure that sign in doesn't succeeded
     * */
    protected void handleSignInStates(int resultCode, IdpResponse response){
        if (resultCode == ResultCodes.OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Do Nothing
            } else {
                // User is signed out of Firebase
            }
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                Log.v(LOG_TAG,"SIGNIN_CANCELED");
                finish();
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                Log.v(LOG_TAG,"NO_NETWORK");
                Config.toastLong(this, getString(R.string.no_internet));
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Log.v(LOG_TAG,"UNKNOWN_ERROR");
                Config.toastLong(this, getString(R.string.unknown_error));
            }
        }

    }

    /**
     * This method check if the user number and email is exists or not.
     * If not the application will redirect the user to another screen to add his email or his
     * mobile number.
     * */
    private void checkUserInfo(){
        //ToDo: check on the user email and mobile number is null or not
    }

    /**
     * This method add the user info to the Database
     * @param profile social network profile
     * */
    private void addUserToDB(UserInfo profile){
        user = new User(profile.getDisplayName(), profile.getProviderId(),profile.getEmail(),
                profile.getUid(), profile.getPhotoUrl().toString(), null);
        //ToDo: add the user info to the database
        //ToDo: add the default settings to the database
    }

    /**
     * This method check if the user exists in the Database of not. If exists will set the user info
     * in the user object then return true. If not will return false.
     * @param profileUid profile id
     * @return boolean that indicate that if the user exists or not
     * */
    private boolean getUserDataFromDB(String profileUid){
        //ToDo: check if the user exists in the Database or not using the profile id
        return false;
    }

}
