package com.cityguide;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class CategoryFragment extends Fragment {


   Button button_showall,button_historic,button_museums,button_shopping, button_entertainment, button_eating;
   Button button_closest, button_toprated, button_mostcomments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        button_showall = view.findViewById(R.id.btn_all);
        button_historic = view.findViewById(R.id.btn_historic);
        button_museums = view.findViewById(R.id.btn_museums);
        button_shopping = view.findViewById(R.id.btn_shopping);
        button_entertainment = view.findViewById(R.id.btn_entertainment);
        button_eating = view.findViewById(R.id.btn_eating);
        button_toprated = view.findViewById(R.id.toprated);
        button_mostcomments = view.findViewById(R.id.mostcomments);
        button_closest = view.findViewById(R.id.closest);

        button_showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("all");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        button_historic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("historic");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });
        button_entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("entertainment");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });
        button_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("shopping");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });
        button_museums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("museum");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });
        button_eating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("eating");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        button_toprated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("toprated");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        button_closest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("closest");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        button_mostcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment postFragment = PostFragment.newInstance("mostcomments");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        return view;
    }
}