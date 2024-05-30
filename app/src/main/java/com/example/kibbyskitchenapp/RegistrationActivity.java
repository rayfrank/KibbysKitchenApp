package com.example.kibbyskitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationactivity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        // If registration fails, display a message to the user.
                        Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
