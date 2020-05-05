package com.example.menudemo.ui.home;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



import com.example.menudemo.R;


public class HomeFragment extends Fragment {


    private Button settings;
    private Button personalinfo;
    private Button tasklist;
    private Button wallet;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView bt_settings = root.findViewById(R.id.my_settings);
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置页面设置页面
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
            }
        });

        TextView bt_personalinfo = root.findViewById(R.id.my_info);
        bt_personalinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //跳转到设置页面个人信息页面
                Intent intent = new Intent(getActivity(),PersonalinfoActivity.class);
                startActivity(intent);
            }
        });


        TextView bt_wallet = root.findViewById(R.id.my_wallet);
        bt_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到钱包页面

                //跳转到钱包页面

                Intent intent = new Intent(getActivity(),WalletActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}