package com.example.vivekgoel.mapdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vivekgoel on 3/1/16.
 */
public class CarParkedDialog extends android.app.DialogFragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       return inflater.inflate(R.layout.car_parked_dialog,null);
    }


//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Done Parking ??")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // FIRE ZE MISSILES!
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }
}
