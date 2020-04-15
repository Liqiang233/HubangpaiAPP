package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.menudemo.R;


public class Settings_SafetyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings_safety);
        //返回和标题
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView PassWordTittle = findViewById(R.id.PassWordTitlle);
        TextView PassWordButton = findViewById(R.id.PassWordButton);
        PassWordTittle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改密码界面
                Intent intent = new Intent(Settings_SafetyActivity.this,ChangePassWordActivity.class);
                startActivity(intent);
            }
        });
        PassWordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改密码界面
                Intent intent = new Intent(Settings_SafetyActivity.this,ChangePassWordActivity.class);
                startActivity(intent);
            }
        });

        TextView EmailTittle = findViewById(R.id.Emailitlle);
        TextView EmailButton = findViewById(R.id.EmailButton);
        EmailTittle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改邮箱界面
                Intent intent = new Intent(Settings_SafetyActivity.this,ChangeEmailActivity.class);
                startActivity(intent);
            }
        });
        EmailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改邮箱界面
                Intent intent = new Intent(Settings_SafetyActivity.this,ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        TextView NumTitlle = findViewById(R.id.NumTitlle);
        TextView NumButton = findViewById(R.id.NumButton);
        NumTitlle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改手机号码界面
                Intent intent = new Intent(Settings_SafetyActivity.this,ChangePhoneNumberActivity.class);
                startActivity(intent);
            }
        });
        NumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改手机号码界面
                Intent intent = new Intent(Settings_SafetyActivity.this,ChangePhoneNumberActivity.class);
                startActivity(intent);
            }
        });

        TextView LandingRecordTittle = findViewById(R.id.LandingRecordTittle);
        TextView LandingRecordButton = findViewById(R.id.LandingRecordButton);
        LandingRecordTittle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到登陆记录界面
                Intent intent = new Intent(Settings_SafetyActivity.this,LandingRecordActivity.class);
                startActivity(intent);
            }
        });
        LandingRecordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到登陆记录界面
                Intent intent = new Intent(Settings_SafetyActivity.this,LandingRecordActivity.class);
                startActivity(intent);
            }
        });
    }
}
