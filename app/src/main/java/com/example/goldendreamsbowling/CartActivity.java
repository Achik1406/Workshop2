package com.example.goldendreamsbowling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.goldendreamsbowling.Guest.merchFrag2;
import com.example.goldendreamsbowling.JavaFile.CartAdapter;
import com.example.goldendreamsbowling.JavaFile.add;
import com.example.goldendreamsbowling.JavaFile.cartModel;
import com.example.goldendreamsbowling.JavaFile.productModel;
import com.example.goldendreamsbowling.LoggedInUser.merchFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<cartModel > cartModels;
    CartAdapter cartAdapter;
    Button PlaceOrder;
    FirebaseAuth ID;
    double a,b,total;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.cartRecView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        cartModels = new ArrayList<>();
        getCartitem();
        PlaceOrder = findViewById(R.id.placeOrder);
        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
                progressDialog.setTitle("Proceed Payment ");
                progressDialog.setMessage("Please wait,while we connecting to server");
                progressDialog.show();
                 a = add.getAdda();
                 b = add.getDelete();
                 total = a-b;
                String parseTotal = String.valueOf(total);
                Handler handler = new Handler();
                FirebaseDatabase.getInstance().getReference().child("AddTotal").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase.getInstance().getReference().child("AddTotal").child(UID).child("Total").setValue(parseTotal);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMessage("Done!");
                        progressDialog.dismiss();
                        startActivity(new Intent(CartActivity.this, PaymentAfterCart.class));
                        finish();

                    }
                },1500);

            }
        });
    }


    private void getCartitem() {
        FirebaseRecyclerOptions<cartModel> options =
                new FirebaseRecyclerOptions.Builder<cartModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AddToCart").child(UID),cartModel.class)
                        .build();
        cartAdapter= new CartAdapter(options);
        recyclerView.setAdapter(cartAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CartActivity.this, merchFragment.class));
        finish();
    }
}