package com.example.app.androidproject.fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.R;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddPost extends Fragment {
    EditText input_titre, input_desc, input_prix;
    Spinner spinner;
    Button btn_upload, btn_ajouter;
    /******IMAGE UPLOAD*******/

    private String UPLOAD_URL = Constants.WEBSERVICE_URL+"/mdw/uploadimage/upload.php", image_extension;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Boolean image_changed = false;
    /***************/
    public FragmentAddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        input_titre = view.findViewById(R.id.input_title);
        input_desc = view.findViewById(R.id.input_desc);
        input_prix = view.findViewById(R.id.input_prix);
        btn_upload = view.findViewById(R.id.upload_img);
        btn_ajouter = view.findViewById(R.id.btn_ajouter);
        spinner = view.findViewById(R.id.spinner);


        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(RegisterActivity.this, getStringImage(bitmap), Toast.LENGTH_SHORT).show();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                image_extension = getImageExtension(getContext(), filePath);
                image_changed = true;
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/

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
}
