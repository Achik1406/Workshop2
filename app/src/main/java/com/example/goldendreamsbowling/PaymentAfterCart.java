package com.example.goldendreamsbowling;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldendreamsbowling.Booking.CheckOutBook;
import com.example.goldendreamsbowling.JavaFile.PayCartModel;
import com.example.goldendreamsbowling.JavaFile.PaymentCartAdapter;
import com.example.goldendreamsbowling.LoggedInUser.merchFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PaymentAfterCart extends AppCompatActivity {

    RecyclerView recyclerView;
    List<PayCartModel> paycartModels;
    PaymentCartAdapter paymentCartAdapter;
    Button pay;
    FirebaseAuth ID;
    String UID;
    CheckBox c1,c2,c3;
    String payMethod,PayID;
    TextView ViewCart;
    TextView txtless;
    TextView txtmore;
    TextView totalPrice;
    String totalP;
    ScrollView scrollView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_after_cart);
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();


        ViewCart = findViewById(R.id.txtviewcart);
        txtless = findViewById(R.id.txtexpandless);
        txtmore = findViewById(R.id.txtexpandmore);
        recyclerView = findViewById(R.id.PaycartRecView);
        scrollView = findViewById(R.id.swip);
        totalPrice = findViewById(R.id.TotalPrice);
        recyclerView.setHasFixedSize(true);
        pay = findViewById(R.id.Pay);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentAfterCart.this);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        paycartModels = new ArrayList<>();
        getCartitem();
        getPrice();

        c1 = findViewById(R.id.Debit);
        c2 = findViewById(R.id.Visa);
        c3 = findViewById(R.id.onlineBank);
        Random random = new Random();
        int value = random.nextInt(10000);
        PayID = Integer.toString(value);
        String pID = "BNT0021"+PayID;

        ViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Isvisible = scrollView.getVisibility();
                if(Isvisible==View.VISIBLE){
                    scrollView.setVisibility(View.GONE);
                    txtless.setVisibility(View.GONE);
                    txtmore.setVisibility(View.VISIBLE);
                }
                else{
                    scrollView.setVisibility(View.VISIBLE);
                    txtless.setVisibility(View.VISIBLE);
                    txtmore.setVisibility(View.GONE);


                }
            }
        });

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

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(PaymentAfterCart.this);
                progressDialog.setTitle("Process Payment ");
                progressDialog.setMessage("Please wait,while we processing the payment");
                progressDialog.show();
                Handler handler = new Handler();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);
                String currentString = thisDate;
                String[] separated = currentString.split("-");
                String day = separated[0];
                String month = separated[1];
                String year = separated[2];
                int a = Integer.parseInt(day);
                int b = Integer.parseInt(month);
                int c = Integer.parseInt(year);
                String date = a+"-"+(b)+"-"+c;

                databaseReference.child("AddToCart").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.child("MerchPayment").child(UID).child("productBuy").setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Log.d(TAG, "Success!");
                                } else {
                                    Log.d(TAG, "Failed!");
                                }
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child("MerchPayment").child(UID).child("Payment").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("MerchPayment").child(UID).child("Payment").child("TotalPrice").setValue(totalP);
                        databaseReference.child("MerchPayment").child(UID).child("Payment").child("PaymentID").setValue(pID);
                        databaseReference.child("MerchPayment").child(UID).child("Payment").child("PaymentMethod").setValue(payMethod);
                        databaseReference.child("MerchPayment").child(UID).child("Payment").child("Date").setValue(date);
                        databaseReference.child("AddTotal").child(UID).child("Total").removeValue();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                databaseReference.child("AddToCart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.child(UID).getRef().removeValue();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMessage("Payment Success!");
                        progressDialog.dismiss();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(PaymentAfterCart.this,"GoldenDream");
                        builder.setSmallIcon(R.drawable.logo);
                        builder.setContentTitle("Merchandise Purchasing");
                        builder.setContentText("Thanks for buying merchandise with us");
                        builder.setAutoCancel(true);
                        NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(PaymentAfterCart.this);
                        notificationManagerCompat.notify(1,builder.build());
                        startActivity(new Intent(PaymentAfterCart.this, merchFragment.class));
                        finish();

                    }
                },1500);


            }
        });

    }

    private void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d(TAG, "Success!");
                        } else {
                            Log.d(TAG, "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Never ignore potential errors!
            }
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }




    private void getPrice() {
        FirebaseDatabase.getInstance().getReference().child("AddTotal").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    final String dataAdd=snapshot.child("Total").getValue().toString();
                    totalPrice.setText("Total Price is : RM "+dataAdd);
                    totalP = dataAdd;
                }
                else
                {
                    //totalPrice.setText("Total Price is : RM0.00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCartitem() {
        FirebaseRecyclerOptions<PayCartModel> options =
                new FirebaseRecyclerOptions.Builder<PayCartModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AddToCart").child(UID), PayCartModel.class)
                        .build();
        paymentCartAdapter= new PaymentCartAdapter(options);
        recyclerView.setAdapter(paymentCartAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        paymentCartAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        paymentCartAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentAfterCart.this, CartActivity.class));
        finish();
    }
}