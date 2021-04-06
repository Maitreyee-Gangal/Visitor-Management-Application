package com.e.maidregistrationrcm.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.e.maidregistrationrcm.others.CaptureImage;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.MaidVO;
import com.e.maidregistrationrcm.VOs.VisitorVO;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Aquila_reg extends AppCompatActivity {
    public static int id = -1;
    public static byte[] imgdata = null;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),LogIn.class);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquila_reg);

        final LinearLayout linearLayout = findViewById(R.id.Aquila_moreInfoPanel);
        final Switch regswitch = findViewById(R.id.registered);
        final ImageView imageView = findViewById(R.id.Aquila_ischeakedIN);

        final ImageButton searchbtn = findViewById(R.id.Aquila_searchbtn);
        final Button in_btn = findViewById(R.id.Aquila_IN);
        final ImageButton scanQr  = findViewById(R.id.Aquila_Scanebtn);
        final EditText token = findViewById(R.id.Aquila_Token);
        final EditText temp = findViewById(R.id.Aquila_temp);
        final EditText o2 = findViewById(R.id.Aquila_o2Level);
        final EditText name = findViewById(R.id.Aquila_name);
        final EditText phone = findViewById(R.id.Aquila_phone);
        final EditText address = findViewById(R.id.Aquila_address);
        final ImageView yourphoto = findViewById(R.id.visitorPhoto);

        o2.setText("98");
        temp.setText("38");
        scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Aquila_reg.this);
                integrator.setCaptureActivity(CaptureImage.class);
                integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scanning Code");
                integrator.initiateScan();

            }
        });

        regswitch.setChecked(true);
        regswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cheaked = regswitch.isChecked();
                if(cheaked) {
                    linearLayout.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ok_24);
                    searchbtn.setEnabled(true);
                    try{
                        token.setEnabled(true);
                        token.setText("");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.flag_24);
                    searchbtn.setEnabled(false);
                    token.setText("9000");
                    token.setEnabled(false);
                    in_btn.setEnabled(true);
                    temp.setText("38");
                    o2.setText("98");
                }
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reply = Service.confirmEntry(Integer.parseInt(token.getText().toString().trim()));
                //System.out.println(" rejvvj"+reply);
                if(reply != -1) {
                    imageView.setImageResource(R.drawable.ok_24);
                    in_btn.setEnabled(true);
                }else {
                    imageView.setImageResource(R.drawable.no_24);
                    in_btn.setEnabled(false);
                }
            }
        });

        in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isRegisteredChecked = regswitch.isChecked();
                int cheaked = 0;
                if(isRegisteredChecked){
                    cheaked = 0;
                }else {
                    cheaked = 1;
                }
                VisitorVO visitorVO = new VisitorVO();

                visitorVO.setToken(Integer.parseInt(token.getText().toString().trim()));
                visitorVO.setTemperature(temp.getText().toString());
                visitorVO.setO2level(o2.getText().toString());

                visitorVO.setName(name.getText().toString());
                visitorVO.setPhone(phone.getText().toString());
                visitorVO.setAddress(address.getText().toString());

                int i = Service.saveVisitor(visitorVO,cheaked);
                if(i == 0 && regswitch.isChecked()){
                    token.setText("");
                    temp.setText("38.2");
                    o2.setText("98");
                    Toast.makeText(Aquila_reg.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                }else if(i == 0 && regswitch.isChecked() == false){
                    token.setText("9000");
                    temp.setText("38.2");
                    o2.setText("98");
                    name.setText("");
                    phone.setText("");
                    address.setText("");
                    saveImage(Aquila_reg.imgdata);
                    yourphoto.setImageResource(R.drawable.account_box_black_24dp);
                    Toast.makeText(Aquila_reg.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                } else if(i == 1){
                    Toast.makeText(Aquila_reg.this,"Registration Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton takepick = findViewById(R.id.takePhoto);
        takepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        try {
            if(requestCode == 100){
                 ImageView yourphoto = findViewById(R.id.visitorPhoto);

                Bitmap imgFromCamera = (Bitmap) data.getExtras().get("data");

                yourphoto.setImageBitmap(imgFromCamera);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imgFromCamera.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imgdata = stream.toByteArray();
            }else {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
                System.out.println("Qr text"+result.getContents());
                EditText editText = findViewById(R.id.Aquila_Token);
                final Button in_btn = findViewById(R.id.Aquila_IN);
                ImageView imageView = findViewById(R.id.Aquila_ischeakedIN);
                String readData = result.getContents();
                String[] edf = readData.split(",");
                Integer maidId = Integer.parseInt(edf[0].trim());
                System.out.println(maidId);
                MaidVO maidVO = Service.getMaidInfo(maidId);
                editText.setText("" + maidVO.getToken());

                int reply = Service.confirmEntry(Integer.parseInt(editText.getText().toString().trim()));
                System.out.println(" rejvvj" + reply);
                if (reply != -1) {
                    imageView.setImageResource(R.drawable.ok_24);
                    in_btn.setEnabled(true);
                } else {
                    imageView.setImageResource(R.drawable.no_24);
                    in_btn.setEnabled(false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_idbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backtosearch:
                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public static void saveImage(byte[] bytes){
        String imageref = "V"+Aquila_reg.id;
        try{
            Socket imgsocket = new Socket(Service.IP, 7002);
            DataOutputStream dout = new DataOutputStream(imgsocket.getOutputStream());
            dout.writeUTF("SaveImage");
            dout.writeUTF(imageref);
            if (bytes == null) {
                bytes = new byte[0];
            }
            dout.writeInt(bytes.length);
            dout.write(bytes);
            dout.flush();
            dout.close();
            imgsocket.close();
            bytes = null;

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void setId(int id){
        Aquila_reg.id = id;
    }
}