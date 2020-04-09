package com.example.menudemo.ui.home;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menudemo.R;

public class RegisterActivity extends AppCompatActivity {
    private Button bt_login;
    private EditText login_username;
    private EditText login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);
    }
}
