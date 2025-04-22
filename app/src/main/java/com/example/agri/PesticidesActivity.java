package com.example.agri;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class PesticidesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticides);

        // Basic setup for Pesticides screen
        TextView title = findViewById(R.id.pesticidesTitle);
        if (title != null) {
            title.setText("Pesticides Screen");
        }
    }
}