package com.example.customerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.Manifest;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class Profile extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private TextView PName, PEmail;
    private Button btAdd, btLogout;
    private ImageView Pfp;

    private FirebaseAuth Auth;
    private FirebaseFirestore db;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Pfp = findViewById(R.id.Picture);
        btAdd = findViewById(R.id.ProfileBtSAddress);
        btLogout = findViewById(R.id.BTLogout);
        PName = findViewById(R.id.ProfileName);
        PEmail = findViewById(R.id.ProfileEmail);

        Auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = Auth.getCurrentUser();

        if (user != null) {
            loadUserDataFromFirestore(user.getUid());

            String savedImageUri = getImageUri();
            if (savedImageUri != null) {
                Log.d("ProfileActivity", "Loading image URI: " + savedImageUri);
                Pfp.setImageURI(Uri.parse(savedImageUri));
            }

            BottomNavigationView BotNav3 = findViewById(R.id.bottom_navigation4);
            BotNav3.setSelectedItemId(R.id.prof);
            BotNav3.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(Profile.this, Homepage.class));
                            return true;
                        case R.id.loc:
                            startActivity(new Intent(Profile.this, Location.class));
                            return true;
                        case R.id.pay:
                            startActivity(new Intent(Profile.this, Payment.class));
                            return true;
                        default:
                            return false;
                    }
                }
            });

            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Profile.this, ShippingAddress.class));
                }
            });

            btLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    finish();
                }
            });

            Pfp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                }
            });
        }
    }

    private String getImageUri() {
        SharedPreferences prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        return prefs.getString("image_uri", null);
    }

    private void loadUserDataFromFirestore(String uid) {
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.getString("name");
                            String email = document.getString("email");

                            Log.d("ProfileActivity", "Retrieved Name: " + name);
                            Log.d("ProfileActivity", "Retrieved Email: " + email);

                            PName.setText(name);
                            PEmail.setText(email);
                        } else {
                            Log.d("ProfileActivity", "No such document");
                            Toast.makeText(Profile.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("ProfileActivity", "Get failed with ", task.getException());
                        Toast.makeText(Profile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Log.d("ProfileActivity", "Image URI: " + imageUri.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Pfp.setImageBitmap(bitmap);
                    saveImageUri(imageUri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveImageUri(String uri) {
        SharedPreferences prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("image_uri", uri);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
