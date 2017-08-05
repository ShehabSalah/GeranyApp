package com.shehabsalah.geranyapp.controllers;

import android.content.Context;

import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.util.Config;

import java.util.ArrayList;

/**
 * Created by ShehabSalah on 8/5/17.
 *
 */

public class MyPlacesController {
    private ArrayList<MyPlaces> myPlaces;

    public MyPlacesController() {
        myPlaces = new ArrayList<>();
    }

    public String getTheCurrentLocation(){
        //ToDo: get the user current location from API [AsyncTask Needed]. (IMPLEMENTATION: #1)
        return "Al Maged Street, 9th district, Al Obur City, Cairo, Egypt";
    }

    public MyPlaces addNewPlace(String placeAddress){

        MyPlaces myPlaces = new MyPlaces(placeAddress, "", false);
        //ToDo: add the place to the server (IMPLEMENTATION: #2)

        return myPlaces;
    }

    public void updatePlaceNickname(MyPlaces myPlaces, String nickname){
        if (nickname!=null && !nickname.isEmpty()){
            myPlaces.setPlaceNickname(nickname);
            //ToDo: update the nickname in the server (IMPLEMENTATION: #3)
        }
    }

    public void fillPlaces(){
        //ToDo: replace content with fetching places from the server (IMPLEMENTATION: #4)
        myPlaces = new ArrayList<>();
        myPlaces.add(new MyPlaces(
                "Al Maged Street, 9th district, Al Obur City, Cairo, Egypt",
                "Home",
                true
        ));
        myPlaces.add(new MyPlaces(
                "Shehab Salah Street, 5th district, Al Obur City, Cairo, Egypt",
                "Shehab Salah",
                false
        ));
        myPlaces.add(new MyPlaces(
                "Makram Abead Street, Nasr City, Cairo, Egypt",
                "Nasr City Home",
                false
        ));
        myPlaces.add(new MyPlaces(
                "Freezone,Al Wafaa W El Amal, Nasr City, Cairo, Egypt",
                "Work",
                false
        ));
        myPlaces.add(new MyPlaces(
                "Al Dlivrand Street, Misr El Gdeeda, Cairo, Egypt",
                "Appcorp Work",
                false
        ));
    }

    public ArrayList<MyPlaces> getMyPlaces() {
        return myPlaces;
    }


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

    public boolean checkIfCurrentLocationExits(String address){
        fillPlaces();
        for (int i = 0; i < myPlaces.size(); i++){
            if(address.equals(myPlaces.get(i).getPlaceAddress()))
                return true;
        }
        return false;
    }
}
