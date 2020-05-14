package com.example.menudemo.ui.notifications.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.menudemo.R;
import com.example.menudemo.ui.notifications.adapter.chatAdapter;
import com.example.menudemo.ui.utills.chat_content;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import java.util.ArrayList;
import java.util.List;

import static com.hyphenate.chat.EMMessage.Direct.RECEIVE;
import static com.hyphenate.chat.EMMessage.Direct.SEND;

public class ChatActivity extends AppCompatActivity implements EMMessageListener
{
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    // 发送按钮
    private  Button sendMsgButton;
    // 聊天信息输入框
    private EditText ed_msg_input;



    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String sendto;
    // 当前会话对象
    private EMConversation mConversation;
    private RecyclerView mRecyclerView;
    private List mList = new ArrayList();
    private chatAdapter adapter;


    private SharedPreferences sp;
    private static  final String USERINFO = "userInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        verifyStoragePermissions(this);
        Intent intent = getIntent();
        sendto = intent.getStringExtra("sendto");
        mMessageListener = this;

        initConversation();
        initView();
        refreshUI();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(sendto);





     /*   sp = getSharedPreferences(USERINFO, MODE_PRIVATE);
       String username = sp.getString("id", null);
       String  password = "123456";
      */


    }
    //用于返回上一页
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return super.onSupportNavigateUp();
    }
        @Override
        protected void onResume () {
            super.onResume();
            // 添加消息监听
            EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
        }
         @Override
        protected void onStop()
        {
            super.onStop();
            // 移除消息监听
            EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);

        }
        @Override
        protected void onDestroy () {
            super.onDestroy();
        }


        /**
         * 初始化会话对象，并且根据需要加载更多消息
         */
        private void initConversation () {

            mConversation =  EMClient.getInstance().chatManager().getConversation(sendto, EMConversation.EMConversationType.Chat, true);
                    //EMClient.getInstance().chatManager().getConversation(sendto, null, true);
            // 设置当前会话未读数为 0
            mConversation.markAllMessagesAsRead();
            List<EMMessage> msgs = mConversation.getAllMessages();
            for(int i=0;i<msgs.size();i++)
            {
                EMMessage emmessage=msgs.get(i);

                if( emmessage.direct().equals(RECEIVE))
                {
                    String result = msgs.get(i).getBody().toString();
                    String msgReceived = result.substring(5, result.length() - 1);

                    final chat_content msg = new chat_content(msgReceived, chat_content.TYPE_RECEIVED);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mList.add(msg);
                        }
                    });
                }
                if(emmessage.direct().equals(SEND))
                {
                    String result = msgs.get(i).getBody().toString();
                    String msgReceived = result.substring(5, result.length() - 1);

                    final chat_content msg = new chat_content(msgReceived, chat_content.TYPE_SEND);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mList.add(msg);
                        }
                    });
                }
            }
        }
            private void refreshUI ()
        {
            if (adapter == null) {
                //初始化adapter
                adapter = new chatAdapter((List<chat_content>) mList);

                //set adapter
                mRecyclerView.setAdapter(adapter);
                //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
                //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                //初次进入程序时 展示全部数据

            } else {
                adapter.notifyDataSetChanged();
            }
        }
            private void initView ()
            {
                sendMsgButton = (Button) findViewById(R.id.send);
                ed_msg_input = (EditText) findViewById(R.id.sendMsg);
                mRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(layoutManager);



                // 设置发送按钮的点击事件
                sendMsgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = ed_msg_input.getText().toString().trim();
                        if (!TextUtils.isEmpty(content)) {
                            //环信部分的发送消息
                            chat_content msg = new chat_content(content, chat_content.TYPE_SEND);
                            mList.add(msg);
                            refreshUI();


                            //当有新消息时，刷新RecyclerView中的显示
                            adapter.notifyItemInserted(mList.size() - 1);
                            //使用RecyclerView加载新聊天页面
                            mRecyclerView.scrollToPosition(mList.size() - 1);
                            ed_msg_input.setText("");


                            EMMessage message = EMMessage.createTxtSendMessage(content, sendto);
                            mConversation.appendMessage(message);
                            EMClient.getInstance().chatManager().saveMessage(message);
                            EMClient.getInstance().chatManager().sendMessage(message);
                            message.setMessageStatusCallback(new EMCallBack() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });

                        }
                    }
                });
            }

    /**
     * --------------------------------- Message Listener -------------------------------------
     * 环信消息监听主要方法
     */
            @Override
            public void onMessageReceived (List < EMMessage > list) {
                //收到消息
                String result = list.get(0).getBody().toString();
                String msgReceived = result.substring(5, result.length() - 1);
                final chat_content msg = new chat_content(msgReceived, chat_content.TYPE_RECEIVED);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mList.add(msg);
                refreshUI();
                mRecyclerView.scrollToPosition(mList.size() - 1);
            }
        });

            }

            @Override
            public void onCmdMessageReceived (List < EMMessage > list) {
                for (int i = 0; i < list.size(); i++) {
                    // 透传消息
                    EMMessage cmdMessage = list.get(i);
                    EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                    Log.i("lzan13", "收到 CMD 透传消息" + body.action());
                }

        }

            @Override
            public void onMessageRead (List < EMMessage > list) {

        }

            @Override
            public void onMessageDelivered (List < EMMessage > list) {

        }

            @Override
            public void onMessageRecalled (List < EMMessage > list) {

        }

            @Override
            public void onMessageChanged (EMMessage emMessage, Object o){

        }


    /**
     * 验证读取sd卡的权限
     *
     * @param activity
     */
    public boolean verifyStoragePermissions(Activity activity) {
/*******below android 6.0*******/

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }



    }

    }
