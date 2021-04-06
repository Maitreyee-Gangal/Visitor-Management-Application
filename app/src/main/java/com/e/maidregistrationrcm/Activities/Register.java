package com.e.maidregistrationrcm.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.e.maidregistrationrcm.others.Dilog_addemployer;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.EmployerVO;
import com.e.maidregistrationrcm.VOs.MaidVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Register extends AppCompatActivity {
    private static final int PORT = 7001;
    boolean flag = false;
    public static String[] buldingsTOSET = null;

    public static ArrayList<EmployerVO> employerArrayList = new ArrayList();
    public static MaidVO currentMaidVO = null;
    byte[] imgdata = null;
    private boolean isEditing = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri ImageURI;
    int permision = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.nav_register);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_register:
                         return true;

                    case R.id.nav_log:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_Search:
                        startActivity(new Intent(getApplicationContext(),Search.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_reports:
                        startActivity(new Intent(getApplicationContext(),Reports.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                        /*
                    case R.id.nav_setting:
                        startActivity(new Intent(getApplicationContext(),Setting.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                         */
                }
                return false;
            }
        });

        final ImageView yourphoto = findViewById(R.id.maidphoto);


        final ImageButton choose = findViewById(R.id.cohose);
        final EditText  token_Number = findViewById(R.id.token_Number123);
        final EditText  nameTxt = findViewById(R.id.Name);
        final EditText address = findViewById(R.id.your_adress);
        final EditText  mobile = findViewById(R.id.Phone_number);
        final EditText adhar = findViewById(R.id.Adhar_num);
        final Button save_btn = findViewById(R.id.save34);
        final AutoCompleteTextView  ocupation = findViewById(R.id.ocupation);

        final ImageView flagview = findViewById(R.id.falgpermision);
        token_Number.setText(Service.getnextInt().trim());
        flagview.setImageResource(R.drawable.ok_24);



        String[] occupation = {"Maid","Cook","Driver","CareGiver","Gardner","House Keeping","Watchman","Other"};

        ArrayAdapter<String> occupations = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,occupation);
        ocupation.setAdapter(occupations);

        try {
            buldingsTOSET = Service.getBuilding();
        }catch (Exception e){
           Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        if(currentMaidVO != null) {
            String token = ""+currentMaidVO.getToken();
            token_Number.setText(token);
            token_Number.setEnabled(false);
            nameTxt.setText(currentMaidVO.getName());
            address.setText(currentMaidVO.getAddress());
            mobile.setText(currentMaidVO.getMobile());
            adhar.setText(currentMaidVO.getAdharNo());
            ocupation.setText(currentMaidVO.getOcupation());
            employerArrayList = currentMaidVO.getEmployers();
            permision = currentMaidVO.getAccess();
            if(currentMaidVO.getAccess() == 0){
                flagview.setImageResource(R.drawable.no_24);
            }else {
                flagview.setImageResource(R.drawable.ok_24);
            }
            try {
                imgdata = Service.getPhoto(currentMaidVO.getImageRef());
                Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(imgdata));
                yourphoto.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            isEditing = true;
        }
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (token_Number.getText().toString().trim().length() < 1) {
                        token_Number.setError("Invalid Token Number");
                        return;
                    }

                    if (nameTxt.getText().toString().trim().length() < 4) {
                        nameTxt.setError("Invalid Name");
                        return;
                    }

                    if (address.getText().toString().trim().length() < 6) {
                        address.setError("Invalid Address");
                        return;
                    }

                    if (mobile.getText().toString().trim().length() < 9 || mobile.getText().toString().trim().length() > 11) {
                        mobile.setError("Invalid Mobile Number");
                        return;
                    }

                    if (adhar.getText().toString().trim().length() < 6) {
                        adhar.setError("Invalid Adhar Number");
                        return;
                    }
                    if (ocupation.getText().toString().trim().length() < 3) {
                        ocupation.setError("Invalid ocupation Number");
                        return;
                    }
                    if (flag = false) {
                        Toast.makeText(Register.this, "Take a photo", Toast.LENGTH_LONG).show();
                        return;
                    }

                            String mobileno = mobile.getText().toString();
                            String namei = nameTxt.getText().toString();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Cmd", "registorMaid");
                            //String imageref = ""+System.currentTimeMillis();
                            String imageref = "M"+token_Number.getText().toString().trim();

                            if(currentMaidVO != null) {
                                jsonObject.put("id", currentMaidVO.getMaidId()); // if update
                            }else {
                                jsonObject.put("id", 0); // if new entry
                            }

                            jsonObject.put("Permission",permision);
                            jsonObject.put("DeviceID",Service.readDeviceId());
                            jsonObject.put("token", token_Number.getText().toString().trim());
                            jsonObject.put("name", namei);
                            jsonObject.put("address", address.getText().toString());
                            jsonObject.put("mobile", mobileno);
                            jsonObject.put("adhar", adhar.getText().toString());
                            jsonObject.put("ocupation", ocupation.getText().toString());
                            jsonObject.put("imgref", imageref);
                            JSONArray jsonArray = Service.toJSONArray(employerArrayList);
                            jsonObject.put("employers", jsonArray.toJSONString());

                            System.out.println(jsonObject.toJSONString());

                            try {
                                System.out.println("ipAddress-->" + Service.IP);

                                String jsonReply = Service.send(jsonObject.toJSONString());
                                JSONParser jsonParser = new JSONParser();
                                JSONObject jsonResObj = (JSONObject)jsonParser.parse(jsonReply);
                                final int errorCode = Integer.parseInt(jsonResObj.get("ErrorCode").toString());
                                final String status = jsonResObj.get("Status").toString();
                                Register.this.runOnUiThread(new Runnable() {
                                    public void run() {

                                        if(errorCode == 0) {
                                            token_Number.setText("");
                                            nameTxt.setText("");
                                            address.setText("");
                                            mobile.setText("");
                                            adhar.setText("");
                                            ocupation.setText("");
                                            employerArrayList.clear();
                                            yourphoto.setImageResource(R.drawable.account_box_black_24dp);
                                            currentMaidVO = null;
                                            employerArrayList.clear();
                                            flag = false;
                                        }

                                        Toast.makeText(Register.this, status, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                if(errorCode == 0) {
                                    System.out.println("Sending image");
                                    Socket imgsocket = new Socket(Service.IP, 7002);
                                    DataOutputStream dout = new DataOutputStream(imgsocket.getOutputStream());
                                    dout.writeUTF("SaveImage");
                                    dout.writeUTF(imageref);
                                    if (imgdata == null) {
                                        imgdata = new byte[0];
                                    }
                                    dout.writeInt(imgdata.length);
                                    dout.write(imgdata);
                                    dout.flush();
                                    dout.close();
                                    imgsocket.close();
                                    imgdata = null;
                                }

                            } catch (final Exception e) {
                                e.printStackTrace();
                                Register.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        /*Toast toast = Toast.makeText(MainActivity.this,toshow, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        */
                                    }
                                });
                            }
            }
        });

        Button addemployer = findViewById(R.id.addEmployerBtn);

        addemployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dilog_addemployer dilog_addemployer = new Dilog_addemployer(Register.this);
                dilog_addemployer.show(getSupportFragmentManager(),"example");

            }
        });


        ImageButton takephoto = findViewById(R.id.tikeimagebtn);


        if (ContextCompat.checkSelfPermission(Register.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Register.this,
                    new String[]{

                            Manifest.permission.CAMERA
                  },100);

        }

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent,100);

                }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                final ImageView yourphoto = findViewById(R.id.maidphoto);

                Bitmap imgFromCamera = (Bitmap) data.getExtras().get("data");

                yourphoto.setImageBitmap(imgFromCamera);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imgFromCamera.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imgdata = stream.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("Request code"+requestCode);
        System.out.println("should be "+RESULT_OK);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            try {
                ImageURI = data.getData();
                final ImageView yourphoto = findViewById(R.id.maidphoto);
                yourphoto.setImageURI(ImageURI);

                InputStream iStream = getContentResolver().openInputStream(ImageURI);
                byte[] inputData = getBytes(iStream);
                imgdata = inputData;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        try {

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return byteBuffer.toByteArray();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_appbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.appBarLogout:
                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

}