package com.mabesstudio.emailku.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mabesstudio.emailku.R;

public class ForgotActivity extends AppCompatActivity {

    EditText editForgot;
    Button btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        editForgot = (EditText) findViewById(R.id.edit_forgot_email);
        btnForgot = (Button) findViewById(R.id.btn_forgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editForgot.getText().toString().trim().isEmpty()){
                    editForgot.setError("Kolom ii harus diisi !");
                } else if (!isValidEmail(editForgot.getText().toString().trim())){
                    editForgot.setError("Gunakan format email yang benar !");
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public final static boolean isValidEmail (CharSequence sequence){
        return !TextUtils.isEmpty(sequence) && Patterns.EMAIL_ADDRESS.matcher(sequence).matches();
    }
}
