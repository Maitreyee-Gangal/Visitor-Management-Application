package com.e.maidregistrationrcm.others;

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

import com.e.maidregistrationrcm.Activities.Register;
import com.e.maidregistrationrcm.Activities.Search;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.MaidVO;

import java.io.ByteArrayInputStream;

public class IDcard extends AppCompatActivity {
    public static MaidVO maidVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_id);

        TextView nameView = findViewById(R.id.NameId);
        TextView tokenView = findViewById(R.id.TokenId);
        TextView mobileView = findViewById(R.id.MobileId);
        TextView addressView = findViewById(R.id.IDAddress);
        TextView adharView = findViewById(R.id.IDAdharNo);
        TextView ocupationView = findViewById(R.id.IDocumation);
        ImageView imageView = findViewById(R.id.photo);
        Button editbtn = findViewById(R.id.editbtn);
        ImageView linearLayout = findViewById(R.id.premisionIDcard);
        if(maidVO.getAccess() == 1){
            linearLayout.setImageResource(R.drawable.ok_24);
        }else {
            linearLayout.setImageResource(R.drawable.no_24);
        }
        byte[] data = null;
        try{
            data = Service.getPhoto(maidVO.getImageRef());
            Bitmap myBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            imageView.setImageBitmap(myBitmap);

        }catch (Exception e){
            e.printStackTrace();
        }
        nameView.setText(maidVO.getName());
        tokenView.setText(""+maidVO.getToken());
        mobileView.setText(maidVO.getMobile());
        addressView.setText(maidVO.getAddress());
        adharView.setText(maidVO.getAdharNo());
        ocupationView.setText(maidVO.getOcupation());

        Button deleteBtn = findViewById(R.id.deleteWorker);
        deleteBtn.setEnabled(false);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maidId = maidVO.getMaidId();

            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MaidVO currentMaidVO = Service.getMaidInfo(maidVO.getMaidId());
                    Register.currentMaidVO = currentMaidVO;
                    startActivity(new Intent(getApplicationContext(),Register.class));
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(IDcard.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        Button delete = findViewById(R.id.deleteWorker);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Service.deleteMaid(maidVO.getMaidId());
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
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}