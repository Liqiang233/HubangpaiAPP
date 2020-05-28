package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.menudemo.R;


public class WalletActivity extends AppCompatActivity {
    private TextView wallet;
    private SharedPreferences sp;
    private static  final String USERINFO = "userInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wallet=findViewById(R.id.points);
        sp = getSharedPreferences(USERINFO , MODE_PRIVATE);
        wallet.setText(sp.getString("wallet", null));
    }

}



