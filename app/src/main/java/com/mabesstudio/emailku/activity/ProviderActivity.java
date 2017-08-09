package com.mabesstudio.emailku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.mabesstudio.emailku.R;

public class ProviderActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtEmail, txtPassword;
    EditText editSmtp, editPortSmtp, editImap, editPortImap;
    LinearLayout inputProvider, inputOtherProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        btnLogin = (Button) findViewById(R.id.btn_login);
        txtEmail = (EditText) findViewById(R.id.login_email);
        txtPassword = (EditText) findViewById(R.id.pass_email);

        editSmtp = (EditText) findViewById(R.id.edit_smtp);
        editPortSmtp = (EditText) findViewById(R.id.edit_port_smtp);
        editImap = (EditText) findViewById(R.id.edit_imap);
        editPortImap = (EditText) findViewById(R.id.edit_port_imap);

        inputProvider = (LinearLayout) findViewById(R.id.provider_layout);
        inputOtherProvider = (LinearLayout) findViewById(R.id.other_provider_layout);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.provider,
                android.R.layout.simple_spinner_item);
//        spinner.setPrompt("Gmail");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().contentEquals("Other")) {
                    inputProvider.setVisibility(View.GONE);
                    inputOtherProvider.setVisibility(View.VISIBLE);
                } else {
                    inputProvider.setVisibility(View.VISIBLE);
                    inputOtherProvider.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputProvider.getVisibility() == View.VISIBLE) {
                    if (txtEmail.getText().toString().trim().isEmpty()) {
                        txtEmail.setError("Kolom ini harus diisi !");
                    } else if (!isValidEmail(txtEmail.getText().toString().trim())) {
                        txtEmail.setError("Gunakan format email yang benar !");
                    } else if (txtPassword.getText().toString().trim().isEmpty()) {
                        txtPassword.setError("Kolom ini harus diisi !");
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } else if (inputOtherProvider.getVisibility() == View.VISIBLE) {
                    if (editSmtp.getText().toString().trim().isEmpty()) {
                        editSmtp.setError("Kolom ini harus diisi !");
                    } else if (editPortSmtp.getText().toString().trim().isEmpty()) {
                        editPortSmtp.setError("Kolom ini harus diisi !");
                    } else if (editImap.getText().toString().trim().isEmpty()) {
                        editImap.setError("Kolom ini harus diisi !");
                    } else if (editPortImap.getText().toString().trim().isEmpty()) {
                        editPortImap.setError("Kolom ini harus diisi !");
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    public final static boolean isValidEmail(CharSequence sequence) {
        return !TextUtils.isEmpty(sequence) && Patterns.EMAIL_ADDRESS.matcher(sequence).matches();
    }
}
