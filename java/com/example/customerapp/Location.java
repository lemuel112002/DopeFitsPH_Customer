package com.example.customerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Location extends AppCompatActivity {
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        txt = findViewById(R.id.textView0);

        BottomNavigationView BotNav2 = findViewById(R.id.bottom_navigation2);
        BotNav2.setSelectedItemId(R.id.loc);


        BotNav2.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    startActivity(new Intent(Location.this, Homepage.class));
                    return true;
                } else if (id == R.id.pay) {
                    startActivity(new Intent(Location.this, Payment.class));
                    return true;
                } else if (id == R.id.prof) {
                    startActivity(new Intent(Location.this, Profile.class));
                    return true;
                }
                return false;
            }
        });

        }
    }
