package com.example.e_mentor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.e_mentor.IntroSlidesActivity;
import com.example.e_mentor.R;
import com.example.e_mentor.WelcomeActivity;

public class Slide3Fragment extends Fragment {

    public Slide3Fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slidethree, container, false);

        Button buttonSlide3 = view.findViewById(R.id.button_slide3);
        buttonSlide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(welcomeIntent);
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
