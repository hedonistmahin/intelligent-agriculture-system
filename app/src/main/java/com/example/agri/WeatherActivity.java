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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeatherActivity extends AppCompatActivity {
    private static final String API_KEY = "f158cc8f703a554c078b339da07bdb7d";
    private BottomNavigationView bottomNavigationView;
    private Spinner citySpinner;
    private Button btnRefreshWeather;
    private String selectedCity = "Dhaka"; // Default city (English name for API)
    private Map<String, String> districtNameMapping; // Map Bangla to English district names
    private List<String> banglaDistricts; // List of Bangla district names for spinner

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

        // Load districts from district_mapping.json
        try {
            loadDistricts();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading districts: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        // Set up city spinner with districts
        List<String> cities = new ArrayList<>();
        cities.add("নির্বাচন করুন"); // Default option for user to choose
        cities.addAll(banglaDistricts); // Add all Bangla district names
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // Set up Spinner listener to update selected city
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
                if (!city.equals("নির্বাচন করুন")) {
                    selectedCity = districtNameMapping.get(city); // Get English name for API
                    fetchWeatherData(selectedCity); // Fetch weather data for selected city
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.black)); // Set text color to black for valid selections
                } else {
                    ((TextView) view).setTextColor(getResources().getColor(android.R.color.darker_gray)); // Gray out default option
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle when nothing is selected
            }
        });

        // Set up Refresh Button listener
        btnRefreshWeather.setOnClickListener(v -> {
            if (selectedCity.equals("নির্বাচন করুন")) {
                Toast.makeText(WeatherActivity.this, "একটি শহর নির্বাচন করুন", Toast.LENGTH_SHORT).show(); // Toast for invalid city selection
            } else {
                fetchWeatherData(selectedCity); // Refresh weather for selected city
            }
        });

        // Initial fetch for default city (Dhaka)
        fetchWeatherData(selectedCity);
    }

    // Load districts from district_mapping.json and create name mapping
    private void loadDistricts() throws Exception {
        String districtJson = readAssetFile("district_mapping.json");
        JSONObject districtJsonObject = new JSONObject(districtJson);

        banglaDistricts = new ArrayList<>();
        districtNameMapping = new HashMap<>();

        // English district names for OpenWeatherMap API (you may need to adjust these)
        Map<String, String> banglaToEnglishMapping = new HashMap<>();
        banglaToEnglishMapping.put("বাগেরহাট", "Bagerhat");
        banglaToEnglishMapping.put("বান্দরবান", "Bandarban");
        banglaToEnglishMapping.put("বরগুনা", "Barguna");
        banglaToEnglishMapping.put("বরিশাল", "Barisal");
        banglaToEnglishMapping.put("ভোলা", "Bhola");
        banglaToEnglishMapping.put("বগুড়া", "Bogra");
        banglaToEnglishMapping.put("ব্রাহ্মণবাড়িয়া", "Brahmanbaria");
        banglaToEnglishMapping.put("চাঁদপুর", "Chandpur");
        banglaToEnglishMapping.put("চট্টগ্রাম", "Chittagong");
        banglaToEnglishMapping.put("চুয়াডাঙ্গা", "Chuadanga");
        banglaToEnglishMapping.put("কুমিল্লা", "Comilla");
        banglaToEnglishMapping.put("কক্সবাজার", "Cox's Bazar");
        banglaToEnglishMapping.put("ঢাকা", "Dhaka");
        banglaToEnglishMapping.put("দিনাজপুর", "Dinajpur");
        banglaToEnglishMapping.put("ফরিদপুর", "Faridpur");
        banglaToEnglishMapping.put("ফেনী", "Feni");
        banglaToEnglishMapping.put("গাইবান্ধা", "Gaibandha");
        banglaToEnglishMapping.put("গাজীপুর", "Gazipur");
        banglaToEnglishMapping.put("গোপালগঞ্জ", "Gopalganj");
        banglaToEnglishMapping.put("হবিগঞ্জ", "Habiganj");
        banglaToEnglishMapping.put("জামালপুর", "Jamalpur");
        banglaToEnglishMapping.put("যশোর", "Jessore");
        banglaToEnglishMapping.put("ঝালকাঠি", "Jhalokati");
        banglaToEnglishMapping.put("ঝিনাইদহ", "Jhenaidah");
        banglaToEnglishMapping.put("জয়পুরহাট", "Joypurhat");
        banglaToEnglishMapping.put("খাগড়াছড়ি", "Khagrachari");
        banglaToEnglishMapping.put("খুলনা", "Khulna");
        banglaToEnglishMapping.put("কিশোরগঞ্জ", "Kishoreganj");
        banglaToEnglishMapping.put("কুষ্টিয়া", "Kushtia");
        banglaToEnglishMapping.put("লক্ষ্মীপুর", "Lakshmipur");
        banglaToEnglishMapping.put("লালমনিরহাট", "Lalmonirhat");
        banglaToEnglishMapping.put("মাদারীপুর", "Madaripur");
        banglaToEnglishMapping.put("মাগুরা", "Magura");
        banglaToEnglishMapping.put("মানিকগঞ্জ", "Manikganj");
        banglaToEnglishMapping.put("মৌলভীবাজার", "Moulvibazar");
        banglaToEnglishMapping.put("মেহেরপুর", "Meherpur");
        banglaToEnglishMapping.put("মুন্সীগঞ্জ", "Munshiganj");
        banglaToEnglishMapping.put("ময়মনসিংহ", "Mymensingh");
        banglaToEnglishMapping.put("নওগাঁ", "Naogaon");
        banglaToEnglishMapping.put("নড়াইল", "Narail");
        banglaToEnglishMapping.put("নারায়ণগঞ্জ", "Narayanganj");
        banglaToEnglishMapping.put("নরসিংদী", "Narsingdi");
        banglaToEnglishMapping.put("নাটোর", "Natore");
        banglaToEnglishMapping.put("নেত্রকোণা", "Netrokona");
        banglaToEnglishMapping.put("নীলফামারী", "Nilphamari");
        banglaToEnglishMapping.put("নোয়াখালী", "Noakhali");
        banglaToEnglishMapping.put("পাবনা", "Pabna");
        banglaToEnglishMapping.put("পঞ্চগড়", "Panchagarh");
        banglaToEnglishMapping.put("পটুয়াখালী", "Patuakhali");
        banglaToEnglishMapping.put("পিরোজপুর", "Pirojpur");
        banglaToEnglishMapping.put("রাজবাড়ী", "Rajbari");
        banglaToEnglishMapping.put("রাজশাহী", "Rajshahi");
        banglaToEnglishMapping.put("রাঙামাটি", "Rangamati");
        banglaToEnglishMapping.put("রংপুর", "Rangpur");
        banglaToEnglishMapping.put("সাতক্ষীরা", "Satkhira");
        banglaToEnglishMapping.put("শরীয়তপুর", "Shariatpur");
        banglaToEnglishMapping.put("শেরপুর", "Sherpur");
        banglaToEnglishMapping.put("সিরাজগঞ্জ", "Sirajganj");
        banglaToEnglishMapping.put("সুনামগঞ্জ", "Sunamganj");
        banglaToEnglishMapping.put("সিলেট", "Sylhet");
        banglaToEnglishMapping.put("টাঙ্গাইল", "Tangail");
        banglaToEnglishMapping.put("ঠাকুরগাঁও", "Thakurgaon");
        banglaToEnglishMapping.put("কুড়িগ্রাম", "Kurigram");
        banglaToEnglishMapping.put("বর্তমান জেলা", "Dhaka"); // Fallback to Dhaka

        // Parse district_mapping.json and build lists
        for (Iterator<String> keys = districtJsonObject.keys(); keys.hasNext(); ) {
            String key = keys.next();
            String banglaName = districtJsonObject.getString(key);
            banglaDistricts.add(banglaName);
            String englishName = banglaToEnglishMapping.get(banglaName);
            districtNameMapping.put(banglaName, englishName != null ? englishName : banglaName); // Fallback to Bangla if no mapping
        }
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

                    // UV Index (Mock data for now)
                    TextView uvIndexValue = findViewById(R.id.uvIndexValue);
                    TextView uvIndexDesc = findViewById(R.id.uvIndexDesc);
                    ProgressBar uvIndexProgress = findViewById(R.id.uvIndexProgress);
                    uvIndexValue.setText(getString(R.string.uv_index_value));
                    uvIndexDesc.setText(getString(R.string.uv_index_desc));
                    uvIndexProgress.setProgress(8); // Example UV index progress

                    // Sunrise and Sunset times
                    TextView sunriseValue = findViewById(R.id.sunriseValue);
                    TextView sunsetValue = findViewById(R.id.sunsetValue);
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", new Locale("bn"));
                    String sunrise = sdf.format(new Date(weather.getSys().getSunrise() * 1000));
                    String sunset = sdf.format(new Date(weather.getSys().getSunset() * 1000));
                    sunriseValue.setText(convertToBanglaDigits(sunrise));
                    sunsetValue.setText(convertToBanglaDigits(sunset));

                    // Wind data
                    TextView windDirection = findViewById(R.id.windDirection);
                    TextView windSpeed = findViewById(R.id.windSpeed);
                    String direction = getWindDirection(weather.getWind().getDeg());
                    windDirection.setText(direction);
                    String speed = String.format(Locale.getDefault(), "%.1f কিমি/ঘণ্টা", weather.getWind().getSpeed());
                    windSpeed.setText(convertToBanglaDigits(speed));

                    // Rainfall data
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

                    // Feels Like data
                    TextView feelsLikeValue = findViewById(R.id.feelsLikeValue);
                    TextView feelsLikeDesc = findViewById(R.id.feelsLikeDesc);
                    String feelsLike = String.format(Locale.getDefault(), "%.0f°", weather.getMain().getFeelsLike());
                    feelsLikeValue.setText(convertToBanglaDigits(feelsLike));
                    feelsLikeDesc.setText(getString(R.string.feels_like_desc));

                    // Humidity data
                    TextView humidityValue = findViewById(R.id.humidityValue);
                    TextView humidityDesc = findViewById(R.id.humidityDesc);
                    String humidity = String.format(Locale.getDefault(), "%d%%", weather.getMain().getHumidity());
                    humidityValue.setText(convertToBanglaDigits(humidity));
                    String dewPoint = String.format(Locale.getDefault(), "এখন শিশির বিন্দু %d", 17); // Mock dew point data
                    humidityDesc.setText(convertToBanglaDigits(dewPoint));
                } else {
                    Toast.makeText(WeatherActivity.this, "Weather data couldn't be fetched: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "Weather data fetch failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(WeatherActivity.this, "বায়ুর গুণমান ডেটা পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AirQualityResponse> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "বায়ুর গুণমান ডেটা ফেচ করতে ব্যর্থ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_weather);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_weather) {
                return true;
            } else if (item.getItemId() == R.id.navigation_home) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // Changed MainActivity to HomeActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (item.getItemId() == R.id.navigation_prediction) {
                startActivity(new Intent(getApplicationContext(), PredictionActivity.class));
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