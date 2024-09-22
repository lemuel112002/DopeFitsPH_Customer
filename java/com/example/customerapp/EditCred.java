package com.example.customerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCred extends AppCompatActivity {
        private EditText etName, etEmail;
        private Button btSave, btback;

        private FirebaseFirestore db;
        private FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_details);

            etName = findViewById(R.id.editName);
            etEmail = findViewById(R.id.editEmail);
            btSave = findViewById(R.id.btnSave);
            btback = findViewById(R.id.EditBack);

            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();


            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");

            etName.setText(name);
            etEmail.setText(email);


            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newName = etName.getText().toString();
                    String newEmail = etEmail.getText().toString();

                    String uid = auth.getCurrentUser().getUid();
                    db.collection("users").document(uid)
                            .update("name", newName, "email", newEmail)
                            .addOnSuccessListener(aVoid -> {
                                // Pass data back to Profile Activity
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("updatedName", newName);
                                resultIntent.putExtra("updatedEmail", newEmail);
                                setResult(RESULT_OK, resultIntent);
                                Toast.makeText(EditCred.this, "Successfully save data", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditCred.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                            });
                }
            });

            btback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EditCred.this, Profile.class));
                }
            });

        }
    }