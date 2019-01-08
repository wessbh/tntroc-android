package com.example.app.androidproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.Entity.User;
import com.example.app.androidproject.utils.AnnonceGridAdapter;
import com.example.app.androidproject.utils.AnnonceListAdapter;
import com.example.app.androidproject.utils.Constants;
import com.example.app.androidproject.R;
import com.example.app.androidproject.utils.MyAdapter;
import com.orhanobut.android.dialogplussample.SimpleAdapter;
import com.orhanobut.dialogplus.*;
import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetails extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener,  ExpandableLayout.OnExpansionUpdateListener{
    private int id;
    private ArrayList<String> imgList = new ArrayList<>();
    private List<Annonce> annoncesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AnnonceListAdapter listAdapter;
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    private Annonce annonce;
    private static ViewPager mPager;
    private static int currentPage = 0;
    public static String TAG = "FragmentDetails";
    private TextView titre_value, prix_value, description_label, description_value, username;
    ImageView arrow, imageView_profile;
    Boolean expanded;
    ExpandableLayout expandableLayout;
    User user;
    Button btn_action, btn_achat, btn_echange;
    Toolbar toolbar;
    DialogPlus dialogBottom, dialogListview;
    MyDialogFragment dialogFragment;
    public FragmentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_annonce, container, false);
        final View contentView = inflater.inflate(R.layout.content, container, false);
        final View list_view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        mQueue = Volley.newRequestQueue(getContext());
        expanded = false;

        //--------------- LAYOUT COMPONENTS----------------------------\\
        dialogFragment = new MyDialogFragment();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Détails");
        id = getArguments().getInt("postID");
        titre_value = (TextView) view.findViewById(R.id.titre_value);
        description_label = (TextView) view.findViewById(R.id.description);
        description_value = (TextView) view.findViewById(R.id.description_value);
        prix_value = (TextView) view.findViewById(R.id.prix_value);
        expandableLayout= view.findViewById(R.id.expandableLayout);
        arrow = (ImageView) view.findViewById(R.id.arrow);
        imageView_profile = (ImageView) view.findViewById(R.id.imageView_profile);
        username = (TextView) view.findViewById(R.id.username);
        btn_action = (Button) view.findViewById(R.id.btn_action) ;
        btn_achat = (Button) contentView.findViewById(R.id.btn_achat) ;
        btn_echange = (Button) contentView.findViewById(R.id.btn_echange);
        recyclerView =list_view.findViewById(R.id.recycler_view);
        //--------------------------------------------------------------\\
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getUserPosts();
        dialogFragment.setShowsDialog(true);
        dialogBottom = DialogPlus.newDialog(getContext())
                .setAdapter(new SimpleAdapter(getContext(), false,2))
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {
                    }
                })
                .setContentHolder(new ViewHolder(contentView))
                .create();
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBottom.show();
            }
        });
        btn_achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBottom.dismiss();
            }
        });
        btn_echange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBottom.dismiss();
                //dialogFragment.show(getActivity().getSupportFragmentManager(),"dialog");
                showDialog();
            }
        });
        description_label.setOnClickListener(this);
        expandableLayout.setOnExpansionUpdateListener(this);
        arrow.setOnClickListener(this);
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
                                int userId = post.getInt("user_id");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                Annonce myAnnonce = new Annonce();
                                myAnnonce.setId(id);
                                myAnnonce.setTitle(titre);
                                myAnnonce.setDescription(desc);
                                myAnnonce.setCreated_at(strDate);
                                myAnnonce.setImg(img);
                                myAnnonce.setUser_id(userId);
                                myAnnonce.setCategorie(categorie);
                                myAnnonce.setPrix(Integer.valueOf(prix));
                                annonce = myAnnonce;
                            }
                            getUser(annonce.getUser_id());
                            titre_value.setText(annonce.getTitle());
                            prix_value.setText(String.valueOf(annonce.getPrix())+" Dt");
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


    public void getUser(int id) {
        final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/user_by_id/"+id;
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Retrieving data...");
        user = new User();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("user");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject user_json = jsonArray.getJSONObject(i);
                                user.setId(Integer.valueOf(user_json.get("id").toString()));
                                user.setUsername(user_json.get("username").toString());
                                user.setName(user_json.get("name").toString());
                                user.setLast_name(user_json.get("last_name").toString());
                                user.setEmail(user_json.get("email").toString());
                                user.setNumtel(user_json.get("num_tel").toString());
                                user.setAdresse(user_json.get("adresse").toString());
                                user.setDate_naissance(user_json.get("date_naissance").toString());
                                user.setApi_key(user_json.get("apiKey").toString());
                                user.setImage(user_json.get("image").toString());
                                user.setLast_login(user_json.get("last_login").toString());
                            }
                            username.setText(user.getLast_name()+" "+user.getName());
//                            Picasso.get().load(Constants.USER_IMG_PATH+user.getImage())
//                                    .resize(300,200)
//                                    .centerCrop()
//                                    .error(R.drawable.error_img)
//                                    .placeholder(R.drawable.placeholder)
//                                    .into(imageView_profile);
                            Glide.with(getActivity())
                                    .load(Constants.USER_IMG_PATH+user.getImage())
                                    .apply(new RequestOptions().override(400, 300))
                                    .into(imageView_profile);
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


    public void getUserPosts() {
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/"+"posts_user/"+Constants.user.getId();
        Log.d("dialogFragment", "in function: ");
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
                                Annonce annonce = new Annonce();
                                annonce.setId(id);
                                annonce.setTitle(titre);
                                annonce.setDescription(desc);
                                annonce.setCreated_at(strDate);
                                annonce.setImg(img);
                                annonce.setCategorie(categorie);
                                annonce.setPrix(Integer.valueOf(prix));
                                annoncesList.add(annonce);
                            }
                            listAdapter = new AnnonceListAdapter(annoncesList);
                            recyclerView.setAdapter(listAdapter);
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

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {

        arrow.setRotation(expansionFraction * 180);
    }

    @Override
    public void onClick(View v) {
        expandableLayout.toggle();
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

    public void showDialog(){

        //------------------------------- Alert Dialog---------------------------------\\
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        alertDialogBuilder.setTitle("Sélectionner une annonce ");
        if(recyclerView.getParent() != null) {
            ((ViewGroup)recyclerView.getParent()).removeView(recyclerView);
        }
        alertDialogBuilder.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setView(recyclerView);
        alertDialogBuilder.show();
        //-----------------------------------------------------------------------------\\
    }
}
