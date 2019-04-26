package com.example.firebasedemo.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.MainActivity;
import com.example.firebasedemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneAuthActivity extends AppCompatActivity {

    @BindView(R.id.eTxtPhone)
    EditText eTxtPhone;

    @BindView(R.id.buttonProceed)
    Button btnProceed;

    PhoneAuthProvider authProvider;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        ButterKnife.bind(this);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = eTxtPhone.getText().toString().trim();

                authProvider.verifyPhoneNumber(
                        phone,
                        60,
                        TimeUnit.SECONDS,
                        PhoneAuthActivity.this,
                        callbacks);
            }
        });
        authProvider = PhoneAuthProvider.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String otp = phoneAuthCredential.getSmsCode();

            signInUser(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneAuthActivity.this,"OTP Verification Failed"+e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    void signInUser(PhoneAuthCredential phoneAuthCredential){
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()) {
                    FirebaseUser user = task.getResult().getUser();
                    String userId = user.getUid();
                    Intent intent = new Intent(PhoneAuthActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
