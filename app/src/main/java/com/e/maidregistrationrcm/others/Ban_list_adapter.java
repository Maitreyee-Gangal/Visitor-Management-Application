package com.e.maidregistrationrcm.others;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.VOs.BlackListVO;

import java.util.ArrayList;

public class Ban_list_adapter extends ArrayAdapter<BlackListVO> {
    private int resorce;
    private ArrayList<BlackListVO> blackListVOS;
    public Ban_list_adapter(Context context, int resource, ArrayList<BlackListVO> records) {
        super(context, resource, records);
        this.resorce = resource;
        this.blackListVOS = records;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resorce, parent,false);
        String type = getItem(position).getType().trim();
        if(type.equalsIgnoreCase("blacklist")){

            int token = getItem(position).getToken();
            String name = getItem(position).getName();
            String banTill = getItem(position).getToDate().toLocaleString();

            TextView nameTF = convertView.findViewById(R.id.nameBAN);
            TextView banto = convertView.findViewById(R.id.BANTILL);

            banto.setTextColor(Color.parseColor("#ff6f3b"));
            nameTF.setText(token+" "+name);
            banto.setText(banTill);

        }else {
            int token = getItem(position).getToken();
            String name = getItem(position).getName();
            String banTill = "BANED MANUALLY";

            TextView nameTF = convertView.findViewById(R.id.nameBAN);
            TextView banto = convertView.findViewById(R.id.BANTILL);
            banto.setTextColor(Color.RED);
            nameTF.setText(token+" "+name);
            banto.setText(banTill);
        }


        return convertView;
    }


}
