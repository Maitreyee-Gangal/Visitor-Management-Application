package com.e.maidregistrationrcm.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.UserVO;
import com.google.android.material.textfield.TextInputLayout;

import java.io.DataOutputStream;
import java.net.Socket;

public class LogIn extends AppCompatActivity {
    int STORAGE_PERMISSION_CODE = 1;
    String DeviceID = "Error Restart the app";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView version = findViewById(R.id.versionLable);
        version.setText("Version:"+ Service.VERSION);


        TextView deviceId = findViewById(R.id.deviceId);
        if (ContextCompat.checkSelfPermission(LogIn.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(LogIn.this, "You have already granted this permission!",Toast.LENGTH_SHORT).show();
            try {
                Service.generateDeviceID();
                DeviceID = Service.readDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            requestStoragePermission();
        }
        /*
        try{

            UserVO userVO = Service.readUserInfo();
            int rollid = userVO.getRoleId();
            if(rollid == 1){
                MainActivity.roleId = 1;
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }else if(rollid == 2){
                MainActivity.roleId = 2;
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        */

        Service.generateDeviceID();
        DeviceID = Service.readDeviceId();

        final TextInputLayout username = findViewById(R.id.Username);
        final TextInputLayout password = findViewById(R.id.password);
        Button button = findViewById(R.id.LogIn);
        deviceId.setText("Device ID:"+DeviceID);
        Service.generateDeviceID();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                        UserVO userVO = Service.login(DeviceID,username.getEditText().getText().toString().trim(),password.getEditText().getText().toString().trim(),Service.VERSION);

                        Toast.makeText(LogIn.this, "Verification Successful", Toast.LENGTH_LONG).show();

                        if(userVO.getRoleId() == 1 || userVO.getRoleId() == 2) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            MainActivity.roleId = userVO.getRoleId();
                            startActivity(intent);
                            finish();
                        }

                        if(userVO.getRoleId() == 3){
                            Intent intent = new Intent(getApplicationContext(),Aquila_reg.class);
                            startActivity(intent);
                            finish();
                        }


                } catch (Exception e) {
                    e.printStackTrace();
                Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LogIn.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                try {
                    Service.generateDeviceID();
                    DeviceID = Service.readDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_startup,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.server_staus:
                Intent intent = new Intent(getApplicationContext(),Server_status.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}



        