package com.example.goldendreamsbowling.Guest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.goldendreamsbowling.Guest.DrawerGuest;
import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.Login;
import com.example.goldendreamsbowling.MainActivity;
import com.example.goldendreamsbowling.databinding.ActivityMainInterfaceBinding;

public class MainInterface extends DrawerGuest {

    private long pressedTime;
    ActivityMainInterfaceBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =bind.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

    }
}