package com.example.goldendreamsbowling.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldendreamsbowling.AboutUsFragment;
import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.LoggedInUser.BookingFragment;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.SubsMember;
import com.example.goldendreamsbowling.databinding.ActivityStartBookBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class StartBook extends AppCompatActivity {

    ActivityStartBookBinding binding;
    DatePickerDialog datePicker;
    int NumGame,NumPerson;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");
    FirebaseAuth ID;
    String UID;
    TextView price;
    int k,n,countL1;
    CheckBox c1;
    double TotalPrice,tax=0.08,discount;
    Double PricePerGame = 5.50, PriceShoes,PriceRM;
    String Times,gk,Shoes,value;
    String lane;
    int countplayer=0,countgame=0,countshoes=0;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        databaseReference.child("ClaimedPromo").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    final String dataName=snapshot.child("PromoCode").getValue().toString();
                    final String datavalue=snapshot.child("PromoValue").getValue().toString();

                    binding.couponValue.setText("You get RM" + datavalue);
                    binding.couponCode.setText("From Coupon : "+dataName);
                    value = datavalue;
                }
                else{
                    binding.couponValue.setText("No coupon Claimed!");
                    binding.couponCode.setText("");
                    value = String.valueOf(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        price = (TextView) findViewById(R.id.Price);

        binding.btnDecPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countplayer <=0){
                    countplayer=0;
                }
                else
                {
                    countplayer--;
                    binding.valueplayer.setText(""+countplayer);
                }

            }
        });
        binding.btnIncPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countplayer++;
                binding.valueplayer.setText(""+countplayer);
            }
        });
        binding.btnDecGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countgame <=0){
                    countgame=0;
                }
                else
                {
                    countgame--;
                    binding.valuegame.setText(""+countgame);
                }

            }
        });
        binding.btnIncGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countgame>=3){
                    countgame=3;
                }
                else
                {
                    countgame++;
                    binding.valuegame.setText(""+countgame);
                }

            }
        });

        binding.btnDecShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countshoes <=0){
                    countshoes=0;
                }
                else
                {
                    countshoes--;
                    binding.valueShoes.setText(""+countshoes);
                }

            }
        });
        binding.btnIncShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countshoes++;
                binding.valueShoes.setText(""+countshoes);
            }
        });

        binding.Date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b)
            {
                if(b) {
                    fnInvokeDatePicker();
                }
                if(!b)
                {
                    fnFormValidaton();
                }

            }
        });
        binding.Date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                fnInvokeDatePicker();
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price.setText("");
                binding.linLane.setVisibility(View.GONE);
                binding.linSelection.setVisibility(View.GONE);
                binding.spnTime.setVisibility(View.INVISIBLE);
                binding.spnTime2.setVisibility(View.INVISIBLE);
                binding.spnTime3.setVisibility(View.INVISIBLE);
                NumPerson = Integer.parseInt(binding.valueplayer.getText().toString());
                Shoes = binding.valueShoes.getText().toString();
                int s = Integer.parseInt(Shoes);
                String date = binding.Date.getText().toString().trim();

                if(Shoes != "0")
                {
                    PriceShoes = 1.20;
                }
                else
                {
                    PriceShoes = 0.00;
                }
                NumGame = Integer.parseInt(binding.valuegame.getText().toString());
                double coupon = Double.valueOf(value);

                PriceRM = (PricePerGame*countgame*countplayer)+(PriceShoes*s)-coupon;
                gk= df.format(PriceRM);
                price.setText(gk);

                if(date.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Date", Toast.LENGTH_SHORT).show();
                }
                else if(countgame==1&&countplayer>0){
                    binding.linSelection.setVisibility(View.VISIBLE);
                    binding.spnTime.setVisibility(View.VISIBLE);
                    binding.txtChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Times = binding.spnTime.getSelectedItem().toString().trim();
                            binding.linLane.setVisibility(View.VISIBLE);
                            databaseReference.child("CheckLane").child(date).child(Times).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane1").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane2").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane3").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane4").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane5").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane6").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane7").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane8").child("k1").setValue("a");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane1").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green1.setVisibility(View.GONE);
                                            binding.red1.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red1.setVisibility(View.GONE);
                                            binding.green1.setVisibility(View.VISIBLE);
                                            binding.green1.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green1.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane1";
                                                }
                                            });


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane2").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green2.setVisibility(View.GONE);
                                            binding.red2.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red2.setVisibility(View.GONE);
                                            binding.green2.setVisibility(View.VISIBLE);
                                            binding.green2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green2.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green1.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane2";


                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane3").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green3.setVisibility(View.GONE);
                                            binding.red3.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red3.setVisibility(View.GONE);
                                            binding.green3.setVisibility(View.VISIBLE);
                                            binding.green3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green3.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane3";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane4").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green4.setVisibility(View.GONE);
                                            binding.red4.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red4.setVisibility(View.GONE);
                                            binding.green4.setVisibility(View.VISIBLE);
                                            binding.green4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green4.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane4";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane5").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green5.setVisibility(View.GONE);
                                            binding.red5.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red5.setVisibility(View.GONE);
                                            binding.green5.setVisibility(View.VISIBLE);
                                            binding.green5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green5.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane5";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane6").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green6.setVisibility(View.GONE);
                                            binding.red6.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red6.setVisibility(View.GONE);
                                            binding.green6.setVisibility(View.VISIBLE);
                                            binding.green6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green6.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane6";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane7").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green7.setVisibility(View.GONE);
                                            binding.red7.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red7.setVisibility(View.GONE);
                                            binding.green7.setVisibility(View.VISIBLE);
                                            binding.green7.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green7.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane7";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane8").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green8.setVisibility(View.GONE);
                                            binding.red8.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red8.setVisibility(View.GONE);
                                            binding.green8.setVisibility(View.VISIBLE);
                                            binding.green8.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green8.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    lane = "lane8";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            binding.checkout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Times = binding.spnTime.getSelectedItem().toString().trim();
                                    check();
                                }
                            });


                        }


                    });



                }
                else if(countgame==2&&countplayer>0)
                {
                    binding.linSelection.setVisibility(View.VISIBLE);
                    binding.spnTime2.setVisibility(View.VISIBLE);
                    binding.txtChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //binding.txtexpandless.setVisibility(View.VISIBLE);
                            //binding.txtexpandmore.setVisibility(View.GONE);
                            binding.linLane.setVisibility(View.VISIBLE);
                            Times = binding.spnTime2.getSelectedItem().toString().trim();
                            databaseReference.child("CheckLane").child(date).child(Times).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane1").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane2").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane3").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane4").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane5").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane6").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane7").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane8").child("k1").setValue("a");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane1").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green1.setVisibility(View.GONE);
                                            binding.red1.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red1.setVisibility(View.GONE);
                                            binding.green1.setVisibility(View.VISIBLE);
                                            binding.green1.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green1.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane1";
                                                }
                                            });


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane2").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green2.setVisibility(View.GONE);
                                            binding.red2.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red2.setVisibility(View.GONE);
                                            binding.green2.setVisibility(View.VISIBLE);
                                            binding.green2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green2.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green1.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane2";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane3").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green3.setVisibility(View.GONE);
                                            binding.red3.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red3.setVisibility(View.GONE);
                                            binding.green3.setVisibility(View.VISIBLE);
                                            binding.green3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green3.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane3";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane4").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green4.setVisibility(View.GONE);
                                            binding.red4.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red4.setVisibility(View.GONE);
                                            binding.green4.setVisibility(View.VISIBLE);
                                            binding.green4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green4.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane4";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane5").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green5.setVisibility(View.GONE);
                                            binding.red5.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red5.setVisibility(View.GONE);
                                            binding.green5.setVisibility(View.VISIBLE);
                                            binding.green5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green5.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane5";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane6").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green6.setVisibility(View.GONE);
                                            binding.red6.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red6.setVisibility(View.GONE);
                                            binding.green6.setVisibility(View.VISIBLE);
                                            binding.green6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green6.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane6";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane7").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green7.setVisibility(View.GONE);
                                            binding.red7.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red7.setVisibility(View.GONE);
                                            binding.green7.setVisibility(View.VISIBLE);
                                            binding.green7.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green7.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane7";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane8").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green8.setVisibility(View.GONE);
                                            binding.red8.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red8.setVisibility(View.GONE);
                                            binding.green8.setVisibility(View.VISIBLE);
                                            binding.green8.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green8.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    lane = "lane8";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }


                    });

                    binding.checkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Times = binding.spnTime2.getSelectedItem().toString().trim();
                            check();
                        }
                    });


                }
                else if(countgame==3&&countplayer>0)
                {
                    binding.linSelection.setVisibility(View.VISIBLE);
                    binding.spnTime3.setVisibility(View.VISIBLE);
                    binding.txtChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //binding.txtexpandless.setVisibility(View.VISIBLE);
                            //binding.txtexpandmore.setVisibility(View.GONE);
                            binding.linLane.setVisibility(View.VISIBLE);
                            Times = binding.spnTime3.getSelectedItem().toString().trim();
                            databaseReference.child("CheckLane").child(date).child(Times).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane1").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane2").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane3").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane4").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane5").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane6").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane7").child("k1").setValue("a");
                                    databaseReference.child("CheckLane").child(date).child(Times).child("lane8").child("k1").setValue("a");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane1").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==5){
                                            binding.green1.setVisibility(View.GONE);
                                            binding.red1.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red1.setVisibility(View.GONE);
                                            binding.green1.setVisibility(View.VISIBLE);
                                            binding.green1.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green1.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane1";
                                                }
                                            });


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane2").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green2.setVisibility(View.GONE);
                                            binding.red2.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red2.setVisibility(View.GONE);
                                            binding.green2.setVisibility(View.VISIBLE);
                                            binding.green2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green2.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green1.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane2";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane3").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green3.setVisibility(View.GONE);
                                            binding.red3.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red3.setVisibility(View.GONE);
                                            binding.green3.setVisibility(View.VISIBLE);
                                            binding.green3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green3.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane3";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane4").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green4.setVisibility(View.GONE);
                                            binding.red4.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red4.setVisibility(View.GONE);
                                            binding.green4.setVisibility(View.VISIBLE);
                                            binding.green4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green4.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane4";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane5").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green5.setVisibility(View.GONE);
                                            binding.red5.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red5.setVisibility(View.GONE);
                                            binding.green5.setVisibility(View.VISIBLE);
                                            binding.green5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green5.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane5";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane6").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green6.setVisibility(View.GONE);
                                            binding.red6.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red6.setVisibility(View.GONE);
                                            binding.green6.setVisibility(View.VISIBLE);
                                            binding.green6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green6.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane6";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            databaseReference.child("CheckLane").child(date).child(Times).child("lane7").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green7.setVisibility(View.GONE);
                                            binding.red7.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red7.setVisibility(View.GONE);
                                            binding.green7.setVisibility(View.VISIBLE);
                                            binding.green7.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green7.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    binding.green8.setChecked(false);
                                                    lane = "lane7";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("CheckLane").child(date).child(Times).child("lane8").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        countL1 = (int) snapshot.getChildrenCount();
                                        if(countL1==2){
                                            binding.green8.setVisibility(View.GONE);
                                            binding.red8.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            binding.red8.setVisibility(View.GONE);
                                            binding.green8.setVisibility(View.VISIBLE);
                                            binding.green8.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(binding.green8.isChecked())
                                                    {
                                                        binding.checkout.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        binding.checkout.setVisibility(View.GONE);
                                                    }
                                                    binding.green2.setChecked(false);
                                                    binding.green3.setChecked(false);
                                                    binding.green4.setChecked(false);
                                                    binding.green5.setChecked(false);
                                                    binding.green6.setChecked(false);
                                                    binding.green7.setChecked(false);
                                                    binding.green1.setChecked(false);
                                                    lane = "lane8";
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }


                    });
                    binding.checkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Times = binding.spnTime3.getSelectedItem().toString().trim();
                            check();
                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Game or Player", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void check() {

        SharedPreferences sharedPreferences = getSharedPreferences(SubsMember.Subb,0);
        boolean hasSubbed = sharedPreferences.getBoolean("hasSubbed",false);
        if(hasSubbed)
        {
            discount = PriceRM*0.15;
        }
        else
        {
            discount=0.0;
        }
        tax = PriceRM*tax;
        String t = df.format(tax);
        TotalPrice = (PriceRM-discount)+tax;
        String totalPrice = df.format(TotalPrice);
        String TotPrice = (totalPrice);
        String Taxx = (t);
        String Dis = String.valueOf(discount);
        String date = binding.Date.getText().toString().trim();
        String Numplayer = String.valueOf(NumPerson);
        String Numgames = String.valueOf(NumGame);
        String pickShoes = binding.valueShoes.getText().toString().trim();
        String time = Times;
        String pricecheck = gk;

        Intent intent = new Intent(StartBook.this, CheckOutBook.class);
        intent.putExtra("DATE_TEXT", date);
        intent.putExtra("PLAYER_TEXT",Numplayer);
        intent.putExtra("GAME_TEXT",Numgames);
        intent.putExtra("SHOES_TEXT",pickShoes);
        intent.putExtra("TIME_TEXT",time);
        intent.putExtra("PRICE_TEXT",pricecheck);
        intent.putExtra("Total_Price",TotPrice);
        intent.putExtra("tax",Taxx);
        intent.putExtra("Diss",Dis);
        intent.putExtra("Lane",lane);
        startActivity(intent);
        finish();
    }

    private void fnFormValidaton() {
    }

    private void fnInvokeDatePicker()
    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog

        datePicker = new DatePickerDialog(StartBook.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.Date.setText(dayOfMonth + "-" + (month+1) + "-" + year);
            }
        },year,month,day);
        datePicker.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StartBook.this, BookingFragment.class));
        finish();
    }
}