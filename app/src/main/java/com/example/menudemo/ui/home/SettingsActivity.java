package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.menudemo.R;

public class SettingsActivity extends AppCompatActivity {

    private Button paymentsettings;
    private Button securitycenter;
    private Button commonsettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        paymentsettings = findViewById(R.id.settings_button_paymentsettings);
        paymentsettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到支付设置页面
                Intent intent = new Intent(SettingsActivity.this,Settings_PayActivity.class);
                startActivity(intent);
            }
        });

        securitycenter = findViewById(R.id.settings_button_securitycenter);
        securitycenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到安全中心设置页面
                Intent intent = new Intent(SettingsActivity.this,Settings_SafetyActivity.class);
                startActivity(intent);
            }
        });

        commonsettings = findViewById(R.id.settings_button_commonsettings);
        commonsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到通用设置页面
                Intent intent = new Intent(SettingsActivity.this,Settings_CommonActivity.class);
                startActivity(intent);
            }
        });
    }
}
