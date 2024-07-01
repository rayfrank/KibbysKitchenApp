package com.example.kibbyskitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No user is signed in, show the login screen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Button ExploreMenu = findViewById(R.id.exploreMenuButton);
        Button CreateAccount = findViewById(R.id.CreateAccount);
        Button LoginButton = findViewById(R.id.LogIn);

        ExploreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMenu();
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInAccount();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int id = item.getItemId();

                if (id == R.id.action_home) {
                    // Intent for the Home Activity
                    intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.action_dashboard) {
                    // Intent for the Dashboard Activity
                    intent = new Intent(MainActivity.this, dashboardactivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.action_notifications) {
                    // Intent for the Notifications Activity
                    intent = new Intent(MainActivity.this, NotificationsActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void OpenMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    public void CreateAccount() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void LogInAccount() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
