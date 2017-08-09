package com.mabesstudio.emailku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.mabesstudio.emailku.R;
import com.mabesstudio.emailku.adapter.AddressbookAdapter;
import com.mabesstudio.emailku.model.Addressbook;

import java.util.ArrayList;

public class AddressbookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Addressbook> addressbookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressbook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_addressbook);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Address Book");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = (RecyclerView) findViewById(R.id.rv_addressbook);
        addressbookArrayList = new ArrayList<>();
        loadData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AddressbookAdapter(addressbookArrayList, this));
        animateRecyclerView(recyclerView);
    }

    //TODO: method untuk memuat data kontak
    private void loadData(){
        for (int i = 0; i < 20; i++) {
            Addressbook addressbook = new Addressbook();
            addressbook.setName("Aris Kloning " + i);
            addressbook.setEmail("aris.kloning" + i + "@gmail.com");
            addressbookArrayList.add(addressbook);
        }
    }

    //set animation fade in for recycler view
    private void animateRecyclerView(final RecyclerView rv) {
        rv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rv.getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < rv.getChildCount(); i++) {
                    View view = rv.getChildAt(i);
                    view.setAlpha(0.0f);
                    view.animate().alpha(1.0f).setDuration(300).setStartDelay(i * 50).start();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //fungsi item menu pada toolbar (get item id lalu set methodnya)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
