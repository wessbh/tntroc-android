package com.example.app.androidproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.activities.MainActivity;
import com.example.app.androidproject.utils.Constants;
import com.example.app.androidproject.R;
import com.example.app.androidproject.utils.MyAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetails extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private int id;
    private ArrayList<String> imgList = new ArrayList<>();
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    private Annonce annonce;
    private static ViewPager mPager;
    private static int currentPage = 0;
    public static String TAG = "FragmentDetails";
    private TextView titre_value, prix_value, description_label, description_value;
    ImageView arrow;
    Boolean expanded;
    ExpandableLinearLayout  expandableLayout;

    public FragmentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_annonce, container, false);
        mQueue = Volley.newRequestQueue(getContext());
        expanded = false;
        //--------------- LAYOUT COMPONENTS----------------------------\\
        id = getArguments().getInt("postID");
        titre_value = (TextView) view.findViewById(R.id.titre_value);
        description_label = (TextView) view.findViewById(R.id.description);
        description_value = (TextView) view.findViewById(R.id.description_value);
        prix_value = (TextView) view.findViewById(R.id.prix_value);
        expandableLayout= view.findViewById(R.id.expandableLayout);
        arrow = (ImageView) view.findViewById(R.id.arrow);
        arrow.setRotation(-180);
        //--------------------------------------------------------------\\


        description_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();

                if(expandableLayout.isExpanded()){
                    arrow.setRotation(180);
                    Toast.makeText(getActivity(), "Expanded", Toast.LENGTH_SHORT).show();

                }
                if(!expandableLayout.isExpanded())
                    arrow.setRotation(-180);
                Toast.makeText(getActivity(), "Not Expanded", Toast.LENGTH_SHORT).show();

            }
        });
        getImageList(id, new CallBack() {
            @Override
            public void onSuccess(ArrayList<String> imageList) {
                init(view, imageList);
            }

            @Override
            public void onFail(String msg) {

            }
        });
        jsonParse(id);
        return view;
    }

    public void jsonParse(int id) {
        final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/post/"+id;
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                int id = post.getInt("id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                Annonce myAnnonce = new Annonce();
                                myAnnonce.setId(id);
                                myAnnonce.setTitle(titre);
                                myAnnonce.setDescription(desc);
                                myAnnonce.setCreated_at(strDate);
                                myAnnonce.setImg(img);
                                myAnnonce.setCategorie(categorie);
                                myAnnonce.setPrix(Integer.valueOf(prix));
                                annonce = myAnnonce;
                            }
                            titre_value.setText(annonce.getTitle());
                            prix_value.setText(String.valueOf(annonce.getPrix()));
                            description_value.setText(annonce.getDescription());
                            progressDialog.dismiss();
                            // progressBar.setVisibility(View.GONE);
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
                headers.put("Authorization", Constants.user.getApi_key());
                return headers;
            }
        };
        mQueue.add(request);
    }



    public void getImageList(int id, final CallBack onCallBack) {

        final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/post_image/"+id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                String img = post.getString("image");
                                imgList.add(img);
                            }
                            onCallBack.onSuccess(imgList);
                            // progressBar.setVisibility(View.GONE);
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
                headers.put("Authorization", Constants.user.getApi_key());
                return headers;
            }
        };
        mQueue.add(request);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface CallBack {
        void onSuccess(ArrayList<String> imageList);

        void onFail(String msg);
    }
    private void init(View view, final ArrayList<String > myList) {
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(getContext(),myList));
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == myList.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
    }
}
