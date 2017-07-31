package com.mabesstudio.emailku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mabesstudio.emailku.R;

public class DetailContactActivity extends AppCompatActivity {

    private EditText etName, etEmail, etID, etCompany, etPhone;
    private Button btnOK, btnCancel;
    private LinearLayout layoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_contact);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Contact Detail");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etID = (EditText) findViewById(R.id.et_id);
        etCompany = (EditText) findViewById(R.id.et_company);
        etPhone = (EditText) findViewById(R.id.et_phone);
        btnOK = (Button) findViewById(R.id.button_ok);
        btnCancel = (Button) findViewById(R.id.button_cancel);
        layoutBtn = (LinearLayout) findViewById(R.id.button_edit_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEdit();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEdit();
            }
        });
    }

    private void editProfile(){

    }

    private void disableEdit(){
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etID.setEnabled(false);
        etCompany.setEnabled(false);
        etPhone.setEnabled(false);
        layoutBtn.setVisibility(View.INVISIBLE);
    }

    private void enableEdit(){
        etName.setEnabled(true);
        etEmail.setEnabled(true);
        etID.setEnabled(true);
        etCompany.setEnabled(true);
        etPhone.setEnabled(true);
        layoutBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                enableEdit();
                return true;
            case R.id.action_delete:
                Toast.makeText(getApplicationContext(), "Delete this contact", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
