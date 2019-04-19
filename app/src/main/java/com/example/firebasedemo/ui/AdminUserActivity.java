package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.firebasedemo.R;

public class AdminUserActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddP,btnViewP,btnViewC,btnViewO;
    ProgressDialog progressDialog;

    void initViews() {

        btnAddP = findViewById(R.id.buttonAddP);
        btnViewP = findViewById(R.id.buttonView);
        btnViewC = findViewById(R.id.buttonViewC);
        btnViewO = findViewById(R.id.buttonViewO);

        btnAddP.setOnClickListener(this);
        btnViewP.setOnClickListener(this);
        btnViewC.setOnClickListener(this);
        btnViewO.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
        initViews();
    }

    @Override
    public void onClick(View v) {

        if(v == btnAddP){

            Intent intent = new Intent(AdminUserActivity.this,AddProductActivity.class);
            startActivity(intent);

        }else if(v == btnViewP){

            Intent intent = new Intent(AdminUserActivity.this,ViewProductActivity.class);
            startActivity(intent);

        }else if(v == btnViewC){
            Intent intent = new Intent(AdminUserActivity.this,FirstActivity.class);
            startActivity(intent);

        }else{

        }
    }

}