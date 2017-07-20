package com.mabesstudio.emailku.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mabesstudio.emailku.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtEmail, txtPassword;
    TextView txtForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = (EditText) findViewById(R.id.login_email);
        txtPassword = (EditText) findViewById(R.id.pass_email);
        txtForgot = (TextView) findViewById(R.id.txt_forgot);
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(intent);
            }
        });
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().trim().isEmpty()){
                    txtEmail.setError("Kolom ini harus diisi !");
                } else if (!isValidEmail(txtEmail.getText().toString().trim())) {
                    txtEmail.setError("Gunakan format email yang benar !");
                } else if (txtPassword.getText().toString().trim().isEmpty()){
                    txtPassword.setError("Kolom ini harus diisi !");
                } else {
                    Intent intent = new Intent(getApplicationContext(), ProviderActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public final static boolean isValidEmail (CharSequence sequence){
        return !TextUtils.isEmpty(sequence) && Patterns.EMAIL_ADDRESS.matcher(sequence).matches();
    }
}
