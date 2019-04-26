package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.firebasedemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginMainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnLoginEmail)
    Button btnLoginEmail;

    @BindView(R.id.btnLoginPhone)
    Button getBtnLoginPhone;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Blue Pop");

        btnLoginEmail.setOnClickListener(this);
        getBtnLoginPhone.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLoginEmail) {
            Intent intent = new Intent(LoginMainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginMainActivity.this, PhoneAuthActivity.class);
            startActivity(intent);
        }
    }
}