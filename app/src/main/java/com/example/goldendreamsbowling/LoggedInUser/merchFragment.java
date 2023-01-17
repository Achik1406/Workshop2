package com.example.goldendreamsbowling.LoggedInUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.goldendreamsbowling.Cap_page;
import com.example.goldendreamsbowling.CartActivity;
import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.MainActivity;
import com.example.goldendreamsbowling.Mug_page;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.Totebag_page;
import com.example.goldendreamsbowling.Tshirt_page;
import com.example.goldendreamsbowling.databinding.ActivityMerchFragmentBinding;
import com.example.goldendreamsbowling.viewMerchPayment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class merchFragment extends Drawer_base {

    ActivityMerchFragmentBinding activityMerchFragmentBinding;
    FloatingActionButton floatingActionButton;
    TextView cartCounter;
    FirebaseAuth ID;
    String UID;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMerchFragmentBinding = activityMerchFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityMerchFragmentBinding.getRoot());
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        floatingActionButton = findViewById(R.id.floatingCart);
        cartCounter = findViewById(R.id.cartCounter);
        getcartCount();
        activityMerchFragmentBinding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, viewMerchPayment.class));
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, CartActivity.class));
                finish();
            }
        });

        activityMerchFragmentBinding.layoutTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Tshirt_page.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutMug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Mug_page.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Cap_page.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutTotebag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Totebag_page.class));
                finish();
            }
        });


    }
    public void getcartCount() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");

        databaseReference.child("AddToCart").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    count = (int) snapshot.getChildrenCount();
                    cartCounter.setText(Integer.toString(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomePage.class));
        overridePendingTransition(0,0);
        finish();
    }
}