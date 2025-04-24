package com.example.agri;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;
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
        // Update helpline number based on district (replace with actual numbers and information)
        String helplineInfo;
        switch (district) {
            case "ঢাকা":
                helplineInfo = "কৃষি অফিস: ঢাকা জেলা কৃষি অফিস\nঅফিসারের নাম: মোঃ কামাল হোসেন\nফোন নম্বর: 01711-123456";
                break;
            case "চট্টগ্রাম":
                helplineInfo = "কৃষি অফিস: চট্টগ্রাম কৃষি অফিস\nঅফিসারের নাম: শারমিন আক্তার\nফোন নম্বর: 01812-654321";
                break;
            case "রাজশাহী":
                helplineInfo = "কৃষি অফিস: রাজশাহী কৃষি অফিস\nঅফিসারের নাম: রবিউল ইসলাম\nফোন নম্বর: 01719-987654";
                break;
            case "খুলনা":
                helplineInfo = "কৃষি অফিস: খুলনা সদর কৃষি অফিস\nঅফিসারের নাম: আমিনা বেগম\nফোন নম্বর: 01911-345678";
                break;
            case "বরিশাল":
                helplineInfo = "কৃষি অফিস: বরিশাল সদর কৃষি অফিস\nঅফিসারের নাম: সাইফুল ইসলাম\nফোন নম্বর: 01675-456789";
                break;
            case "সিলেট":
                helplineInfo = "কৃষি অফিস: সিলেট সদর কৃষি অফিস\nঅফিসারের নাম: রেহানা পারভীন\nফোন নম্বর: 01713-876543";
                break;
            case "রংপুর":
                helplineInfo = "কৃষি অফিস: রংপুর কৃষি অফিস\nঅফিসারের নাম: আবু তালেব\nফোন নম্বর: 01888-123789";
                break;
            case "ময়মনসিংহ":
                helplineInfo = "কৃষি অফিস: ময়মনসিংহ কৃষি অফিস\nঅফিসারের নাম: জাহিদা সুলতানা\nফোন নম্বর: 01722-567890";
                break;
            case "কুমিল্লা":
                helplineInfo = "কৃষি অফিস: কুমিল্লা সদর কৃষি অফিস\nঅফিসারের নাম: তরিকুল ইসলাম\nফোন নম্বর: 01890-112233";
                break;
            case "নরসিংদী":
                helplineInfo = "কৃষি অফিস: নরসিংদী কৃষি অফিস\nঅফিসারের নাম: মাহবুব আলম\nফোন নম্বর: 01733-445566";
                break;
            case "গাজীপুর":
                helplineInfo = "কৃষি অফিস: গাজীপুর কৃষি অফিস\nঅফিসারের নাম: নাজমুল হাসান\nফোন নম্বর: 01744-556677";
                break;
            case "ব্রাহ্মণবাড়িয়া":
                helplineInfo = "কৃষি অফিস: ব্রাহ্মণবাড়িয়া কৃষি অফিস\nঅফিসারের নাম: শবনম আক্তার\nফোন নম্বর: 01818-998877";
                break;
            case "নোয়াখালী":
                helplineInfo = "কৃষি অফিস: নোয়াখালী কৃষি অফিস\nঅফিসারের নাম: শামসুল হক\nফোন নম্বর: 01799-223344";
                break;
            // Add more cases for the other districts
            default:
                helplineInfo = "অজানা জেলা, দয়া করে সঠিক জেলা নির্বাচন করুন";
                break;
        }
        helplineNumbers.setText("হেল্পলাইন নম্বর: \n" + helplineInfo);
    }
}
