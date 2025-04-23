package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {
    private static final String API_KEY = "f158cc8f703a554c078b339da07bdb7d";
    private BottomNavigationView bottomNavigationView;
    private Spinner citySpinner;
    private Button btnRefreshWeather;
    private String selectedCity = "Dhaka";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();

        // Initialize Spinner and Refresh Button
        citySpinner = findViewById(R.id.citySpinner);
        btnRefreshWeather = findViewById(R.id.btnRefreshWeather);

        // Set up city spinner
        List<String> cities = Arrays.asList(
                "নির্বাচন করুন",
                "ঢাকা", "চট্টগ্রাম", "রাজশাহী", "খুলনা", "বরিশাল",
                "সিলেট", "রংপুর", "ময়মনসিংহ", "কুমিল্লা", "নারায়ণগঞ্জ",
                "গাজীপুর", "দিনাজপুর", "ফরিদপুর", "যশোর", "বগুড়া"
        );
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // Set up Spinner listener to update selected city
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
                if (!city.equals("নির্বাচন করুন")) {
                    selectedCity = city;
                    fetchWeatherData(selectedCity);
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up Refresh Button listener
        btnRefreshWeather.setOnClickListener(v -> {
            if (selectedCity.equals("নির্বাচন করুন")) {
                Toast.makeText(WeatherActivity.this, "একটি শহর নির্বাচন করুন", Toast.LENGTH_SHORT).show();
            } else {
                fetchWeatherData(selectedCity);
            }
        });

        // Initial fetch for default city (Dhaka)
        fetchWeatherData(selectedCity);
    }

    private void fetchWeatherData(String city) {
        WeatherApiService weatherService = WeatherApiService.create();
        Call<WeatherResponse> call = weatherService.getWeather(city, API_KEY, "metric", "bn");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();

                    // Fetch air quality data using coordinates
                    double lat = weather.getCoord().getLat();
                    double lon = weather.getCoord().getLon();
                    fetchAirQualityData(lat, lon);

                    // UV Index (Mock data)
                    TextView uvIndexValue = findViewById(R.id.uvIndexValue);
                    TextView uvIndexDesc = findViewById(R.id.uvIndexDesc);
                    ProgressBar uvIndexProgress = findViewById(R.id.uvIndexProgress);
                    uvIndexValue.setText(getString(R.string.uv_index_value));
                    uvIndexDesc.setText(getString(R.string.uv_index_desc));
                    uvIndexProgress.setProgress(8);

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
                    String dewPoint = String.format(Locale.getDefault(), "এখন শিশির বিন্দু %d", 17);
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

    private void fetchAirQualityData(double lat, double lon) {
        WeatherApiService weatherService = WeatherApiService.create();
        Call<AirQualityResponse> call = weatherService.getAirQuality(lat, lon, API_KEY);

        call.enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(Call<AirQualityResponse> call, Response<AirQualityResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().list.length > 0) {
                    AirQualityResponse airQuality = response.body();
                    int aqi = airQuality.list[0].main.aqi;
                    float pm25 = airQuality.list[0].components.pm2_5;

                    // Map AQI to a description
                    String[] aqiDescriptions = {"ভালো", "ন্যায্য", "মাঝারি", "দুর্বল", "খুব দুর্বল"};
                    String aqiDesc = aqiDescriptions[aqi - 1];

                    // Update UI
                    TextView airQualityValue = findViewById(R.id.airQualityValue);
                    TextView airQualityDetails = findViewById(R.id.airQualityDetails);
                    ProgressBar airQualityProgress = findViewById(R.id.airQualityProgress);
                    airQualityValue.setText(aqiDesc);

                    // Map AQI to progress (1-5 scale to 0-100 progress)
                    int progress = (aqi - 1) * 25;
                    airQualityProgress.setProgress(progress);

                    // Display PM2.5 concentration
                    String pm25Text = String.format(Locale.getDefault(), "PM2.5: %.1f µg/m³", pm25);
                    airQualityDetails.setText(convertToBanglaDigits(pm25Text));
                } else {
                    Toast.makeText(WeatherActivity.this,
                            "বায়ুর গুণমান ডেটা পাওয়া যায়নি",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AirQualityResponse> call, Throwable t) {
                Toast.makeText(WeatherActivity.this,
                        "বায়ুর গুণমান ডেটা ফেচ করতে ব্যর্থ: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_weather);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_weather) {
                return true;
            } else if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private String getWindDirection(int degrees) {
        String[] directions = {"উ", "উ-পূ", "পূ", "দ-পূ", "দ", "দ-প", "প", "উ-প"};
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