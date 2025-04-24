package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.util.Log;

public class PredictionActivity extends AppCompatActivity {

    private Spinner cropSpinner, areaSpinner;
    private TextView tvPredictionResult;
    private Button btnGeneratePrediction;
    private BottomNavigationView bottomNavigationView;
    private EditText etDate;

    // TFLite-related variables
    private Interpreter tflite;
    private Map<String, Integer> cropMapping;
    private Map<String, Integer> districtMapping;
    private Map<String, Float> scalerMin;
    private Map<String, Float> scalerMax;

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
        etDate = findViewById(R.id.etDate);
        setupBottomNavigation();

        // Initialize TFLite model and mappings
        try {
            loadModelAndMappings();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading model: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

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

        // Disable the hint item from being selectable in crop spinner
        cropSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Set up area spinner with districts from districtMapping
        List<String> districts = new ArrayList<>();
        districts.add("এলাকা নির্বাচন করুন (জেলা)"); // Hint as the first item
        districts.addAll(districtMapping.keySet()); // Add all districts from districtMapping
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        // Disable the hint item from being selectable in area spinner
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
            // Hide the keyboard
            hideKeyboard();

            String selectedCrop = cropSpinner.getSelectedItem().toString();
            String selectedArea = areaSpinner.getSelectedItem().toString();
            String date = etDate.getText().toString();

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

            // Generate the predicted price
            try {
                float predictedPrice = generatePrediction(selectedCrop, selectedArea, date);
                tvPredictionResult.setText(String.format("পূর্বাভাস মূল্য: ৳%.2f", predictedPrice));
                tvPredictionResult.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(PredictionActivity.this, "Prediction error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    // Method to hide the keyboard
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_prediction);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_prediction) {
                return true; // Already on Prediction page
            } else if (item.getItemId() == R.id.navigation_home) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // Changed MainActivity to HomeActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.navigation_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_infromation) { // Changed navigation_infromation to navigation_profile
                startActivity(new Intent(getApplicationContext(), InformationActivity.class)); // Changed InformationActivity to ProfileActivity
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    // Load the TFLite model and JSON mappings
    private void loadModelAndMappings() throws Exception {
        // Load the TFLite model
        Log.d("PredictionActivity", "Attempting to load crop_price_model.tflite");
        InputStream modelInputStream = getAssets().open("crop_price_model.tflite");
        Log.d("PredictionActivity", "Model file opened successfully");
        ByteBuffer modelBuffer = ByteBuffer.allocateDirect(modelInputStream.available());
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = modelInputStream.read(buffer)) != -1) {
            modelBuffer.put(buffer, 0, bytesRead);
        }
        modelInputStream.close();
        modelBuffer.rewind();
        Log.d("PredictionActivity", "Model buffer created successfully");
        tflite = new Interpreter(modelBuffer);
        Log.d("PredictionActivity", "TFLite Interpreter initialized successfully");

        // Load crop mapping
        Log.d("PredictionActivity", "Attempting to load crop_mapping.json");
        String cropJson = readAssetFile("crop_mapping.json");
        Log.d("PredictionActivity", "Crop mapping loaded: " + cropJson);
        JSONObject cropJsonObject = new JSONObject(cropJson);
        cropMapping = new HashMap<>();
        for (Iterator<String> keys = cropJsonObject.keys(); keys.hasNext(); ) {
            String key = keys.next();
            String value = cropJsonObject.getString(key);
            cropMapping.put(value, Integer.parseInt(key));
        }
        Log.d("PredictionActivity", "Crop mapping parsed successfully");

        // Load district mapping
        Log.d("PredictionActivity", "Attempting to load district_mapping.json");
        String districtJson = readAssetFile("district_mapping.json");
        Log.d("PredictionActivity", "District mapping loaded: " + districtJson);
        JSONObject districtJsonObject = new JSONObject(districtJson);
        districtMapping = new HashMap<>();
        for (Iterator<String> keys = districtJsonObject.keys(); keys.hasNext(); ) {
            String key = keys.next();
            String value = districtJsonObject.getString(key);
            districtMapping.put(value, Integer.parseInt(key));
        }
        Log.d("PredictionActivity", "District mapping parsed successfully");

        // Load scalers
        Log.d("PredictionActivity", "Attempting to load scalers.json");
        String scalersJson = readAssetFile("scalers.json");
        Log.d("PredictionActivity", "Scalers loaded: " + scalersJson);
        JSONObject scalersJsonObject = new JSONObject(scalersJson);
        scalerMin = new HashMap<>();
        scalerMax = new HashMap<>();

        JSONObject monthScaler = scalersJsonObject.getJSONObject("month");
        scalerMin.put("month", (float) monthScaler.getDouble("min"));
        scalerMax.put("month", (float) monthScaler.getDouble("max"));

        JSONObject yearScaler = scalersJsonObject.getJSONObject("year");
        scalerMin.put("year", (float) yearScaler.getDouble("min"));
        scalerMax.put("year", (float) yearScaler.getDouble("max"));

        JSONObject priceScaler = scalersJsonObject.getJSONObject("price");
        scalerMin.put("price", (float) priceScaler.getDouble("min"));
        scalerMax.put("price", (float) priceScaler.getDouble("max"));
        Log.d("PredictionActivity", "Scalers parsed successfully");
    }

    // Utility method to read a file from assets
    private String readAssetFile(String fileName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        inputStream.close();
        return stringBuilder.toString();
    }

    // Method to generate the predicted price using TFLite
    private float generatePrediction(String crop, String area, String date) throws Exception {
        // Parse the date to extract month and year
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new Exception("Invalid date format. Please use YYYY-MM-DD.");
        }

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        float month = Float.parseFloat(monthFormat.format(parsedDate));
        float year = Float.parseFloat(yearFormat.format(parsedDate));

        // Encode crop and district
        Integer cropEncoded = cropMapping.get(crop);
        Integer districtEncoded = districtMapping.get(area);
        if (cropEncoded == null || districtEncoded == null) {
            throw new Exception("Invalid crop or district selected.");
        }

        // Scale the inputs
        float monthScaled = (month - scalerMin.get("month")) / (scalerMax.get("month") - scalerMin.get("month"));
        float yearScaled = (year - scalerMin.get("year")) / (scalerMax.get("year") - scalerMin.get("year"));

        // Prepare the input buffer for TFLite
        float[][] input = new float[1][4];
        input[0][0] = cropEncoded;
        input[0][1] = districtEncoded;
        input[0][2] = monthScaled;
        input[0][3] = yearScaled;

        // Prepare the output buffer
        float[][] output = new float[1][1];

        // Run inference
        tflite.run(input, output);

        // Get the predicted price (scaled)
        float predictedPriceScaled = output[0][0];

        // Inverse transform the predicted price to original scale
        float predictedPrice = predictedPriceScaled * (scalerMax.get("price") - scalerMin.get("price")) + scalerMin.get("price");

        return predictedPrice;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close();
        }
    }
}
