package com.shehabsalah.geranyapp.views.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.fragments.CollaboratorFragment;

public class CollaboratorActivity extends AppCompatActivity {
     User userProfile;
    String postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator);

        Intent intent = getIntent();
        if (intent.hasExtra(Config.POST_ID_EXTRA) && intent.hasExtra(Config.USER_INFO)){
            postId          = intent.getStringExtra(Config.POST_ID_EXTRA);
            userProfile     = intent.getParcelableExtra(Config.USER_INFO);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.collaborators_activity_title));
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return CollaboratorFragment.newInstance(position + 1, postId, userProfile);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case Config.FEEDBACK_INDICATOR:
                    return Config.FEEDBACK_NAME;
                case Config.VOLUNTEER_INDICATOR:
                    return Config.VOLUNTEERS_NAME;
                case Config.DONATION_INDICATOR:
                    return Config.DONATION_NAME;
            }
            return  super.getPageTitle(position);
        }
    }
}
