package com.e.maidregistrationrcm.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.maidregistrationrcm.others.CaptureImage;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.MaidVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayInputStream;

public class MainActivity extends AppCompatActivity {
    private static final int PORT = 7001;
    Integer maidId  = null;
    private static String imageref  = null;
    EditText tokenNumber = null;
    public static int roleId = 0;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        BottomNavigationView bottomNavigationViewurs = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_log);
        bottomNavigationView.setSelectedItemId(R.id.Helper);
        bottomNavigationView.setVisibility(View.GONE);
        bottomNavigationViewurs.setVisibility(View.GONE);
        if(roleId == 1){
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationViewurs.setVisibility(View.GONE);
        }else if(roleId == 2){
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavigationViewurs.setVisibility(View.VISIBLE);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Service.writeDeviceID(MainActivity.this);
        //System.out.println("device id"+Service.readDeviceId(MainActivity.this));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_log:
                        return true;
                    case R.id.nav_register:
                        startActivity(new Intent(getApplicationContext(),Register.class));
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
        bottomNavigationViewurs.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Helper:
                        return true;
                    case R.id.nav_CAR:
                        startActivity(new Intent(getApplicationContext(), GateVisitor.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
        //----------------------------------------------------------------------------------------------------


        ImageButton search_btn = findViewById(R.id.LSearch_btn);
        tokenNumber = findViewById(R.id.Sbytoken_number);
        final TextView name_lable = findViewById(R.id.nameLable);
        final TextView address_lable = findViewById(R.id.addresslable);
         final Button inbtn = findViewById(R.id.Savelog);
        final Button logout = findViewById(R.id.exit);
        final ImageView yourimage = findViewById(R.id.yourphoto);
        final ImageButton scanQR = findViewById(R.id.ScanQR);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    maidId = null;

                    String tokenstr = tokenNumber.getText().toString().trim();
                    Integer tokenNo = Integer.parseInt(tokenstr.trim());

                    maidId = Service.getMaidId(tokenNo);

                    MaidVO maidVO = null;

                    maidVO =  Service.getMaidInfo(maidId);
                    Intent intent = new Intent(getApplicationContext(), LogId.class);
                    startActivity(intent);
                    LogId.maidVOC = maidVO;
                    LogId.maidId = maidId;
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });



        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setCaptureActivity(CaptureImage.class);
                integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scanning Code");
                integrator.initiateScan();

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        TextView device = v.findViewById(R.id.deviceIdND);
        TextView name = v.findViewById(R.id.nameND);
        device.setText(""+Service.readDeviceId());
        name.setText("Arnav Badhe");

        ImageView imageView = v.findViewById(R.id.profilePhoto);
        imageView.setImageResource(R.drawable.profile_24);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_devices){
                    if(roleId == 1) {
                        Intent intent = new Intent(getApplicationContext(), Devices.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this,"User Don't have privileges",Toast.LENGTH_LONG).show();
                    }
                    }else if(item.getItemId() == R.id.nav_blacklist){
                    if(roleId == 1) {
                        Intent intent = new Intent(getApplicationContext(), Blacklist.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this,"User Don't have privileges",Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        System.out.println("Qr text"+result.getContents());
        try {
            TextView tokenNumber = findViewById(R.id.Sbytoken_number);
            String readData = result.getContents();
            String[] edf = readData.split(",");
            Integer maidId = Integer.parseInt(edf[0].trim());
            System.out.println(maidId);
            MaidVO maidVO = Service.getMaidInfo(maidId);
            tokenNumber.setText(""+maidVO.getToken());
            Intent intent = new Intent(getApplicationContext(),LogId.class);
            startActivity(intent);
            startActivity(intent);
            LogId.maidVOC = maidVO;
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(roleId == 2) {
            menuInflater.inflate(R.menu.menu_appbar, menu);
        }else {
            menuInflater.inflate(R.menu.menu_superuser, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.appBarLogout:
                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.appBarshortcut:
                Intent intent2 = new Intent(getApplicationContext(),Devices.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
