package com.example.agri;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    // Declare views
    private TextView appTitle, tempText, locationText, fullName, emailLabel, phoneNumber, country, genre, address;
    private EditText searchBar;
    private ImageView weatherIcon;
    private LinearLayout cropCategories, priceList;
    private ImageButton settingsButton;
    private ImageView cropImage;
    private TextView cropName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Make sure the XML file is named correctly

        // Initialize views
        appTitle = findViewById(R.id.appTitle);
        tempText = findViewById(R.id.tempText);
        locationText = findViewById(R.id.locationText);
        searchBar = findViewById(R.id.searchBar);
        weatherIcon = findViewById(R.id.weatherIcon);
        cropCategories = findViewById(R.id.cropCategories);
        priceList = findViewById(R.id.priceList);
        settingsButton = findViewById(R.id.settingsButton);

        // Initialize crop categories and prices (dynamically setting these from data)
        initializeCropCategories();
        initializePriceList();

        // Set app title
        appTitle.setText("সোনালী ফসল");

        // Handle search functionality (for example, search weather data, crops, etc.)
        searchBar.setOnClickListener(view -> {
            // Implement search functionality here
        });

        // Set weather details (as a placeholder, replace with actual data)
        tempText.setText("১৯°");
        locationText.setText("মোহনপুর, রাজশাহী");
        weatherIcon.setImageResource(R.drawable.cloud_rain);  // Example icon

        // Handle settings button click
        settingsButton.setOnClickListener(view -> {
            // Open settings or perform any other action
            openSettings();
        });
    }

    // Initialize crop categories (this would be dynamic in real app, static here as an example)
    private void initializeCropCategories() {
        // Example: Dynamically create categories for crops like Oil Seeds, Cotton, and Potatoes
        addCropCategory("তেল বীজ", R.drawable.oil_seeds);
        addCropCategory("তুলা", R.drawable.cotton);
        addCropCategory("আলু", R.drawable.potato);
    }

    // Add individual crop category
    private void addCropCategory(String cropNameText, int cropImageResId) {
        LinearLayout cropCategoryLayout = new LinearLayout(this);
        cropCategoryLayout.setOrientation(LinearLayout.VERTICAL);
        cropCategoryLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);

        // Create crop image
        ImageView cropImage = new ImageView(this);
        cropImage.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        cropImage.setImageResource(cropImageResId);

        // Create crop name TextView
        TextView cropNameText = new TextView(this);
        cropNameText.setText(cropNameText);
        cropNameText.setTextSize(12);

        // Add image and name to the category layout
        cropCategoryLayout.addView(cropImage);
        cropCategoryLayout.addView(cropNameText);

        // Add the category layout to the main layout
        cropCategories.addView(cropCategoryLayout);
    }

    // Initialize price list (dynamically setting prices for crops)
    private void initializePriceList() {
        addPrice("টমেটো", "৩০ টাকা/কেজি", R.drawable.tomatoes);
        addPrice("চাল", "৫৬ টাকা/কেজি", R.drawable.rice);
        addPrice("গম", "৫০ টাকা/কেজি", R.drawable.wheat);
    }

    // Add individual price item
    private void addPrice(String cropNameText, String cropPrice, int cropImageResId) {
        LinearLayout priceItemLayout = new LinearLayout(this);
        priceItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        priceItemLayout.setPadding(10, 10, 10, 10);

        // Crop name and price
        TextView cropText = new TextView(this);
        cropText.setText(cropNameText + "\n" + cropPrice);
        cropText.setTextSize(16);
        cropText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Crop image
        ImageView cropImage = new ImageView(this);
        cropImage.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        cropImage.setImageResource(cropImageResId);

        // Add the name, price, and image to the price item layout
        priceItemLayout.addView(cropText);
        priceItemLayout.addView(cropImage);

        // Add the price item layout to the price list layout
        priceList.addView(priceItemLayout);
    }

    // Open settings activity or perform any settings-related tasks
    private void openSettings() {
        // For now, show a simple message or navigate to settings
        // Implement your settings functionality here
    }
}
