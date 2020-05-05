package com.example.menudemo.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.task.TasklistActivity;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    private EditText SignText;
    private String Sign;
    private String USERID;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        SignText =  root.findViewById(R.id.my_signature);

        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        USERID = sp.getString("id","读取不到返回的默认值");//获取用户id号

        new Thread(new Runnable() {

            public void run() {
                //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
                String url = HttpUtillConnection.Qiang_URL+"ChangeSign";
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

                            Sign = (String) json.get("usersign");
                            SignText.setHint(Sign);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }).start();






        TextView bt_settings = root.findViewById(R.id.my_settings);
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置页面设置页面
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
            }
        });

        TextView bt_personalinfo = root.findViewById(R.id.my_info);
        bt_personalinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //跳转到设置页面个人信息页面
                Intent intent = new Intent(getActivity(),PersonalinfoActivity.class);
                startActivity(intent);

            }
        });

        TextView bt_tasklist = root.findViewById(R.id.my_tasklist);
        bt_tasklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到设置页面任务页面

                //跳转到我的任务页面

                Intent intent = new Intent(getActivity(), TasklistActivity.class);
                startActivity(intent);
            }
        });

        TextView bt_wallet = root.findViewById(R.id.my_wallet);
        bt_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到钱包页面

                //跳转到钱包页面

                Intent intent = new Intent(getActivity(),WalletActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}