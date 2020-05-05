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
import android.widget.EditText;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/*
   * @author  Yapi
   * @note  登陆界面的逻辑设计
   * @time  2020.4.6
   * @version 1.0
 */

public class LoginActivity extends AppCompatActivity {
    //定义
    private Button bt_login;
    private EditText login_username;
    private EditText login_password;
    private Button bt_forgetpsd;
    private Button bt_register;

    public UserInfo userInfo;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String USER_NAME = "id";
    private static final String PASSWORD = "password";
    private static final String ISSAVEPASS = "savepassword";
    private static final String AUTOLOGIN = "autologin";
    private static  final String USERINFO = "userInfo";
    private String username;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);


        context = getApplicationContext();
        sp = getSharedPreferences(USERINFO , MODE_PRIVATE);
        editor = sp.edit();//获取编辑者
        userInfo = new UserInfo(this);


        bt_login = findViewById(R.id.bt_login);
        login_username=findViewById(R.id.login_username);
        login_password=findViewById(R.id.login_password);
        bt_register = findViewById(R.id.login_button_register);
        bt_forgetpsd = findViewById(R.id.login_button_forgetpsw);



        //用户注册
        bt_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //跳转到注册界面
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //忘记密码
        bt_forgetpsd.setOnClickListener(new View.OnClickListener()
            {
              @Override
              public void onClick(View v) {
              Intent intent = new Intent(LoginActivity.this,ForgetpsdActivity.class);
              startActivity(intent);
              }
            }
        );
        //登陆按钮  操作
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login_username.getText().toString().trim().equals("")|login_password.getText().toString().trim().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "账号、密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else  //账号密码 不为空
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
                            String url = HttpUtillConnection.base_URL+"LoginUser";
                            Map<String, String> params = new HashMap<String, String>();
                            String name = login_username.getText().toString();
                            String psd = login_password.getText().toString();
                            username = new String (name);
                            params.put("USERID", name);
                            params.put("USERPSD", psd);

                            String result = HttpUtillConnection.getContextByHttp(url, params);
                            Log.i("===========", result.toString());
                            Message msg = new Message();
                            msg.what = 0x12;
                            Bundle data = new Bundle();   //bundle 主要用于app内传递数据，以键值对的形势存在
                            data.putString("result", result);
                            Log.i("*********************", result.toString());
                            msg.setData(data);
                            hander.sendMessage(msg);
                        }

                        @SuppressLint("HandlerLeak")    //Handler用于管理线程或者进程的消息队列
                        Handler hander = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 0x12) {
                                    Bundle data = msg.getData();
                                    String key = data.getString("result");//得到json返回的json
                                    if (key != null && key.startsWith("\ufeff")) {
                                        key = key.substring(1);
                                    }
                                    try {
                                        JSONObject json = new JSONObject(key);
                                        String result = (String) json.get("result");
                                        if ("success".equals(result)) {

                                            //返回成功后  将登陆的用户名保存在SharedPreferences中
                                            //SharedPreferences:轻量级的存储类，以键值对的形式存在
                                            username = login_username.getText().toString();
                                            password = login_password.getText().toString();
                                            editor.putString("id",username);
                                            editor.commit();
                                            /*userInfo.setUserInfo(USER_NAME,username);
                                            userInfo.setUserInfo(PASSWORD, password);
                                             */
                                            //页面跳转
                                            Intent intent = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("USERID", login_username.getText().toString());
                                            intent.putExtras(bundle);
                                            intent.setClass(context, MainActivity.class);//跳转到app主界面
                                            startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        } else if ("passError".equals(result)) {
                                            Toast.makeText(LoginActivity.this, "登录失败，密码错误", Toast.LENGTH_SHORT).show();
                                            login_password.setText("");
                                        } else if ("noUser".equals(result)) {
                                            Toast.makeText(LoginActivity.this, "登录失败，用户不存在，请注册", Toast.LENGTH_SHORT).show();
                                            login_password.setText("");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    }).start();
                }
            }
        });
    }

    }
       /*
       @author  TendaG
       SharedPreferences getdataPreferences = getSharedPreferences("login",
                MODE_PRIVATE);
        // 读取数据，第一个参数是键值，第二个参数是找不到对应键值时的返回值
        String getdata = getdataPreferences.getString("name", null);
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
                        "login", MODE_PRIVATE).edit();
                // 2、向SharedPreferences.Editor对象中添加数据
                spEditor.putString("name", data);
                spEditor.putString("pwd", data2);
                // 3、将添加的数据提交
                spEditor.commit();


                Toast.makeText(LoginActivity.this,data+"登陆成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });     */
