package com.example.goldendreamsbowling.Guest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.JavaFile.listAnn;
import com.example.goldendreamsbowling.MainActivity;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.databinding.ActivityAnnounceFrag2Binding;
import com.example.goldendreamsbowling.list_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnnounceFrag2 extends DrawerGuest {

    ActivityAnnounceFrag2Binding activityAnnounceFragmentBinding;

    List<listAnn> listAnnounce;
    ListView listView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAnnounceFragmentBinding =activityAnnounceFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityAnnounceFragmentBinding.getRoot());

        listView = findViewById(R.id.listviewID);
        listAnnounce = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Announcement");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAnnounce.clear();
                for( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    listAnn listAnn = dataSnapshot.getValue(listAnn.class);
                    listAnnounce.add(listAnn);
                }

                list_adapter list = new list_adapter(AnnounceFrag2.this,listAnnounce);

                listView.setAdapter(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}