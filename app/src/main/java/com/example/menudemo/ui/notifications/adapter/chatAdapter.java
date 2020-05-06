package com.example.menudemo.ui.notifications.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.menudemo.R;
import com.example.menudemo.ui.utills.chat_content;

import java.util.List;


public class chatAdapter extends  RecyclerView.Adapter<chatAdapter.MyViewHolder> {
    private List<chat_content> mMsgList;

    public chatAdapter(List<chat_content> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.chatdetails_item, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        chat_content msg = mMsgList.get(position);
        if (msg.getType() == chat_content.TYPE_RECEIVED) {
            //如果是收到的消息，显示左边布局，隐藏右边布局
            holder.llLeft.setVisibility(View.VISIBLE);
            holder.llRight.setVisibility(View.GONE);
            holder.tv_Left.setText(msg.getContent());
        } else if (msg.getType() == chat_content.TYPE_SEND) {
            //如果是发送的消息，显示右边布局，隐藏左边布局
            holder.llLeft.setVisibility(View.GONE);
            holder.llRight.setVisibility(View.VISIBLE);
            holder.tv_Right.setText(msg.getContent());
        }
    }


    @Override
    public int getItemCount() {

        return mMsgList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llLeft;
        LinearLayout llRight;
        TextView tv_Left;
        TextView tv_Right;

        public MyViewHolder(View itemView) {
            super(itemView);
            llLeft = itemView.findViewById(R.id.ll_msg_left);
            llRight = itemView.findViewById(R.id.ll_msg_right);
            tv_Left = itemView.findViewById(R.id.tv_msg_left);
            tv_Right = itemView.findViewById(R.id.tv_msg_right);

        }
    }
}