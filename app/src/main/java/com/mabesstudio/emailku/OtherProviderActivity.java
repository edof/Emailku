package com.mabesstudio.emailku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mabesstudio.emailku.activity.MainActivity;

public class OtherProviderActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText editSmtp, editPortSmtp, editImap, editPortImap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_provider);

        editSmtp = (EditText) findViewById(R.id.edit_smtp);
        editPortSmtp = (EditText) findViewById(R.id.edit_port_smtp);
        editImap = (EditText) findViewById(R.id.edit_imap);
        editPortImap = (EditText) findViewById(R.id.edit_port_imap);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSmtp.getText().toString().trim().isEmpty()){
                    editSmtp.setError("Kolom ini harus diisi !");
                } else if (editPortSmtp.getText().toString().trim().isEmpty()) {
                    editPortSmtp.setError("Kolom ini harus diisi !");
                } else if (editImap.getText().toString().trim().isEmpty()){
                    editImap.setError("Kolom ini harus diisi !");
                }else if (editPortImap.getText().toString().trim().isEmpty()){
                    editPortImap.setError("Kolom ini harus diisi !");
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
