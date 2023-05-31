package com.melodybeauty.melody_beauty_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_signin;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //cek user login
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLogin = preferences.getBoolean("isLogin", false);

        if (isLogin) {
            Intent intent = new Intent(WelcomeActivity.this, HomepageActivity.class);
            startActivity(intent);
            finish();
        }

        btn_signin = findViewById(R.id.btn_signin);
        textView = findViewById(R.id.tv_signup);
        btn_signin.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_signin) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}