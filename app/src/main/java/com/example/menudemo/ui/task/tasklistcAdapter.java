package com.example.menudemo.ui.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menudemo.R;
import com.example.menudemo.ui.utills.Task;

import java.util.ArrayList;
import java.util.List;

public class tasklistcAdapter extends RecyclerView.Adapter<tasklistcAdapter.taskviewHolder> {

    private Context context;
    private List<Task> taskList ;

    public tasklistcAdapter(Context context, List<Task> taskList){
        this.context=context;
        this.taskList=taskList;
    }

    //创建viewHolder
    @NonNull
    @Override
    public taskviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new taskviewHolder(LayoutInflater.from(context).inflate(R.layout.task_item,parent,false));
    }
    @Override
    public int getItemCount()
    {
        return taskList.size();

    } //设置item数
   //绑定数据
    @Override
    public void onBindViewHolder(@NonNull taskviewHolder holder, int position) {
           //根据点击位置绑定数据
   Log.i("adataskList:",""+taskList.size());
                holder.textView.setText(taskList.get(position).getMessionname());
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                    public void onClick(View view,int position) {

                    Intent intent=new Intent(context,MessionDetailsActivity.class);
                    intent.putExtra("meissionid",taskList.get(position).getMessionid());
                    context.startActivity(intent);

                    }
                });
    }




    class taskviewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public taskviewHolder(@NonNull View context) {
            super(context);
            textView=context.findViewById(R.id.ts_item1);
        }
    }
    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener
    {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, Task data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}
