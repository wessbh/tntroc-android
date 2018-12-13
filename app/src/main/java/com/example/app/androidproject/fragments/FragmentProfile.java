package com.example.app.androidproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.app.androidproject.Entity.Constants;
import com.example.app.androidproject.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    EditText nom, lieu,propos,email, phone;
    Button btn_editer;
    ImageView img;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        img = view.findViewById(R.id.imageView_profile);
        Picasso.get().load(Constants.USER_IMG_PATH+Constants.user.getImage())
                .resize(100,100)
                .centerCrop()
                .error(R.drawable.error_img)
                .placeholder(R.drawable.placeholder)
                .into(img);
        nom = view.findViewById(R.id.name);
        nom.setText(Constants.user.getName()+" "+Constants.user.getLast_name());
        nom.setEnabled(false);
        lieu = view.findViewById(R.id.address);
        lieu.setEnabled(false);
        email = view.findViewById(R.id.email);
        email.setText(Constants.user.getEmail());
        email.setEnabled(false);
        phone = view.findViewById(R.id.phone);
        phone.setText(Constants.user.getNumtel());
        phone.setEnabled(false);
        btn_editer = view.findViewById(R.id.btn_editer);
        btn_editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnStr = btn_editer.getText().toString();
                if(btnStr.equals("editer")){
                    btn_editer.setText("confirmer");
                    nom.setEnabled(true);
                    lieu.setEnabled(true);
                    email.setEnabled(true);
                    phone.setEnabled(true);
                }
                if(btnStr.equals("confirmer")){
                    btn_editer.setText("editer");
                    nom.setEnabled(false);
                    lieu.setEnabled(false);
                    email.setEnabled(false);
                    phone.setEnabled(false);
                }
            }
        });
        return view;
    }

}
