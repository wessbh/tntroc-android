package com.example.app.androidproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.Entity.User;
import com.example.app.androidproject.R;
import com.example.app.androidproject.fragments.FragmentAddPost;
import com.example.app.androidproject.fragments.FragmentHome;
import com.example.app.androidproject.fragments.FragmentHomeGrid;
import com.example.app.androidproject.fragments.FragmentViewPager;
import com.example.app.androidproject.utils.Pager;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity /*implements TabLayout.OnTabSelectedListener */{
    private static final int PROFILE_SETTING = 100000;
    private String apikey, full_name, img_url;
    private FloatingActionButton btn_add;
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setConstants();
        img_url = Constants.USER_IMG_PATH+Constants.user.getImage();
        apikey = getAPIKey();
        full_name = getFullName();
        btn_add = (FloatingActionButton) findViewById(R.id.add_btn);
        btn_add.setVisibility(View.GONE);
        if (getAPIKey().equals("")){
            Intent intent = new Intent( getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentViewPager()).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final IProfile profile = new ProfileDrawerItem().withName(full_name).withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);


        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile
                )
                .withSelectionListEnabled(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(img_url).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Accueil").withIcon(FontAwesome.Icon.faw_tachometer_alt).withIdentifier(1).withSelectable(true),
                        new SectionDrawerItem().withName("Travail").withDivider(true),
                        new PrimaryDrawerItem().withName("Mon Espace").withIcon(FontAwesome.Icon.faw_user).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName("Mes annonces").withIcon(FontAwesome.Icon.faw_clipboard_list).withIdentifier(3).withSelectable(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Offres").withIcon(FontAwesome.Icon.faw_chalkboard_teacher).withIdentifier(4).withSelectable(true),
                        new SectionDrawerItem().withName("Mise A jour Profil").withDivider(true),
                        new PrimaryDrawerItem().withName("Mot de passe").withIcon(FontAwesome.Icon.faw_user_lock).withIdentifier(5).withSelectable(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Se déconnecter").withIcon(FontAwesome.Icon.faw_sign_out_alt).withIdentifier(21).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        Fragment f2 = null;
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                btn_add.setVisibility(View.GONE);
                                f2 = new FragmentViewPager();
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                btn_add.setVisibility(View.GONE);
                                f2 = new FragmentHome();
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                btn_add.setVisibility(View.VISIBLE);
                                btn_add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       FragmentAddPost fr = new FragmentAddPost();
                                       changeFragment(fr);
                                    }
                                });
                                f2 = FragmentHomeGrid.newInstance(0, "posts_user/"+Constants.user.getId());
                            }
                            if (drawerItem.getIdentifier() == 21) {
                                disconnect();
                            }
                            if (f2 != null) {
                                changeFragment(f2);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        onBackPressed();
                        //}
                        return true;
                    }
                })
                .build();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    //change to back arrow
                    result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                } else {
                    //change to hamburger icon

                    result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    result.getActionBarDrawerToggle().syncState();
                }
            }
        });
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(1, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
    public String getAPIKey (){
        String s;
        s = Constants.user.getApi_key();
        return s;
    }
    public String getFullName (){
        String s;
        s= Constants.user.getName()+" "+Constants.user.getLast_name();
        return s;
    }
    public void setSharedPrefs(final String name, final String api_key){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USERNAME, name);
        editor.putString(Constants.API_KEY, api_key);
        editor.apply();
    }
    public void disconnect(){
        setSharedPrefs(null, null);
        Intent intent = new Intent( getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void setConstants (){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        String myStr = sharedPreferences.getString(Constants.USER_STR, null);
        User u = gson.fromJson(myStr, User.class);
        Constants.user = u;
    }
    public void changeFragment(Fragment fragment){

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}
