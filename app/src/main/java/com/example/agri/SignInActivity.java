package com.example.agri;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private ImageView eyeIcon;
    private CheckBox checkboxAgreement;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in); // Make sure your XML file is named activity_sign_in.xml

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        eyeIcon = findViewById(R.id.eyeIcon);
        checkboxAgreement = findViewById(R.id.checkboxAgreement);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView userPolicyText = findViewById(R.id.userPolicyText);
        Button signInBtn = findViewById(R.id.signInBtn);
        TextView createAccountText = findViewById(R.id.createAccountText);

        // Password visibility toggle
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.baseline_visibility_off_24);
                } else {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.baseline_visibility_24);
                }
                isPasswordVisible = !isPasswordVisible;
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        // Sign In Button click listener
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!checkboxAgreement.isChecked()) {
                    Toast.makeText(SignInActivity.this, "Please agree to the terms", Toast.LENGTH_SHORT).show();
                } else {
                    // Add your sign-in logic here
                    performSignIn(email, password);
                }
            }
        });

        // Create Account click listener
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add navigation to registration screen
                Toast.makeText(SignInActivity.this, "Navigate to registration", Toast.LENGTH_SHORT).show();
            }
        });

        // Forgot Password click listener
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add forgot password logic
                Toast.makeText(SignInActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Privacy Policy click listener
        userPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add policy viewing logic
                Toast.makeText(SignInActivity.this, "Show policies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSignIn(String email, String password) {
        // Implement your actual sign-in logic here
        // This is just a placeholder
        if (email.equals("test@example.com") && password.equals("password")) {
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}