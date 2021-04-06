package com.e.maidregistrationrcm.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.VOs.DevicesVO;

import java.util.ArrayList;

public class CListAdapter extends ArrayAdapter<DevicesVO> {
private int resourceID;
private ArrayList<DevicesVO> records;
    public CListAdapter(Context context,ArrayList<DevicesVO> records,int resourceID) {
        super(context,resourceID,records);
        this.resourceID = resourceID;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resourceID, parent,false);
        TextView deviceID = convertView.findViewById(R.id.lat_deviceId);
        TextView name = convertView.findViewById(R.id.lat_name);
        TextView status = convertView.findViewById(R.id.lat_state);
        System.out.println("posiyion"+position);

        String namestr = getItem(position).getName();
        String deviceIDstr = getItem(position).getDeviceid();
        int ac = Integer.parseInt(getItem(position).getStatus().trim());

        deviceID.setText(deviceIDstr);
        name.setText("Name: "+namestr);
        status.setText("Status: "+"inactive");

        if(ac > 0){
        status.setText("Status:"+"active");
        }

        return convertView;
    }
}