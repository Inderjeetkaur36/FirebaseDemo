package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.model.User;
import com.example.firebasedemo.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTxtName,eTxtEmail,eTxtPassword;
    TextView txtLogin;
    Button btnRegister;

    User user;

    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    void initViews(){
        eTxtName = findViewById(R.id.editTextName);
        eTxtEmail = findViewById(R.id.editTextEmail);
        eTxtPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        txtLogin = findViewById(R.id.textViewLogin);

        btnRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        user = new User();

       auth = FirebaseAuth.getInstance();
       db = FirebaseFirestore.getInstance();
       firebaseUser = auth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.buttonRegister) {
            //Get the data from UI and put it into User Object
            user.name = eTxtName.getText().toString();
            user.email = eTxtEmail.getText().toString();
            user.password = eTxtPassword.getText().toString();

            if(Util.isInternetConnected(this)) {
                progressDialog.show();
                registerUser();
            }else{
                Toast.makeText(this,"Please Connect to Internet and Try Again",Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    void registerUser(){

            auth.createUserWithEmailAndPassword(user.email,user.password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {
                                Toast.makeText(RegistrationActivity.this, user.name + "User created ", Toast.LENGTH_LONG).show();
                               // progressDialog.dismiss();
                               /* Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();*/

                               saveUserInCloudDB();
                            }

                        }
                    });

    }

    void saveUserInCloudDB(){

        /*db.collection("users").add(user)
                .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isComplete()) {
                            Toast.makeText(RegistrationActivity.this, user.name + "Registered Sucessfully", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });*/

        db.collection("users").document(firebaseUser.getUid()).set(user)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()) {
                    Toast.makeText(RegistrationActivity.this, user.name + "Registered Sucessfully", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
