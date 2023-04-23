package com.example.e_mentor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.e_mentor.IntroSlidesActivity;
import com.example.e_mentor.R;

public class Slide1Fragment extends Fragment {

    public Slide1Fragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slideone, container, false);

        Button buttonSlide1 = view.findViewById(R.id.button_slide1);
        buttonSlide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroSlidesActivity activity = (IntroSlidesActivity) getActivity();
                if (activity != null) {
                    ViewPager viewPager = activity.findViewById(R.id.intro_view_pager);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });

        return view;
    }

}
