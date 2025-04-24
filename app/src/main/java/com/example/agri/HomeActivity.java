package com.example.agri;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    // UI elements
    private SearchView searchBar;
    private ImageView weatherIcon;
    private TextView tempText, locationText;
    private ViewPager2 posterSlider;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout cropBox, pesticideBox;
    private CircleIndicator3 posterIndicator;

    // Weather API key
    private static final String API_KEY = "f158cc8f703a554c078b339da07bdb7d";
    private static final String DEFAULT_LOCATION = "Rajshahi"; // Fallback location
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Location client
    private FusedLocationProviderClient fusedLocationClient;

    // Map for translating location names to Bangla
    private static final Map<String, String> LOCATION_MAP = new HashMap<>();

    static {
        LOCATION_MAP.put("Rajshahi", "রাজশাহী");
        LOCATION_MAP.put("Mohonpur", "মোহনপুর");
        LOCATION_MAP.put("SAMair", "সামাইর");
        LOCATION_MAP.put("Dhaka", "ঢাকা");
        LOCATION_MAP.put("Chittagong", "চট্টগ্রাম");
        LOCATION_MAP.put("Khulna", "খুলনা");
        // Add more locations as needed
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions granted, fetch location
            fetchUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                fetchUserLocation();
            } else {
                // Permission denied, use default location
                Toast.makeText(this, "Location permission denied. Using default location (Rajshahi).", Toast.LENGTH_SHORT).show();
                fetchWeatherDataByCity(DEFAULT_LOCATION);
            }
        }
    }

    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Log the coordinates for debugging
                Log.d("HomeActivity", "User Location - Latitude: " + latitude + ", Longitude: " + longitude);
                // Fetch weather data using coordinates
                fetchWeatherDataByCoordinates(latitude, longitude);
            } else {
                // Location not available, use default location
                Toast.makeText(this, "Unable to get location. Using default location (Rajshahi).", Toast.LENGTH_SHORT).show();
                fetchWeatherDataByCity(DEFAULT_LOCATION);
            }
        }).addOnFailureListener(this, e -> {
            Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            fetchWeatherDataByCity(DEFAULT_LOCATION);
        });
    }

    private void updateLocationText(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, new Locale("bn"));
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String subAdminArea = address.getSubAdminArea(); // e.g., Mohonpur
                String adminArea = address.getAdminArea(); // e.g., Rajshahi

                // Translate to Bangla if necessary
                String banglaSubAdminArea = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    banglaSubAdminArea = LOCATION_MAP.getOrDefault(subAdminArea, subAdminArea != null ? subAdminArea : "অজানা");
                }
                String banglaAdminArea = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    banglaAdminArea = LOCATION_MAP.getOrDefault(adminArea, adminArea != null ? adminArea : "অজানা");
                }

                if (banglaSubAdminArea != null && banglaAdminArea != null) {
                    locationText.setText(banglaSubAdminArea + ", " + banglaAdminArea);
                } else if (banglaAdminArea != null) {
                    locationText.setText(banglaAdminArea);
                } else {
                    locationText.setText("অজানা স্থান");
                }
            } else {
                locationText.setText("অজানা স্থান");
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationText.setText("অজানা স্থান");
        }
    }

    private void fetchWeatherDataByCoordinates(double latitude, double longitude) {
        WeatherApiService weatherService = WeatherApiService.create();
        Call<WeatherResponse> call = weatherService.getWeatherByCoordinates(latitude, longitude, API_KEY, "metric", "bn");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();

                    // Log the entire response for debugging
                    Log.d("HomeActivity", "Weather Response: " + weather.toString());

                    // Update temperature
                    String temperature = String.format("%.0f°", weather.getMain().getTemp());
                    String banglaTemperature = convertToBanglaDigits(temperature);
                    Log.d("HomeActivity", "Temperature (English): " + temperature + ", Bangla: " + banglaTemperature);
                    tempText.setText(banglaTemperature);

                    // Update location with manual translation
                    String locationName = weather.getName();
                    if (locationName != null && !locationName.isEmpty()) {
                        String banglaLocation = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            banglaLocation = LOCATION_MAP.getOrDefault(locationName, locationName);
                        }
                        locationText.setText(banglaLocation);
                        Log.d("HomeActivity", "Location (English): " + locationName + ", Bangla: " + banglaLocation);
                    } else {
                        // Fallback to Geocoder if API name is null or empty
                        Log.w("HomeActivity", "Location name is null or empty in API response, using Geocoder");
                        updateLocationText(latitude, longitude);
                    }

                    // Update weather icon
                    if (weather.getWeather() != null && weather.getWeather().size() > 0) {
                        String iconCode = weather.getWeather().get(0).getIcon();
                        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                        Log.d("HomeActivity", "Loading weather icon from URL: " + iconUrl);
                        Glide.with(HomeActivity.this)
                                .load(iconUrl)
                                .placeholder(R.drawable.ic_fallback_weather)
                                .error(R.drawable.ic_fallback_weather)
                                .into(weatherIcon);
                    } else {
                        weatherIcon.setImageResource(R.drawable.ic_fallback_weather);
                        Log.w("HomeActivity", "Weather icon data is missing in API response");
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Weather data couldn't be fetched: " + response.message(), Toast.LENGTH_SHORT).show();
                    // Fallback to default values
                    tempText.setText(convertToBanglaDigits("19°"));
                    locationText.setText("মোহনপুর, রাজশাহী");
                    weatherIcon.setImageResource(R.drawable.ic_fallback_weather);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Weather data fetch failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Fallback to default values
                tempText.setText(convertToBanglaDigits("19°"));
                locationText.setText("মোহনপুর, রাজশাহী");
                weatherIcon.setImageResource(R.drawable.ic_fallback_weather);
            }
        });
    }

    private void fetchWeatherDataByCity(String city) {
        WeatherApiService weatherService = WeatherApiService.create();
        Call<WeatherResponse> call = weatherService.getWeather(city, API_KEY, "metric", "bn");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();

                    // Log the entire response for debugging
                    Log.d("HomeActivity", "Weather Response: " + weather.toString());

                    // Update temperature
                    String temperature = String.format("%.0f°", weather.getMain().getTemp());
                    String banglaTemperature = convertToBanglaDigits(temperature);
                    Log.d("HomeActivity", "Temperature (English): " + temperature + ", Bangla: " + banglaTemperature);
                    tempText.setText(banglaTemperature);

                    // Update location with manual translation
                    String locationName = weather.getName();
                    if (locationName != null && !locationName.isEmpty()) {
                        String banglaLocation = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            banglaLocation = LOCATION_MAP.getOrDefault(locationName, locationName);
                        }
                        locationText.setText(banglaLocation);
                        Log.d("HomeActivity", "Location (English): " + locationName + ", Bangla: " + banglaLocation);
                    } else {
                        locationText.setText("অজানা স্থান");
                        Log.w("HomeActivity", "Location name is null or empty in API response");
                    }

                    // Update weather icon
                    if (weather.getWeather() != null && weather.getWeather().size() > 0) {
                        String iconCode = weather.getWeather().get(0).getIcon();
                        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                        Log.d("HomeActivity", "Loading weather icon from URL: " + iconUrl);
                        Glide.with(HomeActivity.this)
                                .load(iconUrl)
                                .placeholder(R.drawable.ic_fallback_weather)
                                .error(R.drawable.ic_fallback_weather)
                                .into(weatherIcon);
                    } else {
                        weatherIcon.setImageResource(R.drawable.ic_fallback_weather);
                        Log.w("HomeActivity", "Weather icon data is missing in API response");
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Weather data couldn't be fetched: " + response.message(), Toast.LENGTH_SHORT).show();
                    // Fallback to default values
                    tempText.setText(convertToBanglaDigits("19°"));
                    locationText.setText("মোহনপুর, রাজশাহী");
                    weatherIcon.setImageResource(R.drawable.ic_fallback_weather);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Weather data fetch failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Fallback to default values
                tempText.setText(convertToBanglaDigits("19°"));
                locationText.setText("মোহনপুর, রাজশাহী");
                weatherIcon.setImageResource(R.drawable.ic_fallback_weather);
            }
        });
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
        return new int[] {
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
            if (item.getItemId() == R.id.navigation_home) {
                return true; // Already on Home page
            } else if (item.getItemId() == R.id.navigation_weather) {
                startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_prediction) {
                startActivity(new Intent(getApplicationContext(), PredictionActivity.class));
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

    // Utility method to convert numbers to Bangla digits
    private String convertToBanglaDigits(String input) {
        char[] banglaDigits = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(banglaDigits[Character.getNumericValue(c)]);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
