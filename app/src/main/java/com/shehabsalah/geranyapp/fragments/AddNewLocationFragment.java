package com.shehabsalah.geranyapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.util.Config;

/**
 * Created by ShehabSalah on 7/31/17.
 * This fragment will display when the user hit on (Add New Place) icon in Bottom navigation menu
 */

public class AddNewLocationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.add_new_location_layout, container, false);
        // ToDo: Add (No Signal message and icon) to  ADD_NEW_PLACE_FRAGMENT layout (#1)
        // ToDo: Add (Your current place is in your places list message and icon[beenhere icon]) to ADD_NEW_PLACE_FRAGMENT layout (#1.1)
        // ToDo: after the user click on add to my places button make the fullscreen black transparent layout that show square white box contain message (Do you like to give a name to your place?) and green right circle and gray close circle (#1.2)
        // ToDo: if the user choose close: make the icon smooth changing from drop icon to beenhere icon and the text change to (Your current place is in your places list) [victor transition] (#1.3)
        // ToDo: if the user choose yes: make the dialog change to add Place Name with input box, red cancel button and done blue button (#1.4)
        /*ButterKnife.bind(this, mainView);
        if(savedInstanceState == null){
            bindViews();
        } else{
            recipeHolder = savedInstanceState.getParcelable(Config.RECIPE_DETAILS_STATE_TAG);
            restoreViews(savedInstanceState.getParcelable(Config.DETAILS_LIST_STATE));
        }*/
        return mainView;
    }
}
