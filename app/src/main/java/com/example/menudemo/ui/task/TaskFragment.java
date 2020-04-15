package com.example.menudemo.ui.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.home.LoginActivity;
import com.example.menudemo.ui.utills.HttpUtillConnection;
import com.example.menudemo.ui.utills.Task;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskFragment extends Fragment{

    private View view;  //定义view用来设置fragment的layout
    private RecyclerView recy;   //定义RecyclerView
    private tasklistcAdapter taskAdapter;  //定义自己创建的Adapter
    public List<Task> taskList=new ArrayList<>();

    //String []arr={"帮忙买辣条","帮养宠物","辅导孩子写作业","陪护老人两天" ,"帮忙买辣条","帮忙打扫家务","帮养宠物","辅导孩子写作业","陪护老人两天","帮忙买辣条","帮忙打扫家务","帮养宠物","辅导孩子写作业","陪护老人两天"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//启用menu
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
                String url = HttpUtillConnection.Ya_URL+"ReturnTask";
                String result = HttpUtillConnection.getContextByHttp(url);
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
                        String key = data.getString("result");//得到json返回的json
                        if (key != null && key.startsWith("\ufeff")) {
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
                                Task task1 = new Task(messionid,messionname,messiontype,initiator,acceptor,price,address,deadline,launchtime,status,details);

                                taskList.add(task1);
                                Log.i("messionid:",messionid);
                                Log.i("idezhi:",""+i);
                                Log.i("taskList:",taskList.toString());
                                Log.i("task:",task1.toString());


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

        }).start();

        //获取RecyclerView
        recy=(RecyclerView)view.findViewById(R.id.task_rec);
        //创建adapter
        taskAdapter = new tasklistcAdapter(getActivity(),taskList);
        //set adapter
        recy.setAdapter(taskAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        recy.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        Log.i("zhutaskList:",taskList.toString());

        return view;
    }




    //展示actionbar的菜单按钮
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_task_menu,menu);
    }
     //给菜单按钮定义点击动作
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.task_add_item:
                Intent intent = new Intent(getActivity(),PublishActivity.class);
                startActivity(intent);
                return true;
            case R.id.option_normal_2:
                return true;
            case R.id.option_normal_3:
                return true;
            case R.id.option_normal_4:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}