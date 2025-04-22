package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agri.PredictionActivity;
import com.example.agri.WeatherApiService;
import com.example.agri.WeatherResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your OpenWeatherMap API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_weather); // Highlight Weather tab

        // Set up navigation item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_prediction:
                    startActivity(new Intent(WeatherActivity.this, PredictionActivity.class));
                    finish();
                    return true;
                case R.id.nav_market_analysis:
                    startActivity(new Intent(WeatherActivity.this, MarketAnalysisActivity.class));
                    finish();
                    return true;
                case R.id.nav_weather:
                    // Already on Weather page, do nothing
                    return true;
                case R.id.nav_profile:
                    startActivity(new Intent(WeatherActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                default:
                    return false;
            }
        });

        // Fetch weather data
        WeatherApiService weatherService = WeatherApiService.create();
        Call<WeatherResponse> call = weatherService.getWeather("Dhaka", API_KEY, "metric", "bn");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();

                    // Air Quality (Mock data)
                    TextView airQualityValue = findViewById(R.id.airQualityValue);
                    ProgressBar airQualityProgress = findViewById(R.id.airQualityProgress);
                    airQualityValue.setText(getString(R.string.air_quality_value));
                    airQualityProgress.setProgress(0); // Updated to match image

                    // UV Index (Mock data)
                    TextView uvIndexValue = findViewById(R.id.uvIndexValue);
                    TextView uvIndexDesc = findViewById(R.id.uvIndexDesc);
                    ProgressBar uvIndexProgress = findViewById(R.id.uvIndexProgress);
                    uvIndexValue.setText(getString(R.string.uv_index_value));
                    uvIndexDesc.setText(getString(R.string.uv_index_desc));
                    uvIndexProgress.setProgress(8); // Updated to match image

                    // Sunrise and Sunset
                    TextView sunriseValue = findViewById(R.id.sunriseValue);
                    TextView sunsetValue = findViewById(R.id.sunsetValue);
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", new Locale("bn"));
                    String sunrise = sdf.format(new Date(weather.getSys().getSunrise() * 1000));
                    String sunset = sdf.format(new Date(weather.getSys().getSunset() * 1000));
                    sunriseValue.setText(convertToBanglaDigits(sunrise));
                    sunsetValue.setText(convertToBanglaDigits(sunset));

                    // Wind
                    TextView windDirection = findViewById(R.id.windDirection);
                    TextView windSpeed = findViewById(R.id.windSpeed);
                    String direction = getWindDirection(weather.getWind().getDeg());
                    windDirection.setText(direction);
                    String speed = String.format(Locale.getDefault(), "%.1f কিমি/ঘণ্টা", weather.getWind().getSpeed());
                    windSpeed.setText(convertToBanglaDigits(speed));

                    // Rainfall
                    TextView rainfallValue = findViewById(R.id.rainfallValue);
                    TextView rainfallDesc = findViewById(R.id.rainfallDesc);
                    TextView rainfallNext = findViewById(R.id.rainfallNext);
                    if (weather.getRain() != null) {
                        String rainfall = String.format(Locale.getDefault(), "%.1f মিমি", weather.getRain().get1h());
                        rainfallValue.setText(convertToBanglaDigits(rainfall));
                        rainfallDesc.setText(getString(R.string.rainfall_desc));
                    } else {
                        rainfallValue.setText(convertToBanglaDigits("0 মিমি"));
                        rainfallDesc.setText(getString(R.string.rainfall_desc));
                    }
                    rainfallNext.setText(getString(R.string.rainfall_next));

                    // Feels Like
                    TextView feelsLikeValue = findViewById(R.id.feelsLikeValue);
                    TextView feelsLikeDesc = findViewById(R.id.feelsLikeDesc);
                    String feelsLike = String.format(Locale.getDefault(), "%.0f°", weather.getMain().getFeelsLike());
                    feelsLikeValue.setText(convertToBanglaDigits(feelsLike));
                    feelsLikeDesc.setText(getString(R.string.feels_like_desc));

                    // Humidity
                    TextView humidityValue = findViewById(R.id.humidityValue);
                    TextView humidityDesc = findViewById(R.id.humidityDesc);
                    String humidity = String.format(Locale.getDefault(), "%d%%", weather.getMain().getHumidity());
                    humidityValue.setText(convertToBanglaDigits(humidity));
                    String dewPoint = String.format(Locale.getDefault(), "এখন শিশির বিন্দু %d", 17); // Mock dew point
                    humidityDesc.setText(convertToBanglaDigits(dewPoint));
                } else {
                    Toast.makeText(WeatherActivity.this,
                            String.format(getString(R.string.error), response.message()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(WeatherActivity.this,
                        String.format(getString(R.string.error), t.getMessage()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getWindDirection(int degrees) {
        String[] directions = {"উ", "উ-পূ", "পূ", "দ-পূ", "দ", "দ-প", "প", "উ-প"}; // N, NE, E, SE, S, SW, W, NW in Bangla
        return directions[(int) Math.round(((degrees % 360) / 45)) % 8];
    }

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
        return result.toString().replace("AM", "পূর্বাহ্ন").replace("PM", "অপরাহ্ন");
    }
}