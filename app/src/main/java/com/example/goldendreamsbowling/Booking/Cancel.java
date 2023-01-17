package com.example.goldendreamsbowling.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.LoggedInUser.BookingFragment;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.databinding.ActivityCancelBinding;
import com.example.goldendreamsbowling.signUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cancel extends AppCompatActivity {

    ActivityCancelBinding bind;
    FirebaseAuth ID;
    String UID;
    int count;
    String dataDatess,dataTimess,dataLanee,dataID,dataEmails,dataPayID,dataEmail;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =bind.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        ID = FirebaseAuth.getInstance();

        bind.yess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UID = ID.getCurrentUser().getUid();
                bind.cancelEmail.setVisibility(View.VISIBLE);
                bind.cancelID.setVisibility(View.VISIBLE);
                bind.submit.setVisibility(View.VISIBLE);
                Intent intent = getIntent();
                String mail = intent.getStringExtra("E_TEXT");
                String id = intent.getStringExtra("I_TEXT");
                bind.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = bind.cancelEmail.getText().toString();
                        final String PayID = bind.cancelID.getText().toString();
                        databaseReference.child("Payment").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(mail.equals(email) && id.equals(PayID))
                                {
                                    for(DataSnapshot capshot : snapshot.getChildren())
                                    {
                                        final String dataPayID=capshot.child("PaymentID").getValue().toString().trim();
                                        final String dataEmail=capshot.child("Email").getValue().toString().trim();
                                        if(dataEmail.equals(email) && dataPayID.equals(PayID) )
                                        {
                                            capshot.child("Date").getRef().removeValue();
                                            capshot.child("Time").getRef().removeValue();
                                            capshot.child("Email").getRef().removeValue();
                                            capshot.child("PaymentMethod").getRef().removeValue();
                                            capshot.child("NumberGame").getRef().removeValue();
                                            capshot.child("NumberPlayer").getRef().removeValue();
                                            capshot.child("NumberShoes").getRef().removeValue();
                                            capshot.child("PaymentID").getRef().removeValue();
                                            capshot.child("TotalPrice").getRef().removeValue();
                                            databaseReference.child("Booking").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot napshot) {
                                                    for (DataSnapshot capshot : napshot.getChildren())
                                                    {
                                                        dataID=capshot.child("PaymentID").getValue().toString().trim();
                                                        dataEmails=capshot.child("Email").getValue().toString().trim();
                                                        if(dataEmails.equals(email) && dataID.equals(PayID))
                                                        {
                                                            dataDatess = capshot.child("Date").getValue().toString();
                                                            dataTimess = capshot.child("Time").getValue().toString();
                                                            dataLanee = capshot.child("Lane").getValue().toString();
                                                            if(dataEmails.equals(email) && dataID.equals(PayID) )
                                                            {
                                                                capshot.child("Date").getRef().removeValue();
                                                                capshot.child("Time").getRef().removeValue();
                                                                capshot.child("FullName").getRef().removeValue();
                                                                capshot.child("Email").getRef().removeValue();
                                                                capshot.child("Lane").getRef().removeValue();
                                                                capshot.child("NumberGame").getRef().removeValue();
                                                                capshot.child("NumberPlayer").getRef().removeValue();
                                                                capshot.child("NumberShoes").getRef().removeValue();
                                                                capshot.child("PaymentID").getRef().removeValue();
                                                                capshot.child("TotalPrice").getRef().removeValue();
                                                                databaseReference.child("CheckLane").child(dataDatess).child(dataTimess).child(dataLanee).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        snapshot.child(UID).child("status").getRef().removeValue();
                                                                        databaseReference.child("CounterBook").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                if(snapshot.exists())
                                                                                {
                                                                                    count = (int) snapshot.getChildrenCount();
                                                                                    String n = String.valueOf(count);
                                                                                    databaseReference.child("CounterBook").child(UID).child(n).child("k1").removeValue();
                                                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Cancel.this,"GoldenDream");
                                                                                    builder.setSmallIcon(R.drawable.logo);
                                                                                    builder.setContentTitle("Golden Refund");
                                                                                    builder.setContentText("Refund will be send in 14 days working day");
                                                                                    builder.setAutoCancel(true);
                                                                                    NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(Cancel.this);
                                                                                    notificationManagerCompat.notify(1,builder.build());
                                                                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                }

                                                                            }
                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {}
                                                                        });
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }

                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(Cancel.this, "Error!", Toast.LENGTH_SHORT).show();
                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

            }
        });

        bind.nope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookingFragment.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Cancel.this, BookingFragment.class));
        finish();
    }
}