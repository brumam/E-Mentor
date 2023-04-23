package com.example.e_mentor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        int SPLASH_DISPLAY_LENGTH = 3000; // 3000 milliseconds = 3 seconds

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    // User is signed in, start HomePage
                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomePage.class);
                    startActivity(mainIntent);
                } else {
                    // User is not signed in, start IntroSlidesActivity or Login
                    Intent mainIntent = new Intent(SplashScreenActivity.this, IntroSlidesActivity.class);

                    startActivity(mainIntent);
                }
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
