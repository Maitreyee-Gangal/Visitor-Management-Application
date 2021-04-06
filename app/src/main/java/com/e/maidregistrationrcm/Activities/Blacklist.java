package com.e.maidregistrationrcm.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.e.maidregistrationrcm.others.Ban_list_adapter;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.BlackListVO;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Blacklist extends AppCompatActivity {
public static int PERMISSION = -1;
public static int MAIDID = 0;
public static boolean isManualChecked = false;
public static boolean editingFORMALIST = false;
public static Date TODateTime = null;
public static Date FROMDateTime = null;
public static TextView TODATE = null;
public static TextView FROMDATE = null;
private DrawerLayout drawer;
private static int ID = 0;
private LinearLayout layout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        final Button select_FROMDATE = findViewById(R.id.from);
        final Button select_TODATE = findViewById(R.id.todate);
        final Button add = findViewById(R.id.addTObanList);
        final Button remove = findViewById(R.id.removeFROMbanlist);
        TODATE = findViewById(R.id.todateTV);
        FROMDATE = findViewById(R.id.fromDateTV);
        final TextView TODATE = findViewById(R.id.todateTV);
        final EditText currentedidting = findViewById(R.id.tokenBAN);

        final Switch banfroEver = findViewById(R.id.banforever);

        final ListView banList = findViewById(R.id.banList);
        layout = findViewById(R.id.mainlayoutBAN);
        ArrayList<BlackListVO> blackListVOS = Service.getAllBan();
        Ban_list_adapter adapter = new Ban_list_adapter(Blacklist.this,R.layout.ban_list_adapter,blackListVOS);
        banList.setAdapter(adapter);
        select_TODATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                // TODO Auto-generated method stub
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                TODateTime = cal.getTime();
                                System.out.println(TODateTime.toLocaleString());
                                starttime(Blacklist.this,0);
                            }

                        };

                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        // TODO Auto-generated method stub
                        new DatePickerDialog(Blacklist.this,myDateListener,year,month,day).show();


            }
        });
        select_FROMDATE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar cal = Calendar.getInstance();
                            // TODO Auto-generated method stub
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, monthOfYear);
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");
                            System.out.println(sdf.format(cal.getTime()));
                            FROMDateTime = cal.getTime();
                            System.out.println(FROMDateTime.toLocaleString());
                            starttime(Blacklist.this,1);
                        }

                    };

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    // TODO Auto-generated method stub
                    new DatePickerDialog(Blacklist.this,myDateListener,year,month,day).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlackListVO blackListVO = new BlackListVO();
                blackListVO.setMaidId(MAIDID);
                blackListVO.setFromDate(FROMDateTime);
                blackListVO.setToDate(TODateTime);
                blackListVO.setId(ID);
                blackListVO.setToken(Integer.parseInt(currentedidting.getText().toString().trim()));
                Service.addToBlacklist(blackListVO);
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
    public static void starttime(Context context, final int type){
        Calendar calendar = Calendar.getInstance();
        int MIN = calendar.get(Calendar.MINUTE);
        int HR = calendar.get(Calendar.HOUR_OF_DAY);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
               if(type == 0){
                   TODateTime.setHours(hourOfDay);
                   TODateTime.setMinutes(minute);
                   System.out.println("final "+TODateTime.toLocaleString());
                   TODATE.setText(simpleDateFormat.format(TODateTime));
               }else{
                   FROMDateTime.setHours(hourOfDay);
                   FROMDateTime.setMinutes(minute);
                   System.out.println("final "+FROMDateTime.toLocaleString());
                   FROMDATE.setText(simpleDateFormat.format(FROMDateTime));
               }

            }
        },HR,MIN,false);
        timePickerDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_to_blacklist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addtoBlacklist:

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void setVisible(){
        layout.setVisibility(View.VISIBLE);
    }
    public void setInVisible(){
        layout.setVisibility(View.INVISIBLE);
    }
}