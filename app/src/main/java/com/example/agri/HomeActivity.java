package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import me.relex.circleindicator.CircleIndicator3;

public class HomeActivity extends AppCompatActivity {

    // UI elements
    private SearchView searchBar;
    private ImageView weatherIcon;
    private TextView tempText, locationText;
    private ViewPager2 posterSlider;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout cropBox, pesticideBox;
    private CircleIndicator3 posterIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        initializeViews();

        // Setup UI components
        setupSearchBar();
        setupWeatherCard();
        setupPosterSlider();
        setupBottomNavigation();
        setupCropPesticideBoxes();
    }

    private void initializeViews() {
        searchBar = findViewById(R.id.searchBar);
        weatherIcon = findViewById(R.id.weatherIcon);
        tempText = findViewById(R.id.tempText);
        locationText = findViewById(R.id.locationText);
        posterSlider = findViewById(R.id.posterSlider);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        cropBox = findViewById(R.id.cropBox);
        pesticideBox = findViewById(R.id.pesticideBox);
        posterIndicator = findViewById(R.id.posterIndicator);

        // Check for null views
        if (searchBar == null || weatherIcon == null || tempText == null ||
                locationText == null || posterSlider == null || bottomNavigationView == null ||
                cropBox == null || pesticideBox == null || posterIndicator == null) {
            Log.e("HomeActivity", "One or more views not found");
            finish(); // Exit activity if critical views are missing
        }
    }

    private void setupSearchBar() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Update UI dynamically as the user types
                return false;
            }
        });
    }

    private void performSearch(String query) {
        Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
        // TODO: Implement actual search logic (e.g., filter crops/pesticides or query a database)
    }

    private void setupWeatherCard() {
        // TODO: Replace with API call to fetch real weather data
        weatherIcon.setImageResource(R.drawable.cloud_rain);
        tempText.setText("১৯°");
        locationText.setText("মোহনপুর, রাজশাহী");
    }

    private void setupPosterSlider() {
        PosterAdapter posterAdapter = new PosterAdapter(getPosters());
        posterSlider.setAdapter(posterAdapter);

        // Attach CircleIndicator3
        posterIndicator.setViewPager(posterSlider);

        // Auto-scroll for poster slider
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = posterSlider.getCurrentItem();
                int totalItems = posterAdapter.getItemCount();
                posterSlider.setCurrentItem((currentItem + 1) % totalItems);
                handler.postDelayed(this, 3000); // Scroll every 3 seconds
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private int[] getPosters() {
        return new int[]{
                R.drawable.poster7,
                R.drawable.poster8,
                R.drawable.poster9,
                R.drawable.poster10,
                R.drawable.poster11,
                R.drawable.poster12,
                R.drawable.poster13,
                R.drawable.poster16,
                R.drawable.poster17,
                R.drawable.poster18,
                R.drawable.poster21,
                R.drawable.poster22
        };
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Already on HomeActivity, do nothing
                return true;
            } else if (itemId == R.id.navigation_prediction) {
                Intent intent = new Intent(HomeActivity.this, PredictionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                // Do NOT call finish() here
                return true;
            } else if (itemId == R.id.navigation_weather) {
                Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                // Do NOT call finish() here
                return true;
            } else if (itemId == R.id.navigation_infromation) {
                Intent intent = new Intent(HomeActivity.this, InformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                // Do NOT call finish() here
                return true;
            }
            return false;
        });
    }

    private void setupCropPesticideBoxes() {
        cropBox.setOnClickListener(v -> {
            // Navigate to Crops screen
            startActivity(new Intent(HomeActivity.this, CropsActivity.class));
        });

        pesticideBox.setOnClickListener(v -> {
            // Navigate to Pesticides screen
            startActivity(new Intent(HomeActivity.this, PesticidesActivity.class));
        });
    }
}