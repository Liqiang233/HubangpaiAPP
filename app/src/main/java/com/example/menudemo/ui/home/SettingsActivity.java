package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.utills.HttpUtillConnection;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private Button paymentsettings;
    private Button securitycenter;
    private Button commonsettings;
    private  Button exitaccount;

    public UserInfo userInfo;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        //返回和标题
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = getApplicationContext();
        sp = getSharedPreferences("userInfo" , MODE_PRIVATE);
        editor = sp.edit();//获取编辑者
        userInfo = new UserInfo(this);



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
        //注销
        exitaccount = findViewById(R.id.settings_button_exit);
        exitaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i("huanxinlogout","success");
                    }

                    @Override
                    public void onError(int code, String error) {
                        Log.i("huanxinlogout","fail");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
                editor.clear();
                editor.commit();
                Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });



    }
}
