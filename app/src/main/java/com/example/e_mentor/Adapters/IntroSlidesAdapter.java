package com.example.e_mentor.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.e_mentor.Fragments.Slide1Fragment;
import com.example.e_mentor.Fragments.Slide2Fragment;
import com.example.e_mentor.Fragments.Slide3Fragment;

public class IntroSlidesAdapter extends FragmentPagerAdapter {

    public IntroSlidesAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Slide1Fragment();
            case 1:
                return new Slide2Fragment();
            case 2:
                return new Slide3Fragment();
            default:
                return new Slide1Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
