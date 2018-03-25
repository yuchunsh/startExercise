package com.example.jenny.startexercise.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jenny.startexercise.R;

/**
 * Created by jenny on 2018/3/1.
 */

public class Home2Fragment extends Fragment {
    private static final String TAG = "Home2Fragment";

    //vars
    private ListView mListView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        return view;
    }





}






