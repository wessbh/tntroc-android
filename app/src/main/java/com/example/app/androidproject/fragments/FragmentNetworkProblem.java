package com.example.app.androidproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app.androidproject.R;
import com.example.app.androidproject.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNetworkProblem extends Fragment {

    Button btn_network;
    public FragmentNetworkProblem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_network_problem, container, false);

        btn_network = (Button) view.findViewById(R.id.btn_network);
        btn_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity myAct = new MainActivity();
                Intent i = getActivity().getIntent();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(i);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
