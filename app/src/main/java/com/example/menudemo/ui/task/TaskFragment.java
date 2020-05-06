package com.example.menudemo.ui.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

/*
    * author shijizhe
    * note: 发送http请求
    * 接收eclipse传回来的数据库数据
    * 并将数据传到对应的Adapter
    *
 */
public class TaskFragment extends Fragment{

    //String url = HttpUtillConnection.base_URL+"ReturnTask";
    String url = HttpUtillConnection.Ya_URL+"ReturnTask";
    private EditText task_et_search;
    /**
     * 删除按钮
     */
    private ImageView mImgvDelete;
    private View view;  //定义view用来设置fragment的layout
    public RecyclerView recy;   //定义RecyclerView
    private tasklistcAdapter taskAdapter;  //定义自己创建的Adapter
    public List<Task> taskList=new ArrayList<>();   //定义全部实体类
    public List<Task> list=new ArrayList<>();   ////定义符合搜索条件的数据
    private SharedPreferences sp;
    String Initiator=null;
//    public RecyclerView layout;
//    public MotionEvent mv;


    @NonNull
    @Override
    public View onCreateView( LayoutInflater inflater,
                              @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task,container,false);
        setHasOptionsMenu(true);//启用menu
        sp = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        task_et_search = view.findViewById(R.id.task_et_sertext);
        mImgvDelete = view.findViewById(R.id.imgv_delete);
        recy=view.findViewById(R.id.task_rec);


         initData();
         refreshUI();
         setListener();

        return view;



    }
    //初始化数据
public   void initData()
{
    new Thread(new Runnable() {
        @Override
        public void run() {
            //    String url = HttpUtillConnection.BASE_URL+"/servlet/LoginServlet";
            Initiator = sp.getString("id", null);
            final Map<String, String> params = new HashMap<String, String>();
          //  String result = HttpUtillConnection.getContextByHttp(url);
            params.put("Initiator", Initiator);
            params.put("Status", "待接受");
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
                            //创建adapter
                            Log.i("zhutaskList:",taskList.toString());

                            //初始化adapter
                            taskAdapter = new tasklistcAdapter(getActivity(),list);
                            //set adapter
                            recy.setAdapter(taskAdapter);
                            //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
                            //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
                            recy.setLayoutManager(new LinearLayoutManager(TaskFragment.this.getActivity()));
                            //初次进入程序时 展示全部数据

                        }

                        list.addAll(taskList);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }).start();
}


  //设置搜索框监听
    private  void setListener()
    {

         task_et_search.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }
             //每次edittext内容改变时执行 控制删除按钮的显示隐藏
             @Override
             public void afterTextChanged(Editable editable) {
                 if (editable.length() == 0) {
                     mImgvDelete.setVisibility(View.GONE);
                 } else {
                     mImgvDelete.setVisibility(View.VISIBLE);
                 }
                 //匹配文字 变色
                 doChangeColor(editable.toString().trim());
             }
         });
        //删除按钮的监听
        mImgvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               task_et_search.setText("");
            }
        });

    }

    /**
     * 字体匹配方法
     * text:需要变色的字
     */
    private void doChangeColor(String text) {
           list.clear();
        //不需要匹配 把所有数据都传进来 不需要变色
           if(text.equals(""))
           {
               list.addAll(taskList);
               //防止匹配过文字之后点击删除按钮 字体仍然变色的问题
               taskAdapter.setText(null);
               refreshUI();
           }
           else //如果edittext里面有数据 则根据edittext里面的数据进行匹配
           // 用contains判断是否包含该条数据 包含的话则加入到list中
           {

               for(int i=0;i<taskList.size();i++)
               {
                   String str=taskList.get(i).getMessionname();
                   if(str.contains(text))
                   {
                       list.add(taskList.get(i));
                   }
               }

           }
           taskAdapter.setText(text);
           refreshUI();
    }
    /**
     * 刷新UI
     */
    private void refreshUI() {
        if (taskAdapter == null) {
            //初始化adapter
            taskAdapter = new tasklistcAdapter(getActivity(),list);
            //set adapter
            recy.setAdapter(taskAdapter);
            //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
            //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
            recy.setLayoutManager(new LinearLayoutManager(TaskFragment.this.getActivity()));
            //初次进入程序时 展示全部数据

        } else {
            taskAdapter.notifyDataSetChanged();
        }
    }


    //展示actionbar的菜单按钮
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_task_menu,menu);
    }
     //给菜单按钮定义点击动作
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.task_add_item:
                intent = new Intent(getActivity(),PublishActivity.class);
                startActivity(intent);
                return true;
            case R.id.option_normal_2:
                intent = new Intent(getActivity(),MyPublishActivity.class);
                startActivity(intent);
                return true;
            case R.id.option_normal_3:
                intent = new Intent(getActivity(),MyAcceptActivity.class);
                startActivity(intent);
                return true;
            case R.id.option_normal_4:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

