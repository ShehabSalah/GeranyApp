package com.shehabsalah.geranyapp.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {
    static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.settings_title));

        Intent intent = getIntent();
        if (intent.hasExtra(Config.USER_INFO)){
            user        = intent.getParcelableExtra(Config.USER_INFO);
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        protected DatabaseReference userRef;
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            userRef = database.getReference(Config.DB_USERS);
            PreferenceScreen root = getPreferenceManager().createPreferenceScreen(getActivity());
            SharedPreferences.Editor editor = root.getPreferenceManager().getSharedPreferences().edit();
            editor.putBoolean(getString(R.string.e_v), user.isAllowDisplayingEmail());
            editor.putBoolean(getString(R.string.m_v), user.isAllowDisplayingMobileNumber());
            editor.commit();

            addPreferencesFromResource(R.xml.preferences);



            Preference button = findPreference(getString(R.string.logout_key));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //code for what you want it to do
                    AuthUI.getInstance()
                            .signOut(getActivity())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                            /* User is now signed out, so restart the activity to apply the
                             authentication layout.*/
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                    return true;
                }
            });


            Preference email = findPreference(getString(R.string.e_v));
            email.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                @Override
                public boolean onPreferenceChange(Preference preference,
                                                  Object newValue) {
                    //code for what you want it to do
                    boolean switched = ((SwitchPreference) preference)
                            .isChecked();
                    user.setAllowDisplayingEmail(!switched);
                    userRef.child(user.getProfileUid()).setValue(user);
                    return true;

                }
            });



            Preference mobile = findPreference(getString(R.string.m_v));
            mobile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                @Override
                public boolean onPreferenceChange(Preference preference,
                                                  Object newValue) {
                    //code for what you want it to do
                    boolean switched = ((SwitchPreference) preference)
                            .isChecked();
                    user.setAllowDisplayingMobileNumber(!switched);
                    userRef.child(user.getProfileUid()).setValue(user);
                    return true;

                }

            });

        }
    }
}
