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

import java.util.List;

public class tasklistcAdapter extends RecyclerView.Adapter<tasklistcAdapter.taskviewHolder> {

    private Context context;
    private List<String> lists;
    String [] list;

    public tasklistcAdapter(Context context,String [] list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public taskviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new taskviewHolder(LayoutInflater.from(context).inflate(R.layout.task_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull taskviewHolder holder, int position) {
                holder.textView.setText("帮忙打扫家务");
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,MessionDetailsActivity.class);
                        context.startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return 15;
    }
    class taskviewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public taskviewHolder(@NonNull View context) {
            super(context);
            textView=context.findViewById(R.id.ts_item1);
        }
    }
}
