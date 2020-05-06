package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.menudemo.ui.notifications.activity.ChatActivity;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/*
 *    点击RecyclerView的item进来，展示任务详情
 *    @author shijizhe
 *    @time 2020/4/17
 */
public class MessionDetailsActivity extends AppCompatActivity {
    private TextView mclass = null;
    private TextView mname = null;
    private TextView maddress = null;
    private TextView mdetails = null;
    private TextView mdeadline = null;
    private TextView mpay = null;
    private Button mcommit = null;
    private Button mchat = null;
    private TextView mid = null;
    private TextView mtime = null;
    private TextView mman = null;
    private TextView mstatus = null;
    private SharedPreferences sp;
    public String result;
    public String acceptUrl = HttpUtillConnection.base_URL + "AcceptTask";
    String Acceptor,Status,id;
    String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mession_details);

        sp = getSharedPreferences("userInfo", MODE_PRIVATE);

        mclass = findViewById(R.id.ac_messionclass);  //任务类型
        mname = findViewById(R.id.ac_messionnname);   //任务名称
        maddress = findViewById(R.id.ac_messionaddress); //地址
        mdetails = findViewById(R.id.ac_messionndetails);  //详细描述
        mdeadline = findViewById(R.id.ac_messionndeadline);  //截止日期
        mpay = findViewById(R.id.ac_messionpay);  //酬劳
        mtime = findViewById(R.id.ac_messionlaunchtime);  //  发布日期
        mman = findViewById(R.id.ac_messionninitiator);   //发布者
        mstatus = findViewById(R.id.ac_messionnstatus);   //状态
        mcommit = findViewById(R.id.bt_accept);    //接受任务
        mchat = findViewById(R.id.bt_chat);

        //界面间传递数据
        Intent intent = getIntent();
        mname.setText(intent.getStringExtra("messionname"));
        mclass.setText(intent.getStringExtra("messiontype"));
        maddress.setText(intent.getStringExtra("messionaddress"));
        mdetails.setText(intent.getStringExtra("messiondetails"));
        mpay.setText(intent.getStringExtra("messionprice"));
        mtime.setText(intent.getStringExtra("messionlaunchtime"));
        mman.setText(intent.getStringExtra("messioninitiator"));
        mstatus.setText(intent.getStringExtra("messionstatus"));
        mdeadline.setText(intent.getStringExtra("messiondeadline"));

        id = intent.getStringExtra("messionid");
        Acceptor = intent.getStringExtra("messionacceptor");
        Status = intent.getStringExtra("messionstatus");
        receiver =intent.getStringExtra("messioninitiator");
        Log.i("initiator",receiver);
        if (Status.equals("已接受")) {
            mcommit.setEnabled(false);
            mchat.setEnabled(false);
            mcommit.setText("任务已被接受");

        } else {
            //点击mcommit接受任务
            mchat.setEnabled(true);
            mcommit.setEnabled(true);
            mcommit.setText("接受任务");

            /*
             *  点击谈一谈按钮进入chat界面
             */
            mchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(MessionDetailsActivity.this, ChatActivity.class);
                   // intent.putExtra("sendto",mman.getText().toString());
                    intent.putExtra("sendto",receiver);
                    startActivity(intent);
                }
            });
            mcommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Acceptor = sp.getString("id", null);
                    Log.i("Acceptor", Acceptor);
                    Status="已接受";
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("Acceptor", Acceptor);
                    params.put("Status",Status);
                    params.put("MessionID",id);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //1、网络访问
                            try {
                                result = HttpUtillConnection.getContextByHttp(acceptUrl, params);
                                Log.i("000000000000000000", result.toString().trim());

                            } catch (Exception e) {
                                e.printStackTrace();
                                result = "网络请求失败,请稍后再试。";
                            }

                            //2、UI线程显示服务器的响应结果
                            Message msg = new Message();
                            msg.what = 0x12;
                            Bundle data = new Bundle();
                            data.putString("result", result);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }

                        @SuppressLint("HandlerLeak")
                        Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 0x12) {
                                    Bundle data = msg.getData();
                                    String key = data.getString("result");//得到json返回的json
                                    if (key.startsWith("\ufeff")) {
                                        key = key.substring(1);
                                    }

                                    try {
                                        JSONObject json = new JSONObject(key);
                                        String result = (String) json.get("result");
                                        if ("fail".equals(result)) {
                                            Toast.makeText(MessionDetailsActivity.this, "接受失败", Toast.LENGTH_SHORT).show();
                                        }
                                        else if ("success".equals(result))
                                        {//接受成功

                                            Toast.makeText(MessionDetailsActivity.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                            /*暂停1.5秒后跳转到登录界面*/
                                            final Intent localIntent = new Intent(MessionDetailsActivity.this, MainActivity.class);
                                            Timer timer = new Timer();
                                            TimerTask tast = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    startActivity(localIntent);
                                                }
                                            };
                                            timer.schedule(tast, 1500);
                                        }
                                        else
                                        {
                                            Toast.makeText(MessionDetailsActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    }).start();
                }


            });


        }
    }
    protected void onResume()
    {
        super.onResume();
    }
}