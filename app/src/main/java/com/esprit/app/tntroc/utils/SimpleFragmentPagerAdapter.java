package com.esprit.app.tntroc.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.esprit.app.tntroc.fragments.FragmentActionsList;
import com.esprit.app.tntroc.fragments.FragmentHomeGrid;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FragmentActionsList.newInstance(1,"show_actions_recu");
        }
        else {
            return FragmentActionsList.newInstance(2,"show_actions_envoyee");
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Demandes envoyées";
            case 1:
                return "Demandes reçues";
            default:
                return null;
        }
    }
}
