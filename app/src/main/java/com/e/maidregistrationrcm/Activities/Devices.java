package com.e.maidregistrationrcm.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.e.maidregistrationrcm.others.CListAdapter;
import com.e.maidregistrationrcm.others.Dilog_pascode;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.DevicesVO;
import com.e.maidregistrationrcm.others.addDevice;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Devices extends AppCompatActivity {
public static ArrayList<DevicesVO> devicesVOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        /*ProgressBar progressBar = findViewById(R.id.hellopb);
        WanderingCubes foldingCube = new WanderingCubes();
        progressBar.setIndeterminateDrawable(foldingCube);

        try{
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }

        progressBar.setVisibility(View.GONE);*/

        LinearLayout linearLayout = findViewById(R.id.mainlayoutD);
        linearLayout.setVisibility(View.VISIBLE);

        final ListView listView = findViewById(R.id.list_devices);
        devicesVOS = Service.getdevicesList();

        Button addbtn = findViewById(R.id.add_device);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice addDevice = new addDevice(Devices.this,0,devicesVOS,0,listView);
                addDevice.show(getSupportFragmentManager(),"example");
            }
        });

        CListAdapter cListAdapter = new CListAdapter(getApplicationContext(),devicesVOS,R.layout.list_adapter_template);
        listView.setAdapter(cListAdapter);
        final Context context = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addDevice addDevice = new addDevice(context,position,devicesVOS,1,listView);
                addDevice.show(getSupportFragmentManager(),"example");
            }
        });

        Button updatebtn = findViewById(R.id.update_changes);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                Executor executor = Executors.newSingleThreadExecutor();
                BiometricPrompt prompt = new BiometricPrompt.Builder(Devices.this)
                        .setTitle("FingerPrint")
                        .setDescription("let us confirm that its you")
                        .setNegativeButton("Use Password Insted", executor, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dilog_pascode addDevice = new Dilog_pascode(devicesVOS);
                                addDevice.show(getSupportFragmentManager(),"example");
                            }
                        }).build();

                prompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        super.onAuthenticationHelp(helpCode, helpString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        System.out.println("succes");
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_idbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backtosearch:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}