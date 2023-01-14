package com.example.goldendreamsbowling.LoggedInUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.goldendreamsbowling.Cap_page;
import com.example.goldendreamsbowling.HomePage;
import com.example.goldendreamsbowling.MainActivity;
import com.example.goldendreamsbowling.Mug_page;
import com.example.goldendreamsbowling.Totebag_page;
import com.example.goldendreamsbowling.Tshirt_page;
import com.example.goldendreamsbowling.databinding.ActivityMerchFragmentBinding;

public class merchFragment extends Drawer_base {

    ActivityMerchFragmentBinding activityMerchFragmentBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMerchFragmentBinding = activityMerchFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityMerchFragmentBinding.getRoot());

        activityMerchFragmentBinding.layoutTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Tshirt_page.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutMug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Mug_page.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Cap_page.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutTotebag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFragment.this, Totebag_page.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomePage.class));
        overridePendingTransition(0,0);
        finish();
    }
}