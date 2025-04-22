package com.example.agri;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class CropsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops);

        // Basic setup for Crops screen
        TextView title = findViewById(R.id.cropsTitle);
        if (title != null) {
            title.setText("Crops Screen");
        }
    }
}