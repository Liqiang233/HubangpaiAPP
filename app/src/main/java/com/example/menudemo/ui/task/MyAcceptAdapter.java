package com.example.menudemo.ui.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menudemo.R;
import com.example.menudemo.ui.utills.Task;

import java.util.List;

public class MyAcceptAdapter extends RecyclerView.Adapter<MyAcceptAdapter.taskviewHolder> {

    private Context context;
    private List<Task> taskList ;

    public MyAcceptAdapter(Context context, List<Task> taskList){
        this.context=context;
        this.taskList=taskList;
    }

    //创建viewHolder
    @NonNull
    @Override
    public taskviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.mypub_item,parent,false);
        final taskviewHolder holder = new taskviewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //传递数据
                Intent intent=new Intent(context,MessionCancelActivity.class);
                intent.putExtra("messionname",taskList.get(position).getMessionname());
                intent.putExtra("messionid",taskList.get(position).getMessionid());
                intent.putExtra("messiontype",taskList.get(position).getMessiontype());
                intent.putExtra("messioninitiator",taskList.get(position).getInitiator());
                intent.putExtra("messionacceptor",taskList.get(position).getAcceptor());
                intent.putExtra("messionaddress",taskList.get(position).getAddress());
                intent.putExtra("messionlaunchtime",taskList.get(position).getLaunchtime());
                intent.putExtra("messiondeadline",taskList.get(position).getDeadline());
                intent.putExtra("messiondetails",taskList.get(position).getDetails());
                intent.putExtra("messionprice",taskList.get(position).getPrice());
                intent.putExtra("messionstatus",taskList.get(position).getStatus());
                context.startActivity(intent);
            }
        });
        return holder;
    }
    @Override //设置item数
    public int getItemCount()
    {
        return taskList.size();

    }
   //绑定数据
    @Override
    public void onBindViewHolder(@NonNull taskviewHolder holder, int position) {
           //根据点击位置绑定数据

                holder.textView.setText(taskList.get(position).getMessionname());

    }

    class taskviewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public taskviewHolder(@NonNull View context) {
            super(context);
            textView=context.findViewById(R.id.ts_item2);
        }
    }


}
