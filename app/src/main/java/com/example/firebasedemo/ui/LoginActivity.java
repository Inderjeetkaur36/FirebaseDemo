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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.editTextEmail)
    EditText eTxtEmail;

    @BindView(R.id.editTextPassword)
    EditText eTxtPassword;

    @BindView(R.id.buttonLogin)
    Button btnLogin;

    ProgressDialog progressDialog;
    User user;
    FirebaseAuth auth;

    void initViews() {

        ButterKnife.bind(this);
        user = new User();
        btnLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    public void onClick(View v) {

        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+
                "\\@"+
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+
                "("+
                "\\."+
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+
                ")+";

        String email = eTxtEmail.getText().toString();
        Matcher matcher = Pattern.compile(validemail).matcher(email);
        if(matcher.matches()){
            Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext()," Login",Toast.LENGTH_LONG).show();
        }
        if (eTxtEmail.getText().toString().trim().length() == 0) {
            eTxtEmail.setError("Email is not entered");
            eTxtEmail.requestFocus();
        }
        if (eTxtPassword.getText().toString().trim().length() == 0) {
            eTxtPassword.setError("Password is not entered");
            eTxtPassword.requestFocus();
        } else {

            user.email = eTxtEmail.getText().toString();
            user.password = eTxtPassword.getText().toString();
            loginUser();
        }

    }
    void loginUser(){
        progressDialog.show();

        auth.signInWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isComplete()) {
                            Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}