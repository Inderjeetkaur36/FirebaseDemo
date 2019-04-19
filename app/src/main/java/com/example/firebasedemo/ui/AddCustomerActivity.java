package com.example.firebasedemo.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.SplashActivity;
import com.example.firebasedemo.adapter.CustomerAdapter;
import com.example.firebasedemo.model.Customer;
import com.example.firebasedemo.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCustomerActivity extends AppCompatActivity {

    EditText eTxtName,eTxtPhone,eTxtEmail;
    Button btnAddCustomer;

    Customer customer;
    ContentResolver resolver;

    boolean updateMode;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    CustomerAdapter customerAdapter;
    ProgressDialog progressDialog;

    void initViews(){

        resolver = getContentResolver();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

        eTxtName = findViewById(R.id.editTextName);
        eTxtPhone = findViewById(R.id.editTextPhone);
        eTxtEmail = findViewById(R.id.editTextEmail);
        btnAddCustomer = findViewById(R.id.buttonAdd);

        customer = new Customer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customer.name = eTxtName.getText().toString();
                customer.phone = eTxtPhone.getText().toString();
                customer.email = eTxtEmail.getText().toString();

                //addCustomerInDB();

                if(Util.isInternetConnected(AddCustomerActivity.this)) {
                    progressDialog.show();
                    saveCustomerInCloudDB();
                }else{
                    Toast.makeText(AddCustomerActivity.this,"Please Connect to Internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyCustomer");

        if(updateMode){
            customer = (Customer) rcv.getSerializableExtra("keyCustomer");
            eTxtName.setText(customer.name);
            eTxtPhone.setText(customer.phone);
            eTxtEmail.setText(customer.email);
            btnAddCustomer.setText("Update Customer");
            //customerAdapter.notifyDataSetChanged();

        }
    }

   void addCustomerInDB(){

        ContentValues values = new ContentValues();
        values.put(Util.COL_NAME,customer.name);
        values.put(Util.COL_PHONE,customer.phone);
        values.put(Util.COL_EMAIL,customer.email);

        if(updateMode){

            String where = Util.COL_ID+" = "+customer.id;
            int i = resolver.update(Util.CUSTOMER_URI,values,where,null);
            if(i>0){
                Toast.makeText(this,"Updation Finished",Toast.LENGTH_LONG).show();
                customerAdapter.notifyDataSetChanged();

                finish();

            }else{
                Toast.makeText(this,"Updation Failed",Toast.LENGTH_LONG).show();
            }

        }else{
            Uri uri = resolver.insert(Util.CUSTOMER_URI, values);
            Toast.makeText(this,customer.name+" Added in Database: "+uri.getLastPathSegment(),Toast.LENGTH_LONG).show();

            clearFields();
        }
    }

    void saveCustomerInCloudDB() {

        if(updateMode){

            db.collection("users").document(firebaseUser.getUid())
                .collection("customers").document(customer.docId)
                .set(customer)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(AddCustomerActivity.this,"Updation Finished",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            customerAdapter.notifyDataSetChanged();
                            finish();
                        }else{
                            Toast.makeText(AddCustomerActivity.this,"Updation Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }else {

            db.collection("users").document(firebaseUser.getUid())
            .collection("customers").add(customer)
                .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if(task.isComplete()){
                            Toast.makeText(AddCustomerActivity.this,"Customer saved ",Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_add_customer);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,101,1,"All Customers");
        menu.add(2,201,2,"SignOut");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == 101){
            Intent intent = new Intent(AddCustomerActivity.this, AllCustomerActivity.class);
            startActivity(intent);
        }else{
                auth.signOut();    //Clear login user in shared preferences
                Intent intent = new Intent(AddCustomerActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void clearFields(){
        eTxtName.setText("");
        eTxtPhone.setText("");
        eTxtEmail.setText("");

    }
}
