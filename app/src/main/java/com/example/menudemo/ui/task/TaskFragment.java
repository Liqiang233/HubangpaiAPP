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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskFragment extends Fragment{

    private View view;  //定义view用来设置fragment的layout
    public RecyclerView recy;   //定义RecyclerView
    private tasklistcAdapter taskAdapter;  //定义自己创建的Adapter
    public List<Task> taskList=new ArrayList<>();   //定义实体类


    @NonNull
    @Override
    public View onCreateView( LayoutInflater inflater,
                              @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task,container,false);
        setHasOptionsMenu(true);//启用menu
        initRecyclerView();
        initData();
        return view;



    }
public   void initData()
{
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



                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }).start();
}
public void initRecyclerView()
    {
        //获取RecyclerView
        recy=view.findViewById(R.id.task_rec);
        //创建adapter
        Log.i("zhutaskList:",taskList.toString());
        taskAdapter = new tasklistcAdapter(getActivity(),taskList);
        //set adapter
        recy.setAdapter(taskAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        recy.setLayoutManager(new LinearLayoutManager(this.getActivity()));

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