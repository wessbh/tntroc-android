package com.example.app.androidproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.Entity.User;
import com.example.app.androidproject.R;
import com.example.app.androidproject.fragments.FragmentHome;
import com.example.app.androidproject.fragments.FragmentHomeGrid;
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


public class MainActivity extends AppCompatActivity {
    private static final int PROFILE_SETTING = 100000;
    private String apikey, full_name;
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
        //Toast.makeText(getApplicationContext(), Constants.user.getLast_login(), Toast.LENGTH_SHORT).show();
        apikey = getAPIKey();
        full_name = getFullName();
        if (getAPIKey().equals("")){
            Intent intent = new Intent( getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentHome()).commit();

        // Handle Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
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
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
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
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Fragment f2 = null;
                            if (drawerItem.getIdentifier() == 1) {
                                f2 = new FragmentHome();
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                f2 = new FragmentHomeGrid();
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                f2 = new FragmentHome();
                            }
                            if (drawerItem.getIdentifier() == 21) {
                                disconnect();
                            }

                            if (f2 != null) {
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f2).commit();
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//              .withShowDrawerUntilDraggedOpened(true)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //In our case this method is always called with getSupportFragmentManager().getBackStackEntryCount() > 0 because of our addOnBackStackChangedListener
                        //if (getSupportFragmentManager().getBackStackEntryCount() > 0) {//can go back
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

                    //if you dont want the drawer to be opened in Fragment
                    //  result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    //change to hamburger icon

                    result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    //call this method to display hamburger icon
                    result.getActionBarDrawerToggle().syncState();

                    //result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });
        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(1, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    /*
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };
    */

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
    public String getApi_key() {
        return apikey;
    }

    public void setConstants (){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        String myStr = sharedPreferences.getString(Constants.USER_STR, null);
        User u = gson.fromJson(myStr, User.class);
        Constants.user = u;
    }
}
