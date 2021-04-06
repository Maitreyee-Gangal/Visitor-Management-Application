package com.e.maidregistrationrcm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.MaidVO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayInputStream;

public class LogId extends AppCompatActivity {
    public static MaidVO maidVOC ;
    public static int maidId;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_id);


        final Button inbtn = findViewById(R.id.Savelog);
        final Button logout = findViewById(R.id.exit);

        showMaidInfo(maidVOC);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Cmd","logout");
                        jsonObject.put("DeviceID", Service.readDeviceId());
                        jsonObject.put("maidId",maidId);
                        try {

                            String jsonReply = Service.send(jsonObject.toJSONString());
                            JSONParser jsonParser = new JSONParser();
                            JSONObject jsonResObject = (JSONObject) jsonParser.parse(jsonReply);
                            final int errorCode = Integer.parseInt(jsonResObject.get("ErrorCode").toString());
                            final String msg = jsonResObject.get("ErrorMsg").toString();
                            if(errorCode != 0) {
                                LogId.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(LogId.this, msg, Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }

                            MaidVO maidVO =  Service.getMaidInfo(maidId);
                            showMaidInfo(maidVO);
                            Toast.makeText(LogId.this,"out Successful",Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
            }
        });

                        inbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Cmd", "SaveLog");
                        jsonObject.put("DeviceID",Service.readDeviceId());
                        jsonObject.put("maidId", maidId);
                        try {
                            String jsonReply = Service.send(jsonObject.toJSONString());
                            JSONParser jsonParser = new JSONParser();
                            JSONObject jsonResObject = (JSONObject) jsonParser.parse(jsonReply);
                            final int errorCode = Integer.parseInt(jsonResObject.get("ErrorCode").toString());
                            final String msg = jsonResObject.get("ErrorMsg").toString();

                            if(errorCode != 0) {
                                LogId.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(LogId.this, msg, Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }


                            final MaidVO maidVO =  Service.getMaidInfo(maidId);

                            showMaidInfo(maidVO);
                            Toast.makeText(LogId.this,"In Successful",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            }
        });

    }

    private  void  showMaidInfo(final MaidVO maidVO){

        this.maidId = maidVO.getMaidId();

        final TextView name_lable = findViewById(R.id.nameLable);
        final TextView address_lable = findViewById(R.id.addresslable);
        final Button inbtn = findViewById(R.id.Savelog);
        final Button logout = findViewById(R.id.exit);
        final ImageView yourimage = findViewById(R.id.yourphoto);
        final ImageView permision = findViewById(R.id.perm);
        final TextView token = findViewById(R.id.TokenLableid);
        final TextView bantodate = findViewById(R.id.bantodate);

        if(maidVO.getAccess() == 1) {
            permision.setImageResource(R.drawable.ok_24);
        }else {
            permision.setImageResource(R.drawable.no_24);
        }

        int foundinbl = maidVO.getInBL();
        if(foundinbl == 1){
            permision.setImageResource(R.drawable.ban);
            bantodate.setVisibility(View.VISIBLE);
            bantodate.setText("Ban till "+maidVO.getBanTo());
        }else if(foundinbl == 2){
            permision.setImageResource(R.drawable.no_24);
            bantodate.setVisibility(View.VISIBLE);
            bantodate.setText("has been blocked by authorities");
        }

        name_lable.setText(maidVO.getName());
        address_lable.setText(maidVO.getAddress());
        token.setText(""+maidVO.getToken());

        byte[] imagedata = maidVO.getImage();
        Bitmap myBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(imagedata));
        yourimage.setImageBitmap(myBitmap);

        LogId.this.runOnUiThread(new Runnable() {
            public void run() {
                if(maidVO.getInTime() == null){
                    System.out.println("in time is null");
                    inbtn.setEnabled(true);
                }else{
                    inbtn.setEnabled(false);
                    System.out.println("in time is not null");
                }
                logout.setEnabled(false);
                if(maidVO.getOutTime() == null) {
                    System.out.println("out time is null");
                    logout.setEnabled(true);
                }else if(maidVO.getInTime() != null && maidVO.getOutTime() != null){
                    inbtn.setEnabled(true);
                }else if(maidVO.getInBL() == 1){
                    inbtn.setEnabled(false);
                    logout.setEnabled(false);
                }
            }

        });
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
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}