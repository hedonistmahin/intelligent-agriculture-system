package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InformationActivity extends AppCompatActivity {

    private Button videoButton, cropGuidelineButton, cropTimingButton, helplineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information); // Make sure the layout name is correct

        // Initialize buttons
        videoButton = findViewById(R.id.videoButton);
        cropGuidelineButton = findViewById(R.id.cropGuidelineButton);
        cropTimingButton = findViewById(R.id.cropTimingButton);
        helplineButton = findViewById(R.id.helplineButton);

        // Set up video button click listener
        videoButton.setOnClickListener(v -> {
            // Open the Video activity
            Intent intent = new Intent(InformationActivity.this, VideoActivity.class);
            startActivity(intent);
        });

        // Set up crop guideline button click listener
        cropGuidelineButton.setOnClickListener(v -> {
            // Open crop guideline details page
            Intent intent = new Intent(InformationActivity.this, CropGuidelineActivity.class);
            startActivity(intent);
        });

        // Set up crop timing button click listener
        cropTimingButton.setOnClickListener(v -> {
            // Open crop timing details page
            Intent intent = new Intent(InformationActivity.this, CropTimingActivity.class);
            startActivity(intent);
        });

        // Set up helpline button click listener
        helplineButton.setOnClickListener(v -> {
            // Open helpline info page
            Intent intent = new Intent(InformationActivity.this, HelplineActivity.class);
            startActivity(intent);
        });

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item to be the "Information" tab
        bottomNavigationView.setSelectedItemId(R.id.navigation_infromation);

        // Set up navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_infromation) {
                return true; // ইতিমধ্যে Information পেজে আছে, কিছু করার দরকার নেই
            } else if (item.getItemId() == R.id.navigation_home) {
                // হোম পেজে ফিরে যাওয়ার জন্য Intent তৈরি করো
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // FLAG_ACTIVITY_CLEAR_TOP এবং FLAG_ACTIVITY_SINGLE_TOP ব্যবহার করো
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.navigation_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_prediction) {
                startActivity(new Intent(getApplicationContext(), PredictionActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }
}
