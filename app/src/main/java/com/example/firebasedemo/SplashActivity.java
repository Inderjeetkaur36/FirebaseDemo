package com.example.firebasedemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.firebasedemo.model.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        /*if(firebaseUser == null){
                handler.sendEmptyMessageDelayed(101,3000);
        }else {
                handler.sendEmptyMessageDelayed(201,3000);
            }*/
        handler.sendEmptyMessageDelayed(101,3000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                if (Util.isInternetConnected(SplashActivity.this)) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }/* else {
                if (Util.isInternetConnected(SplashActivity.this)) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                Toast.makeText(SplashActivity.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }*/
        }

    };
}