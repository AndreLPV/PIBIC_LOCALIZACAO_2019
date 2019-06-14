package com.bluee.localizacaoobjeto.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bluee.localizacaoobjeto.R;

public class HomeFragment extends Fragment {


    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_tab_home, container, false);
        Log.v("fatal","cria");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("fatal","recria");
        tv = (TextView)view.findViewById(R.id.textViewHome);
    }

    @Nullable
    public void receive(String msg){
       tv.setText(msg);

    }
}