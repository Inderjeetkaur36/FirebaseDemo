package com.example.firebasedemo.ui;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.adapter.CustomerAdapter;
import com.example.firebasedemo.adapter.RecyclerAdapter;
import com.example.firebasedemo.listener.OnRecyclerItemClickListener;
import com.example.firebasedemo.model.Customer;
import com.example.firebasedemo.model.Shoes;
import com.example.firebasedemo.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity implements OnRecyclerItemClickListener{

    RecyclerView recyclerView;
    ArrayList<Shoes> list;

    RecyclerAdapter recyclerAdapter;
    RelativeLayout relativeLayout;

    int position;
    Shoes shoes;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        relativeLayout = findViewById(R.id.relative);
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

        if(Util.isInternetConnected(this)) {
            fetchProductsFromFirebase();
        }else{
            Toast.makeText(ViewProductActivity.this,"Please Connect to Internet and Try Again",Toast.LENGTH_LONG).show();
        }
    }

    void fetchProductsFromFirebase() {

        db.collection("Products").get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {

                            list = new ArrayList<>();

                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();

                            for (DocumentSnapshot snapshot : documentSnapshots) {
                                String docId = snapshot.getId();
                                Shoes shoes = snapshot.toObject(Shoes.class);
                                shoes.docId = docId;
                                list.add(shoes);
                            }

                            getSupportActionBar().setTitle("Total Products: ");

                            recyclerAdapter = new RecyclerAdapter(ViewProductActivity.this, R.layout.bluepop_item, list);

                            recyclerAdapter.setOnRecyclerItemClickListener(ViewProductActivity.this);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewProductActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(recyclerAdapter);

                        } else {
                            Toast.makeText(ViewProductActivity.this, "Some Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    void showCustomerDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(shoes.name+" Details:");
        builder.setMessage(shoes.toString());
        builder.setPositiveButton("Done",null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    void deleteCustomerFromFirebase(){
        db.collection("Products").document(shoes.docId)
                .delete()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(ViewProductActivity.this,"Deletion Finished",Toast.LENGTH_LONG).show();
                            list.remove(position);
                            recyclerAdapter.notifyDataSetChanged();// Refresh Your RecyclerView
                        }else{
                            Toast.makeText(ViewProductActivity.this,"Deletion Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void askForDeletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+shoes.name);
        builder.setMessage("Are You Sure ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCustomerFromFirebase();
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"View "+shoes.name, "Update "+shoes.name, "Delete "+shoes.name, "Cancel"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0:
                        showCustomerDetails();
                        break;

                    case 1:

                        Intent intent = new Intent(ViewProductActivity.this, AddProductActivity.class);
                        intent.putExtra("keyShoes",shoes);
                        startActivity(intent);
                        finish();
                        break;

                    case 2:
                        askForDeletion();

                        break;

                }

            }
        });

        //builder.setCancelable(false);
        recyclerAdapter.notifyDataSetChanged();
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void onItemClick(int position) {
        this.position = position;
        shoes = list.get(position);
        //Intent intent = new Intent(ViewProductActivity.this,DisplayActivity.class);
       // startActivity(intent);
        showOptions();
    }
}


