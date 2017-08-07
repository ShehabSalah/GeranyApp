package com.shehabsalah.geranyapp.views.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shehabsalah.geranyapp.R;
import com.shehabsalah.geranyapp.controllers.MyPlacesController;
import com.shehabsalah.geranyapp.util.Config;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 8/5/17.
 * recyclerView
 */

public class MyPlacesListAdapter extends SelectableAdapter<MyPlacesListAdapter.MyViewHolder> {
    private MyPlacesController myPlacesController;
    private Context context;
    private int activePosition = 0;
    private AlertDialog alertDialog;

    public MyPlacesListAdapter(MyPlacesController myPlacesController, Context context) {
        this.myPlacesController = myPlacesController;
        this.context = context;
        getSelectedPlace();
        clearSelection();
        toggleSelection(activePosition);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_places_item_holder) RelativeLayout placeHolder;
        @BindView(R.id.my_places_address_nickname) TextView nickname;
        @BindView(R.id.my_places_address) TextView placeAddress;
        @BindView(R.id.edit) ImageView edit;
        @BindView(R.id.delete_active) ImageView delete_active;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            placeHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    clearSelection();
                    toggleSelection(clickedPosition);
                    myPlacesController.getMyPlaces().get(activePosition).setActive(false);
                    myPlacesController.getMyPlaces().get(clickedPosition).setActive(true);
                    activePosition = clickedPosition;
                    String placeName = myPlacesController.getMyPlaces().get(activePosition).getPlaceNickname();
                    if (placeName == null || placeName.isEmpty())
                        placeName = myPlacesController.getMyPlaces().get(activePosition).getPlaceAddress();
                    Config.toastShort(context,
                            String.format(context.getString(R.string.selected_place_message),
                                    placeName));
                    //add selected message
                }
            });

            delete_active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    if (!isSelected(clickedPosition)){
                        deleteAlert(clickedPosition);
                    }
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    showRenamePlaceFormDialog(clickedPosition);
                }
            });
        }

    }

    @Override
    public MyPlacesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_places_item_view, parent, false);
        return new MyPlacesListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyPlacesListAdapter.MyViewHolder holder, final int position) {
        holder.nickname.setText(myPlacesController.getMyPlaces().get(position).getPlaceNickname());
        holder.placeAddress.setText(myPlacesController.getMyPlaces().get(position ).getPlaceAddress());
        holder.delete_active.setImageDrawable(ContextCompat.getDrawable(
                context,
                isSelected(position)?R.drawable.ic_check_circle:R.drawable.ic_delete
        ));
        if (isSelected(position)){
            activePosition = holder.getAdapterPosition();
        }
    }

    /**
     * Method that loop on the places list and check if it active or not. If it active add the value
     * of the active position to activePosition variable.
     * NOTE: ActivePosition is the position that the user currently used and participate in it.
     * */
    private void getSelectedPlace(){
        for (int i = 0; i < myPlacesController.getMyPlaces().size(); i++){
            if (myPlacesController.getMyPlaces().get(i).isActive()){
                activePosition = i;
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return myPlacesController.getMyPlaces()==null?0:myPlacesController.getMyPlaces().size();
    }

    /**
     * This method indicate the position of the place that the user clicked on it and want to delete
     * it and delete it, then shift the rest of the list smoothly.
     * */
    private void getDeletedPositionItem(int position){
        if (position == myPlacesController.getMyPlaces().size() - 1) { // if last element is deleted, no need to shift
            deleteItem(position, 0);
        } else if(position == 0){
            deleteItem(position, 1);
        } else{ // if the element deleted is not the last one
            int shift=1; // not zero, shift=0 is the case where position == dataList.size() - 1, which is already checked above
            while (true) {
                try {
                    deleteItem(position,  shift);
                    break;
                } catch (IndexOutOfBoundsException e) { // if fails, increment the shift and try again
                    shift++;
                }
            }
        }
    }

    /**
     * This method delete the item at given position, shift the selected position indicator and
     * notify the adapter that the data changed.
     * @param position index of the item to delete.
     * @param shift number needed to shift the selected position indicator.
     * */
    private void deleteItem(int position,  int shift){
        if (myPlacesController.deleteMyPlaceAtPosition(context,position)){
            notifyItemRemoved(position);
            notifyItemRangeChanged(0, getItemCount());
            clearSelection();
            if(activePosition == 0 || position > activePosition){
                toggleSelection(activePosition);
            }else{
                toggleSelection(activePosition - shift);
            }
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
        String placeName = myPlacesController.getMyPlaces().get(position).getPlaceNickname();
        if (placeName == null || placeName.isEmpty())
            placeName = myPlacesController.getMyPlaces().get(position).getPlaceAddress();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(context.getString(R.string.delete_place_header))
                .setMessage(String.format(context.getString(R.string.delete_place_message), placeName))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        getDeletedPositionItem(position);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(ContextCompat.getDrawable(context, R.drawable.ic_location_off))
                .show();
    }

    /**
     * This method will display the (Edit Location) form to enable users to enter their preferred name
     * to their current location and save it.
     * */
    private void showRenamePlaceFormDialog(final int position){
        View v = LayoutInflater.from(context).inflate(R.layout.add_new_place_rename_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText placeName = (EditText)v.findViewById(R.id.edit_text_location_dialog);
        TextView cancel = (TextView)v.findViewById(R.id.cancel_button_location_dialog);
        TextView save = (TextView)v.findViewById(R.id.save_button_location_dialog);
        TextView currentLocationTextView = (TextView)v.findViewById(R.id.current_location_edit_dialog);
        final TextView textLength = (TextView)v.findViewById(R.id.edit_text_length);

        currentLocationTextView.setText(myPlacesController.getMyPlaces().get(position).getPlaceAddress());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlaceName = placeName.getText().toString().trim();
                //Check if the user entered a non-white space characters
                if (!newPlaceName.isEmpty() && myPlacesController.getMyPlaces().get(position)!=null){
                    myPlacesController.updatePlaceNickname(myPlacesController.getMyPlaces().get(position), newPlaceName);
                    notifyDataSetChanged();
                    //Dismiss the dialog
                    alertDialog.dismiss();
                }else{
                    Config.toastLong(context, context.getString(R.string.empty_name_error_message));
                }
            }
        });

        placeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //update max length text view (0 / 50) with the entered text length
                textLength.setText(String.format(context.getString(R.string.formatting_max_length), s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setView(v);
        alertDialog = builder.create();
        alertDialog.show();
    }

}
