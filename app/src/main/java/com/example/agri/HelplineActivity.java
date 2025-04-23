package com.example.agri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelplineActivity extends AppCompatActivity {

    private Spinner districtSpinner;
    private TextView helplineNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);

        // Initialize views
        districtSpinner = findViewById(R.id.districtSpinner);
        helplineNumbers = findViewById(R.id.helplineNumbers);

        // Set up the Spinner with district names
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.districts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        // Set OnItemSelectedListener for the Spinner
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected district
                String selectedDistrict = parentView.getItemAtPosition(position).toString();
                // Update the helpline number based on the selected district
                updateHelplineNumber(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when nothing is selected
                helplineNumbers.setText("দয়া করে একটি জেলা নির্বাচন করুন");
            }
        });
    }

    private void updateHelplineNumber(String district) {
        // Update helpline number based on district (replace with actual numbers)
        String helplineNumber;
        switch (district) {
            case "ঢাকা":
                helplineNumber = "০১৭১২৩৪৫৬৭৮";
                break;
            case "রাজশাহী":
                helplineNumber = "০১৭২৩৪৫৬৭৮৯";
                break;
            case "চট্টগ্রাম":
                helplineNumber = "০১৭৩৪৫৬৭৮৯০";
                break;
            default:
                helplineNumber = "অজানা জেলা, দয়া করে সঠিক জেলা নির্বাচন করুন";
                break;
        }
        helplineNumbers.setText("হেল্পলাইন নম্বর: " + helplineNumber);
    }
}