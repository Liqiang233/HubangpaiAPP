package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import java.util.Timer;
import java.util.TimerTask;
/*
*    取消任务

 */
public class MessionCancelActivity extends AppCompatActivity {
    private TextView mclass = null;
    private TextView mname = null;
    private TextView maddress = null;
    private TextView mdetails = null;
    private TextView mdeadline = null;
    private TextView mpay = null;
    private Button mcommit = null;
    private TextView mid = null;
    private TextView mtime = null;
    private TextView mman = null;
    private TextView mstatus = null;
    private Button mcomple=null;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public String result;
    private static  final String USERINFO = "userInfo";
   // public String CancelUrl = HttpUtillConnection.base_URL + "CancelTask";
    public String CancelUrl = HttpUtillConnection.gtd_URL+"CancelTask";
    public String CompleUrl = HttpUtillConnection.gtd_URL+"CompleteTaskRequest";
    String Acceptor,Status,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mession_cancel);
        sp = getSharedPreferences(USERINFO , MODE_PRIVATE);
        editor = sp.edit();
        mclass = findViewById(R.id.myac_messionclass);  //任务类型
        mname = findViewById(R.id.myac_messionnname);   //任务名称
        maddress = findViewById(R.id.myac_messionaddress); //地址
        mdetails = findViewById(R.id.myac_messionndetails);  //详细描述
        mdeadline = findViewById(R.id.myac_messionndeadline);  //截止日期
        mpay = findViewById(R.id.myac_messionpay);  //酬劳
        mtime = findViewById(R.id.myac_messionlaunchtime);  //  发布日期
        mman = findViewById(R.id.myac_messionninitiator);   //发布者
        mstatus = findViewById(R.id.myac_messionnstatus);   //状态
        mcommit = findViewById(R.id.bt_cancel);    //取消任务
        mcomple = findViewById(R.id.bt_comple);


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
        Log.i("Status",Status);
        if (Status.equals("完结申请中")) {
            mcommit.setEnabled(false);
            mcomple.setEnabled(false);
            mcomple.setText("任务正在等待完结");
        } else if(Status.equals("已完结")) {
            mcommit.setEnabled(false);
            mcomple.setEnabled(false);
            mcomple.setText("任务已完结");
            mcommit.setVisibility(View.GONE);
        }else {
            mcommit.setEnabled(true);
            mcomple.setEnabled(true);
            mcomple.setText("发起完结申请");
        }

            //点击mcommit取消任务
            mcommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Acceptor = sp.getString("id", null);
                    Status="待接受";
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("Acceptor", Acceptor);
                    params.put("Status",Status);
                    params.put("MessionID",id);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //1、网络访问
                            try {
                                result = HttpUtillConnection.getContextByHttp(CancelUrl, params);
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
                                            Toast.makeText(MessionCancelActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                                        }
                                        else if ("success".equals(result))
                                        {//接受成功

                                            Toast.makeText(MessionCancelActivity.this, "取消任务成功", Toast.LENGTH_SHORT).show();
                                            /*暂停1.5秒后跳转到登录界面*/
                                            final Intent localIntent = new Intent(MessionCancelActivity.this, MainActivity.class);
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
                                            Toast.makeText(MessionCancelActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
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

//点击mcomple完结任务申请
        mcomple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("wallet", String.valueOf(Integer.parseInt(sp.getString("wallet", null))+Integer.parseInt(intent.getStringExtra("messionprice"))));
                //Toast.makeText(MessionCancelActivity.this,String.valueOf(Integer.parseInt(sp.getString("wallet", null))+Integer.parseInt(intent.getStringExtra("messionprice"))),Toast.LENGTH_SHORT).show();
                editor.commit();
                Acceptor = sp.getString("id", null);
                Status="完结申请中";
                final Map<String, String> params = new HashMap<String, String>();
                params.put("Status",Status);
                params.put("MessionID",id);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //1、网络访问
                        try {
                            result = HttpUtillConnection.getContextByHttp(CompleUrl, params);
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
                                        Toast.makeText(MessionCancelActivity.this, "发起失败", Toast.LENGTH_SHORT).show();
                                    }
                                    else if ("success".equals(result))
                                    {//接受成功

                                        Toast.makeText(MessionCancelActivity.this, "发起完结任务申请成功", Toast.LENGTH_SHORT).show();
                                        /*暂停1.5秒后跳转到登录界面*/
                                        final Intent localIntent = new Intent(MessionCancelActivity.this, MainActivity.class);
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
                                        Toast.makeText(MessionCancelActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
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
