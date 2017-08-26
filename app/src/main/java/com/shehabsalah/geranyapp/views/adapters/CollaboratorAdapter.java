package com.shehabsalah.geranyapp.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.Collaborator;
import com.shehabsalah.geranyapp.model.User;
import com.shehabsalah.geranyapp.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ShehabSalah on 2017-08-24.
 *
 */

public abstract class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Collaborator> collaborators;
    private AlertDialog alertDialog;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    public CollaboratorAdapter(Context context, ArrayList<Collaborator> collaborators) {
        this.context = context;
        this.collaborators = collaborators;
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Config.DB_USERS);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_picture) CircleImageView profilePicture;
        @BindView(R.id.collaborator_holder) LinearLayout row;
        @BindView(R.id.username) TextView username;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserInfo(getAdapterPosition());
                }
            });

        }

    }

    @Override
    public CollaboratorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collaborator, parent, false);
        return new CollaboratorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CollaboratorAdapter.MyViewHolder holder, final int position) {
        Picasso.with(context)
                .load(collaborators.get(position).getProfile_pic())
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.profilePicture);
        holder.username.setText(collaborators.get(position).getUsername());
    }
    @Override
    public int getItemCount() {
        return collaborators==null?0:collaborators.size();
    }

    private void showProfileDialog(final User user){
        View v = LayoutInflater.from(context).inflate(R.layout.profile_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ImageView close = (ImageView)v.findViewById(R.id.close_icon);
        CircleImageView profilePicture = (CircleImageView)v.findViewById(R.id.profile_picture);
        TextView username = (TextView)v.findViewById(R.id.username_profile);
        RelativeLayout call = (RelativeLayout)v.findViewById(R.id.call_container);
        RelativeLayout mail = (RelativeLayout)v.findViewById(R.id.mail_container);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermission(user.getPhoneNumber());
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, user.getProfileDisplayName() + " via Gerany App");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setData(Uri.parse("mailto:"+user.getProfileEmail()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                context.startActivity(intent);
            }
        });

        if (user.isAllowDisplayingEmail())
            mail.setVisibility(View.VISIBLE);
        else mail.setVisibility(View.GONE);

        if (user.isAllowDisplayingMobileNumber())
            call.setVisibility(View.VISIBLE);
        else call.setVisibility(View.GONE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        Picasso.with(context)
                .load(user.getProfilePhotoUrl())
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(profilePicture);
        username.setText(user.getProfileDisplayName());


        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void getUserInfo(final int position){
        final String uid = collaborators.get(position).getUser_id();
        Query query = userRef.orderByChild(Config.PROFILE_ID).equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnap: snapshot.getChildren()) {
                    User userTemp = userSnap.getValue(User.class);
                    if (userTemp.getProfileUid().equals(uid)){
                        showProfileDialog(userTemp);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public  abstract void callPermission(String phoneNumber);
}
