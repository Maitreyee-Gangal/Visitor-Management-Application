package com.e.maidregistrationrcm.others;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import com.e.maidregistrationrcm.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Bottom_sheet extends BottomSheetDialogFragment {
Context context;
public static String TIME = "";
public static String DATE = "";
public static String FULLSTRING = "";

    public Bottom_sheet(Context context) {
        this.context = context;
    }

    private BottomSheetListener mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_card, container, false);
        final TextView button1 = v.findViewById(R.id.button1);
        final TextView button2 = v.findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int MIN = calendar.get(Calendar.MINUTE);
                int HR = calendar.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeString = hourOfDay+":"+minute;
                        System.out.println("time is "+timeString);
                        button2.setText("Time: "+timeString);
                        TIME = timeString;
                        FULLSTRING = TIME+" "+DATE;
                    }
                },HR,MIN,false);
                timePickerDialog.show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
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
                        button1.setText("Date: "+sdf.format(cal.getTime()));
                        System.out.println(sdf.format(cal.getTime()));
                        DATE = sdf.format(cal.getTime());
                        FULLSTRING = TIME+" "+DATE;
                    }

                };

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                // TODO Auto-generated method stub
                new DatePickerDialog(context,myDateListener,year,month,day).show();
            }
        });

        final TextView fromdate = v.findViewById(R.id.tobutton);
        fromdate.setOnClickListener(new View.OnClickListener() {
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
                        fromdate.setText("from Date: "+sdf.format(cal.getTime()));
                        System.out.println(sdf.format(cal.getTime()));
                        DATE = sdf.format(cal.getTime());
                        FULLSTRING = TIME+" "+DATE;
                    }

                };

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                // TODO Auto-generated method stub
                new DatePickerDialog(context,myDateListener,year,month,day).show();
            }
        });
        Button button = v.findViewById(R.id.set);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        return v;
    }
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
}