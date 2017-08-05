package com.shehabsalah.geranyapp.views.main;

import android.net.Uri;
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
import com.shehabsalah.geranyapp.util.Config;

import java.util.Arrays;

/**
 * Created by ShehabSalah on 7/26/17.
 * This class contain the main configurations of the parent of Activities
 */

public class ApplicationMain extends AppCompatActivity {
    private final String TAG = ApplicationMain.class.getSimpleName();
    protected static int RC_SIGN_IN = 0;
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected UserInfo userInfo;


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
                    // Even a user's provider-specific profile information
                    // only reveals basic information
                        UserInfo profile = user.getProviderData().get(0);
                        // Id of the provider (ex: google.com)
                        String providerId = profile.getProviderId();
                        // UID specific to the provider
                        String profileUid = profile.getUid();
                        // Name, email address, and profile photo Url
                        String profileDisplayName = profile.getDisplayName();
                        String profileEmail = profile.getEmail();
                        Uri profilePhotoUrl = profile.getPhotoUrl();
                        Log.v("LOGIN",providerId);
                        Log.v("LOGIN",profileUid);
                        Log.v("LOGIN",profileDisplayName);
                        if (profileEmail!=null)
                            Log.v("LOGIN",profileEmail);
                        Log.v("LOGIN",profilePhotoUrl.toString());
                        userInfo = profile;

                    Log.d("LOGIN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("mesh shebo", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
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
                Log.v("LOGIN","SIGNIN_CANCELED");
                finish();
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                Log.v("LOGIN","NO_NETWORK");
                Config.toastLong(this, getString(R.string.no_internet));
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Log.v("LOGIN","UNKNOWN_ERROR");
                Config.toastLong(this, getString(R.string.unknown_error));
            }
        }

    }

}
