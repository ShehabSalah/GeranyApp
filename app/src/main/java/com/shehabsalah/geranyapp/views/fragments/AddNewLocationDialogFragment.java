package com.shehabsalah.geranyapp.views.fragments;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.shehabsalah.geranyapp.R;
/**
 * Created by ShehabSalah on 8/2/17.
 * This class responsible on creating BottomSheet for add new location tab
 */

public class AddNewLocationDialogFragment extends BottomSheetDialogFragment {

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.add_new_place_button_sheet, null);
        dialog.setContentView(contentView);
        ImageView renameTrue = (ImageView) contentView.findViewById(R.id.rename_location_true);
        ImageView renameClose = (ImageView) contentView.findViewById(R.id.rename_location_close);

        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        renameClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                    ((Callback) getActivity()).onClickCallBack(false);
                }
            }
        });
        renameTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                    ((Callback) getActivity()).onClickCallBack(true);
                }
            }
        });
    }
    public interface Callback {
        /**
         * This method called from BottomSheet fragment after the user choose if he want to rename his
         * new place or not. This method will be implemented in AddNewLocationFragment.
         * @param renamePlace boolean variable its value passed by BottomSheet with true if the place will rename
         * */
        void onClickCallBack(boolean renamePlace);
    }
}