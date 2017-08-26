package com.shehabsalah.geranyapp.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.fragments.PostFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.main_linear_title) LinearLayout titleContainer;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.username_profile) TextView usernameProfile;
    @BindView(R.id.email_profile) TextView emailProfile;
    @BindView(R.id.username_profile_toolbar) TextView usernameProfileToolbar;
    @BindView(R.id.profile_picture) CircleImageView profilePicture;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private View mUpButton;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        bindActivity();

        appBar.addOnOffsetChangedListener(this);

        toolbar.inflateMenu(R.menu.menu_profile);
        toolbar.setTitle("");
       // ToDo: i removed this line toolbar.setTitle(""); must see later if it effect on the profile view or not
        setSupportActionBar(toolbar);
        startAlphaAnimation(usernameProfileToolbar, 0, View.INVISIBLE);

        mUpButton = findViewById(R.id.action_up);
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSupportNavigateUp();
            }
        });

    }
    private void bindActivity() {

        Intent intent = getIntent();
        if (intent.hasExtra(Config.USER_INFO) && intent.hasExtra(Config.PLACES_EXTRA)){
            user        = intent.getParcelableExtra(Config.USER_INFO);
            String activePlace = intent.getStringExtra(Config.PLACES_EXTRA);
            usernameProfile.setText(user.getProfileDisplayName());
            usernameProfileToolbar.setText(user.getProfileDisplayName());
            if (user.getProfileEmail()!=null){
                emailProfile.setText(user.getProfileEmail());
            }
            Picasso.with(getApplicationContext())
                    .load(user.getProfilePhotoUrl())
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(profilePicture);

            PostFragment postFragment = new PostFragment();
            postFragment.setExtra(user, activePlace,Config.PROFILE_POSTS);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profile_container, postFragment, Config.POST_FRAGMENT)
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(usernameProfileToolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(usernameProfileToolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                intent.putExtra(Config.USER_INFO, user);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
