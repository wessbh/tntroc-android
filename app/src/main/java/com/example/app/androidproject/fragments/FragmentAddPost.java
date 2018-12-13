package com.example.app.androidproject.fragments;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.R;
import com.example.app.androidproject.activities.RegisterActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddPost extends Fragment {
    EditText input_titre, input_desc, input_prix;
    Spinner spinner;
    Button btn_upload, btn_ajouter;
    ImageView imageView;
    /******IMAGE UPLOAD*******/

    private String UPLOAD_URL = Constants.WEBSERVICE_URL+"/mdw/uploadimage/uploadannonce.php", image_extension;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Boolean image_changed = false;
    /***************/

    private RequestQueue mQueue;
    public FragmentAddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        input_titre = view.findViewById(R.id.input_title);
        input_desc = view.findViewById(R.id.input_desc);
        input_prix = view.findViewById(R.id.input_prix);
        imageView = view.findViewById(R.id.imageView_annonce);
        btn_upload = view.findViewById(R.id.upload_img);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn_ajouter = view.findViewById(R.id.btn_ajouter);
        btn_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                if (validate()){
                    ajouterAnnonceRequest(input_titre.getText().toString(), input_desc.getText().toString(), Integer.valueOf(input_prix.getText().toString()), spinner.getSelectedItem().toString());
                    uploadImage();
                }
            }
        });
        spinner = view.findViewById(R.id.spinner);


        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                image_extension = getImageExtension(getContext(), filePath);
                image_changed = true;
                imageView.setImageBitmap(bitmap);
                image_changed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{

            Toast.makeText(getContext(), "oops :(", Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public  String getImageExtension(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
    public  Boolean validate(){
        boolean valid = true;

        if (input_titre.getText().toString().isEmpty() || input_titre.getText().toString().length() < 3) {
            input_titre.setError("at least 3 characters");
            valid = false;
        } else {
            input_titre.setError(null);
        }
        if (input_desc.getText().toString().isEmpty() || input_desc.getText().toString().length() < 3) {
            input_desc.setError("at least 3 characters");
            valid = false;
        } else {
            input_desc.setError(null);
        }
        if (input_prix.getText().toString().isEmpty() || input_prix.getText().toString().length() < 3) {
            input_prix.setError("at least 3 characters");
            valid = false;
        } else {
            input_prix.setError(null);
        }
        if (!image_changed){
            valid = false;
            Toast.makeText(getContext(), "Pas d'image !", Toast.LENGTH_SHORT).show();
        }
        if (spinner.getSelectedItem().toString().equals("Selectionner votre Categorie")){
            valid = false;
            Toast.makeText(getContext(), "Veuillez choisir une categorie !", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
    private void uploadImage(){
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getContext(), s , Toast.LENGTH_LONG).show();
                        Log.d("error_image", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = input_titre.getText().toString().trim()+"-img."+image_extension;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("filename", name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void ajouterAnnonceRequest (final String title, final String description , final int prix, final String category){
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/addpost";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Toast.makeText(getContext(),response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getContext(),error+"", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                String imgName = input_titre.getText().toString().trim()+"-img."+image_extension;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", title);
                params.put("description", description);
                params.put("img", imgName);
                params.put("date_exp", "NULL");
                params.put("categorie", category);
                params.put("prix", String.valueOf(prix));

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", Constants.user.getApi_key());
                return headers;
            }
        };

        mQueue.add(postRequest);
    }
}
