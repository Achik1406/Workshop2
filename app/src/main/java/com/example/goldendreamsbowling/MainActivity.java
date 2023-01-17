package com.example.goldendreamsbowling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;;import com.example.goldendreamsbowling.Guest.MainInterface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Login.Pref_Name,0);
                boolean hasLogin = sharedPreferences.getBoolean("hasLoggedIn",false);
                if(hasLogin)
                {
                    startActivity(new Intent(MainActivity.this, HomePage.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, MainInterface.class));
                    finish();
                }

            }
        },0);

    }
}