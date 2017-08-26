package com.shehabsalah.geranyapp.views.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.Collaborator;
import com.shehabsalah.geranyapp.model.Dislike;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.adapters.CollaboratorAdapter;
import com.shehabsalah.geranyapp.views.adapters.FeedbackAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 2017-08-24.
 *
 */

public class CollaboratorFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    @BindView(R.id.my_places_swipe_refresh_layout) SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.my_places_list) RecyclerView recyclerView;
    @BindView(R.id.no_result) TextView noResult;

    private FirebaseDatabase database;
    private final int REQUEST_CALL_PERMISSION = 10;
    int sectionNumber = -1;
    String postId = null;
    User userProfile;
    String collaberatorNumber = null;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public CollaboratorFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CollaboratorFragment newInstance(int sectionNumber, String postId, User user) {
        CollaboratorFragment fragment = new CollaboratorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(Config.POST_ID_EXTRA, postId);
        args.putParcelable(Config.USER_INFO, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_layout, container, false);
        ButterKnife.bind(this, rootView);
        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        postId = getArguments().getString(Config.POST_ID_EXTRA);
        userProfile = getArguments().getParcelable(Config.USER_INFO);
        database = FirebaseDatabase.getInstance();
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
        swipeToRefresh.setRefreshing(true);
        switch(sectionNumber){
            case Config.FEEDBACK_SECTION:
                loadFeedBack();
                break;
            case Config.VOLUNTEER_SECTION:
            case Config.DONATION_SECTION:
                loadCollaborators(sectionNumber);
                break;
        }
        return rootView;
    }

    private void loadFeedBack(){
        if (Config.isNetworkConnected(getActivity())){
            noResult.setVisibility(View.GONE);
            DatabaseReference myRefFeedback = database.getReference(Config.DB_DISLIKES);
            Query query = myRefFeedback.orderByChild(Config.POST_ID_EXTRA).equalTo(postId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Dislike> dislikes = new ArrayList<>();
                    for (DataSnapshot dislikeSnap: snapshot.getChildren()) {
                        Dislike dislike = dislikeSnap.getValue(Dislike.class);
                        dislikes.add(0,dislike);

                    }
                    displayFeedback(dislikes);
                    //displayCollaborator(collaborators, section);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }else{
            noResult.setVisibility(View.VISIBLE);
            noResult.setText(getResources().getString(R.string.no_internet));
            swipeToRefresh.setRefreshing(false);
        }
    }

    private void loadCollaborators(final int section){
        if (Config.isNetworkConnected(getActivity())){
            noResult.setVisibility(View.GONE);
            Query query;
            if (section == Config.VOLUNTEER_SECTION){
                DatabaseReference myRefVolun = database.getReference(Config.DB_VOLUNTEER);
                query = myRefVolun.orderByChild(Config.POST_ID_EXTRA).equalTo(postId);
            }else {
                DatabaseReference myRefDonat = database.getReference(Config.DB_DONATIONS);
                query = myRefDonat.orderByChild(Config.POST_ID_EXTRA).equalTo(postId);
            }
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Collaborator> collaborators = new ArrayList<>();
                    for (DataSnapshot collaboratorsSnap: snapshot.getChildren()) {
                        Collaborator volunteer = collaboratorsSnap.getValue(Collaborator.class);
                        collaborators.add(0,volunteer);
                    }
                    displayCollaborator(collaborators, section);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }else{
            noResult.setVisibility(View.VISIBLE);
            noResult.setText(getResources().getString(R.string.no_internet));
            swipeToRefresh.setRefreshing(false);
        }
    }

    private void displayCollaborator( ArrayList<Collaborator> collaborators, int sectionNumber){
        if (collaborators.isEmpty()){
            noResult.setVisibility(View.VISIBLE);
            noResult.setText(sectionNumber == Config.VOLUNTEER_SECTION? getResources().getString(R.string.no_volunteers) : getResources().getString(R.string.no_donation));
            swipeToRefresh.setRefreshing(false);
        }else{
            noResult.setVisibility(View.GONE);
            CollaboratorAdapter collaboratorAdapter = new CollaboratorAdapter(getActivity(), collaborators) {
                @Override
                public void callPermission(String phoneNumber) {
                    collaberatorNumber = phoneNumber;
                    requestCallPermission();
                }
            };
            recyclerView.setAdapter(collaboratorAdapter);
            swipeToRefresh.setRefreshing(false);
        }

    }

    private void displayFeedback(ArrayList<Dislike> feedbackArrayList){
        if (feedbackArrayList.isEmpty()){
            noResult.setVisibility(View.VISIBLE);
            noResult.setText(getResources().getString(R.string.no_feedback));
            swipeToRefresh.setRefreshing(false);
        }else{
            noResult.setVisibility(View.GONE);
            FeedbackAdapter feedbackAdapter = new FeedbackAdapter(getActivity(), feedbackArrayList, userProfile);
            recyclerView.setAdapter(feedbackAdapter);
            swipeToRefresh.setRefreshing(false);
        }
    }

    private void refreshContent(){
        switch(sectionNumber){
            case Config.FEEDBACK_SECTION:
                loadFeedBack();
                break;
            case Config.VOLUNTEER_SECTION:
            case Config.DONATION_SECTION:
                loadCollaborators(sectionNumber);
                break;
        }
    }
    private void requestCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            callUser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callUser();
        }
    }

    private void callUser(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + collaberatorNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }
}
