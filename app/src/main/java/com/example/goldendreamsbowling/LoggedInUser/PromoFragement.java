package com.example.goldendreamsbowling.LoggedInUser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.goldendreamsbowling.R;
import com.example.goldendreamsbowling.databinding.ActivityPromoFragementBinding;
import com.example.goldendreamsbowling.promoMainFrag;

public class PromoFragement extends Drawer_base {

    ActivityPromoFragementBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind= ActivityPromoFragementBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new promoMainFrag()).commit();
    }
}