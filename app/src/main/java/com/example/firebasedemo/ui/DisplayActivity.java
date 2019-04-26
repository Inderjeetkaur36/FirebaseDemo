package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.model.Shoes;
import com.example.firebasedemo.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayActivity extends AppCompatActivity {

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.textViewPrice)
    TextView textViewPrice;

    @BindView(R.id.textViewId)
    TextView textViewId;

    @BindView(R.id.textViewColor)
    TextView textViewColor;

    @BindView(R.id.textViewSize)
    TextView textViewSize;

    @BindView(R.id.imageView)
    ImageView img;

    @BindView(R.id.buttonCart)
    Button btnCart;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;

    Shoes shoes;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        getSupportActionBar().setTitle("Blue Pop");
        ButterKnife.bind(this);

        shoes = new Shoes();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

        Intent rcv = getIntent();
        String name = rcv.getStringExtra("keyName");
        String price = rcv.getStringExtra("keyPrice");
        String id = rcv.getStringExtra("keyId");
        String color = rcv.getStringExtra("keyColor");
        String size = rcv.getStringExtra("keySize");

        textViewName.setText(name);
        textViewPrice.setText(price);
        textViewId.setText(id);
        textViewColor.setText(color);
        textViewSize.setText(size);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shoes.id = textViewId.getText().toString();
                shoes.size = textViewSize.getText().toString();
                shoes.name = textViewName.getText().toString();
                shoes.price = textViewPrice.getText().toString();
                shoes.color = textViewColor.getText().toString();

                if(Util.isInternetConnected(DisplayActivity.this)) {
                    progressDialog.show();
                    saveDataTocart();
                }else{
                    Toast.makeText(DisplayActivity.this,"Please Connect to Internet",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void saveDataTocart(){
        db.collection("Persons").document(firebaseUser.getUid())
                .collection("Cart").add(shoes)
                .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isComplete()) {
                            Toast.makeText(DisplayActivity.this, "Item Saved", Toast.LENGTH_LONG).show();

                            Intent intent =  new Intent(DisplayActivity.this,AddCartActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}

