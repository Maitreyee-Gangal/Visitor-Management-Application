package com.e.maidregistrationrcm.others;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.DevicesVO;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Dilog_pascode extends AppCompatDialogFragment {
    ArrayList<DevicesVO> devicesVOS;

public Dilog_pascode(ArrayList<DevicesVO> devicesVOS){
    this.devicesVOS = devicesVOS;
}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_pascode,null);
        final TextInputLayout textInputLayout = view.findViewById(R.id.P_password);

        bilder.setView(view)
                .setTitle("Add Device")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String password = textInputLayout.getEditText().getText().toString().trim();
                       if(password.equals("root")){
                        Service.saveDevices();
                       }
                    }
                });
        return bilder.create();
    }
}