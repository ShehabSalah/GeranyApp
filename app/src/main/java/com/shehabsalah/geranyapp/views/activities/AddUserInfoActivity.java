package com.shehabsalah.geranyapp.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddUserInfoActivity extends AppCompatActivity {
    @BindView(R.id.edit_text_email)
    EditText editTextEmail;
    @BindView(R.id.edit_text_phone_number)
    EditText editTextMobile;
    @BindView(R.id.done_text_view)
    TextView textViewDone;
    @BindView(R.id.number_container)
    RelativeLayout numberContainer;
    @BindView(R.id.email_container)
    RelativeLayout emailContainer;


    private boolean isEmailExist;
    private boolean isNumberExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);
        if (getActionBar()!=null){
            getActionBar().hide();
        }
        ButterKnife.bind(this);
        if (getIntent().hasExtra(Config.USER_HAS_EMAIL) && getIntent().hasExtra(Config.USER_HAS_NUMBER)){
            isEmailExist = getIntent().getBooleanExtra(Config.USER_HAS_EMAIL, false);
            isNumberExist = getIntent().getBooleanExtra(Config.USER_HAS_NUMBER, false);

            if (!isEmailExist && !isNumberExist)
                finish();

            checkVisibility();

        }else {
            finish();
        }

        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmailExist){
                    String emailHolder = editTextEmail.getText().toString().trim();
                    if (!emailHolder.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailHolder).matches()){
                        addEmailToUserInfo(emailHolder);
                        isEmailExist = true;
                    }else{
                        Config.toastLong(getApplicationContext(), getString(R.string.email_not_valid));
                        checkVisibility();
                    }
                }

                if (!isNumberExist){
                    String numberHolder = editTextMobile.getText().toString().trim();
                    if (!numberHolder.isEmpty() && android.util.Patterns.PHONE.matcher(numberHolder).matches()){
                        addMobileNumberToUserInfo(numberHolder);
                        isNumberExist = true;
                    }else{
                        Config.toastLong(getApplicationContext(), getString(R.string.mobile_not_valid));
                        checkVisibility();
                    }
                }


            }
        });

    }

    /**
     * This method responsible on adding the user email to the user information in Database.
     * @param email user email address
     * */
    private void addEmailToUserInfo(String email){
        //ToDo: add the user email to the user info in the database

        if (isNumberExist){
            finishTheActivity();
        }
    }

    /**
     * This method responsible on adding the user phone number to the user information in the Database.
     * @param mobile user mobile number
     * */
    private void addMobileNumberToUserInfo(String mobile){
        //ToDo: add the user mobile number to the user info in the database

        finishTheActivity();
    }

    private void finishTheActivity(){
        Intent intent = new Intent(AddUserInfoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void checkVisibility(){
        if (!isEmailExist)
            emailContainer.setVisibility(View.VISIBLE);
        else
            emailContainer.setVisibility(View.GONE);

        if (!isNumberExist)
            numberContainer.setVisibility(View.VISIBLE);
        else
            numberContainer.setVisibility(View.GONE);
    }



}
