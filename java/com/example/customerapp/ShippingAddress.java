package com.example.customerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShippingAddress extends AppCompatActivity {
    private Button BtBack;
    private TextView TvAdd;
    private FirebaseAuth Auth;
    private FirebaseFirestore db;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        BtBack = findViewById(R.id.BtBack);
        TvAdd = findViewById(R.id.AddresOutput);

        Auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = Auth.getCurrentUser();

        if (user != null) {
            loadUserDataFromFirestore(user.getUid());


            BtBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShippingAddress.this, Profile.class));
                }
            });
        }

    }

    private void loadUserDataFromFirestore(String uid) {
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String address = document.getString("address");
                            Log.d("ProfileActivity", "Retrieved Name: " + address);
                            TvAdd.setText(address);
                        } else {
                            Log.d("ProfileActivity", "No such document");
                            Toast.makeText(ShippingAddress.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("ProfileActivity", "Get failed with ", task.getException());
                        Toast.makeText(ShippingAddress.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}