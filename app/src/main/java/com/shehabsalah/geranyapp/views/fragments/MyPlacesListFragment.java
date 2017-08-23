package com.shehabsalah.geranyapp.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.adapters.MyPlacesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 8/4/17.
 * This fragment will display when the user hit on (My Places) icon in Bottom navigation menu.
 * MyPlacesListFragment allow user to switch between their places posts, remove place, and edit
 * their places names.
 */

public class MyPlacesListFragment extends Fragment {
    @BindView(R.id.my_places_swipe_refresh_layout)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.my_places_list)
    RecyclerView recyclerView;
    MyPlacesListAdapter myPlacesListAdapter;
    MyPlacesController myPlacesController;
    User userProfile;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.list_layout, container, false);
        ButterKnife.bind(this, mainView);

        //setup the recycler view
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        //add ItemDecoration
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //change loading colors
        swipeToRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.buttonDarkColor),
                ContextCompat.getColor(getActivity(), R.color.colorAccent)
        );

        //fake connection to test the load
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        myPlacesListAdapter = new MyPlacesListAdapter(myPlacesController, getActivity());
        recyclerView.setAdapter(myPlacesListAdapter);

        return mainView;
    }

    // fake a network operation's delayed response
    // this is just for demonstration, not real code!
    /**
     * refreshContent method fetch user places from the server, fill ArrayList<MyPlaces> with user
     * places, and display those places.
     * */
    private void refreshContent(){
        swipeToRefresh.setRefreshing(true);
        myPlacesController = new MyPlacesController(userProfile, getActivity()) {
            @Override
            public void myPlacesLoadFinish(String location, boolean state) {
                if (state){
                    myPlacesListAdapter = new MyPlacesListAdapter(myPlacesController, getActivity());
                    recyclerView.setAdapter(myPlacesListAdapter);
                    swipeToRefresh.setRefreshing(false);
                }else{
                    Config.toastLong(getActivity(), getString(R.string.no_internet));
                }
            }
        };
        myPlacesController.fillPlaces();
    }

    public void setMyPlaces(MyPlacesController myPlacesController, User userProfile){
        this.myPlacesController = myPlacesController;
        this.userProfile = userProfile;
    }
}
