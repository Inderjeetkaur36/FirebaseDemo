package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.adapter.RecyclerAdapter;
import com.example.firebasedemo.model.Shoes;
import com.example.firebasedemo.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProductActivity extends AppCompatActivity {

    EditText eTxtId,eTxtSize,eTxtName,eTxtPrice,eTxtColor;
    Button buttonAddProduct;

    Shoes shoes;
    ContentResolver resolver;

    boolean updateMode;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    RecyclerAdapter recyclerAdapter;
    ProgressDialog progressDialog;

    void initViews(){

        resolver = getContentResolver();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

        eTxtId = findViewById(R.id.eTxtId);
        eTxtSize = findViewById(R.id.eTxtSize);
        eTxtName = findViewById(R.id.eTxtName);
        eTxtPrice = findViewById(R.id.eTxtPrice);
        eTxtColor = findViewById(R.id.eTxtColor);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        shoes = new Shoes();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shoes.id = eTxtId.getText().toString();
                shoes.size = eTxtSize.getText().toString();
                shoes.name = eTxtName.getText().toString();
                shoes.price = eTxtPrice.getText().toString();
                shoes.color = eTxtColor.getText().toString();

                if(Util.isInternetConnected(AddProductActivity.this)) {
                    progressDialog.show();
                    saveProductInFirebase();
                }else{
                    Toast.makeText(AddProductActivity.this,"Please Connect to Internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyShoes");

        if(updateMode){
            shoes = (Shoes) rcv.getSerializableExtra("keyShoes");
            eTxtId.setText(shoes.id);
            eTxtSize.setText(shoes.size);
            eTxtName.setText(shoes.name);
            eTxtPrice.setText(shoes.price);
            eTxtColor.setText(shoes.color);
            buttonAddProduct.setText("Update Products");

        }
    }

    void saveProductInFirebase() {

        if(updateMode){

            db.collection("Products").document(shoes.docId)
                    .set(shoes)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if(task.isComplete()){
                                Toast.makeText(AddProductActivity.this,"Updation Finished",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                recyclerAdapter.notifyDataSetChanged();
                                finish();
                            }else{
                                Toast.makeText(AddProductActivity.this,"Updation Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else {

            db.collection("Products").add(shoes)
                    .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(Task<DocumentReference> task) {
                            if(task.isComplete()){
                                Toast.makeText(AddProductActivity.this,"Product saved ",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                clearFields();
                            }
                        }
                    });
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initViews();
    }

    void clearFields(){
        eTxtId.setText("");
        eTxtSize.setText("");
        eTxtName.setText("");
        eTxtPrice.setText("");
        eTxtColor.setText("");
    }
}
