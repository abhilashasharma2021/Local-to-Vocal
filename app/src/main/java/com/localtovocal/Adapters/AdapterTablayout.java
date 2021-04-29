package com.localtovocal.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.localtovocal.Fragments.LocalFragment;
import com.localtovocal.Fragments.NewsFragment;

public class AdapterTablayout extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public AdapterTablayout(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsFragment();
            case 1:

                return new LocalFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;

    }
}
