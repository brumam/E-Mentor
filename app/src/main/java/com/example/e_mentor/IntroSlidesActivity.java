package com.example.e_mentor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.e_mentor.Adapters.IntroSlidesAdapter;

public class IntroSlidesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private IntroSlidesAdapter introSlidesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slides);

        viewPager = findViewById(R.id.intro_view_pager);
        introSlidesAdapter = new IntroSlidesAdapter(getSupportFragmentManager());
        viewPager.setAdapter(introSlidesAdapter);
    }
}
