package com.esprit.app.tntroc.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.esprit.app.tntroc.Entity.Annonce;
import com.esprit.app.tntroc.Entity.Comment;
import com.esprit.app.tntroc.Entity.User;
import com.esprit.app.tntroc.R;
import com.esprit.app.tntroc.activities.MainActivity;
import com.esprit.app.tntroc.utils.AnnonceListAdapter;
import com.esprit.app.tntroc.utils.CommentAdapter;
import com.esprit.app.tntroc.utils.Constants;
import com.esprit.app.tntroc.utils.CustomRVItemTouchListener;
import com.esprit.app.tntroc.utils.DatabaseHelper;
import com.esprit.app.tntroc.utils.MyAdapter;
import com.esprit.app.tntroc.utils.RecyclerViewItemClickListener;
import com.orhanobut.android.dialogplussample.SimpleAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;

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
    private ArrayList<Annonce> annoncesFavoris = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_comment;
    private CommentAdapter cAdapter;
    private AnnonceListAdapter listAdapter;
    private RequestQueue mQueue;
    ProgressDialog progressDialog;
    public Annonce annonce;
    private static ViewPager mPager;
    private static int currentPage = 0;
    public static String TAG = "FragmentDetails";
    private TextView titre_value, prix_value, description_label, description_value, username;
    ImageView arrow, imageView_profile, btn_comment,iconeCorrect;
    FloatingActionButton  tofavoris;
    Boolean expanded;
    ExpandableLayout expandableLayout;
    User user;
    Button btn_action, btn_achat, btn_echange;
    Toolbar toolbar;
    DialogPlus dialogBottom, dialogListview;
    MyDialogFragment dialogFragment;
    public int itemSeleceted;
    public Boolean selectedItem = false;
    public AlertDialog alertDialogBuilderEchange;
    public AlertDialog.Builder alertDialogBuilderAchat;
    public AlertDialog.Builder alertDialogBuilderComment;
    DatabaseHelper db;
    View comment_dialog;
    RatingBar rating_cm;
    EditText body_cm;
    public FragmentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_annonce, container, false);
        final View contentView = inflater.inflate(R.layout.content, container, false);
        final View list_view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        comment_dialog = inflater.inflate(R.layout.comment_dialog, container, false);
        rating_cm = comment_dialog.findViewById(R.id.rating_dialog);
        body_cm = comment_dialog.findViewById(R.id.edit_dialog);
        mQueue = Volley.newRequestQueue(getContext());
        expanded = false;
        //--------------- LAYOUT COMPONENTS----------------------------\\
        iconeCorrect = new ImageView(getContext());
        iconeCorrect.setImageResource(R.drawable.ic_correct);
        btn_comment = view.findViewById(R.id.img_comment);
        db = new DatabaseHelper(getContext());
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
        tofavoris = view.findViewById(R.id.tofavoris);
        recyclerView_comment = view.findViewById(R.id.recycler_comment);
        //--------------------------------------------------------------\\

        //--------------- Commentaire  Recycler----------------------------\\
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_comment.setLayoutManager(mLayoutManager);
        recyclerView_comment.setItemAnimator(new DefaultItemAnimator());
        recyclerView_comment.setAdapter(cAdapter);
        //--------------------------------------------------------------\\


        tofavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAnnonce(annonce.getId(), annonce.getTitle(), annonce.getPrix(), annonce.getImg());
                tofavoris.setImageResource(R.drawable.heart_filled);
            }
        });
        RecyclerView.LayoutManager mLayoutManager_junior = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager_junior);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                showDialogEchangeConfirmation(position);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
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
                showDialogAchat();
            }
        });
        btn_echange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBottom.dismiss();
                showDialogEchange();
            }
        });
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCommentaire();
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

    public void getComments(int id) {
        final String url =  Constants.WEBSERVICE_URL+"/mdw/v1/comments/"+id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("comments");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject comment = jsonArray.getJSONObject(i);
                                Log.d("bara", ""+comment);
                                int id = comment.getInt("id");
                                int user_id = comment.getInt("user_id");
                                int post_id = comment.getInt("post_id");
                                String body = comment.getString("comment_body");
                                String created = comment.getString("created_at");
                                int rating = comment.getInt("rating");
                                String user_img = comment.getString("user_img");
                                String username = comment.getString("username");
                                Comment com = new Comment();
                                com.setId(id);
                                com.setUser_id(user_id);
                                com.setPost_id(post_id);
                                com.setBody(body);
                                com.setCreated_at(created);
                                com.setRating(rating);
                                com.setUser_img(user_img);
                                com.setUsername(username);
                                commentList.add(com);
                            }
                            cAdapter = new CommentAdapter(getContext(), commentList);
                            recyclerView_comment.setAdapter(cAdapter);
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
                                int user_id = post.getInt("user_id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                Annonce myAnnonce = new Annonce();
                                myAnnonce.setId(id);
                                myAnnonce.setUser_id(user_id);
                                myAnnonce.setTitle(titre);
                                myAnnonce.setDescription(desc);
                                myAnnonce.setCreated_at(strDate);
                                myAnnonce.setImg(img);
                                myAnnonce.setCategorie(categorie);
                                myAnnonce.setPrix(Integer.valueOf(prix));
                                annonce = myAnnonce;
                                Constants.annonce_static = myAnnonce;
                            }
                            checkOwner(annonce.getUser_id(), Constants.user.getId());
                            getComments(annonce.getId());
                            annoncesFavoris.addAll(db.getAllPosts());
                            if (dejaFavoris(annonce, annoncesFavoris)){
                            }
                            else
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
        progressDialog = new ProgressDialog(getActivity(),
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
                            Glide.with(getContext())
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
                                int user_id = post.getInt("user_id");
                                String titre = post.getString("title");
                                String desc = post.getString("description");
                                String strDate = post.getString("created_at");
                                String img = post.getString("img");
                                String categorie = post.getString("categorie");
                                String prix = post.getString("prix");
                                Annonce annonce = new Annonce();
                                annonce.setId(id);
                                annonce.setUser_id(user_id);
                                annonce.setTitle(titre);
                                annonce.setDescription(desc);
                                annonce.setCreated_at(strDate);
                                annonce.setImg(img);
                                annonce.setCategorie(categorie);
                                annonce.setPrix(Integer.valueOf(prix));
                                annoncesList.add(annonce);
                            }
                            final MainActivity myAct = (MainActivity) getContext();
                            FragmentDetails f = (FragmentDetails) myAct.getSupportFragmentManager().findFragmentById(R.id.frame_container);
                            listAdapter = new AnnonceListAdapter(f, annoncesList);
                            recyclerView.setAdapter(listAdapter);
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

    public void showDialogEchange(){

        //------------------------------- Alert Dialog---------------------------------\\
        alertDialogBuilderEchange = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).create();
        alertDialogBuilderEchange.setTitle("Sélectionner une annonce ");
        if(recyclerView.getParent() != null) {
            ((ViewGroup)recyclerView.getParent()).removeView(recyclerView);
        }

        alertDialogBuilderEchange.setView(recyclerView);
        alertDialogBuilderEchange.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        alertDialogBuilderEchange.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (selectedItem){
                    echange(itemSeleceted);
                }
            }
        });
        alertDialogBuilderEchange.show();
        //-----------------------------------------------------------------------------\\
    }

    public void showDialogEchangeConfirmation(final int pos){

        //------------------------------- Alert Dialog Achat---------------------------------\\
        alertDialogBuilderAchat = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        alertDialogBuilderAchat.setTitle("Echange");
        alertDialogBuilderAchat.setMessage("Veuillez confirmer l'échange");
        alertDialogBuilderAchat.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        echange(pos);
                        alertDialogBuilderEchange.dismiss();
                        showCustomToast("Demande d'échange envoyé", iconeCorrect);
                    }
                });

        alertDialogBuilderAchat.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        alertDialogBuilderAchat.show();
        //-----------------------------------------------------------------------------\\
    }
    public void echange (final int p ){
        String url = Constants.WEBSERVICE_URL+"/mdw/v1/addaction";
        Log.d("ok",url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                Annonce a = annoncesList.get(p);
                int id_annonceur = annonce.getUser_id();
                int id_annonce = annonce.getId();
                int id_client = Constants.user.getId();
                int id_annonce_echange = a.getId();

                params.put("id_annonceur",String.valueOf(id_annonceur));
                params.put("id_annonce", String.valueOf(id_annonce));
                params.put("id_client", String.valueOf(id_client));
                params.put("type", "echange");
                params.put("id_annonce_echange", String.valueOf(id_annonce_echange));
                return params;
            }
        };
        mQueue.add(postRequest);
    }
    public Boolean dejaFavoris (Annonce a, ArrayList<Annonce> list){
        Boolean existe = false;
        for (Annonce aa :list) {
            if(aa.getId() == a.getId()){
                existe = true;
            }
        }
        return existe;
    }
    public void showDialogAchat(){

        //------------------------------- Alert Dialog Achat---------------------------------\\
        alertDialogBuilderAchat = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        alertDialogBuilderAchat.setTitle("Passer une demande d'achat");
        alertDialogBuilderAchat.setMessage("Veuillez confirmer l'achat");
        alertDialogBuilderAchat.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        achat();
                        dialog.cancel();
                        showCustomToast("Demande d'achat envoyé", iconeCorrect);

                    }
                });

        alertDialogBuilderAchat.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        alertDialogBuilderAchat.show();
        //-----------------------------------------------------------------------------\\
    }
    public void showDialogCommentaire(){
            EditText edittext = new EditText(getContext());
            RatingBar rate = new RatingBar(getContext());
        //------------------------------- Alert Dialog Achat---------------------------------\\
        alertDialogBuilderComment = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        alertDialogBuilderComment.setTitle("Commentaire");
        alertDialogBuilderComment.setPositiveButton("Envoyer",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Comment c = new Comment(Constants.user.getId(),annonce.getId(), body_cm.getText().toString(), rating_cm.getNumStars(),Constants.user.getImage(),Constants.user.getUsername());
                        addcomment(c);
                        cAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });

        alertDialogBuilderComment.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        alertDialogBuilderComment.setView(comment_dialog);
        alertDialogBuilderComment.show();
        //-----------------------------------------------------------------------------\\
    }

    public void achat (){
        String url = Constants.WEBSERVICE_URL+"/mdw/v1/addaction";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                int id_annonceur = annonce.getUser_id();
                int id_annonce = annonce.getId();
                int id_client = Constants.user.getId();
                Log.d("okey", annonce.toString());
                params.put("id_annonceur",String.valueOf(id_annonceur));
                params.put("id_annonce", String.valueOf(id_annonce));
                params.put("id_client", String.valueOf(id_client));
                params.put("type", "achat");
                params.put("id_annonce_echange", String.valueOf(0));
                return params;
            }
        };
        mQueue.add(postRequest);
    }

    private void insertAnnonce(int idAnnonce, String titre, int prix, String img) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertAnnonce(idAnnonce, titre, prix, img);
        }

    public void addcomment (final Comment comment ){
        String url = Constants.WEBSERVICE_URL+"/mdw/v1/addcomment";
        Log.d("ok",url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        cAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("comment_body",comment.getBody());
                params.put("post_id", String.valueOf(comment.getPost_id()));
                params.put("user_id", String.valueOf(comment.getUser_id()));
                params.put("rating", "echange");
                params.put("user_img", comment.getUser_img());
                params.put("username", comment.getUsername());
                return params;
            }
        };
        mQueue.add(postRequest);
    }

    public void checkOwner(int ann_id, int usr_id){
        if (ann_id == usr_id){
            btn_action.setEnabled(false);
        }
        else btn_action.setEnabled(true);
    }
    public void showCustomToast (String message, ImageView img){

        Toast toastCorrect = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toastCorrect.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toastCorrect.getView();
        if(img.getParent()==null){
            toastContentView.addView(img, 0);
            toastCorrect.show();
        }
        ((ViewGroup)img.getParent()).removeView(img);
        toastContentView.addView(img, 0);
        toastCorrect.show();
        }

}
