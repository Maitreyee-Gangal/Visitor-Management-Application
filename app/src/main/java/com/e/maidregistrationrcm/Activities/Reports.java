package com.e.maidregistrationrcm.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Reports extends AppCompatActivity {
    //implements NavigationView.OnNavigationItemSelectedListener
    private static final int PORT = 7001;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.nav_reports);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_reports:
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
                    case R.id.nav_register:
                        startActivity(new Intent(getApplicationContext(),Register.class));
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
        ImageButton button = findViewById(R.id.search);

        final EditText searchby = findViewById(R.id.date);
        searchby.setInputType(InputType.TYPE_NULL);
        final WebView webView = findViewById(R.id.tableview);

        final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");

                searchby.setText(sdf.format(cal.getTime()));
            }

        };

        searchby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                // TODO Auto-generated method stub
                new DatePickerDialog(Reports.this,myDateListener,year,month,day).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try{

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("DeviceID", Service.readDeviceId());
                            jsonObject.put("Cmd","getReports");
                            jsonObject.put("SearchBy",searchby.getText().toString());
                            System.out.println(searchby.getText().toString());
                            Socket socket = new Socket(Service.IP,PORT);
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            dataOutputStream.writeUTF(jsonObject.toJSONString());

                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String reply = dataInputStream.readUTF();
                            JSONParser jsonParser = new JSONParser();
                            JSONObject relpyobj = (JSONObject) jsonParser.parse(reply);
                            final String html = relpyobj.get("Report").toString();
                            Reports.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    webView.loadData(html, "text/html", "UTF-8");
                                    System.out.println(html);
                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };thread.start();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
