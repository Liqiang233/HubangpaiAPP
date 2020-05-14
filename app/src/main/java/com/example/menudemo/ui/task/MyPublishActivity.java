package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.menudemo.R;
import com.example.menudemo.ui.utills.HttpUtillConnection;
import com.example.menudemo.ui.utills.Task;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MyPublishActivity extends AppCompatActivity {

 //   String url = HttpUtillConnection.base_URL+"SearchTaskByUSERID";
 String url = HttpUtillConnection.Ya_URL+"SearchTaskByUSERID";

    private View view;  //定义view用来设置fragment的layout
    public RecyclerView recy;   //定义RecyclerView
    private MyPublishAdapter taskAdapter;  //定义自己创建的Adapter
    public List<Task> taskList=new ArrayList<>();   //定义实体类
    private SharedPreferences sp;
    private static  final String USERINFO = "userInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);
        sp = getSharedPreferences(USERINFO , MODE_PRIVATE);
        getSupportActionBar().setTitle("发布的任务");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";

                Map<String, String> params = new HashMap<String, String>();
                String USERID = sp.getString("id", null);
                params.put("USERID", USERID);
                String result = HttpUtillConnection.getContextByHttp(url,params);
                Log.i("searchresult:", result);
                Message msg = new Message();
                msg.what = 0x12;
                Bundle data = new Bundle();   //bundle 主要用于app内传递数据，以键值对的形势存在
                data.putString("result", result);
                msg.setData(data);
                hander.sendMessage(msg);

            }

            @SuppressLint("HandlerLeak")
            Handler hander = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 0x12) {
                        Bundle data = msg.getData();
                        String key = data.getString("result");//得到返回的json
                        if (key != null && key.startsWith("\ufeff")) //去掉bom头
                        {
                            key = key.substring(1);
                        }
                        try {
                            JSONArray js = JSONArray.fromObject(key);
                            int i;
                            for(i=0;i<js.size();i++)
                            {
                                String messionid = js.getJSONObject(i).getString("messionid");
                                String messionname =js.getJSONObject(i).getString("messionname");
                                String messiontype = js.getJSONObject(i).getString("messiontype");
                                String initiator= js.getJSONObject(i).getString("initiator");
                                String price = js.getJSONObject(i).getString("price");
                                String address = js.getJSONObject(i).getString("address");
                                String deadline = js.getJSONObject(i).getString("deadline");
                                String launchtime = js.getJSONObject(i).getString("launchtime");
                                String status=js.getJSONObject(i).getString("status");
                                String details=js.getJSONObject(i).getString("details");
                                String acceptor;
                                if(js.getJSONObject(i).has("acceptor"))
                                {
                                    acceptor = js.getJSONObject(i).getString("acceptor");

                                }else
                                {
                                    acceptor= "";
                                }
                                Task task = new Task(messionid,messionname,messiontype,initiator,acceptor,price,address,deadline,launchtime,status,details);

                                taskList.add(task);

                                Log.i("messionid:",messionid);
                                Log.i("taskList:",taskList.toString());
                                //获取RecyclerView
                                recy=findViewById(R.id.mypublishtask_rec);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(MyPublishActivity.this);
                                recy.setLayoutManager(layoutManager);
                                //创建adapter
                                Log.i("mytaskList:",taskList.toString());
                                taskAdapter = new MyPublishAdapter(MyPublishActivity.this,taskList);
                                //set adapter
                                recy.setAdapter(taskAdapter);
                                //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
                                //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
                                //recy.setLayoutManager(new LinearLayoutManager(MyPublishActivity.this));



                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

        }).start();



    }
}
