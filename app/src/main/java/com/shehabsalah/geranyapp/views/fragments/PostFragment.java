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
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.PostController;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.adapters.PostAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 8/23/17.
 * This fragment will display when the user hit on (Home) icon in Bottom navigation menu or got to the profile.
 * PostFragment allow browse, interact and control posts.
 */

public class PostFragment extends Fragment {
    @BindView(R.id.my_places_swipe_refresh_layout)
    SwipeRefreshLayout swipeToRefresh;
    @BindView(R.id.my_places_list)
    RecyclerView recyclerView;
    @BindView(R.id.no_posts)
    TextView noPosts;
    PostAdapter postAdapter;
    User userProfile;
    int postIndicator;
    PostController postController;
    String activePlace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.post_list, container, false);
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
        swipeToRefresh.setRefreshing(true);
        postController = new PostController(getActivity()) {
            @Override
            public void onLoadFinish() {
                if (postController.getPosts().isEmpty()){
                    noPosts.setVisibility(View.VISIBLE);
                    noPosts.setText(getString(R.string.home_posts_area));
                }else{
                    noPosts.setVisibility(View.GONE);
                }
                postAdapter = new PostAdapter(getActivity(), postController, userProfile);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(postAdapter);
                swipeToRefresh.setRefreshing(false);
            }
        };
        if (postIndicator == Config.HOME_POSTS)
            postController.getHomePostsList(activePlace);
        else postController.getProfilePostsList(userProfile);

        return mainView;
    }

    /**
     * refreshContent method fetch all posts from the server, fill and display it to users.
     * */
    private void refreshContent(){
        swipeToRefresh.setRefreshing(true);
        postController = new PostController(getActivity()) {
            @Override
            public void onLoadFinish() {
                if (postController.getPosts().isEmpty()){
                    noPosts.setVisibility(View.VISIBLE);
                    noPosts.setText(getString(R.string.home_posts_area));
                }else{
                    noPosts.setVisibility(View.GONE);
                }
                postAdapter = new PostAdapter(getActivity(), postController, userProfile);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(postAdapter);
                swipeToRefresh.setRefreshing(false);
            }
        };
        if (postIndicator == Config.HOME_POSTS)
            postController.getHomePostsList(activePlace);
        else postController.getProfilePostsList(userProfile);
    }

    public void setExtra(User userProfile, String activePlace, int postIndicator){
        this.userProfile = userProfile;
        this.activePlace = activePlace;
        this.postIndicator = postIndicator;
    }
}
