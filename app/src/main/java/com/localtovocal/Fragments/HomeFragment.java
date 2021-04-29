package com.localtovocal.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.tabs.TabLayout;
import com.localtovocal.Activities.LoginActivity;
import com.localtovocal.Activities.SearchUsersActivity;
import com.localtovocal.Adapters.AdapterTablayout;
import com.localtovocal.R;

import java.util.Objects;


public class HomeFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    ImageView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        search = view.findViewById(R.id.search);


        search.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), SearchUsersActivity.class));
            Animatoo.animateShrink(Objects.requireNonNull(getActivity()));
        });

        tabLayout.addTab(tabLayout.newTab().setText("News"));
        tabLayout.addTab(tabLayout.newTab().setText("Locals"));


        final AdapterTablayout adapterTablayout = new AdapterTablayout(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapterTablayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        return view;
    }
}