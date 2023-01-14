package com.example.goldendreamsbowling;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.goldendreamsbowling.Booking.CheckOutBook;
import com.example.goldendreamsbowling.LoggedInUser.Drawer_base;
import com.example.goldendreamsbowling.databinding.ActivitySubsMemberBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubsMember extends Drawer_base {

    public static String Subb ="MyFile";

    ActivitySubsMemberBinding activitySubsMemberBinding;
    FirebaseAuth ID;
    Button Subs;

    String UID;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");

    public SubsMember() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySubsMemberBinding = activitySubsMemberBinding.inflate(getLayoutInflater());
        setContentView(activitySubsMemberBinding.getRoot());

        ID = FirebaseAuth.getInstance();
        Subs = findViewById(R.id.subs);

        UID = ID.getCurrentUser().getUid();
        databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String txtName =snapshot.child("fullname").getValue().toString();
                activitySubsMemberBinding.txtName.setText("Hye " + txtName + "!!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscription();

            }
        });
    }

    public void Subscription(){
        UID = ID.getCurrentUser().getUid();
        databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String dataName=snapshot.child("fullname").getValue().toString();
                final String dataEmail=snapshot.child("email").getValue().toString();

                databaseReference.child("membership").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(UID)) {
                            Toast.makeText(SubsMember.this, "User already registered", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(Subb,0);
                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putBoolean("hasSubbed",true);
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {

                            databaseReference.child("membership").child(UID).child("FullName").setValue(dataName);
                            databaseReference.child("membership").child(UID).child("FullName").setValue(dataName);
                            databaseReference.child("membership").child(UID).child("Email").setValue(dataEmail);
                            databaseReference.child("membership").child(UID).child("Status").setValue("Active");
                            Toast.makeText(SubsMember.this, "Subscription successful", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(Subb,0);
                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putBoolean("hasSubbed",true);
                            editor.commit();

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(SubsMember.this,"GoldenDream");
                            builder.setSmallIcon(R.drawable.logo);
                            builder.setContentTitle("Golden Dream Membership");
                            builder.setContentText("You are now members, Thanks for choosing us");
                            builder.setAutoCancel(true);
                            NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(SubsMember.this);
                            notificationManagerCompat.notify(1,builder.build());

                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);
                            finish();

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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