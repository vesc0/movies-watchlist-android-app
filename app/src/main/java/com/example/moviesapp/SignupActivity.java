package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput;
    private Spinner genreSpinner;
    private Button signupButton;
    private TextView loginText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        genreSpinner = findViewById(R.id.genre_spinner);
        signupButton = findViewById(R.id.signup_button);
        loginText = findViewById(R.id.login_text);

        signupButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String favoriteGenre = genreSpinner.getSelectedItem().toString();

            if (validateInputs(name, email, password, favoriteGenre)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name)
                                                    .build())
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "Sign-up successful! Redirecting...", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignupActivity.this, ExploreActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish(); // Close SignupActivity
                                                } else {
                                                    Toast.makeText(SignupActivity.this, "Profile update failed: " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(SignupActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs(String name, String email, String password, String favoriteGenre) {
        if (name.isEmpty()) {
            nameInput.setError("Name required");
            return false;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email required");
            return false;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Password required");
            return false;
        }
        if (favoriteGenre.equals("Select your favorite genre")) {
            Toast.makeText(this, "Please select a genre", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
