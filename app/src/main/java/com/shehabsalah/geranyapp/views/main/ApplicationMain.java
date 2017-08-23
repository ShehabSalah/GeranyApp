package com.shehabsalah.geranyapp.views.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.R;
import com.firebase.ui.auth.AuthUI;
import com.shehabsalah.geranyapp.controllers.CategoriesController;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.activities.AddUserInfoActivity;

import java.util.Arrays;

/**
 * Created by ShehabSalah on 7/26/17.
 * This class contain the main configurations of the parent of Activities
 */

public class ApplicationMain extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private final String LOG_TAG = ApplicationMain.class.getSimpleName();
    protected static int RC_SIGN_IN = 0;
    protected FirebaseAuth mAuth;
    protected FirebaseDatabase database;
    protected DatabaseReference userRef;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected User user;
    private boolean isGoogle = false;
    protected MyPlacesController myPlacesController;
    protected CategoriesController categoriesController;
    private final int LOCATION_PERMISSION = 9999;

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
                    getUserDataFromDB(getUserInfo());
                    UserInfo userInfo = user.getProviderData().get(1);
                    if (userInfo.getProviderId().contains("book")){
                        isGoogle = false;
                    }


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
        if (resultCode != ResultCodes.OK) {
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
    private void userInfoNeeded(){
        boolean isEmailExist = true;
        boolean isNumberExist = true;
        if (user.getProfileEmail() == null)
            isEmailExist = false;
        if (user.getPhoneNumber() == null)
            isNumberExist = false;

        Intent intent = new Intent(this, AddUserInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Config.USER_HAS_EMAIL, isEmailExist);
        intent.putExtra(Config.USER_HAS_NUMBER, isNumberExist);
        intent.putExtra(Config.USER_INFO, user);
        startActivity(intent);
    }

    /**
     * This method add the user info to the Database
     * @param profile social network profile
     * */
    private void addUserToDB(UserInfo profile){
        String userImage = null;
        if (isGoogle){
            userImage = profile.getPhotoUrl().toString().replace("/s96-c/","/s300-c/");
        }

        user = new User(profile.getDisplayName(), profile.getProviderId(),profile.getEmail(),
                profile.getUid(), userImage, null);
        userRef.child(profile.getUid()).setValue(user);
        //userRef.child(Config.DB_USERS).setValue(user);
        if (user.getProfileEmail() == null || user.getPhoneNumber() == null){
            userInfoNeeded();
        }
    }

    /**
     * This method check if the user exists in the Database or not. If exists will set the user info
     * in the user object. If not will add the user into the database.
     * @param profile profile id
     * */
    protected void getUserDataFromDB(final UserInfo profile){
        final String profileUid = profile.getUid();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Config.DB_USERS);
        userRef.keepSynced(true);
        if (Config.isNetworkConnected(getApplicationContext())){
            Query query = userRef.orderByChild(Config.PROFILE_ID).equalTo(profileUid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(profileUid)) {
                        for (DataSnapshot userSnap: snapshot.getChildren()) {
                            User userTemp = userSnap.getValue(User.class);

                            if (userTemp.getProfileUid().equals(profileUid)){
                                user = userTemp;
                                if (userTemp.getProfileEmail() == null || userTemp.getPhoneNumber() == null){
                                    userInfoNeeded();
                                }
                            }
                        }
                    }else{
                        addUserToDB(profile);
                    }
                    requestLocationPermission();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(LOG_TAG, "Failed to read value.", databaseError.toException());
                }
            });
        }else{
            ((Callback) ApplicationMain.this).onLoadFinish(false);
        }
    }


    public interface Callback {
        /**
         * This method called from getUserDataFromDB method after checking the user information.
         * */
        void onLoadFinish(boolean state);
    }


    protected void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        } else{
            myPlacesController = new MyPlacesController(user,getApplicationContext()){
                @Override
                public void myPlacesLoadFinish(String location, boolean state) {
                    if (state){
                        //ToDo: load the categories here and move the callback to the categories
                        ((Callback) ApplicationMain.this).onLoadFinish(true);
                    }
                }
            };
            myPlacesController.fillPlaces();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myPlacesController = new MyPlacesController(user, getApplicationContext()) {
                @Override
                public void myPlacesLoadFinish(String location, boolean state) {
                    if (state){
                        //ToDo: load the categories here and move the callback to the categories
                        ((Callback) ApplicationMain.this).onLoadFinish(true);
                    }
                }
            };
            myPlacesController.fillPlaces();
        }
    }
}
