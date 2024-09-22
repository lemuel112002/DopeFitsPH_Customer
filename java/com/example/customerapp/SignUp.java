package com.example.customerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    private Button BTSign, SUpBTLog;
    private EditText SUpEmail, SUpPass, SUpName, SupAdd;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SUpName = findViewById(R.id.SUpName);
        SUpEmail = findViewById(R.id.SUpEmail);
        SUpPass = findViewById(R.id.SUpPassword);
        SupAdd = findViewById(R.id.SupAdd);
        BTSign = findViewById(R.id.BTSignUp);
        SUpBTLog = findViewById(R.id.SUpBTLogIn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        BTSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = SUpName.getText().toString();
                String email = SUpEmail.getText().toString();
                String password = SUpPass.getText().toString();
                String address = SupAdd.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !address.isEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        saveUserDataToFirestore(user.getUid(), name, email, address);
                                        Log.d(TAG, "Data saved - Name: " + name + ", Email: " + email + ", Address: " + address);
                                        Toast.makeText(SignUp.this, "Sign up  Successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUp.this, MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    Log.w(TAG, "Sign up failed", task.getException());
                                    Toast.makeText(SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.d(TAG, "Fields are empty");
                    Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SUpBTLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });
    }

    private void saveUserDataToFirestore(String uid, String name, String email, String address) {
        // Create a Map of user data
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("address", address);

        // Save user data to Firestore
        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User data saved to Firestore"))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving user data", e));
    }
}
