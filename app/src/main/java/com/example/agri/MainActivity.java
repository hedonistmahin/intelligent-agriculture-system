package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show splash screen layout
        setContentView(R.layout.activity_main); // This is the splash screen layout

        // After 3 seconds, navigate to HomeActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Close MainActivity (splash screen)
            }
        }, 3000); // Delay of 3 seconds
    }
}
