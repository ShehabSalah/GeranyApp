package com.shehabsalah.geranyapp.views.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        @BindView(R.id.my_places_address) TextView placeAddres;
        @BindView(R.id.edit) ImageView edit;
        @BindView(R.id.delete_active) ImageView delete_active;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            //ToDo: set on click here
            placeHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    clearSelection();
                    toggleSelection(clickedPosition);
                    myPlacesController.getMyPlaces().get(activePosition).setActive(false);
                    myPlacesController.getMyPlaces().get(clickedPosition).setActive(true);
                    activePosition = clickedPosition;
                }
            });

            delete_active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    if (!isSelected(clickedPosition)){
                        Config.toastShort(context, "item to delete at position: "+ clickedPosition);
                        //ToDo: remeber to modify this lines ****()****
                        //ToDo: open delete warning message (DESIGN: #1)
                        getDeletedPositionItem(clickedPosition);
                    }else{
                        Config.toastShort(context, "just do nothing");
                    }
                }
            });
            //ToDo: set edit on click and implement the on click functionality
            //ToDo: open rename dialog (DESIGN: #2)
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
        holder.placeAddres.setText(myPlacesController.getMyPlaces().get(position ).getPlaceAddress());
        holder.delete_active.setImageDrawable(ContextCompat.getDrawable(
                context,
                isSelected(position)?R.drawable.ic_check_circle:R.drawable.ic_delete
        ));
        if (isSelected(position)){
            activePosition = holder.getAdapterPosition();
        }
    }

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

    private void getDeletedPositionItem(int position){
        if (position == myPlacesController.getMyPlaces().size() - 1) { // if last element is deleted, no need to shift
            deleteItem(position, position);
        } else if(position == 0){
            deleteItem(position, myPlacesController.getMyPlaces().size() - 1);
        } else{ // if the element deleted is not the last one
            int shift=1; // not zero, shift=0 is the case where position == dataList.size() - 1, which is already checked above
            while (true) {
                try {
                    deleteItem(position, position - shift);
                    break;
                } catch (IndexOutOfBoundsException e) { // if fails, increment the shift and try again
                    shift++;
                }
            }
        }
    }

    private void deleteItem(int position, int positionToDelete){
        if (myPlacesController.deleteMyPlaceAtPosition(context,positionToDelete)){
            notifyItemRemoved(position);

        }else{
            Config.toastShort(context, context.getResources().getString(R.string.no_internet));
        }
    }
}
