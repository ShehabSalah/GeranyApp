package com.shehabsalah.geranyapp.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

            if (!isEmailExist)
                emailContainer.setVisibility(View.VISIBLE);

            if (!isNumberExist)
                numberContainer.setVisibility(View.VISIBLE);


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
                    }
                }

                if (!isNumberExist){
                    String numberHolder = editTextMobile.getText().toString().trim();
                    if (!numberHolder.isEmpty() && android.util.Patterns.PHONE.matcher(numberHolder).matches()){
                        addMobileNumberToUserInfo(numberHolder);
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
    }

    /**
     * This method responsible on adding the user phone number to the user information in the Database.
     * @param mobile user mobile number
     * */
    private void addMobileNumberToUserInfo(String mobile){
        //ToDo: add the user mobile number to the user info in the database
    }



}
