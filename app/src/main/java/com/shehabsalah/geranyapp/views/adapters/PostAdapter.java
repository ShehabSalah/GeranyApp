package com.shehabsalah.geranyapp.views.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.PostController;
import com.shehabsalah.geranyapp.model.Collaborator;
import com.shehabsalah.geranyapp.model.Dislike;
import com.shehabsalah.geranyapp.model.Post;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShehabSalah on 8/23/17.
 *
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private PostController postController;
    private Context context;
    private User userProfile;
    private final String DELETE_POST = "this post";
    private FirebaseDatabase database;
    private DatabaseReference myRefDislikes;
    private DatabaseReference myRefVolun;
    private DatabaseReference myRefDonat;
    private AlertDialog alertDialog;
    private int lastPosition = -1;


    public PostAdapter(Context context, PostController postController, User userProfile) {
        this.postController = postController;
        this.context = context;
        this.userProfile = userProfile;
        database = FirebaseDatabase.getInstance();
        myRefDislikes = database.getReference(Config.DB_DISLIKES);
        myRefVolun = database.getReference(Config.DB_VOLUNTEER);
        myRefDonat = database.getReference(Config.DB_DONATIONS);
        myRefDislikes.keepSynced(true);
        myRefVolun.keepSynced(true);
        myRefDonat.keepSynced(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.post_profile_picture) CircleImageView profilePicture;
        @BindView(R.id.post_username) TextView username;
        @BindView(R.id.post_menuContainer) ImageView more_icon;
        @BindView(R.id.post_category) TextView postCategory;
        @BindView(R.id.post_text) TextView postText;
        @BindView(R.id.post_container) LinearLayout postContainer;
        @BindView(R.id.post_image) ImageView postImage;
        @BindView(R.id.feedback_volunteers_donations) TextView fvd;
        @BindView(R.id.dislike_container) LinearLayout dislikeButton;
        @BindView(R.id.hand_logo) ImageView dislikeIcon;
        @BindView(R.id.post_dislikes) TextView dislikeText;
        @BindView(R.id.volunteer_container) LinearLayout volunteerButton;
        @BindView(R.id.volunteer_logo) ImageView volunteerIcon;
        @BindView(R.id.post_volunteer) TextView volunteerText;
        @BindView(R.id.donation_container) LinearLayout donationButton;
        @BindView(R.id.collaboration_logo) ImageView donationIcon;
        @BindView(R.id.post_donation) TextView donationText;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislikeClicked(getAdapterPosition());
                }
            });

            volunteerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    volunteerClicked(getAdapterPosition());
                }
            });

            donationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    donationClicked(getAdapterPosition());
                }
            });

            postContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToDo: make intent to Collaborators activity
                }
            });

            more_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ToDo: open bottom sheet which display the delete & report
                }
            });

        }

    }

    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);
        return new PostAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PostAdapter.MyViewHolder holder, final int position) {
        Post post = postController.getPosts().get(position);
        holder.username.setText(post.getUsername());
        holder.postText.setText(post.getText());
        holder.postCategory.setText(post.getCategory_name());
        holder.fvd.setText(String.format(context.getString(R.string.post_interactions),
                post.getNumberOfDislikes(),
                post.getNumberOfVolunteers(),
                post.getNumberOfDonation()
        ));
        Picasso.with(context)
                .load(post.getProfile_picture())
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.profilePicture);
        Picasso.with(context)
                .load(post.getImage_url())
                .resize(1080,750)
                .centerCrop()
                .into(holder.postImage);
        myRefDislikes.child(post.getDate() +" "+userProfile.getProfileUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.dislikeIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike_active));
                    holder.dislikeText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                } else{
                    holder.dislikeIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike));
                    holder.dislikeText.setTextColor(ContextCompat.getColor(context, R.color.default_icon_color));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        myRefVolun.child(post.getDate() +" "+userProfile.getProfileUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.volunteerIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_volunteer_active));
                    holder.volunteerText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                } else{
                    holder.volunteerIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_volunteer));
                    holder.volunteerText.setTextColor(ContextCompat.getColor(context, R.color.default_icon_color));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        myRefDonat.child(post.getDate() +" "+userProfile.getProfileUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.donationIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_money_active));
                    holder.donationText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                } else{
                    holder.donationIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_money));
                    holder.donationText.setTextColor(ContextCompat.getColor(context, R.color.default_icon_color));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return postController.getPosts()==null?0:postController.getPosts().size();
    }



    /**
     * This method delete the item at given position, shift the selected position indicator and
     * notify the adapter that the data changed.
     * @param position index of the item to delete.
     * */
    private void deleteItem(int position){
        if (postController.deletePostAtPosition(context,position)){
            notifyItemRemoved(position);
            notifyItemRangeChanged(0, getItemCount());
        }else{
            Config.toastShort(context, context.getResources().getString(R.string.no_internet));
        }
    }

    /**
     * This method show alert on delete a given position.
     * @param position position to delete
     * */
    private void deleteAlert(final int position){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(context.getString(R.string.delete_place_header))
                .setMessage(String.format(context.getString(R.string.delete_place_message), DELETE_POST))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteItem(position);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(ContextCompat.getDrawable(context, R.drawable.ic_delete))
                .show();
    }

    private void dislikeClicked(final int position){
        final Post post = postController.getPosts().get(position);
        myRefDislikes.child(post.getDate() +" "+userProfile.getProfileUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    submitRemovingDislike(post);
                } else{
                    showFeedbackDialog(post);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void volunteerClicked(final int position){
        final Post post = postController.getPosts().get(position);
        myRefVolun.child(post.getDate() +" "+userProfile.getProfileUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    submitRemovingVolunteer(post);
                } else{
                    submitAddingVolunteer(post);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void donationClicked(final int position){
        final Post post = postController.getPosts().get(position);
        myRefDonat.child(post.getDate() +" "+userProfile.getProfileUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    submitRemovingDonation(post);
                } else{
                    submitAddingDonation(post);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * This method will display the (Edit Location) form to enable users to enter their preferred name
     * to their current location and save it.
     * */
    private void showFeedbackDialog(final Post post){
        View v = LayoutInflater.from(context).inflate(R.layout.add_new_place_rename_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText editText = (EditText)v.findViewById(R.id.edit_text_location_dialog);
        ImageView feedBackHeaderIcon = (ImageView)v.findViewById(R.id.location_edit_icon);
        TextView headerDialogText = (TextView)v.findViewById(R.id.dialog_header);

        TextView cancel = (TextView)v.findViewById(R.id.cancel_button_location_dialog);
        TextView save = (TextView)v.findViewById(R.id.save_button_location_dialog);
        TextView DialogBodyHeader = (TextView)v.findViewById(R.id.dialog_body_header);
        TextView currentLocationTextView = (TextView)v.findViewById(R.id.current_location_edit_dialog);
        final TextView textLength = (TextView)v.findViewById(R.id.edit_text_length);
        currentLocationTextView.setVisibility(View.GONE);
        textLength.setVisibility(View.GONE);
        headerDialogText.setText(R.string.feedback_title);
        feedBackHeaderIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_feedback));
        DialogBodyHeader.setText(R.string.feedback_body_header);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editText.getText().toString().trim();
                //Check if the user entered a non-white space characters
                if (!string.isEmpty()){
                    //ToDo: add feedback here
                    submitAddingDislike(post, string);
                    alertDialog.cancel();

                }else{
                    Config.toastLong(context, context.getString(R.string.empty_feedback));
                }
            }
        });
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void submitAddingDislike(Post post, String feedbackText){
        Dislike dislike = new Dislike(userProfile.getProfileUid(),userProfile.getProfileDisplayName(),
                userProfile.getProfilePhotoUrl(),post.getDate() +" "+userProfile.getProfileUid(),
                feedbackText,post.getDate()
        );
        post.setNumberOfDislikes(post.getNumberOfDislikes() + 1);
        myRefDislikes.child(post.getDate() +" "+userProfile.getProfileUid()).setValue(dislike);
        postController.updateNewPost(post);
        notifyDataSetChanged();
    }

    private void submitRemovingDislike(Post post){
        post.setNumberOfDislikes(post.getNumberOfDislikes() - 1);
        myRefDislikes.child(post.getDate() +" "+userProfile.getProfileUid()).removeValue();
        postController.updateNewPost(post);
        notifyDataSetChanged();
    }

    private void submitAddingVolunteer(Post post){
        Collaborator collaborator = new Collaborator(post.getDate() +" "+userProfile.getProfileUid()
                ,userProfile.getProfileUid(),userProfile.getProfileDisplayName(),
                userProfile.getProfilePhotoUrl(),userProfile.getProfileEmail(), userProfile.getPhoneNumber(),
                post.getDate(), userProfile.isAllowDisplayingEmail(), userProfile.isAllowDisplayingMobileNumber() );
        post.setNumberOfVolunteers(post.getNumberOfVolunteers() + 1);
        myRefVolun.child(post.getDate() +" "+userProfile.getProfileUid()).setValue(collaborator);
        postController.updateNewPost(post);
        notifyDataSetChanged();
    }

    private void submitRemovingVolunteer(Post post){
        post.setNumberOfVolunteers(post.getNumberOfVolunteers() - 1);
        myRefVolun.child(post.getDate() +" "+userProfile.getProfileUid()).removeValue();
        postController.updateNewPost(post);
        notifyDataSetChanged();
    }

    private void submitAddingDonation(Post post){
        Collaborator collaborator = new Collaborator(post.getDate() +" "+userProfile.getProfileUid()
                ,userProfile.getProfileUid(),userProfile.getProfileDisplayName(),
                userProfile.getProfilePhotoUrl(),userProfile.getProfileEmail(), userProfile.getPhoneNumber(),
                post.getDate(), userProfile.isAllowDisplayingEmail(), userProfile.isAllowDisplayingMobileNumber() );
        post.setNumberOfDonation(post.getNumberOfDonation() + 1);
        myRefDonat.child(post.getDate() +" "+userProfile.getProfileUid()).setValue(collaborator);
        postController.updateNewPost(post);
        notifyDataSetChanged();
    }

    private void submitRemovingDonation(Post post){
        post.setNumberOfDonation(post.getNumberOfDonation() - 1);
        myRefDonat.child(post.getDate() +" "+userProfile.getProfileUid()).removeValue();
        postController.updateNewPost(post);
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}