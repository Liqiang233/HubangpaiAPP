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
import android.widget.TextView;
import android.widget.Toast;

import com.example.menudemo.R;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChangeNickNameActivity extends AppCompatActivity {

    private EditText ChangeNickNameText;
    private Button ChangeNickNameButton;
    private String USERID;
    private String UserNickName;
    private String Changed;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        sp = getSharedPreferences("userInfo" , MODE_PRIVATE);
        editor = sp.edit();//获取编辑者

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick_name);
        //返回和标题
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ChangeNickNameText = findViewById(R.id.ChangeNickNameText);
        ChangeNickNameButton = findViewById(R.id.ChangeNickNameButton);

        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        USERID = sp.getString("id","读取不到返回的默认值");//获取用户id号
        new Thread(new Runnable() {

            public void run() {
                //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
                String url = HttpUtillConnection.Ya_URL+"ChangeNickName";
                Map<String, String> params = new HashMap<String, String>();
                String name = USERID;
                String flag = "1";
                params.put("USERID", name);
                params.put("FLAG", flag);

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

                            UserNickName = (String) json.get("usernickname");
                            ChangeNickNameText.setHint(UserNickName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }).start();

        ChangeNickNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Changed = ChangeNickNameText.getText().toString();

                if("".equals(Changed)){
                    Toast.makeText(ChangeNickNameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    new Thread(new Runnable() {
                        public void run(){
                            //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
                            String url = HttpUtillConnection.Ya_URL+"ChangeNickName";
                            Map<String, String> params = new HashMap<String, String>();
                            String name = USERID;
                            String flag = Changed;
                            params.put("USERID", name);
                            params.put("FLAG", flag);

                            String result = HttpUtillConnection.getContextByHttp(url, params);




                            Log.i("===========", result.toString());
                            Message msg = new Message();
                            msg.what = 0x12;
                            Bundle data = new Bundle();   //bundle 主要用于app内传递数据，以键值对的形势存在
                            data.putString("result", result);
                            Log.i("*********************", result.toString());
                            msg.setData(data);
                            hander.sendMessage(msg);

                            finish();
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
                                            editor.putString("flag","0");
                                            editor.commit();
                                            Toast.makeText(ChangeNickNameActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ChangeNickNameActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                                            finish();
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
