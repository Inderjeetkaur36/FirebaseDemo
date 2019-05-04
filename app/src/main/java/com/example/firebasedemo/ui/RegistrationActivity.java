package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.editTextPhone)
    EditText editTextPhone;

    @BindView(R.id.editTextAddress)
    EditText editTextAddress;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    User user;

    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    void initViews(){

        ButterKnife.bind(this);
        btnRegister.setOnClickListener(this);

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

        getSupportActionBar().setTitle("Blue Pop");
        initViews();
    }

    @Override
    public void onClick(View v) {

        user.name = editTextName.getText().toString();
        user.email = editTextEmail.getText().toString();
        user.password = editTextPassword.getText().toString();
        user.phone = editTextPhone.getText().toString();
        user.address = editTextAddress.getText().toString();

        progressDialog.show();

        registerUser();
    }

    void registerUser(){

        auth.createUserWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isComplete()){
                            Toast.makeText(RegistrationActivity.this, user.name + " created ", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(RegistrationActivity.this, LoginMainActivity.class);
                            startActivity(intent);
                            finish();

                            saveUserInCloud();
                        }
                    }
                });
    }

    void saveUserInCloud(){
        firebaseUser = auth.getCurrentUser();
        db.collection("Persons").document(firebaseUser.getUid()).set(user)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RegistrationActivity.this,user.name+ "Registered Sucessfully", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                        Intent intent = new Intent(RegistrationActivity.this, LoginMainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }
}
