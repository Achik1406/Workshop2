package com.example.goldendreamsbowling;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goldendreamsbowling.Guest.MainInterface;
import com.example.goldendreamsbowling.LoggedInUser.ForgotPassword;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    public static String Pref_Name ="MyFile";
    TextView textViewSignUp, Emaill, password;
    Button Login;
    ImageButton googleLog;
    private GoogleSignInClient client;
    FirebaseAuth auth,mAuth;
    FirebaseDatabase database;

    String Name,email;
    String userLogin;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workshop2-d8198-default-rtdb.firebaseio.com/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login = findViewById(R.id.login);
        googleLog = findViewById(R.id.Googlelog);
        Emaill = findViewById(R.id.signInEmail);
        password = findViewById(R.id.password);
        textViewSignUp = findViewById(R.id.SignInTextt);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        TextView forgotPass = findViewById(R.id.forgotpass);


        database = FirebaseDatabase.getInstance("https://workshop2-d8198-default-rtdb.firebaseio.com/");

        GoogleSignInOptions  options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);

        googleLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i,143);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), signUp.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String User = Emaill.getText().toString();
                final String pass = password.getText().toString();

                if (User.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter Username and Password", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(User, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(Pref_Name,0);
                                        SharedPreferences.Editor editor =sharedPreferences.edit();
                                        editor.putBoolean("hasLoggedIn",true);
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(Login.this, HomePage.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==143)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(task.isSuccessful())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences(Pref_Name,0);
                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn",true);
                            editor.commit();
                            userLogin = auth.getCurrentUser().getUid();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login.this);
                            if (acct != null) {
                                Name = acct.getDisplayName();
                                email = acct.getEmail();
                            }
                            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(userLogin)) {
                                        Toast.makeText(Login.this, "User already registered", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        databaseReference.child("users").child(userLogin).child("email").setValue(email);
                                        databaseReference.child("users").child(userLogin).child("fullname").setValue(Name);
                                        databaseReference.child("users").child(userLogin).child("phone").setValue("");
                                        databaseReference.child("users").child(userLogin).child("password").setValue("");
                                        Toast.makeText(Login.this, "Registration successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                        else {
                            Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private Context getActivity() {
        return null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainInterface.class));
        overridePendingTransition(0,0);
        finish();
    }


}