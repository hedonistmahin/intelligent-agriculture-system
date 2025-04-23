package com.example.agri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Link to your welcome page layout

        // Find the "Get Started" button by its ID
        Button getStartedBtn = findViewById(R.id.getStartedBtn);

        // Set an OnClickListener to transition to HomeActivity when clicked
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start HomeActivity when the button is clicked
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent); // Start the activity
                finish(); // Close the WelcomeActivity so the user can't return to it
            }
        });
    }
}
