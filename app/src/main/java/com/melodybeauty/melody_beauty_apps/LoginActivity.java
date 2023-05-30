package com.melodybeauty.melody_beauty_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melodybeauty.melody_beauty_apps.AuthServices.AuthServices;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_back;
    TextView tv_signup;
    Button btl_signin;
    EditText etl_email, etl_password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iv_back = findViewById(R.id.iv_back);

        etl_email = findViewById(R.id.etl_email);
        etl_password = findViewById(R.id.etl_password);
        btl_signin = findViewById(R.id.btl_signin);
        tv_signup = findViewById(R.id.tv_sign_up);

        iv_back.setOnClickListener(this);
        btl_signin.setOnClickListener(this);
        tv_signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == iv_back) {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else if (v == btl_signin){
            String email = etl_email.getText().toString().trim();
            String password = etl_password.getText().toString().trim();

            AuthServices.login(LoginActivity.this, email, password, new AuthServices.LoginResponseListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        }
    }
}