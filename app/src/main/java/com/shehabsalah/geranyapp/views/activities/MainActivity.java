package com.shehabsalah.geranyapp.views.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.views.fragments.AddNewLocationDialogFragment;
import com.shehabsalah.geranyapp.views.fragments.AddNewLocationFragment;
import com.shehabsalah.geranyapp.views.fragments.MyPlacesListFragment;
import com.shehabsalah.geranyapp.views.main.ApplicationMain;
import com.shehabsalah.geranyapp.util.Config;

public class MainActivity extends ApplicationMain implements AddNewLocationDialogFragment.Callback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private UserInfo userInfo;
    AddNewLocationFragment addNewLocationFragment;
    MyPlacesListFragment myPlacesListFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_places: // TODO: IMPLEMENTATION PERIOD 1 DAY
                    //ToDo: make the layout of places items (#5)
                    //ToDo: make a fragment to view the places (#6)
                    //ToDo: display recycler view with 10 fake items (#7)

                    //ToDo: add the MyPlacesListFragment fragment here (DESIGN #7)

                    myPlacesListFragment = new MyPlacesListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_container, myPlacesListFragment, Config.MY_NEW_PLACES_LIST)
                            .commit();

                    return true;
                case R.id.navigation_post:
                    //ToDo: make the layout of the new post (#8)
                    //ToDo: make a fragment to display the view (#9)
                    //ToDo: associate the fragment with the view and display it (#10)

                    return true;
                case R.id.navigation_add_place: // TODO: IMPLEMENTATION PERIOD 1 WEEK
                    /******************************Template****************************************/
                    addNewLocationFragment = new AddNewLocationFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_container, addNewLocationFragment, Config.ADD_NEW_PLACE_FRAGMENT)
                            .commit();
                    /******************************Template****************************************/

                    return true;
                case R.id.navigation_profile:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //Initialize the FirebaseAuth instance and the AuthStateListener method so you can track
        //whenever the user signs in or out.
        mAuth = FirebaseAuth.getInstance();
        if(isNotLoggedIn()){
            signIn();
        }
        setFirebaseAuthListener();

        userInfo = getUserInfo();
        if (userInfo!=null){
            Log.i(LOG_TAG, userInfo.getDisplayName());
            Log.i(LOG_TAG, userInfo.getProviderId());
            Log.i(LOG_TAG, userInfo.getEmail());
            Log.i(LOG_TAG, userInfo.getUid());
        }


        /** Test Navigation */
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_places);
        /** End Test Navigation **/

    }

    public void onClick(View v) {
        //ToDo remove this method after adding it in the profile options (Settings)
        if (v.getId() == R.id.sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            /* User is now signed out, so restart the activity to apply the
                             authentication layout.*/
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
        }
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



}
