package com.example.app.androidproject.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.fragments.FragmentHome;
import com.example.app.androidproject.fragments.FragmentHomeGrid;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Context context;
    //Constructor to the class
    public Pager(android.support.v4.app.FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                FragmentHome tab1 = new FragmentHome();
                return tab1;
            case 1:
                FragmentHomeGrid tab2 = new FragmentHomeGrid();
                return tab2;
            case 2:
                FragmentHome tab3 = new FragmentHome();
                return tab3;
            case 3:
                FragmentHomeGrid tab4 = new FragmentHomeGrid();
                return tab4;
            case 4:
                FragmentHome tab5 = new FragmentHome();
                return tab5;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}