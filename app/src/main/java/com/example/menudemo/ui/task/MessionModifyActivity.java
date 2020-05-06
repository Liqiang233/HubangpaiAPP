package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MessionModifyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    //String url = HttpUtillConnection.BASE_URL_ModifyTask;  @Tenda
    String url = HttpUtillConnection.Ya_URL+"ModifyTask";   //@Ya
    public String CompleUrl = HttpUtillConnection.Ya_URL+"CompleteTask";
    // String url=                  //@liqiang



    private Spinner mclass=null;
    private EditText mname=null;
    private EditText maddress=null;
    private EditText mdetail=null;
    private EditText mdeadline=null;
    private EditText mpay=null;
    private Button mcommit=null;
    private Context context;
    private String MessionName;
    private String MessionType;
    private String Deadline;
    private String Address;
    private String Price;
    private String Details;
    private String id;
    private Button mcomple;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mession_modify);
        context = getApplicationContext();
        mclass=findViewById(R.id.md_messionclass);
        mname=findViewById(R.id.md_messionname);
        mdeadline=findViewById(R.id.md_messiondeadline);
        maddress=findViewById(R.id.md_messionaddress);
        mcommit=findViewById(R.id.md_modify);
        mpay=findViewById(R.id.md_messionpay);
        mdetail=findViewById(R.id.md_messiondetails);
        mcomple=findViewById(R.id.md_comple);

        //界面间传递数据
        Intent intent = getIntent();
        mname.setText(intent.getStringExtra("messionname"));
        switch(intent.getStringExtra("messionname")){
            case "跑腿":
                mclass.setSelection(0);
                break;
            case "家政":
                mclass.setSelection(1);
                break;
            case "寄养":
                mclass.setSelection(2);
                break;
            case "辅导":
                mclass.setSelection(3);
                break;
            case "陪护":
                mclass.setSelection(4);
                break;
        }
        maddress.setText(intent.getStringExtra("messionaddress"));
        mdetail.setText(intent.getStringExtra("messiondetails"));
        mpay.setText(intent.getStringExtra("messionprice"));
        mdeadline.setText(intent.getStringExtra("messiondeadline"));
        status=intent.getStringExtra("messionstatus");
        id = intent.getStringExtra("messionid");
        if (status.equals("完结申请中")) {
            mpay.setEnabled(false);
            mcommit.setEnabled(false);
            mcomple.setEnabled(true);
            mcomple.setText("完结任务");
        } else if(status.equals("已完结")){
            mclass.setEnabled(false);
            mname.setEnabled(false);
            mdeadline.setEnabled(false);
            maddress.setEnabled(false);
            mcommit.setEnabled(false);
            mpay.setEnabled(false);
            mdetail.setEnabled(false);
            mcomple.setEnabled(false);
            mcommit.setVisibility(View.GONE);
            mcomple.setText("任务已完结");
        }else if(status.equals("已接受")){
            mpay.setEnabled(false);
            mcommit.setEnabled(true);
            mcomple.setEnabled(false);
        }else{
            mcommit.setEnabled(true);
            mcomple.setEnabled(false);
            mcomple.setText("任务尚未完成");
        }
        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessionName = mname.getText().toString();
                MessionType = mclass.getSelectedItem().toString();
                Deadline = mdeadline.getText().toString();
                Address = maddress.getText().toString();
                Price = mpay.getText().toString();
                Details = mdetail.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        Map<String, String> params = new HashMap<String, String>();
                        params.put("MesionID", id);
                        params.put("MessionName", MessionName);
                        params.put("MessionType", MessionType);
                        params.put("Price", Price);
                        params.put("Address", Address);
                        params.put("Deadline", Deadline);
                        params.put("Details", Details);
                        String result = HttpUtillConnection.getContextByHttp(url, params);
                        Log.i("===========", result.toString());
                        Message msg = new Message();
                        msg.what = 0x12;
                        Bundle data = new Bundle();
                        data.putString("result", result);
                        Log.i("*********************", result.toString());
                        msg.setData(data);
                        hander.sendMessage(msg);

                    }

                    @SuppressLint("HandlerLeak")
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
                                        //页面跳转
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        intent.putExtras(bundle);
                                        intent.setClass(context, MainActivity.class);//跳转到app主界面
                                        startActivity(intent);
                                        Toast.makeText(MessionModifyActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                                    } else if ("fail".equals(result)) {
                                        Toast.makeText(MessionModifyActivity.this, "修改失败", Toast.LENGTH_SHORT).show();

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
        mcomple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("MessionID", id);
                        params.put("Status","已完结");
                        String result = HttpUtillConnection.getContextByHttp(CompleUrl, params);
                        Log.i("===========", result.toString());
                        Message msg = new Message();
                        msg.what = 0x12;
                        Bundle data = new Bundle();
                        data.putString("result", result);
                        Log.i("*********************", result.toString());
                        msg.setData(data);
                        hander.sendMessage(msg);

                    }

                    @SuppressLint("HandlerLeak")
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
                                        //页面跳转
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        intent.putExtras(bundle);
                                        intent.setClass(context, MainActivity.class);//跳转到app主界面
                                        startActivity(intent);
                                        Toast.makeText(MessionModifyActivity.this, "完结成功", Toast.LENGTH_SHORT).show();

                                    } else if ("fail".equals(result)) {
                                        Toast.makeText(MessionModifyActivity.this, "完结失败", Toast.LENGTH_SHORT).show();

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

        //String []arr={"跑腿","家政","寄养","辅导","陪护"};
        //创建ArrayAdapter对象
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_publish,arr);

        //设置adapter
        //mclass.setAdapter(adapter);
        //设置选中事件监听器
        //mclass.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //获得选中的任务类
        String content = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
