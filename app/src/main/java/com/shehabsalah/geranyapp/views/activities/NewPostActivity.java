package com.shehabsalah.geranyapp.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shehabsalah.geranyapp.R;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        setTitle(getString(R.string.title_post));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
