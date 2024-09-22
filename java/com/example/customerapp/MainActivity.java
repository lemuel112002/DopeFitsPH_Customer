package com.example.customerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.preference.PreferenceManager;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button BTLogsup,BTLog,BTGoogle;
    private EditText ETemail,ETpass;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

            auth = FirebaseAuth.getInstance();
            ETemail = findViewById(R.id.Email);
            ETpass = findViewById(R.id.Password);
            BTLog = findViewById(R.id.BTLogIn);
            BTLogsup =  findViewById(R.id.SUpBTLogIn);
//            BTGoogle = findViewById(R.id.sign_in_button);


                BTLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = ETemail.getText().toString().trim();
                    String pass = ETpass.getText().toString().trim();

                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        if (!pass.isEmpty()){
                            auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(MainActivity.this, "Successfully Login!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, Homepage.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ETpass.setError("Password cannot be empthy!");
                        }
                    } else if (email.isEmpty()) {
                        ETemail.setError("Email cannot be empthy!");
                    } else {
                        ETpass.setError("Please enter your password");
                    }
                }
            });

            BTLogsup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SignUp.class);
                    startActivity(intent);
                 }
             });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });





    }

}