package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.menudemo.R;

public class LoginActivity2 extends AppCompatActivity {
    SharedPreferences sprfMain;
    SharedPreferences.Editor editorMain;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login2);


    }

}
