package com.example.goldendreamsbowling.LoggedInUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goldendreamsbowling.Login;
import com.example.goldendreamsbowling.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth ID;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ID = FirebaseAuth.getInstance();
        UID = ID.getCurrentUser().getUid();

        binding.buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = binding.emailforgot.getText().toString();
                final String pass = binding.passwordforgot.getText().toString();
                final String repass = binding.resetpassword.getText().toString();
                databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String dataUsername = snapshot.child("email").getValue().toString();
                        if(repass.length()<6 || pass.length()<6)
                        {
                            binding.passwordforgot.setError("Password must be more than 6 character");
                            binding.passwordforgot.requestFocus();
                            binding.resetpassword.setError("Password must be more than 6 character");
                            binding.resetpassword.requestFocus();
                            return;
                        }
                        if(user.equals(dataUsername)  || pass == repass)
                        {
                            Toast.makeText(getApplicationContext(),"Password Reset Successfully ", Toast.LENGTH_LONG);
                            databaseReference.child("users").child(UID).child("password").setValue(pass);
                            SharedPreferences sharedPreferences = getSharedPreferences(Login.Pref_Name,0);
                            boolean hasLogin = sharedPreferences.getBoolean("hasLoggedIn",false);
                            if(hasLogin)
                            {
                                startActivity(new Intent(ForgotPassword.this, Account.class));
                                finish();
                            }
                            else
                            {
                                startActivity(new Intent(ForgotPassword.this, Login.class));
                                finish();
                            }
                        }
                        else{
                            binding.emailforgot.setError("Invalid Email");
                            binding.emailforgot.requestFocus();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}