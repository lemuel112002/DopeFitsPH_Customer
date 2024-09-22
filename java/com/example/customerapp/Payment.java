package com.example.customerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Payment extends AppCompatActivity {
    private TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        txt1 = findViewById(R.id.textView11);

        BottomNavigationView BotNav3 = findViewById(R.id.bottom_navigation3);
        BotNav3.setSelectedItemId(R.id.pay);


        BotNav3.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    startActivity(new Intent(Payment.this, Homepage.class));
                    return true;
                } else if (id == R.id.loc) {
                    startActivity(new Intent(Payment.this, Location.class));
                    return true;
                } else if (id == R.id.prof) {
                    startActivity(new Intent(Payment.this, Profile.class));
                    return true;
                }
                return false;
            }
        });
    }
}