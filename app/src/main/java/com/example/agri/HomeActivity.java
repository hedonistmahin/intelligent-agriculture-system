package com.example.agri;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    // Declare variables for UI elements
    private SearchView searchBar;
    private ImageView weatherIcon;
    private TextView tempText, locationText;
    private ViewPager2 posterSlider;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Replace with your actual XML file name

        // Initialize UI components
        searchBar = findViewById(R.id.searchBar);
        weatherIcon = findViewById(R.id.weatherIcon);
        tempText = findViewById(R.id.tempText);
        locationText = findViewById(R.id.locationText);
        posterSlider = findViewById(R.id.posterSlider);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Setup SearchView listener
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the query submission (optional)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change in search bar (optional)
                return false;
            }
        });

        // Setup Poster Slider (ViewPager2)
        setupPosterSlider();

        // Setup Weather Card content
        setupWeatherCard();
    }

    // Method to set up the Poster Slider (ViewPager2)
    private void setupPosterSlider() {
        // You can add images to the slider using an adapter
        PosterAdapter posterAdapter = new PosterAdapter(getPosters());
        posterSlider.setAdapter(posterAdapter);

    }

    // Method to create a list of posters for the ViewPager2
    private int[] getPosters() {
        // Example poster images
        return new int[]{
                R.drawable.poster1,  // Replace with actual images in your drawable folder
                R.drawable.poster2,
                R.drawable.poster3
        };
    }

    // Method to set up weather information
    private void setupWeatherCard() {
        // Set weather icon (You can dynamically change the icon based on the weather data)
        weatherIcon.setImageResource(R.drawable.cloud_rain); // Example icon, change it dynamically based on weather

        // Set temperature and location
        tempText.setText("১৯°");
        locationText.setText("মোহনপুর, রাজশাহী");
    }
}
