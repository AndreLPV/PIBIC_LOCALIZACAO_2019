package com.bluee.localizacaoobjeto.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bluee.localizacaoobjeto.R;

public class SettingFragment extends Fragment {


    SendMessage SM;
    EditText et;
    Button bt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_setting, container, false);
        bt = (Button)rootView.findViewById(R.id.buttonSetting);
        et = (EditText) rootView.findViewById(R.id.editTextSetting);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SM.sendData(et.getText().toString());
            }
        });
        return rootView;
    }

    /*=====================================================================================*/
    public interface SendMessage{
        void sendData(String msg);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }


}