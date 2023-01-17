package com.example.goldendreamsbowling.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.goldendreamsbowling.JavaFile.ViewBookAdapter;
import com.example.goldendreamsbowling.JavaFile.ViewBookModel;
import com.example.goldendreamsbowling.LoggedInUser.BookingFragment;
import com.example.goldendreamsbowling.R;

import com.example.goldendreamsbowling.databinding.ActivityViewBookingBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewBooking extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<ViewBookModel> bookModels;
    ViewBookAdapter viewBookAdapter;
    ///problemmmmmmmm
    ActivityViewBookingBinding bind;
    private SwipeRefreshLayout SwipeRefreshLayout;
    FirebaseAuth ID;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = bind.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Booking").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                }
                else{
                    startActivity(new Intent(ViewBooking.this, BookingFragment.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView = findViewById(R.id.cartRecView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewBooking.this);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        bookModels = new ArrayList<>();
        getCartitem();
        SwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swip);
        SwipeRefreshLayout.setOnRefreshListener(this);


    }

    private void getCartitem() {
        FirebaseRecyclerOptions<ViewBookModel> options =
                new FirebaseRecyclerOptions.Builder<ViewBookModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Booking").child(UID),ViewBookModel.class)
                        .build();
        viewBookAdapter= new ViewBookAdapter(options);
        recyclerView.setAdapter(viewBookAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        viewBookAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewBookAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewBooking.this, BookingFragment.class));
        finish();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}