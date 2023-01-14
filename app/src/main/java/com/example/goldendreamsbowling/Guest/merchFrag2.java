package com.example.goldendreamsbowling.Guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.goldendreamsbowling.Cap_page;
import com.example.goldendreamsbowling.LoggedInUser.Drawer_base;
import com.example.goldendreamsbowling.LoggedInUser.merchFragment;
import com.example.goldendreamsbowling.Login;
import com.example.goldendreamsbowling.Mug_page;
import com.example.goldendreamsbowling.Totebag_page;
import com.example.goldendreamsbowling.Tshirt_page;
import com.example.goldendreamsbowling.databinding.ActivityAboutusFragmentBinding;
import com.example.goldendreamsbowling.databinding.ActivityMerchFrag2Binding;

public class merchFrag2 extends DrawerGuest {

    ActivityMerchFrag2Binding activityMerchFragmentBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMerchFragmentBinding = activityMerchFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityMerchFragmentBinding.getRoot());
        allocateActivityTitle("Merchandise");

        activityMerchFragmentBinding.layoutTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFrag2.this, Login.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutMug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFrag2.this, Login.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFrag2.this, Login.class));
                finish();
            }
        });
        activityMerchFragmentBinding.layoutTotebag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(merchFrag2.this, Login.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainInterface.class));
        overridePendingTransition(0,0);
        finish();
    }

}