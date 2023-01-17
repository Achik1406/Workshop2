package com.example.goldendreamsbowling;

import android.content.Intent;
import android.os.Bundle;

import com.example.goldendreamsbowling.LoggedInUser.Drawer_base;
import com.example.goldendreamsbowling.databinding.ActivityHomePageBinding;

public class HomePage extends Drawer_base {



    ActivityHomePageBinding bind;
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