package com.example.firebasedemo.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.R;
import com.example.firebasedemo.adapter.CartRecyclerAdapter;
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

public class AddCartActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Shoes> list;
    CartRecyclerAdapter cartRecyclerAdapter;
    LinearLayout relativeLayout;

    Switch aSwitch;
    Shoes shoes;
    TextView signIn;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        getSupportActionBar().setTitle("Cart Products ");

        //relativeLayout = findViewById(R.id.relative);
        recyclerView = findViewById(R.id.cartRecyclerView);
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

        if(Util.isInternetConnected(this)) {
            fetchProductsFromCart();
        }else{
            Toast.makeText(AddCartActivity.this,"Please Connect to Internet and Try Again",Toast.LENGTH_LONG).show();
        }
    }
    public void fetchProductsFromCart(){
        db.collection("Persons").document(firebaseUser.getUid())
                .collection("Cart").get()
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

                            cartRecyclerAdapter = new CartRecyclerAdapter(AddCartActivity.this, R.layout.cart_list_item, list);

                            //.setOnRecyclerItemClickListener(CartActivity.this);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddCartActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(cartRecyclerAdapter);

                        } else {
                            Toast.makeText(AddCartActivity.this, "Some Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.switchBu);
        item.setActionView(R.layout.switch_lay);

        aSwitch = item.getActionView().findViewById(R.id.switchForActionBar);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(AddCartActivity.this, 2);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    recyclerView.setAdapter(cartRecyclerAdapter);
                } else {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddCartActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(cartRecyclerAdapter);
                    cartRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        signIn = item.getActionView().findViewById(R.id.LogOut);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                finish();
            }
        });
        return true;
    }
}
