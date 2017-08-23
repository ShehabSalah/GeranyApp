package com.shehabsalah.geranyapp.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.CategoriesController;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.views.fragments.AddNewLocationDialogFragment;
import com.shehabsalah.geranyapp.views.fragments.AddNewLocationFragment;
import com.shehabsalah.geranyapp.views.fragments.MyPlacesListFragment;
import com.shehabsalah.geranyapp.views.fragments.PostFragment;
import com.shehabsalah.geranyapp.views.main.ApplicationMain;
import com.shehabsalah.geranyapp.util.Config;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ApplicationMain implements AddNewLocationDialogFragment.Callback, ApplicationMain.Callback{
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.home_container)
    FrameLayout homeFrame;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.signal_off)
    ImageView signalOff;
    @BindView(R.id.signal_off_message)
    TextView signalOffMessage;


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean firstTime = true;
    AddNewLocationFragment addNewLocationFragment;
    PostFragment postFragment;
    MyPlacesListFragment myPlacesListFragment;
    CategoriesController categoriesController;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle(getString(R.string.title_home));
                    postFragment = new PostFragment();
                    postFragment.setExtra(user, myPlacesController.getActivePlace().getPlaceAddress());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_container, postFragment, Config.POST_FRAGMENT)
                            .commit();
                    return true;
                case R.id.navigation_places:
                    setTitle(getString(R.string.title_places));
                    myPlacesListFragment = new MyPlacesListFragment();
                    myPlacesListFragment.setMyPlaces(myPlacesController, user);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_container, myPlacesListFragment, Config.MY_NEW_PLACES_LIST)
                            .commit();
                    return true;
                case R.id.navigation_post:
                    Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                    intent.putExtra(Config.MY_PLACES_EXTRA, myPlacesController.getActivePlace());
                    intent.putExtra(Config.USER_INFO, user);
                    intent.putParcelableArrayListExtra(Config.CATEGORIES_EXTRA, categoriesController.getCategories());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                    return false;
                case R.id.navigation_add_place:
                    addNewLocationFragment = new AddNewLocationFragment();
                    addNewLocationFragment.setMyPlaces(myPlacesController, user);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_container, addNewLocationFragment, Config.ADD_NEW_PLACE_FRAGMENT)
                            .commit();
                    setTitle(getString(R.string.title_add_place));
                    return true;
                case R.id.navigation_profile:
                    Intent profile_intent = new Intent(MainActivity.this, ProfileActivity.class);
                    profile_intent.putExtra(Config.USER_INFO, user);
                    startActivity(profile_intent);
                    return false;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        //Initialize the FirebaseAuth instance and the AuthStateListener method so you can track
        //whenever the user signs in or out.
        mAuth = FirebaseAuth.getInstance();
        if(isNotLoggedIn()){
            signIn();
        }
        setFirebaseAuthListener();
        if (user!=null){
            Log.i(LOG_TAG, user.getProfileDisplayName());
            Log.i(LOG_TAG, user.getProviderId());
            Log.i(LOG_TAG, user.getProfileEmail());
            Log.i(LOG_TAG, user.getProfileUid());
        }

        disableShiftMode(navigation);
        initializeViews();

        signalOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadUser();
            }
        });
        signalOffMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Handle SignIn States
            handleSignInStates(resultCode,response);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClickCallBack(boolean renamePlace) {
        /*
         * This method will called after the Dialog dismiss from AddNewLocationDialogFragment Class
         * passing that if the user will rename the location or not. True if the user will rename
         * the location and false if not.
         */
        if (addNewLocationFragment!=null){
            addNewLocationFragment.onClickCallBack(renamePlace);
        }

    }

    @Override
    public void onLoadFinish(boolean state) {
        if (state){
            firstTime = false;
            progressBar.setVisibility(View.GONE);
            signalOff.setVisibility(View.GONE);
            signalOffMessage.setVisibility(View.GONE);
            homeFrame.setVisibility(View.VISIBLE);
            navigation.setVisibility(View.VISIBLE);
            loadData();
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_home);
        }else{
            noInternetLayout();
        }
    }

    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField(getString(R.string.declared_field));
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //remove logs
            Log.e("BNVHelper", "Unable to get shift mode field", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //remove logs
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
            e.printStackTrace();
        }
    }



    /**
     * This method load all data needed in the application
     * */
    private void loadData(){
        //ToDo: make this method load all data from the server onCreate
        //requestLocationPermission();
        categoriesController = new CategoriesController();
        categoriesController.fillCategories();
    }
    private void noInternetLayout(){
        progressBar.setVisibility(View.GONE);
        homeFrame.setVisibility(View.GONE);
        signalOff.setVisibility(View.VISIBLE);
        signalOffMessage.setVisibility(View.VISIBLE);
    }

    private void initializeViews(){
        if (firstTime){
            progressBar.setVisibility(View.VISIBLE);
            signalOff.setVisibility(View.GONE);
            signalOffMessage.setVisibility(View.GONE);
            homeFrame.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);

        }
    }

    private void reloadUser(){
        if (Config.isNetworkConnected(getApplicationContext())){
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser()!=null){
                auth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser fUser = auth.getCurrentUser();
                        UserInfo fUserInfo = fUser.getProviderData().get(0);
                        getUserDataFromDB(fUserInfo);
                    }
                });
            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    noInternetLayout();
                }
            }, 500);
        }
        initializeViews();
    }

}
