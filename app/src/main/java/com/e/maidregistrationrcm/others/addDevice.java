package com.e.maidregistrationrcm.others;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.e.maidregistrationrcm.Activities.Devices;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.VOs.DevicesVO;

import java.util.ArrayList;

public class addDevice extends AppCompatDialogFragment {
private static Context context;
private static int position = -1;
private static ArrayList<DevicesVO> devicesVOS;
private static int wth = -1;
private static ListView listView;
    public addDevice(Context context,int position,ArrayList<DevicesVO> devicesVOS,int wtdo,ListView listView){
        this.context = context;
        this.position = position;
        this.devicesVOS = devicesVOS;
        this.wth = wtdo;
        this.listView = listView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] info = new String[3];

        final AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_add_device,null);
        final EditText deviceId = view.findViewById(R.id.D_id);
        final EditText name = view.findViewById(R.id.D_Name);
        final AutoCompleteTextView spinner = view.findViewById(R.id.spinner_acsses);
        String[] strings1 = {"A","W","A&W"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,strings1);
        spinner.setAdapter(arrayAdapter);

        if(wth == 1){
            DevicesVO devicesVO = devicesVOS.get(position);
            deviceId.setText(devicesVO.getDeviceid());
            name.setText(devicesVO.getName());
            int ac = Integer.parseInt(devicesVO.getStatus().trim());
            if(ac == 1){
                spinner.setText("A");
            }else if(ac == 2){
                spinner.setText("W");
            }else if(ac == 3){
                spinner.setText("A&W");
            }
        }

        Button button = view.findViewById(R.id.D_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicesVOS.remove(position);
                populateList(devicesVOS);
            }
        });
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
                        info[0] = deviceId.getText().toString();
                        info[1] = name.getText().toString().trim();
                        info[2] = spinner.getText().toString().trim();

                        int stat = -1;
                        if(info[2].trim().equalsIgnoreCase("A")){
                            stat = 1;
                        }else if(info[2].trim().equalsIgnoreCase("W")){
                            stat = 2;
                        }else if(info[2].trim().equalsIgnoreCase("A&W")){
                            stat = 3;
                        }
                        System.out.println(info[0]);
                        System.out.println(info[1]);
                        System.out.println(info[2]);
                        System.out.println(stat);
                    if(wth == 1){
                        devicesVOS.get(position).setDeviceid(info[0]);
                        devicesVOS.get(position).setName(info[1]);
                        devicesVOS.get(position).setStatus(stat+"");
                        populateList(devicesVOS);
                    }else if(wth == 0){
                        DevicesVO devicesVO = new DevicesVO();
                        devicesVO.setDeviceid(info[0]);
                        devicesVO.setName(info[1]);
                        devicesVO.setStatus(stat+"");
                        devicesVOS.add(devicesVO);
                        populateList(devicesVOS);
                    }
                    }
                });
        return bilder.create();
    }
    public void populateList(ArrayList<DevicesVO> devicesVOS){
        Devices.devicesVOS = devicesVOS;
        CListAdapter cListAdapter = new CListAdapter(context,devicesVOS,R.layout.list_adapter_template);
        this.listView.setAdapter(cListAdapter);
    }
}