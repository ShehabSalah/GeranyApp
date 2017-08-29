package com.shehabsalah.geranyapp.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.locationlib.GET_Connector;
import com.shehabsalah.locationlib.GetInfoAsyncTask;
import com.shehabsalah.locationlib.UriBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ShehabSalah on 8/5/17.
 * MyPlacesController class responsible on the communication between server, data model and views.
 * This class contain the business logic of (My Places) tab and its main functionality.
 */

public abstract class MyPlacesController implements LocationListener{
    private ArrayList<MyPlaces> myPlaces;
    private DatabaseReference placeRef;
    private User userInfo;
    private Context activity;
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60 * 10;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
    private static double lat;
    private static double lng;
    private AlertDialog alertDialog;
    private String currentLocation = null;
    private boolean isDefault = false;


    /**
     * Constructor to initiate My Places list
     * */
    public MyPlacesController(User userInfo, Context activity) {
        myPlaces = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        placeRef = database.getReference(Config.DB_PLACES);
        placeRef.keepSynced(true);
        this.userInfo = userInfo;
        this.activity = activity;
    }

    /**
     * This method communicate with Google Map API and the Gerany server to extract the user full address
     * */
    public void getTheCurrentLocation() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled && isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                //Dummy code
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            if(lat == 0.0 && lng == 0.0){
                showLocationDialog();
            }else{
                getLocationAsString();
            }
        }
    }

    /**
     * This method allows to add new place to the user places (only the user current address).
     * @param placeAddress string has the user address that.
     * @param isActive is this place a default place or not?
     * @return MyPlaces object which contain the address information that the user just added.
     * */
    public MyPlaces addNewPlace(String placeAddress, String Nickname, boolean isActive){
        Date d = new Date();
        CharSequence dateSeq = DateFormat.format("yyyy-MM-dd-hh-mm-ss", d.getTime());
        String currentDate = dateSeq.toString();
        MyPlaces myPlace = new MyPlaces(currentDate,userInfo.getProfileUid(),placeAddress, Nickname, isActive);
        placeRef.child(currentDate).setValue(myPlace);
        if (myPlaces!=null)
            myPlaces.add(myPlace);

        if (isActive){
            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(Config.ACTIVE_PLACE, placeAddress).apply();
        }

        return myPlace;
    }

    /**
     * This method allows to update and add place nickname.
     * @param myPlaces Desired place that will change its nickname.
     * @param nickname Desired address nickname.
     * */
    public void updatePlaceNickname(MyPlaces myPlaces, String nickname){
        if (nickname!=null && !nickname.isEmpty()){
            myPlaces.setPlaceNickname(nickname);
            placeRef.child(myPlaces.getPlaceId()).setValue(myPlaces);
        }
    }

    /**
     * This method communicates with Firebase server and fetch all the user places in
     * ArrayList<MyPlaces>
     * */
    public void fillPlaces(){
        Query query = placeRef.orderByChild(Config.USER_ID).equalTo(userInfo.getProfileUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                myPlaces = new ArrayList<>();
                for (DataSnapshot placeSnap: snapshot.getChildren()) {
                    MyPlaces pls = placeSnap.getValue(MyPlaces.class);
                    myPlaces.add(0,pls);
                }
                if (myPlaces.isEmpty()){
                    isDefault = true;
                    getTheCurrentLocation();
                }else{
                    myPlacesLoadFinish(currentLocation, true);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method provide access to the user places.
     * @return ArrayList<MyPlaces> of all the user saved places.
     * */
    public ArrayList<MyPlaces> getMyPlaces() {
        return myPlaces;
    }

    /**
     * This method allows to delete place at given position.
     * @param context the application context.
     * @param position place index that want to delete
     * @return boolean contain true if the place deleted, false if the place doesn't deleted.
     * */
    public boolean deleteMyPlaceAtPosition(Context context, int position){
        if (Config.isNetworkConnected(context)){
            MyPlaces place = myPlaces.get(position);
            placeRef.child(place.getPlaceId()).removeValue();
            myPlaces.remove(position);
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method checks if the given place exists in the user places or not.
     * @return boolean true if the place is exist and false if not.
     * */
    public boolean checkIfCurrentLocationExits(String location){
        for (int i = 0; i < myPlaces.size(); i++){
            if(location.equals(myPlaces.get(i).getPlaceAddress()))
                return true;
        }
        return false;
    }

    /**
     * This method returns the user active place or the place that the user choose in the places list.
     * @return MyPlaces object that contain the user active place.
     * */
    public MyPlaces getActivePlace(){
        for (int i = 0; i < myPlaces.size(); i++){
            if(myPlaces.get(i).isActive())
                return myPlaces.get(i);
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void showLocationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(activity.getString(R.string.location_dialog_header));
        alert.setMessage(activity.getString(R.string.location_dialog_message));
        alert.setPositiveButton(activity.getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fillPlaces();
            }
        });
        alert.setNegativeButton(activity.getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.show();
    }

    private void getLocationAsString(){
        if(Config.isNetworkConnected(activity)){
            UriBuilder uriBuilder = new UriBuilder(Config.SCHEME, Config.BASE_URL + Config.LOCATION_URL, Config.FIND_LOCATION);
            uriBuilder.setParam(Config.LAT, Config.LON);
            uriBuilder.setValues(String.valueOf(lat),String.valueOf(lng));
            GET_Connector getItemAPI = new GET_Connector(uriBuilder.getURL());
            GetInfoAsyncTask getInfoAsyncTask = new GetInfoAsyncTask() {

                @Override
                protected void onPreExecute() {
                }

                @Override
                protected void onProgressUpdate(Long... values) {

                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    if (jsonObject != null){
                        try {
                            currentLocation = jsonObject.getString("address");
                            if (isDefault){
                                addDefaultLocation();
                                isDefault = false;
                            }
                            myPlacesLoadFinish(currentLocation, true);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }else{
                        myPlacesLoadFinish(null, false);
                        Toast.makeText(activity,R.string.no_internet,Toast.LENGTH_LONG).show();
                    }
                }
            };
            getInfoAsyncTask.execute(getItemAPI);
        }
    }

    public abstract void myPlacesLoadFinish(String location, boolean state);

    private void addDefaultLocation(){
        addNewPlace(currentLocation, Config.DEFAULT_PLACE_NAME, true);
    }
}
