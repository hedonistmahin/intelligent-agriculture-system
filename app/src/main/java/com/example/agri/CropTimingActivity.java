package com.example.agri;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CropTimingActivity extends AppCompatActivity {

    private TextView cropTimingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_timing);

        cropTimingText = findViewById(R.id.cropTimingText);

        // Set crop timing information
        String cropTiming = "রোপণের সময়: জানুয়ারী - ফেব্রুয়ারী\nকাটা সময়: মে - জুন";
        cropTimingText.setText(cropTiming);
    }
}
