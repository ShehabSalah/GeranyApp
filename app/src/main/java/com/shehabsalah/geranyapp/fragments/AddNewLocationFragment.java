package com.shehabsalah.geranyapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 7/31/17.
 * This fragment will display when the user hit on (Add New Place) icon in Bottom navigation menu
 */

public class AddNewLocationFragment extends Fragment {

    @BindView(R.id.add_new_location_icon)
    ImageView addNewLocationIcon;
    @BindView(R.id.add_location_text)
    TextView addLocationText;
    @BindView(R.id.add_to_places_button)
    Button addToPlacesButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.add_new_location_layout, container, false);
        ButterKnife.bind(this, mainView);
        displayViews();
        // ToDo: after the user click on add to my places button make the fullscreen black transparent layout that show square white box contain message (Do you like to give a name to your place?) and green right circle and gray close circle (#1.2)
        // ToDo: if the user choose close: make the icon smooth changing from drop icon to beenhere icon and the text change to (Your current place is in your places list) [victor transition] (#1.3)
        // ToDo: if the user choose yes: make the dialog change to add Place Name with input box, red cancel button and done blue button (#1.4)
        return mainView;
    }


    /**
     * This method responsible on display different views according to the internet connection and
     * user current area. If there is no internet connection the method will hide addToPlacesButton
     * button and display the no signal icon and message. If the device connected with the internet,
     * the method will check the user location and display if the location exist in My Places list or
     * not.
     * */
    private void displayViews(){
        //Checking on the internet connection
        if (Config.isNetworkConnected(getActivity())){
            // if connected, then check if this place in My Places list or not
            if (checkIfMyCurrentPlaceInPlacesListOrNot()) {
                //This place is exist in My Places list.
                addNewLocationIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_beenhere));
                addLocationText.setText(R.string.current_place_exist);
                addToPlacesButton.setVisibility(View.GONE);
            }else{
                //This place not exist in My Places list.
                //Enable Add New Place functionality.
                addNewLocationIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_person_pin_circle));
                addLocationText.setText(R.string.add_new_place_text);
                addToPlacesButton.setVisibility(View.VISIBLE);
            }
        }else{
            // if not connected
            addNewLocationIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_signal_wifi_off));
            addLocationText.setText(R.string.no_signal_message);
            addToPlacesButton.setVisibility(View.GONE);
        }
    }
    /**
     * This Method responsible on comparing the user current place with his places list.
     * @return (True) if the place exist and (False) if the place not exist
     * */
    private boolean checkIfMyCurrentPlaceInPlacesListOrNot(){
        //ToDo: get the user current place and check if it exist in My Places list or not.
        return false;
    }

}
