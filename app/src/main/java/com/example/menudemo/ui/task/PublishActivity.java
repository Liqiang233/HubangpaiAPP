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
import com.example.menudemo.ui.datepicker.PickerView;
import com.example.menudemo.ui.home.LoginActivity;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PublishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{



    //  String url = HttpUtillConnection.BASE_URL_Task_Ya;
    String url = HttpUtillConnection.gtd_URL+"PublishTask";

    private Spinner mclass=null;
    private EditText mname=null;
    private EditText maddress=null;
    private EditText mdetail=null;
    //private TextView mdeadline=null;
    private EditText mpay=null;
    private Button mcommit=null;
    private SharedPreferences sp;
    private Context context;
    private String MessionName;
    private String MessionType;
    private String Deadline;
    private String Address;
    private String Price;
    private String Initiator;
    private String LaunchTime;
    private String Details;
    private String Status;
    private TextView mTvSelectedTime;
    private CustomDatePicker mTimerPicker;
    private static  final String USERINFO = "userInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        getSupportActionBar().setTitle("发布任务");

        context = getApplicationContext();
        sp = getSharedPreferences(USERINFO , MODE_PRIVATE);
        mclass=findViewById(R.id.sp_messionclass);
        mname=findViewById(R.id.tv_messionname);
        findViewById(R.id.ll_time).setOnClickListener(this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        initTimerPicker();
        maddress=findViewById(R.id.tv_messionaddress);
        mpay=findViewById(R.id.tv_messionpay);
        mcommit=findViewById(R.id.bt_publish);
        mdetail=findViewById(R.id.tv_messiondetails);
        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessionName = mname.getText().toString();
                MessionType = mclass.getSelectedItem().toString();
                Deadline = mTvSelectedTime.getText().toString();
                Address = maddress.getText().toString();
                Price = mpay.getText().toString();
                Initiator = sp.getString("id", null);
                Details = mdetail.getText().toString();
                Status = "待接受";
                if(mname.getText().toString().trim().equals("")||mclass.getSelectedItem().toString().trim().equals("")||
                        mTvSelectedTime.getText().toString().trim().equals("")||maddress.getText().toString().trim().equals("")||
                        mpay.getText().toString().trim().equals("")){
                    Toast.makeText(PublishActivity.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /*获取当前系统时间*/
                            long time = System.currentTimeMillis();

                            /*时间戳转换成IOS8601字符串*/
                            Date date = new Date(time);
                            TimeZone tz = TimeZone.getTimeZone("Asia/Beijing");
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            df.setTimeZone(tz);
                            String nowAsIOS = df.format(date);
                            LaunchTime = nowAsIOS;


                            Map<String, String> params = new HashMap<String, String>();
                            params.put("MessionName", MessionName);
                            params.put("MessionType", MessionType);
                            params.put("Initiator", Initiator);
                            params.put("Price", Price);
                            params.put("Address", Address);
                            params.put("Deadline", Deadline);
                            params.put("LaunchTime", LaunchTime);
                            params.put("Status", Status);
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
                                            bundle.putString("USERID", sp.getString("id", null));
                                            intent.putExtras(bundle);
                                            intent.setClass(context, MainActivity.class);//跳转到app主界面
                                            startActivity(intent);
                                            Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
//                                            intent.setClass(context, MainActivity.class);//跳转到app主界面
//                                            startActivity(intent);
//                                            finish();
                                        } else if ("fail".equals(result)) {
                                            Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();

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

        // String []arr={"跑腿","家政","寄养","辅导","陪护"};
        //创建ArrayAdapter对象
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;
        }
    }
    private void initTimerPicker() {
        //String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        //String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        //获取当前日期
        Date date = new Date();
        //创建Calendar实例
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date);   //设置截至时间
        cal2.setTime(date);
        //增加一个月的方法：
        cal1.add(Calendar.MONTH, 1);
        cal2.add(Calendar.DAY_OF_MONTH, 1);
        //将时间格式化成yyyy-MM-dd HH:mm:ss的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String beginTime =format.format(cal2.getTime());
        String endTime = format.format(cal1.getTime());
        mTvSelectedTime.setText(beginTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(PublishActivity.this, timestamp -> mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true)), beginTime, endTime);
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
