package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.menudemo.MainActivity.username;


public class PersonalinfoActivity extends AppCompatActivity {
    private static  final String USERINFO = "userInfo";
    //变量定义
    String USERID;
    String UserNickName;
    String UserSign;
    String UserAddress;
    TextView UserIDTextView;
    TextView UserNickNameTextView;
    TextView UserSignTextView;
    TextView UserAddressTextView;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_personalinfo);
        //返回和标题
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //从SharedPreferences中获取用户名并显示出来
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        USERID = sp.getString("id","读取不到返回的默认值");//获取编辑者
        UserIDTextView = findViewById(R.id.pslinfo_textview_userid);
        UserIDTextView.setText(USERID);

        new Thread(new Runnable() {

            public void run() {
                //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
                String url = HttpUtillConnection.Qiang_URL+"Personalinfo";
                Map<String, String> params = new HashMap<String, String>();
                String name = USERID;
                String flag = "text";
                params.put("USERID", name);
                params.put("FLAG2", flag);

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
                            UserSign = (String) json.get("usersign");
                            UserAddress = (String) json.get("useraddress");

                            UserNickNameTextView = findViewById(R.id.pslinfo_textview_nickname);
                            UserNickNameTextView.setText(UserNickName);

                            UserSignTextView = findViewById(R.id.pslinfo_textview_signature);
                            UserSignTextView.setText(UserSign);

                            UserAddressTextView = findViewById(R.id.pslinfo_textview_address);
                            UserAddressTextView.setText(UserAddress);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }).start();


        //修改昵称
        TextView ChangeNickNameView = findViewById(R.id.pslinfo_text_nickname);
        TextView ChangeNickNameButton = findViewById(R.id.pslinfo_button_nickname);
        ChangeNickNameView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改昵称界面
                Intent intent = new Intent(PersonalinfoActivity.this,ChangeNickNameActivity.class);
                startActivity(intent);
            }
        });
        ChangeNickNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改昵称界面
                Intent intent = new Intent(PersonalinfoActivity.this,ChangeNickNameActivity.class);
                startActivity(intent);
            }
        });

        //修改个性签名
        TextView ChangeSignView = findViewById(R.id.pslinfo_text_signature);
        TextView ChangeSignButton = findViewById(R.id.pslinfo_button_signature);
        ChangeSignView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改个性签名界面
                Intent intent = new Intent(PersonalinfoActivity.this,ChangeSignActivity.class);
                startActivity(intent);
            }
        });
        ChangeSignButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改个性签名界面
                Intent intent = new Intent(PersonalinfoActivity.this,ChangeSignActivity.class);
                startActivity(intent);
            }
        });

        //修改收货地址
        TextView ChangeAddressView = findViewById(R.id.pslinfo_text_address);
        TextView ChangeAddressButton = findViewById(R.id.pslinfo_button_address);
        ChangeAddressView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改收货地址界面
                Intent intent = new Intent(PersonalinfoActivity.this,ChangeAddressActivity.class);
                startActivity(intent);
            }
        });
        ChangeAddressButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到修改收货地址界面
                Intent intent = new Intent(PersonalinfoActivity.this,ChangeAddressActivity.class);
                startActivity(intent);
            }
        });
    }
}
