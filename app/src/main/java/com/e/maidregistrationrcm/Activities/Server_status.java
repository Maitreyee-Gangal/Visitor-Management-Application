package com.e.maidregistrationrcm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server_status extends AppCompatActivity {
    private LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>();
    private int rr = 1;
    ProgressBar progressBar;
    TextView ramtext;
    ProgressBar stroragebar;
    TextView stortext;
    TextView uptime;
    TextView startedat;
    boolean EXIT_THREAD = false;
    @Override
    public void onBackPressed() {
        EXIT_THREAD = true;
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_status);

        GraphView graphView = findViewById(R.id.cpu_graph);

        graphView.addSeries(lineGraphSeries);

        graphView.animate();
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Format formatter = new SimpleDateFormat("HH:mm:ss");
                    return formatter.format(value);
                }
                return super.formatLabel(value, isValueX);
            }
        });
          progressBar = findViewById(R.id.ram_bar);
          ramtext = findViewById(R.id.ram);
          stroragebar = findViewById(R.id.storagebar);
          stortext = findViewById(R.id.storageStatus);
          uptime = findViewById(R.id.uptime);
          startedat = findViewById(R.id.statedat);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    JSONParser jsonParser = new JSONParser();
                    while (true){
                        Socket socket = new Socket(Service.IP,7001);
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Cmd","getSystemInfo");
                        jsonObject.put("DeviceID","0");
                        dataOutputStream.writeUTF(jsonObject.toJSONString());
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        final JSONObject jsonObject1 = (JSONObject) jsonParser.parse(dataInputStream.readUTF());
                        System.out.println("cpu "+jsonObject1.get("cpu"));
                        System.out.println("ram "+jsonObject1.get("ram"));
                        System.out.println("str "+jsonObject1.get("storage"));
                        System.out.println("uptime "+jsonObject1.get("uptime"));
                        System.out.println("start "+jsonObject1.get("starttime"));

                        Server_status.this.runOnUiThread(new Runnable() {
                            public void run() {
                                setall(jsonObject1);
                            }

                        });


                        Thread.sleep(1000);
                        socket.close();
                        dataOutputStream.close();
                        dataInputStream.close();
                        if(EXIT_THREAD){
                            throw new Exception("nikal");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };thread.start();

    }
    private void setall(JSONObject jsonObject){
        Date date = new Date();
        double v = Double.parseDouble(jsonObject.get("cpu").toString().trim());
        int u = 500;
        lineGraphSeries.appendData(new DataPoint(date,v),false,u);

        progressBar.setProgress(Integer.parseInt(jsonObject.get("ram").toString().trim()));
        ramtext.setText(jsonObject.get("ram").toString().trim()+"mb");

        StringBuffer str = new StringBuffer();
        char[] chars = jsonObject.get("storage").toString().trim().toCharArray();
        for (int i = 0;i<chars.length;i++){
            if(Character.isDigit(chars[i])){
                str.append(chars[i]+"");
            }
        }
        stroragebar.setProgress(Integer.parseInt(str.toString().trim()));
        stortext.setText(str.toString().trim()+"MB");

        uptime.setText("UpTime: "+jsonObject.get("uptime").toString());
        startedat.setText("StartedAt: "+jsonObject.get("starttime").toString());
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
                EXIT_THREAD = true;
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}