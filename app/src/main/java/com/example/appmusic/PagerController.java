package com.example.appmusic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int tabCounts;
    public PagerController(@NonNull FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllMusicFragment();
            case 1:
                return new AlbumsFragment();
            case 2:
                return new PlayListFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
