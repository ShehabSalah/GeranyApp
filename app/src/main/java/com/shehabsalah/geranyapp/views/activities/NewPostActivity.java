package com.shehabsalah.geranyapp.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.CategoriesController;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.controllers.PostController;
import com.shehabsalah.geranyapp.model.Category;
import com.shehabsalah.geranyapp.model.MyPlaces;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.shehabsalah.geranyapp.views.adapters.PostAdapter;
import com.shehabsalah.locationlib.UploadImageThread;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewPostActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.categories)
    Spinner categoriesSpinner;
    @BindView(R.id.post_button)
    Button postButton;
    @BindView(R.id.profile_picture)
    CircleImageView profilePicture;
    @BindView(R.id.post_text)
    EditText postText;
    @BindView(R.id.upload_image_holder)
    ImageView uploadImageHolder;
    @BindView(R.id.remove_post_image)
    ImageView removeImage;
    @BindView(R.id.note_holder)
    LinearLayout noteHolder;
    @BindView(R.id.remove_post_note)
    ImageView removeNoteHolder;
    @BindView(R.id.post_note)
    TextView postNote;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    PostController postController;
    AlertDialog alertMenuDialog;
    MyPlaces myPlaces;
    User user;

    private int whichMethod = -1;

    private final int BROWSE_GALLERY_INTENT         = 0;
    private final int REQUEST_WRITE_PERMISSION      = 1;
    private final int TAKE_PICTURE                  = 2;
    private final int OPEN_CAMERA_PERMISSION        = 3;
    private Uri imageUri = null;
    private String postTextStr;
    String categoryNameStr;
    ArrayList<Category> categories;
    UploadImageThread uploadImageThread;
    MyPlacesController placesController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        setTitle(getString(R.string.title_post));
        ButterKnife.bind(this);




        Intent intent = getIntent();
        if (intent.hasExtra(Config.USER_INFO) && intent.hasExtra(Config.CATEGORIES_EXTRA)){
            categories  = intent.getParcelableArrayListExtra(Config.CATEGORIES_EXTRA);
            user        = intent.getParcelableExtra(Config.USER_INFO);

        }
        removeNoteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteHolder.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_down));
                noteHolder.setVisibility(View.GONE);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_end));
                uploadImageHolder.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_end));
                removeImage.setVisibility(View.GONE);
                uploadImageHolder.setVisibility(View.GONE);
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.isNetworkConnected(getApplicationContext())){
                    uploadPost();
                }else{
                    Config.toastLong(getApplicationContext(), getString(R.string.no_internet));
                }
            }
        });
        placesController = new MyPlacesController(user, getApplicationContext()) {
            @Override
            public void myPlacesLoadFinish(String location, boolean state) {
                myPlaces = placesController.getActivePlace();
                bindViews();

            }
        };
        placesController.fillPlaces();
    }

    private void bindViews() {
        if ( myPlaces != null ) {
            String placeNickname = myPlaces.getPlaceNickname();
            postNote.setText(String.format(getString(R.string.post_note),
                    placeNickname.trim().isEmpty() ? myPlaces.getPlaceAddress() : placeNickname));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, convertCategoriesToArrayForSpinner());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoriesSpinner.setAdapter(adapter);
            Picasso.with(getApplicationContext())
                    .load(user.getProfilePhotoUrl())
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .into(profilePicture);
        }
        removeImage.setVisibility(View.GONE);
        uploadImageHolder.setVisibility(View.GONE);
    }

    private ArrayList<String> convertCategoriesToArrayForSpinner(){
        ArrayList<String> categoryList = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++){
            categoryList.add(categories.get(i).getCategoryName());
        }
        return categoryList;
    }

    private void showMenuDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(NewPostActivity.this);
        alert.setItems(R.array.camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //LaunchCamera
                    whichMethod = 0;
                    requestCameraPermission();
                } else {
                    //Browse
                    whichMethod = 1;
                    requestWritePermission(false);
                }

            }
        });
        alertMenuDialog = alert.create();
        alertMenuDialog.show();
    }


    private void takePicture(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, TAKE_PICTURE);//zero can be replaced with any action code
    }
    /**
     * This method open the user gallery to pick an image
     * */
    private void browseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, BROWSE_GALLERY_INTENT);
    }

    private void whichGallery(){
        if (whichMethod == 0){
            takePicture();
        }else{
            browseImage();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            imageUri = data.getData();
            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .centerCrop()
                    .resize(220,220)
                    .into(uploadImageHolder);
            uploadImageHolder.setVisibility(View.VISIBLE);
            removeImage.setVisibility(View.VISIBLE);
            removeImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_end));
            uploadImageHolder.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_end));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            whichGallery();
        }else if (requestCode == OPEN_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestWritePermission(true);
        }
    }

    private void requestWritePermission(boolean isCamera) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else if (isCamera){
            takePicture();
        }else{
            browseImage();
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, OPEN_CAMERA_PERMISSION);
        } else {
            requestWritePermission(true);
        }
    }

    private void uploadPost(){

        postTextStr = postText.getText().toString().trim();
        categoryNameStr = categoriesSpinner.getSelectedItem().toString();
        if (!postTextStr.isEmpty()){
            postController = new PostController(getApplicationContext()) {
                @Override
                public void onLoadFinish() {
                    finish();
                }
            };
            if (imageUri!=null){
                doFileUpload(postController.getPath(imageUri), postController.getCurrentDate());
            }else{
                uploadLayout();
                postController.addNewPost(postTextStr, categoryNameStr,
                        myPlaces.getPlaceAddress(), Config.FAKE_IMAGE, user);
                progressBar.setProgress(100);
            }
        }else{
            Config.toastLong(getApplicationContext(), getString(R.string.post_text_error_message));
        }
    }

    private void doFileUpload(String sourceFileUri, String currentDate){

        uploadImageThread = new UploadImageThread() {
            @Override
            protected void onPreExecute() {
                uploadLayout();
            }

            @Override
            protected void onPostExecute(String s) {
                postController.addNewPost(postTextStr, categoryNameStr,
                        myPlaces.getPlaceAddress(), s, user);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setProgress(values[0]);
            }
        };
        uploadImageThread.execute(sourceFileUri, currentDate);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (uploadImageThread!=null)
            uploadImageThread.cancel(true);
    }


    private void uploadLayout(){
        postButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.default_icon_color));
        postButton.setBackgroundResource(R.drawable.disabled_cutom_border);
        postButton.setEnabled(false);
        categoriesSpinner.setEnabled(false);
        postText.setEnabled(false);
        addImage.setEnabled(false);
        //image variables
        removeImage.setEnabled(false);
        removeImage.setAlpha(0.5F);
        removeImage.setVisibility(View.GONE);
        uploadImageHolder.setAlpha(0.5F);
        //image variables
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        setTitle(getResources().getString(R.string.cancel));
    }
}
