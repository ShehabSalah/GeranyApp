package com.shehabsalah.geranyapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.Dislike;
import com.shehabsalah.geranyapp.model.ReportFeedBack;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ShehabSalah on 8/25/17.
 *
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Dislike> feedback;
    private User userProfile;
    private DatabaseReference reportFeedback;
    private final String REPORT_FEEDBACK = "feedback";

    public FeedbackAdapter(Context context, ArrayList<Dislike> feedback, User userProfile) {
        this.context = context;
        this.feedback = feedback;
        this.userProfile = userProfile;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reportFeedback = database.getReference(Config.DB_REPORT_FEEDBACK);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.post_profile_picture) CircleImageView profilePicture;
        @BindView(R.id.username) TextView username;
        @BindView(R.id.menu_button) RelativeLayout menu_button;
        @BindView(R.id.feedback_menuContainer) ImageView feedbackMenuIcon;
        @BindView(R.id.feedback_text) TextView feedbackText;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            menu_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dislike feedbackItem = feedback.get(getAdapterPosition());
                    showFeedbackMenu(feedbackItem, feedbackMenuIcon, menu_button);
                }
            });

        }

    }

    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_layout, parent, false);
        return new FeedbackAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final FeedbackAdapter.MyViewHolder holder, final int position) {
        Picasso.with(context)
                .load(feedback.get(position).getProfile_pic())
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.profilePicture);
        holder.username.setText(feedback.get(position).getUsername());
        holder.feedbackText.setText(feedback.get(position).getFeedBack());
        if (feedback.get(position).getUser_id().equals(userProfile.getProfileUid())){
            //Show delete menu
            holder.menu_button.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return feedback==null?0:feedback.size();
    }

    private void showFeedbackMenu(Dislike feedback, View view, View button){
        reportMenu(feedback, view, button);
    }

    private void reportMenu(final Dislike feedback, View v, final View button){
        Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(wrapper, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.post_report_item_menu, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ReportFeedBack reportFeedbackClass = new ReportFeedBack(userProfile.getProfileUid(),
                        userProfile.getProfileDisplayName(),userProfile.getProfilePhotoUrl(),
                        feedback.getDislikeId());
                reportFeedback.child(feedback.getDislikeId()+" "+userProfile.getProfileUid()).setValue(reportFeedbackClass);
                button.setVisibility(View.GONE);
                Config.toastShort(context, String.format(context.getResources().getString(R.string.report_message), REPORT_FEEDBACK));
                return true;
            }
        });
        popup.show();
    }
}
