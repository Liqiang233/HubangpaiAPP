package com.example.menudemo.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menudemo.R;

import java.util.List;


public class TaskFragment extends Fragment{

    private View view;
    private RecyclerView recy;
    private List<String> list;
    String []arr={"帮忙买辣条","帮养宠物","辅导孩子写作业","陪护老人两天","帮忙买辣条","帮忙打扫家务","帮养宠物","辅导孩子写作业","陪护老人两天","帮忙买辣条","帮忙打扫家务","帮养宠物","辅导孩子写作业","陪护老人两天"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_task, container, false);
        recy=root.findViewById(R.id.task_rec);
        recy.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recy.setAdapter(new tasklistcAdapter(this.getActivity(),arr ));


        return root;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//启用menu
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