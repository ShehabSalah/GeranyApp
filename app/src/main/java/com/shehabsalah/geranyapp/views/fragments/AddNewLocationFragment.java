package com.shehabsalah.geranyapp.views.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 7/31/17.
 * This fragment will display when the user hit on (Add New Place) icon in Bottom navigation menu
 * AddNewLocationFragment class allow users to add their current location to their places list
 */

public class AddNewLocationFragment extends Fragment {

    @BindView(R.id.add_new_location_icon)
    ImageView addNewLocationIcon;
    @BindView(R.id.add_location_text)
    TextView addLocationText;
    @BindView(R.id.add_to_places_button)
    Button addToPlacesButton;

    private AlertDialog alertDialog;
    private MyPlacesController myPlacesController;
    private String currentLocationAddress = null;
    private MyPlaces myPlaces = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.add_new_place_layout, container, false);
        ButterKnife.bind(this, mainView);
        myPlacesController = new MyPlacesController();
        displayViews();

        addToPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checking on the internet connection
                /*
                    This step implemented here because the user may wait time between getting the
                    current location and submit it to the server database. So maybe also he loss the
                    internet connection in this time, for that we need to check on the internet
                    connection again before submitting his data to the server.
                */
                if (Config.isNetworkConnected(getActivity()) && currentLocationAddress!=null){
                    //save the user current location in the server database
                    myPlaces = myPlacesController.addNewPlace(currentLocationAddress);
                    //Open BottomSheet dialog to ask user if he want to change the location name or not
                    //NOTE: the result will return in onClickCallBack() method
                    BottomSheetDialogFragment bottomSheetDialogFragment = new AddNewLocationDialogFragment();
                    bottomSheetDialogFragment.setCancelable(false);
                    bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }else{
                    // if not connected
                    connectionLoss();
                }

            }
        });
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * This method responsible on display different views according to the user current area.
     * The method will check the user location and display if the location exist in My Places list
     * or not.
     * */
    private void displayViews(){
        //Checking on the internet connection
        if (Config.isNetworkConnected(getActivity())){
            // if connected, then check if this place in My Places list or not
            if (checkIfMyCurrentPlaceInPlacesListOrNot() && currentLocationAddress!=null) {
                //This place is exist in My Places list.
                beenHere();
            }else if(currentLocationAddress==null){
                // if not connected
                connectionLoss();
            }else{
                //This place not exist in My Places list
                //Enable Add New Place functionality.
                addLocation();
            }
        }else{
            // if not connected
            connectionLoss();
        }
    }
    /**
     * This Method responsible on comparing the user current place with his places list.
     * @return (True) if the place exist and (False) if the place not exist
     * */
    private boolean checkIfMyCurrentPlaceInPlacesListOrNot(){
        currentLocationAddress = myPlacesController.getTheCurrentLocation();
        return myPlacesController.checkIfCurrentLocationExits(currentLocationAddress);
    }

    /**
     * This method displays the connection loss icon and message in the AddNewLocationFragment layout
     * and hiding (ADD TO MY PLACES) button.
     * */
    private void connectionLoss(){
        addNewLocationIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_signal_wifi_off));
        addLocationText.setText(R.string.no_signal_message);
        addToPlacesButton.setVisibility(View.GONE);
    }

    /**
     * This method display the been here icon and message in the AddNewLocationFragment layout
     * and hiding (ADD TO MY PLACES) button
     * */
    private void beenHere(){
        addNewLocationIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_beenhere));
        addLocationText.setText(R.string.current_place_exist);
        addToPlacesButton.setVisibility(View.GONE);
    }

    /**
     * This method display the add location icon and message in the AddNewLocationFragment layout
     * and display the (ADD TO MY PLACES) button which allow user to add his current location to his
     * location list.
     * */
    private void addLocation(){
        addNewLocationIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_person_pin_circle));
        addLocationText.setText(R.string.add_new_place_text);
        addToPlacesButton.setVisibility(View.VISIBLE);
    }


    /**
     * This method will called after the Dialog dismiss from MainActivity that implement the
     * Callback interface of AddNewLocationDialogFragment passing that if the user will rename
     * the location or not. True if the user will rename the location and false if not.
     * @param renamePlace if true? display input layout that the user will enter the place name in it.
     */
    public void onClickCallBack(boolean renamePlace) {
        if (renamePlace){
            //if the user want to rename the added location, show edit dialog
            showRenamePlaceFormDialog();
        }
        //Change layout to been here icon and text
        beenHere();
    }

    /**
     * This method will display the (Edit Location) form to enable users to enter their preferred name
     * to their current location and save it
     * */
    private void showRenamePlaceFormDialog(){
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_new_place_rename_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText placeName = (EditText)v.findViewById(R.id.edit_text_location_dialog);
        TextView cancel = (TextView)v.findViewById(R.id.cancel_button_location_dialog);
        TextView save = (TextView)v.findViewById(R.id.save_button_location_dialog);
        TextView currentLocationTextView = (TextView)v.findViewById(R.id.current_location_edit_dialog);
        final TextView textLength = (TextView)v.findViewById(R.id.edit_text_length);

        currentLocationTextView.setText(currentLocationAddress);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlaceName = placeName.getText().toString().trim();
                //Check if the user entered a non-white space characters
                if (!newPlaceName.isEmpty() && myPlaces!=null){
                    myPlacesController.updatePlaceNickname(myPlaces, newPlaceName);
                    //Dismiss the dialog
                    alertDialog.dismiss();
                }else{
                   Config.toastLong(getActivity(), getString(R.string.empty_name_error_message));
                }
            }
        });
        placeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //update max length text view (0 / 50) with the entered text length
                textLength.setText(String.format(getString(R.string.formatting_max_length), s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();
    }

}
