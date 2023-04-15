package com.example.e_mentor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.Profile.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Set the default selected menu item
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // Handle the Home menu item click
                return true;
            case R.id.explore:
                // Handle the Explore menu item click
                return true;
            case R.id.e_mentor:
                // Handle the E-Mentor menu item click
                return true;
            case R.id.learning_hub:
                // Handle the Learning Hub menu item click
                return true;
            case R.id.profile:
                // Handle the Profile menu item click
                Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                startActivity(profileIntent);
                return true;
            default:
                return false;
        }
    }
}
