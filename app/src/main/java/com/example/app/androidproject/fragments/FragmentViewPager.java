package com.example.app.androidproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.utils.Constants;
import com.example.app.androidproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewPager extends Fragment {

    List<String> categoriesList = new ArrayList<>();
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    private ViewPager viewPager;
    TabLayout tabLayout;
    public FragmentViewPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        mQueue = Volley.newRequestQueue(getContext());
        jsonParse();
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager= (ViewPager) view.findViewById(R.id.viewpager);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        List<String> cats = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, int NumOfTabs, List<String> cat) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            this.cats = cat;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return FragmentHomeGrid.newInstance(0,"all_posts");
                case 1:
                    return FragmentHomeGrid.newInstance(1,"posts_by_categorie/Accessoire");
                case 2:
                    return FragmentHomeGrid.newInstance(2,"posts_by_categorie/Ordinateur");
                case 3:
                    return FragmentHomeGrid.newInstance(3,"posts_by_categorie/Telephone");
                case 4:
                    return new FragmentHomeGrid();
                case 5:
                    return new FragmentHome();
                case 6:
                    return new FragmentHomeGrid();

                default:
                    return null;
            }
        }
       @Override
        public CharSequence getPageTitle(int position) {
            return cats.get(position);
        }
        @Override
        public int getCount() {
            return cats.size();
        }
    }


    public void jsonParse() {
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/all_categories";
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("cat");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cat = jsonArray.getJSONObject(i);
                                String libelle = cat.getString("libelle");
                                if(libelle.equals("Aall"))
                                    categoriesList.add("Annonces");
                                else
                                categoriesList.add(libelle);
                            }
                            viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount(), categoriesList));
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", Constants.user.getApi_key());
                return headers;
            }
        };
        mQueue.add(request);
    }

}
