package com.example.goldendreamsbowling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.goldendreamsbowling.Guest.merchFrag2;
import com.example.goldendreamsbowling.JavaFile.TshirtAdapter;
import com.example.goldendreamsbowling.JavaFile.productModel;
import com.example.goldendreamsbowling.LoggedInUser.merchFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tshirt_page extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    List<productModel> productModels;
    TshirtAdapter tshirtAdapter;
    TextView cartCounter;
    private int count;
    FirebaseAuth ID;
    String UID;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tshirt_page);
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.productRecView);
        floatingActionButton = findViewById(R.id.floatingCart);
        cartCounter = findViewById(R.id.cartCounter);
        productModels = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(Tshirt_page.this,2);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getProducts();
        getcartCount();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tshirt_page.this, CartActivity.class));
                finish();
            }
        });


    }

    public void getcartCount() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");

        databaseReference.child("AddToCart").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    count = (int) snapshot.getChildrenCount();
                    cartCounter.setText(Integer.toString(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void recreate() {
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void getProducts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tshirt");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModels.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    productModel productModel = dataSnapshot.getValue(productModel.class);
                    productModels.add(productModel);
                    tshirtAdapter = new TshirtAdapter(Tshirt_page.this,productModels,floatingActionButton);
                    recyclerView.setAdapter(tshirtAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences(Login.Pref_Name,0);
        boolean hasLogin = sharedPreferences.getBoolean("hasLoggedIn",false);
        if(hasLogin)
        {
            startActivity(new Intent(Tshirt_page.this, merchFragment.class));
            finish();
        }
        else
        {
            startActivity(new Intent(Tshirt_page.this, merchFrag2.class));
            finish();
        }
    }
}