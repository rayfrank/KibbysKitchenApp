package com.example.kibbyskitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Navigation logic with recommended method
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
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
}















