package com.e.maidregistrationrcm.others;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatDialogFragment;


import com.e.maidregistrationrcm.Activities.Register;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.VOs.EmployerVO;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class Dilog_addemployer extends AppCompatDialogFragment {
   Context context;
   EmployerVO currentEmployerVO = null;
   public static ListView listView = null;
   AutoCompleteTextView Address = null;
   private int positionSTV = -1;
    public Dilog_addemployer(Context context){
        this.context = context;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_addemployer,null);
        final Button delete_btn = view.findViewById(R.id.Delete);
        delete_btn.setEnabled(false);
        final Button savebtn = view.findViewById(R.id.pop_addemployerbtn);
        listView = view.findViewById(R.id.employersList);
        final EditText Name = view.findViewById(R.id.Emplyers_Name);
        Address = view.findViewById(R.id.Employers_Address);
        final EditText Mobile = view.findViewById(R.id.Employers_Phone_Number);
        populateListView(Register.employerArrayList,context,listView);
        final EditText Email = view.findViewById(R.id.Employers_email);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,Register.buldingsTOSET);
        Address.setAdapter(arrayAdapter);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String address = Address.getText().toString();
                String mobile = Mobile.getText().toString();
                String mail = Email.getText().toString();

                boolean newEntry = false;

                if(currentEmployerVO == null){
                    currentEmployerVO = new EmployerVO();
                    newEntry = true;
                }

                currentEmployerVO.setName(name);
                currentEmployerVO.setAddress(address);
                currentEmployerVO.setMobile(mobile.trim());
                currentEmployerVO.setEmail(mail);
                if(newEntry) {
                    Register.employerArrayList.add(currentEmployerVO);
                }
                populateListView(Register.employerArrayList,context,listView);

                Name.setText("");
                Address.setText("");
                Mobile.setText("");
                Email.setText("");
                currentEmployerVO = null;
            }
        });
            final int position = -1;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                EmployerVO employerVO = Register.employerArrayList.get(position);
                currentEmployerVO = employerVO;
                String mobile = employerVO.getMobile();
                String address = employerVO.getAddress();
                String name = employerVO.getName();
                String mail = employerVO.getEmail();
                Name.setText(name);
                Address.setText(address);
                Mobile.setText(mobile);
                Email.setText(mail);
                populateListView(Register.employerArrayList,context,listView);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                delete_btn.setEnabled(true);
                positionSTV = position;

                return true;
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Register.employerArrayList.remove(positionSTV);
            delete_btn.setEnabled(false);
            populateListView(Register.employerArrayList,context,listView);
            Name.setText("");
            Mobile.setText("");
            Address.setText("");
            Email.setText("");
            }
        });

        bilder.setView(view)
                .setTitle("Add Employer")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        }
                });
        return bilder.create();

     }
    public static void populateListView(ArrayList<EmployerVO> arrayList,Context context,ListView listView){
        ArrayList names = new ArrayList();
        for(int i = 0;i<arrayList.size();i++) {
            EmployerVO employerVO = arrayList.get(i);
            names.add(employerVO.getName());
        }
        ListAdapter adapter = new ArrayAdapter(context, simple_list_item_1,names);
        listView.setAdapter(adapter);
    }

}