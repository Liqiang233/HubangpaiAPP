package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;

public class LoginActivity extends AppCompatActivity {
    private Button bt_login;
    private EditText login_username;
    private EditText login_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_login = findViewById(R.id.bt_login);
        login_username=findViewById(R.id.login_username);
        login_password=findViewById(R.id.login_password);
        SharedPreferences getdataPreferences = getSharedPreferences("mydata",
                MODE_PRIVATE);
        // 读取数据，第一个参数是键值，第二个参数是找不到对应键值时的返回值
        String getdata = getdataPreferences.getString("username", null);
        // 将读取到的值显示到TextView上
        login_username.setText(getdata);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取EditText中输入的数据
                String data = login_username.getText().toString();
                String data2 = login_password.getText().toString();
                // 1、获取一个SharedPreferences.Editor对象
                SharedPreferences.Editor spEditor = getSharedPreferences(
                        "mydatas", MODE_PRIVATE).edit();
                // 2、向SharedPreferences.Editor对象中添加数据
                spEditor.putString("username", data);
                spEditor.putString("pwd", data2);
                // 3、将添加的数据提交
                spEditor.commit();


                Toast.makeText(LoginActivity.this,data+"登陆成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
