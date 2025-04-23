package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for MainActivity (your splash screen layout)

        // Show Splash screen for 2 seconds, then transition to HomeActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the HomeActivity after splash
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish(); // Finish MainActivity so the user can't return to it using the back button
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
