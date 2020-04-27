package com.example.menudemo.ui.task;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
   *  author  shijizhe
   *  note:在任务主界面展示数据，
   *  作为任务Fragment的adapter，
   *  并定义了点击item的动作与传值。
   *  version 1.0
   *
   *
   *
   * version2.0  计划添加搜索功能
 */
public class tasklistcAdapter extends RecyclerView.Adapter<tasklistcAdapter.taskviewHolder> {

    private Context context;
    private List<Task> taskList ;
    /**
     * 属性动画
     */
    private Animator animator;

    /**
     * 需要改变颜色的text
     */
    private String text;
    /**
     * 变色数据的起始位置 position
     */
    private int beginChangePos;
    /**
     * text改变的颜色
     */
    private ForegroundColorSpan span;
    public tasklistcAdapter(Context context, List<Task> taskList){
        this.context=context;
        this.taskList=taskList;
    }

    public void setText(String text) {
        this.text = text;
    }

    //创建viewHolder
    @NonNull
    @Override
    public taskviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.task_item,parent,false);
        final taskviewHolder holder = new taskviewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                //传递数据
                Intent intent=new Intent(context,MessionDetailsActivity.class);
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


        /**如果没有进行搜索操作或者搜索之后点击了删除按钮 我们会在Fragment中把text置空并传递过来*/
    if(text!=null)
    {
        //设置span
        SpannableString string = matcherSearchText(Color.rgb(150, 20, 60), taskList.get(position).getMessionname(), text);
        holder.textView.setText(string);
    }
    else  //text为空
    {
        holder.textView.setText(taskList.get(position).getMessionname());
    }
        //属性动画
        animator = AnimatorInflater.loadAnimator(context,R.animator.anim_set);
        animator.setTarget(holder.textView);
        animator.start();

}
    /**
     * 正则匹配 返回值是一个SpannableString 即经过变色处理的数据
     */

    private SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString spannableString = new SpannableString(text);
        //条件 keyword
        Pattern pattern = Pattern.compile(keyword);
        //匹配
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            //ForegroundColorSpan 需要new 不然也只能是部分变色
            spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //返回变色处理的结果
        return spannableString;

    }




    class taskviewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public taskviewHolder(@NonNull View context) {
            super(context);
            textView=context.findViewById(R.id.ts_item1);
        }
    }


}
