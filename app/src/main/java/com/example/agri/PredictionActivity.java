package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredictionActivity extends AppCompatActivity {

    private Spinner cropSpinner, areaSpinner;
    private TextView tvPredictionResult;
    private Button btnGeneratePrediction;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_prediction);

        // Initialize UI components
        cropSpinner = findViewById(R.id.cropSpinner);
        areaSpinner = findViewById(R.id.areaSpinner);
        tvPredictionResult = findViewById(R.id.tvPredictionResult);
        btnGeneratePrediction = findViewById(R.id.btnGeneratePrediction);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        // Set up crop spinner with hint
        List<String> crops = new ArrayList<>();
        crops.add("ফসল নির্বাচন করুন"); // Hint as the first item
        crops.addAll(Arrays.asList(
                "ভুট্টা", "পেঁয়াজ", "আলু", "ধান", "সরিষা",
                "রসুন", "গোল আলু", "পাট", "গম", "চাল"
        ));
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, crops);
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);

        // Disable the hint item from being selectable
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray)); // Hint color
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black)); // Selected item color
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up area spinner with hint
        List<String> districts = new ArrayList<>();
        districts.add("এলাকা নির্বাচন করুন (জেলা)"); // Hint as the first item
        districts.addAll(Arrays.asList(
                "বাগেরহাট", "বান্দরবান", "বরগুনা", "বরিশাল", "ভোলা",
                "বগুড়া", "ব্রাহ্মণবাড়িয়া", "চাঁদপুর", "চট্টগ্রাম", "চুয়াডাঙ্গা",
                "কুমিল্লা", "কক্সবাজার", "ঢাকা", "দিনাজপুর", "ফরিদপুর",
                "ফেনী", "গাইবান্ধা", "গাজীপুর", "গোপালগঞ্জ", "হবিগঞ্জ",
                "জামালপুর", "যশোর", "ঝালকাঠি", "ঝিনাইদহ", "জয়পুরহাট",
                "খাগড়াছড়ি", "খুলনা", "কিশোরগঞ্জ", "কুষ্টিয়া", "লক্ষ্মীপুর",
                "লালমনিরহাট", "মাদারীপুর", "মাগুরা", "মানিকগঞ্জ", "মৌলভীবাজার",
                "মেহেরপুর", "মুন্সীগঞ্জ", "ময়মনসিংহ", "নওগাঁ", "নড়াইল",
                "নারায়ণগঞ্জ", "নরসিংদী", "নাটোর", "নেত্রকোণা", "নীলফামারী",
                "নোয়াখালী", "পাবনা", "পঞ্চগড়", "পটুয়াখালী", "পিরোজপুর",
                "রাজবাড়ী", "রাজশাহী", "রাঙামাটি", "রংপুর", "সাতক্ষীরা",
                "শরীয়তপুর", "শেরপুর", "সিরাজগঞ্জ", "সুনামগঞ্জ", "সিলেট",
                "টাঙ্গাইল", "ঠাকুরগাঁও", "কুড়িগ্রাম", "বর্তমান জেলা"
        ));
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        // Disable the hint item from being selectable
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up button click listener
        btnGeneratePrediction.setOnClickListener(v -> {
            String selectedCrop = cropSpinner.getSelectedItem().toString();
            String selectedArea = areaSpinner.getSelectedItem().toString();
            String date = ((EditText) findViewById(R.id.etDate)).getText().toString();

            // Check if hint items are selected
            if (selectedCrop.equals("ফসল নির্বাচন করুন")) {
                Toast.makeText(PredictionActivity.this, "একটি ফসল নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedArea.equals("এলাকা নির্বাচন করুন (জেলা)")) {
                Toast.makeText(PredictionActivity.this, "একটি এলাকা নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                return;
            }
            if (date.isEmpty()) {
                Toast.makeText(PredictionActivity.this, "তারিখ দিন", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate the predicted price (dummy result for now)
            String predictedPrice = generatePrediction(selectedCrop, selectedArea, date);

            // Display the result
            tvPredictionResult.setText("পূর্বাভাস মূল্য: ৳" + predictedPrice);
            tvPredictionResult.setVisibility(View.VISIBLE);
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_prediction);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_prediction) {
                return true;
            } else if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.navigation_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_infromation) {
                startActivity(new Intent(getApplicationContext(), InformationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    // Dummy method to generate the predicted price
    private String generatePrediction(String crop, String area, String date) {
        // Implement your prediction logic here
        return "1500";
    }
}