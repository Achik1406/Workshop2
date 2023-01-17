package com.example.goldendreamsbowling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.goldendreamsbowling.LoggedInUser.Drawer_base;
import com.example.goldendreamsbowling.LoggedInUser.merchFragment;
import com.example.goldendreamsbowling.databinding.ActivityAboutusFragmentBinding;

public class AboutUsFragment extends Drawer_base {

    ActivityAboutusFragmentBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind =bind.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutUsFragment.this, HomePage.class));
        finish();
    }
}