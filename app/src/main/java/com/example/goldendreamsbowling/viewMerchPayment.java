package com.example.goldendreamsbowling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldendreamsbowling.Guest.merchFrag2;
import com.example.goldendreamsbowling.JavaFile.PayCartModel;
import com.example.goldendreamsbowling.JavaFile.PaymentCartAdapter;
import com.example.goldendreamsbowling.JavaFile.ViewModel;
import com.example.goldendreamsbowling.JavaFile.viewmerchpayment;
import com.example.goldendreamsbowling.LoggedInUser.merchFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewMerchPayment extends AppCompatActivity {

    TextView ViewCart,date,paymentID,price;
    FirebaseAuth ID;
    viewmerchpayment viewMerchPayment;
    String UID;
    ScrollView scrollView;
    List<ViewModel> vModels;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_merch_payment);
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        scrollView = findViewById(R.id.swip);
        ViewCart = findViewById(R.id.txtviewitem);
        recyclerView = findViewById(R.id.productRecView);
        date = findViewById(R.id.date);
        paymentID = findViewById(R.id.PaymentID);
        price = findViewById(R.id.TotalPrice);

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewMerchPayment.this);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        vModels = new ArrayList<>();
        getmerchitem();
        getdetail();

    }

    private void getdetail() {
        FirebaseDatabase.getInstance().getReference().child("MerchPayment").child(UID).child("Payment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    final String datapay=snapshot.child("TotalPrice").getValue().toString();
                    final String datadate=snapshot.child("Date").getValue().toString();
                    final String dataid=snapshot.child("PaymentID").getValue().toString();
                    price.setText("Total Price is : RM "+datapay);
                    date.setText("Date : "+datadate);
                    paymentID.setText("Payment ID : "+dataid);
                }
                else
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getmerchitem() {
        FirebaseDatabase.getInstance().getReference().child("MerchPayment").child(UID).child("productBuy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                }
                else
                {
                    Toast.makeText(viewMerchPayment.this, "Buy a product first!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(viewMerchPayment.this, merchFragment.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        FirebaseRecyclerOptions<ViewModel> options =
                new FirebaseRecyclerOptions.Builder<ViewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MerchPayment").child(UID).child("productBuy"), ViewModel.class)
                        .build();
        viewMerchPayment= new viewmerchpayment(options);
        recyclerView.setAdapter(viewMerchPayment);

    }
    @Override
    public void onStart() {
        super.onStart();
        viewMerchPayment.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewMerchPayment.stopListening();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(viewMerchPayment.this, merchFragment.class));
        finish();
    }
}