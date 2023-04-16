package com.example.e_mentor.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.HomePage;
import com.example.e_mentor.R;

public class ChangePasswordSuccessfulActivity extends AppCompatActivity {
    private Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_successful);
        getStarted = findViewById(R.id.get_started);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordSuccessfulActivity.this, HomePage.class));
                finish();
            }
        });

    }
}