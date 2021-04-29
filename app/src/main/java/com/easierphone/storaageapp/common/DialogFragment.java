package com.easierphone.storaageapp.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.easierphone.storaageapp.misc.Utils;

/**
 * Created by Ayyaz on 12/06/16.
 */

public class DialogFragment extends AppCompatDialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!getShowsDialog()){
            return;
        }
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                try{
                    tintButtons(getDialog());
                } catch (Exception e){

                }
            }
        });
    }

    public static void tintButtons(Dialog dialog){
        Utils.tintButton(getButton(dialog, DialogInterface.BUTTON_POSITIVE));
        Utils.tintButton(getButton(dialog, DialogInterface.BUTTON_NEGATIVE));
        Utils.tintButton(getButton(dialog, DialogInterface.BUTTON_NEUTRAL));
    }

    private static Button getButton(Dialog dialog, int which){
        return ((AlertDialog)dialog).getButton(which);
    }
}
