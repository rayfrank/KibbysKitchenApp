package com.kibbyskitchen.kibbyskitchenapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kibbyskitchen.kibbyskitchenapp.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private RecyclerView recyclerViewNotifications;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Request necessary permissions
        if (!hasPermissions()) {
            requestPermissions();
        } else {
            initializeNotifications();
        }
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, initialize notifications
                initializeNotifications();
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(this, "Permissions denied. The app needs these permissions to function properly.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeNotifications() {
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerViewNotifications.setAdapter(notificationAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("notifications")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        notificationList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("title") != null && doc.get("message") != null) {
                                notificationList.add(new Notification(doc.getString("title"), doc.getString("message")));
                            }
                        }
                        notificationAdapter.notifyDataSetChanged();
                    }
                });
    }
}
