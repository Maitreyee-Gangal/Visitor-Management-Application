package com.e.maidregistrationrcm.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.GateVisitorVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GateVisitor extends AppCompatActivity {
private static byte[] imgData = new byte[0];
    private static boolean imageTaken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_visitor);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_CAR);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_CAR:
                        return true;
                    case R.id.Helper:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        final Spinner types = findViewById(R.id.VehicleType);
        final EditText DriverNameTP = findViewById(R.id.driverNameTxt);
        final EditText DriverNumberTP = findViewById(R.id.driverNumberTxt);
        final EditText CarNumbeTP = findViewById(R.id.carNoTxt);
        final Spinner ResonTP = findViewById(R.id.reasonTxt);
        final AutoCompleteTextView BuldingTP = findViewById(R.id.buildingNameTxt);
        final Button button = findViewById(R.id.saveBtn);
        final ImageButton takephohtobtn = findViewById(R.id.takephotoGV);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Taxi");
        list.add("Car");
        list.add("Two Wheeler");
        list.add("Delivery Vehicle");
        list.add("Commercial vehicle");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        types.setAdapter(dataAdapter);


        takephohtobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,100);
            }
        });

        String[] buldings = new String[0];
        try {
             buldings = Service.getBuilding();
             ArrayAdapter<String> bulding = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,buldings);
            BuldingTP.setAdapter(bulding);

        }catch (Exception e){
            e.printStackTrace();
        }

        ImageButton imageButton = findViewById(R.id.scanner);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,101);
            }
        });
        String[] work = {"Drop/Pick","Delivery","Visit","Other"};
        ArrayAdapter<String> works = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,work);
        ResonTP.setAdapter(works);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DriverNameTP.getText().toString().length() == 0){
                    DriverNameTP.setError("Enter visitors Name");
                    return;

                }

                if(DriverNumberTP.getText().toString().length() < 9){
                    System.out.println(DriverNumberTP.getText().toString());
                    System.out.println(DriverNumberTP.getText().toString().length());
                    DriverNumberTP.setError("Invalid visitors Number");
                    return;


                }

                if(CarNumbeTP.getText().toString().length() < 6){
                    CarNumbeTP.setError("Invalid Car Number");
                    return;
                }
                System.out.println("btn clicked");
                String driverName = DriverNameTP.getText().toString();
                String driverNumber = DriverNumberTP.getText().toString();
                String carnumber  = CarNumbeTP.getText().toString();
                String bulding = BuldingTP.getText().toString();
                String reson = ResonTP.getSelectedItem().toString();
                String type = types.getSelectedItem().toString();

                GateVisitorVO gateVisitorVO = new GateVisitorVO();

                gateVisitorVO.setName(driverName);
                gateVisitorVO.setMobile(driverNumber);
                gateVisitorVO.setCarno(carnumber);
                gateVisitorVO.setBuilding(bulding);
                gateVisitorVO.setReason(reson);
                gateVisitorVO.setType(type);
                gateVisitorVO.setImage(imgData);
                Service.saveGatevisitor(gateVisitorVO);

                DriverNameTP.setText("");
                DriverNumberTP.setText("");
                CarNumbeTP.setText("");
                BuldingTP.setText("");
                final ImageView photo = findViewById(R.id.photo_GV);
                photo.setImageResource(R.drawable.account_box_black_24dp);
                Toast.makeText(GateVisitor.this,"Successful",Toast.LENGTH_LONG).show();
                
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                final ImageView photo = findViewById(R.id.photo_GV);

                Bitmap imgFromCamera = (Bitmap) data.getExtras().get("data");

                photo.setImageBitmap(imgFromCamera);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imgFromCamera.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imgData = stream.toByteArray();
                imageTaken = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if( requestCode == 101) {
            Bitmap imgFromCamera = (Bitmap) data.getExtras().get("data");
            
            System.out.println();
        }
    }
}