package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InformationActivity extends AppCompatActivity {

    private Spinner districtSpinner;
    private TextView helplineNumbers;

    // Sample data: Districts and their helpline numbers
    private String[] districts = {"ঢাকা", "চট্টগ্রাম", "রাজশাহী", "বরিশাল", "খুলনা"};
    private String[] helplines = {
            "ঢাকা: ০১৭XXXXXXXX",
            "চট্টগ্রাম: ০১৮XXXXXXXX",
            "রাজশাহী: ০১৯XXXXXXXX",
            "বরিশাল: ০২০XXXXXXXX",
            "খুলনা: ০২১XXXXXXXX"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information); // Ensure the layout name matches

        districtSpinner = findViewById(R.id.districtSpinner);
        helplineNumbers = findViewById(R.id.helplineNumbers);

        // Set up the Spinner with district names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        // Set a listener for Spinner item selection using an anonymous class
        districtSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, android.view.View view, int position, long id) {
                // Display the corresponding helpline number based on the selected district
                helplineNumbers.setText(helplines[position]);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Optionally handle the case where nothing is selected
                helplineNumbers.setText("নির্বাচন করা হয়নি");
            }
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
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_prediction) {
                startActivity(new Intent(getApplicationContext(), PredictionActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_infromation) {
                // Already on Information page, do nothing
                return true;
            }
            return false;
        });
    }
}
