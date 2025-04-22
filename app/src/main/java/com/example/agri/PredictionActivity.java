package com.example.agri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PredictionActivity extends AppCompatActivity {

    private Spinner cropSpinner, areaSpinner;
    private TextView tvPredictionResult;
    private Button btnGeneratePrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        // Initialize UI components
        cropSpinner = findViewById(R.id.cropSpinner);
        areaSpinner = findViewById(R.id.areaSpinner);
        tvPredictionResult = findViewById(R.id.tvPredictionResult);
        btnGeneratePrediction = findViewById(R.id.btnGeneratePrediction);

        // Set up button click listener
        btnGeneratePrediction.setOnClickListener(v -> {
            String selectedCrop = cropSpinner.getSelectedItem().toString();
            String selectedArea = areaSpinner.getSelectedItem().toString();
            String date = ((EditText) findViewById(R.id.etDate)).getText().toString();

            if (date.isEmpty()) {
                Toast.makeText(PredictionActivity.this, "তারিখ দিন", Toast.LENGTH_SHORT).show(); // "Please enter a date"
                return;
            }

            // Generate the predicted price (dummy result for now)
            String predictedPrice = generatePrediction(selectedCrop, selectedArea, date);

            // Display the result
            tvPredictionResult.setText("পূর্বাভাস মূল্য: ৳" + predictedPrice);
            tvPredictionResult.setVisibility(View.VISIBLE); // Make the result visible
        });
    }

    // Dummy method to generate the predicted price
    private String generatePrediction(String crop, String area, String date) {
        // Implement your prediction logic here
        return "1500";  // Returning a dummy price for now
    }
}
