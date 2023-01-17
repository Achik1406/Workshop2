package com.example.goldendreamsbowling.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.goldendreamsbowling.LoggedInUser.BookingFragment;
import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.databinding.ActivityCancelUpdateBookingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CancelUpdateBooking extends AppCompatActivity {

    ActivityCancelUpdateBookingBinding bind;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");
    FirebaseAuth ID;
    String UID;
    DatePickerDialog datePicker;
    String dataDate,dataTime,dataID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =bind.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        bind.choosedate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b)
            {
                if(b) {
                    fnInvokeDatePicker2();
                }
                if(!b)
                {
                    fnFormValidaton2();
                }

            }
        });
        bind.choosedate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                fnInvokeDatePicker2();
            }
        });



        bind.btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datechoose = bind.choosedate.getText().toString();
                String timechoose = bind.choosetime.getText().toString();
                String IDchoose = bind.chooseID.getText().toString();
                if(datechoose.isEmpty()||timechoose.isEmpty())
                {
                    Toast.makeText(CancelUpdateBooking.this, "Please Enter Date And Time!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("Booking").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataDate = dataSnapshot.child("Date").getValue(String.class);
                                    dataTime = dataSnapshot.child("Time").getValue(String.class);
                                    dataID = dataSnapshot.child("PaymentID").getValue(String.class);
                                    if (datechoose.equals(dataDate) && timechoose.equals(dataTime) && IDchoose.equals(dataID)) {
                                        bind.linDisplay.setVisibility(View.VISIBLE);
                                        bind.lindisplay2.setVisibility(View.VISIBLE);
                                        final String Name = dataSnapshot.child("FullName").getValue().toString();
                                        final String Email = dataSnapshot.child("Email").getValue().toString();
                                        final String Date = dataSnapshot.child("Date").getValue().toString();
                                        final String Lane = dataSnapshot.child("Lane").getValue().toString();
                                        final String Time = dataSnapshot.child("Time").getValue().toString();
                                        final String Game = dataSnapshot.child("NumberGame").getValue().toString();
                                        final String Player = dataSnapshot.child("NumberPlayer").getValue().toString();
                                        final String Price = dataSnapshot.child("TotalPrice").getValue().toString();
                                        final String ID = dataSnapshot.child("PaymentID").getValue().toString();

                                        bind.Date.setText("Date : " + Date);
                                        bind.Name.setText("Name : " + Name);
                                        bind.Email.setText("Email : " + Email);
                                        bind.Lanee.setText("Lane : " + Lane);
                                        bind.Player.setText("Number of Player : " + Player);
                                        bind.Game.setText("Number of Games : " + Game);
                                        bind.Time.setText("Time : " + Time);
                                        bind.Price.setText("Total Price : RM" + Price);
                                        bind.edtDate.setText(Date);
                                        bind.edtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View v, boolean b) {
                                                if (b) {
                                                    fnInvokeDatePicker();
                                                }
                                                if (!b) {
                                                    fnFormValidaton();
                                                }
                                            }
                                        });
                                        bind.edtDate.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                fnInvokeDatePicker();
                                            }
                                        });

                                        bind.UpdateBook.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String dates = bind.edtDate.getText().toString().trim();
                                                dataSnapshot.child("Date").getRef().setValue(dates);
                                                databaseReference.child("CheckLane").child(dates).child(dataTime).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane1").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane2").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane3").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane4").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane5").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane6").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane7").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child("lane8").child("k1").setValue("a");
                                                        databaseReference.child("CheckLane").child(dates).child(dataTime).child(Lane).child(UID).child("status").setValue("occupied");
                                                        databaseReference.child("CheckLane").child(Date).child(Time).child(Lane).child(UID).removeValue();
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(CancelUpdateBooking.this,"GoldenDream");
                                                builder.setSmallIcon(R.drawable.logo);
                                                builder.setContentTitle("Dreamer's Reminder");
                                                builder.setContentText("New Date Updated!");
                                                builder.setAutoCancel(true);
                                                NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(CancelUpdateBooking.this);
                                                notificationManagerCompat.notify(1,builder.build());
                                                Toast.makeText(getApplicationContext(), "Update Success! ", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), BookingFragment.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                        bind.cancelBook.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                Intent intent = new Intent(CancelUpdateBooking.this, Cancel.class);
                                                intent.putExtra("DATE_TEXT", datechoose);
                                                intent.putExtra("T_TEXT", timechoose);
                                                intent.putExtra("E_TEXT", Email);
                                                intent.putExtra("I_TEXT", ID);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            }
                            else
                            {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(CancelUpdateBooking.this,"GoldenDream");
                                builder.setSmallIcon(R.drawable.logo);
                                builder.setContentTitle("Hello Dreamer's");
                                builder.setContentText("Play with us and get unforgettable experience");
                                builder.setAutoCancel(true);
                                NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(CancelUpdateBooking.this);
                                notificationManagerCompat.notify(1,builder.build());
                                Toast.makeText(getApplicationContext(), "Please Book First! ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CancelUpdateBooking.this, BookingFragment.class);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

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

        datePicker = new DatePickerDialog(CancelUpdateBooking.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                bind.edtDate.setText(dayOfMonth + "-" + (month+1) + "-" + year);
            }
        },year,month,day);
        datePicker.show();
    }

    private void fnFormValidaton2() {
    }

    private void fnInvokeDatePicker2()
    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog

        datePicker = new DatePickerDialog(CancelUpdateBooking.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                bind.choosedate.setText(dayOfMonth + "-" + (month+1) + "-" + year);
            }
        },year,month,day);
        datePicker.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BookingFragment.class));
        overridePendingTransition(0,0);
        finish();
    }
}