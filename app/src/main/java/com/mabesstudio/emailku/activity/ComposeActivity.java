package com.mabesstudio.emailku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mabesstudio.emailku.R;

public class ComposeActivity extends AppCompatActivity {

    private EditText etFrom, etTo, etSubject, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Tulis Email");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etFrom = (EditText) findViewById(R.id.et_compose_from);
        etTo = (EditText) findViewById(R.id.et_compose_to);
        etSubject = (EditText) findViewById(R.id.et_compose_subject);
        etContent = (EditText) findViewById(R.id.edit_compose_content);

//        TODO: method untuk mengirim email, nilai didapatkan dari edittext.getString()
//        sendEmail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO: method untuk item menu toolbar compose, lalu ganti nilai return -> true
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.compose_attach:
//                attachFile();
                return false;
            case R.id.compose_send:
//                sendMail();
                return false;
            case R.id.compose_settings:
//                openComposeSettings();
                return false;
            default:
                return false;
        }
    }
}
