package com.example.app.androidproject.fragments;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.Entity.Annonce;
import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.R;
import com.example.app.androidproject.utils.AnnonceGridAdapter;

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
public class FragmentHomeGrid extends Fragment {
    private RecyclerView recyclerView;
    private AnnonceGridAdapter adapter, mAdapter;
    private List<Annonce> annoncesList = new ArrayList<>();
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    private GridLayoutManager lLayout;

    public FragmentHomeGrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_grid, container, false);

        mQueue = Volley.newRequestQueue(getContext());
        TextView txt = view.findViewById(R.id.myText);
        txt.setText("Changed !!!");
       recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*lLayout = new GridLayoutManager(getActivity(), 2);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);*/
        jsonParse();

        return view;
    }

    public void jsonParse() {
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/all_posts";
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
                            Log.d("annonce", response.getJSONArray("post").getJSONObject(0).toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject post = jsonArray.getJSONObject(i);
                                int id = post.getInt("id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                Annonce annonce = new Annonce();
                                annonce.setId(id);
                                annonce.setTitle(titre);
                                annonce.setDescription(desc);
                                annonce.setCreated_at(strDate);
                                annonce.setImg(img);
                                annonce.setCategorie(categorie);
                                annoncesList.add(annonce);
                            }
                            Log.d("here", annoncesList.toString());
                            progressDialog.dismiss();
                            mAdapter = new AnnonceGridAdapter(getActivity(), annoncesList);
                            recyclerView.setAdapter(mAdapter);
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
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", Constants.user.getApi_key());
                return headers;
            }
        };
        mQueue.add(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
