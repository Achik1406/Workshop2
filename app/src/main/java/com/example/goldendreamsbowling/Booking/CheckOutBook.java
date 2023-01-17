package com.example.goldendreamsbowling.Booking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.LoggedInUser.Account;
import com.example.goldendreamsbowling.LoggedInUser.BookingFragment;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.databinding.ActivityCheckOutBookBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CheckOutBook extends AppCompatActivity {


    ActivityCheckOutBookBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");
    FirebaseAuth ID;
    String UID;
    CheckBox c1,c2,c3;
    int c;
    String payMethod="",PayID,counter ="1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ID = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String date = intent.getStringExtra("DATE_TEXT");
        String noPlayer = intent.getStringExtra("PLAYER_TEXT");
        String noGames = intent.getStringExtra("GAME_TEXT");
        String pickShoes = intent.getStringExtra("SHOES_TEXT");
        String time = intent.getStringExtra("TIME_TEXT");
        String priceCheck = intent.getStringExtra("PRICE_TEXT");
        String Discount = intent.getStringExtra("Diss");
        String Taxx = intent.getStringExtra("tax");
        String totalPrice = intent.getStringExtra("Total_Price");
        String Lane = intent.getStringExtra("Lane");
        binding.DatePay.setText("Date : " + date);
        binding.NoPlayer.setText("Number of Player : "+ noPlayer);
        binding.noGame.setText("Number of Games : "+ noGames);
        binding.ShoesPay.setText("Number of Shoes : "+ pickShoes);
        binding.TimePay.setText("Time : "+ time);
        binding.subtotal.setText("Subtotal : RM"+ priceCheck);
        binding.discount.setText("Discount : RM"+ Discount);
        binding.tax.setText("Tax : RM"+ Taxx);
        binding.totall.setText("Total Price : RM"+ totalPrice);
        Intent i = new Intent(CheckOutBook.this, ViewBooking.class);
        i.putExtra("DATE_TEXT", date);

        c1 = findViewById(R.id.Debit);
        c2 = findViewById(R.id.Visa);
        c3 = findViewById(R.id.onlineBank);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c2.setChecked(false);
                c3.setChecked(false);
                payMethod = c1.getText().toString().trim();
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.setChecked(false);
                c3.setChecked(false);
                payMethod = c2.getText().toString().trim();
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMethod = c3.getText().toString().trim();
                c2.setChecked(false);
                c1.setChecked(false);
            }
        });

        Random random = new Random();
        int value = random.nextInt(10000);
        PayID = Integer.toString(value);
        String pp = "GDB005"+PayID;

        binding.payy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UID = ID.getCurrentUser().getUid();

                databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String dataName=snapshot.child("fullname").getValue().toString();
                        final String dataEmail=snapshot.child("email").getValue().toString();

                        databaseReference.child("CheckLane").child(date).child(time).child(Lane).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("CheckLane").child(date).child(time).child(Lane).child(UID).child("status").setValue("occupied");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        databaseReference.child("ClaimedPromo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("ClaimedPromo").child(UID).child("PromoCode").removeValue();
                                databaseReference.child("ClaimedPromo").child(UID).child("PromoValue").removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        databaseReference.child("CounterBook").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    int  datacounter= (int) snapshot.getChildrenCount();
                                     c = datacounter+1;
                                     String d = String.valueOf(c);
                                    databaseReference.child("CounterBook").child(UID).child(d).child("k1").setValue("");

                                    databaseReference.child("Booking").child(UID).child(d).child("Date").setValue(date);
                                    databaseReference.child("Booking").child(UID).child(d).child("Time").setValue(time);
                                    databaseReference.child("Booking").child(UID).child(d).child("FullName").setValue(dataName);
                                    databaseReference.child("Booking").child(UID).child(d).child("Email").setValue(dataEmail);
                                    databaseReference.child("Booking").child(UID).child(d).child("Lane").setValue(Lane);
                                    databaseReference.child("Booking").child(UID).child(d).child("NumberPlayer").setValue(noPlayer);
                                    databaseReference.child("Booking").child(UID).child(d).child("NumberGame").setValue(noGames);
                                    databaseReference.child("Booking").child(UID).child(d).child("NumberShoes").setValue(pickShoes);
                                    databaseReference.child("Booking").child(UID).child(d).child("TotalPrice").setValue(totalPrice);
                                    databaseReference.child("Booking").child(UID).child(d).child("PaymentID").setValue(pp);

                                    databaseReference.child("Payment").child(UID).child(d).child("Date").setValue(date);
                                    databaseReference.child("Payment").child(UID).child(d).child("Time").setValue(time);
                                    databaseReference.child("Payment").child(UID).child(d).child("PaymentID").setValue(pp);
                                    databaseReference.child("Payment").child(UID).child(d).child("Email").setValue(dataEmail);
                                    databaseReference.child("Payment").child(UID).child(d).child("NumberPlayer").setValue(noPlayer);
                                    databaseReference.child("Payment").child(UID).child(d).child("NumberGame").setValue(noGames);
                                    databaseReference.child("Payment").child(UID).child(d).child("NumberShoes").setValue(pickShoes);
                                    databaseReference.child("Payment").child(UID).child(d).child("TotalPrice").setValue(totalPrice);
                                    databaseReference.child("Payment").child(UID).child(d).child("PaymentMethod").setValue(payMethod);
                                }
                                else
                                {
                                    databaseReference.child("CounterBook").child(UID).child(counter).child("k1").setValue("");

                                    databaseReference.child("Booking").child(UID).child(counter).child("Date").setValue(date);
                                    databaseReference.child("Booking").child(UID).child(counter).child("Time").setValue(time);
                                    databaseReference.child("Booking").child(UID).child(counter).child("FullName").setValue(dataName);
                                    databaseReference.child("Booking").child(UID).child(counter).child("Email").setValue(dataEmail);
                                    databaseReference.child("Booking").child(UID).child(counter).child("Lane").setValue(Lane);
                                    databaseReference.child("Booking").child(UID).child(counter).child("NumberPlayer").setValue(noPlayer);
                                    databaseReference.child("Booking").child(UID).child(counter).child("NumberGame").setValue(noGames);
                                    databaseReference.child("Booking").child(UID).child(counter).child("NumberShoes").setValue(pickShoes);
                                    databaseReference.child("Booking").child(UID).child(counter).child("TotalPrice").setValue(totalPrice);
                                    databaseReference.child("Booking").child(UID).child(counter).child("PaymentID").setValue(pp);

                                    databaseReference.child("Payment").child(UID).child(counter).child("Date").setValue(date);
                                    databaseReference.child("Payment").child(UID).child(counter).child("Time").setValue(time);
                                    databaseReference.child("Payment").child(UID).child(counter).child("PaymentID").setValue(pp);
                                    databaseReference.child("Payment").child(UID).child(counter).child("Email").setValue(dataEmail);
                                    databaseReference.child("Payment").child(UID).child(counter).child("NumberPlayer").setValue(noPlayer);
                                    databaseReference.child("Payment").child(UID).child(counter).child("NumberGame").setValue(noGames);
                                    databaseReference.child("Payment").child(UID).child(counter).child("NumberShoes").setValue(pickShoes);
                                    databaseReference.child("Payment").child(UID).child(counter).child("TotalPrice").setValue(totalPrice);
                                    databaseReference.child("Payment").child(UID).child(counter).child("PaymentMethod").setValue(payMethod);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                            NotificationChannel channel = new NotificationChannel("GoldenDream","GoldenDream", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);
                        }
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(CheckOutBook.this,"GoldenDream");
                        builder.setSmallIcon(R.drawable.logo);
                        builder.setContentTitle("Booking");
                        builder.setContentText("Booking Success! Thank you!");
                        builder.setAutoCancel(true);
                        NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(CheckOutBook.this);
                        notificationManagerCompat.notify(1,builder.build());
                        Toast.makeText(CheckOutBook.this, "Booking successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                        finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CheckOutBook.this, StartBook.class));
        finish();
    }
}