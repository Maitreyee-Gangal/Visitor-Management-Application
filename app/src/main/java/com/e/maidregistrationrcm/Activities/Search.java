package com.e.maidregistrationrcm.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.e.maidregistrationrcm.others.IDcard;
import com.e.maidregistrationrcm.R;
import com.e.maidregistrationrcm.Utils.Service;
import com.e.maidregistrationrcm.VOs.MaidVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private static final int PORT = 7001;
    public static String name;
    private ArrayAdapter adapter;
    private MaidVO toshareVo = null;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.nav_Search);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_Search:
                        return true;
                    case R.id.nav_log:
                        System.out.println("in");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.nav_register:
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.nav_reports:
                        startActivity(new Intent(getApplicationContext(), Reports.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                        /*
                    case R.id.nav_setting:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                         */
                }
                return false;
            }
        });
        //--------------------------------------------------------------------------------------------------------------------------
        final AutoCompleteTextView searchby = findViewById(R.id.Serchbar);
        final ListView Maidlist = findViewById(R.id.MaidsList);
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    final MaidVO[] maids = Service.searchMaids(null);
                    final ArrayList maidsArray = new ArrayList();

                    for(int i = 0;i<maids.length;i++){
                        MaidVO maidVO = maids[i];
                        String toShow = maidVO.getToken() +" "+maidVO.getName();
                        maidsArray.add(toShow);
                    }
                    Search.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1, maids);
                                Maidlist.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        try {
                            final MaidVO[] maids = Service.searchMaids(null);
                            final ArrayList maidsArray = new ArrayList();

                            for(int i = 0;i<maids.length;i++){
                                MaidVO maidVO = maids[i];
                                String toShow = maidVO.getToken() +" "+maidVO.getName();
                                maidsArray.add(toShow);
                            }
                            Search.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        adapter = new ArrayAdapter(Search.this, android.R.layout.simple_list_item_1, maids);
                                        Maidlist.setAdapter(adapter);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        },1000);
                    }
                });

            }
        };
        thread.start();


        EditText editText = findViewById(R.id.Serchbar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ListView listView = findViewById(R.id.MaidsList);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    MaidVO maidVO = (MaidVO) parent.getItemAtPosition(position);

                    maidVO = Service.getMaidInfo(maidVO.getMaidId());
                    IDcard.maidVO = maidVO;
                    Intent intent = new Intent(new Intent(getApplicationContext(),IDcard.class));
                    startActivity(intent);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

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

/*class MyListAdapter extends BaseAdapter implements Filterable {
    MaidVO[] maids  = null;
    NameFilter nameFilter = null;
    public MyListAdapter(Context ctx,int i,MaidVO[] maids){
        super();
        this.maids = maids;
    }
    public int getCount() {
        return maids.length;
    }

    @Override
    public Object getItem(int position) {
        return _Contacts.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public Filter getFilter() {
        if(nameFilter == null){
            nameFilter = new NameFilter(maids);
        }
        return nameFilter;
    }

    class NameFilter extends Filter {
        MaidVO[] maids = null;
        public NameFilter(MaidVO[]  list){
            this.maids = list;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            results.count = 0;
            results.values = new ArrayList<MaidVO>();
            String searchText = constraint.toString().toLowerCase();
            System.out.println("searchText-->"+searchText);
            ArrayList<MaidVO> filterDataList = new ArrayList();
            for(int i=0;i<maids.length;i++){
                MaidVO maidVO = maids[i];
                if(maidVO.getName().toLowerCase().indexOf(searchText) != -1){
                    filterDataList.add(maidVO);
                }
            }

            results.values = filterDataList;
            results.count = filterDataList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results){

        }
    }
}*/
