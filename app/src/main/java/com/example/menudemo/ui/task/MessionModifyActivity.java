package com.example.menudemo.ui.task;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.datepicker.CustomDatePicker;
import com.example.menudemo.ui.datepicker.DateFormatUtils;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MessionModifyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    //String url = HttpUtillConnection.BASE_URL_ModifyTask;  @Tenda
    String url = HttpUtillConnection.gtd_URL+"ModifyTask";   //@gtd
    public String CompleUrl = HttpUtillConnection.gtd_URL+"CompleteTask";
    // String url=                  //@liqiang


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Spinner mclass=null;
    private EditText mname=null;
    private EditText maddress=null;
    private EditText mdetail=null;
    //private EditText mdeadline=null;
    private EditText mpay=null;
    private Button mcommit=null;
    private Context context;
    private String MessionName;
    private String MessionType;
    private String Deadline;
    private String Address;
    private String Price;
    private String Details;
    private String mdeadline=null;
    private String id;
    private Button mcomple;
    private String status;
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    private  String prepay;
    private static  final String USERINFO = "userInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mession_modify);
        context = getApplicationContext();
        mclass=findViewById(R.id.md_messionclass);
        mname=findViewById(R.id.md_messionname);
       // mdeadline=findViewById(R.id.md_messiondeadline);
        maddress=findViewById(R.id.md_messionaddress);
        mcommit=findViewById(R.id.md_modify);
        mpay=findViewById(R.id.md_messionpay);
        mdetail=findViewById(R.id.md_messiondetails);
        mcomple=findViewById(R.id.md_comple);
        mTvSelectedTime = findViewById(R.id.tv2_selected_time);
        sp = getSharedPreferences(USERINFO , MODE_PRIVATE);
        editor = sp.edit();
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
        prepay=intent.getStringExtra("messionprice");
        mdeadline=intent.getStringExtra("messiondeadline");
        status=intent.getStringExtra("messionstatus");
        id = intent.getStringExtra("messionid");
        initTimerPicker();
        mTvSelectedTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mTimerPicker.show(mTvSelectedTime.getText().toString());
            }
        });
        if (status.equals("完结申请中")) {
            mpay.setEnabled(false);
            mcommit.setEnabled(false);
            mcomple.setEnabled(true);
            mcomple.setText("完结任务");
        } else if(status.equals("已完结")){
            mclass.setEnabled(false);
            mname.setEnabled(false);
            mTvSelectedTime.setEnabled(false);
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
                Deadline = mTvSelectedTime.getText().toString();
                Address = maddress.getText().toString();
                Price = mpay.getText().toString();
                Details = mdetail.getText().toString();
                editor.putString("wallet", String.valueOf(Integer.parseInt(sp.getString("wallet", null))+Integer.parseInt(prepay)-Integer.parseInt(Price)));
                editor.commit();
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        Map<String, String> params = new HashMap<String, String>();
                        params.put("MessionID", id);
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
                        params.put("UserID", sp.getString("id", null));
                        params.put("MessionID", id);
                        params.put("Price", intent.getStringExtra("messionprice"));
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

    private void initTimerPicker() {
        //获取当前日期
        Date date = new Date();
        //创建Calendar实例
        Calendar cal1 = Calendar.getInstance();

        cal1.setTime(date);   //设置截至时间

        //增加一个月的方法：
        cal1.add(Calendar.MONTH, 1);
        //将时间格式化成yyyy-MM-dd HH:mm:ss的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String endTime = format.format(cal1.getTime());

        String beginTime = mdeadline;
        mTvSelectedTime.setText(beginTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(MessionModifyActivity.this, timestamp -> mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true)), beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
}
