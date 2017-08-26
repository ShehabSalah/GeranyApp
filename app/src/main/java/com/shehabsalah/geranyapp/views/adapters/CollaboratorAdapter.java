package com.shehabsalah.geranyapp.views.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.model.Collaborator;
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

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Collaborator> collaborators;
    private AlertDialog alertDialog;

    public CollaboratorAdapter(Context context, ArrayList<Collaborator> collaborators) {
        this.context = context;
        this.collaborators = collaborators;
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
                    showProfileDialog(getAdapterPosition());
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

    private void showProfileDialog(final int position){
        View v = LayoutInflater.from(context).inflate(R.layout.profile_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();
    }
}
