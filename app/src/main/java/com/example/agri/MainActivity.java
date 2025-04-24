package com.example.agri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences থেকে চেক করো অ্যাপ প্রথমবার লঞ্চ হচ্ছে কিনা
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            // প্রথমবার লঞ্চ হচ্ছে, স্প্ল্যাশ স্ক্রিন দেখাও
            setContentView(R.layout.activity_main); // স্প্ল্যাশ স্ক্রিনের লেআউট সেট করো

            // SharedPreferences আপডেট করো যাতে পরের লঞ্চে স্প্ল্যাশ স্ক্রিন না দেখায়
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.commit(); // commit() ব্যবহার করো যাতে সিঙ্ক্রোনাসভাবে সেভ হয়

            // ২ সেকেন্ড পরে HomeActivity-তে যাও
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish(); // MainActivity বন্ধ করো
                }
            }, 3000); // ২ সেকেন্ড ডিলে
        } else {
            // প্রথমবার নয়, সরাসরি HomeActivity-তে যাও
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // MainActivity বন্ধ করো
        }
    }
}