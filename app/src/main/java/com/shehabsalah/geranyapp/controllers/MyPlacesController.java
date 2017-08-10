package com.shehabsalah.geranyapp.controllers;

import android.content.Context;

import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.util.Config;

import java.util.ArrayList;

/**
 * Created by ShehabSalah on 8/5/17.
 * MyPlacesController class responsible on the communication between server, data model and views.
 * This class contain the business logic of (My Places) tab and its main functionality.
 */

public class MyPlacesController {
    private ArrayList<MyPlaces> myPlaces;


    /**
     * Constructor to initiate My Places list
     * */
    public MyPlacesController() {
        myPlaces = new ArrayList<>();
    }

    /**
     * This method communicate with Google Map API and the Gerany server to extract the user full address
     * @return string contain the full address of the user
     * */
    public String getTheCurrentLocation(){
        //ToDo: get the user current location from API [AsyncTask Needed]. (IMPLEMENTATION: #1)
        return "Al Maged Street, 9th district, Al Obur City, Cairo, Egypt";
    }

    /**
     * This method allows to add new place to the user places (only the user current address).
     * @param placeAddress string has the user address that.
     * @return MyPlaces object which contain the address information that the user just added.
     * */
    public MyPlaces addNewPlace(String placeAddress){

        MyPlaces myPlaces = new MyPlaces(placeAddress, "", false);
        //ToDo: add the place to the server (IMPLEMENTATION: #2)

        return myPlaces;
    }

    /**
     * This method allows to update and add place nickname.
     * @param myPlaces Desired place that will change its nickname.
     * @param nickname Desired address nickname.
     * */
    public void updatePlaceNickname(MyPlaces myPlaces, String nickname){
        if (nickname!=null && !nickname.isEmpty()){
            myPlaces.setPlaceNickname(nickname);
            //ToDo: update the nickname in the server (IMPLEMENTATION: #3)
        }
    }

    /**
     * This method communicates with Firebase server and fetch all the user places in
     * ArrayList<MyPlaces>
     * */
    public void fillPlaces(){
        //ToDo: replace content with fetching places from the server (IMPLEMENTATION: #4)
        myPlaces = new ArrayList<>();
        myPlaces.add(new MyPlaces(
                "Al Maged Street, 9th district, Al Obur City, Cairo, Egypt",
                "Home 1",
                true
        ));
        myPlaces.add(new MyPlaces(
                "Shehab Salah Street, 5th district, Al Obur City, Cairo, Egypt",
                "Home 2",
                false
        ));
        myPlaces.add(new MyPlaces(
                "Makram Abead Street, Nasr City, Cairo, Egypt",
                "",
                false
        ));
        myPlaces.add(new MyPlaces(
                "Freezone,Al Wafaa W El Amal, Nasr City, Cairo, Egypt",
                "Home 4",
                false
        ));
        myPlaces.add(new MyPlaces(
                "Al Dlivrand Street, Misr El Gdeeda, Cairo, Egypt",
                "Home 5",
                false
        ));
    }

    /**
     * This method provide access to the user places.
     * @return ArrayList<MyPlaces> of all the user saved places.
     * */
    public ArrayList<MyPlaces> getMyPlaces() {
        return myPlaces;
    }

    /**
     * This method provide access to the places list size
     * @return int that contain the places size
     * */
    public int getPlacesSize(){
        return myPlaces.size();
    }

    /**
     * This method allows to delete place at given position.
     * @param context the application context.
     * @param position place index that want to delete
     * @return boolean contain true if the place deleted, false if the place doesn't deleted.
     * */
    public boolean deleteMyPlaceAtPosition(Context context, int position){
        if (Config.isNetworkConnected(context)){
            //ToDo: remove the place from the server (IMPLEMENTATION: #5)
            /**From position get MyPlace object at this index myPlaces.get(position) then get the place id*/

            /**FAKE IMPLEMENTATION TO REMOVE**/
            myPlaces.remove(position);
            /**FAKE IMPLEMENTATION TO REMOVE**/
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method checks if the given place exists in the user places or not.
     * @param address address too check if it exists or not.
     * @return boolean true if the place is exist and false if not.
     * */
    public boolean checkIfCurrentLocationExits(String address){
        for (int i = 0; i < myPlaces.size(); i++){
            if(address.equals(myPlaces.get(i).getPlaceAddress()))
                return true;
        }
        return false;
    }
}
