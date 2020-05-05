package com.example.menudemo.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.menudemo.R;
import com.example.menudemo.ui.notifications.activity.ChatActivity;
import com.example.menudemo.ui.notifications.widget.EaseConversationList;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends  Fragment implements EMConversationListener {

    protected View view;  //定义view用来设置fragment的layout
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    private final static int MSG_REFRESH = 2;
    private EaseConversationListItemClickListener listItemClickListener;


protected  void initView()
{
    conversationListView = view.findViewById(R.id.listtttttt);
}

    protected  void  setUpView()
{
    EMClient.getInstance().addConnectionListener(connectionListener);
    conversationList.addAll(loadConversationList());
    conversationListView.init(conversationList);

    if(listItemClickListener != null){
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                listItemClickListener.onListItemClicked(conversation);
            }
        });
    }
    registerForContextMenu(conversationListView);
    conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    EMConversation conversation = conversationListView.getItem(position);
                    String username = conversation.conversationId();
                    if (username.equals(EMClient.getInstance().getCurrentUser()))
                        Toast.makeText(getActivity(),"不能跟自己聊天", Toast.LENGTH_SHORT).show();
                    else {
                        // start chat acitivity
                        Intent intent = new Intent(getActivity(), ChatActivity.class);

                        // it's single chat
                        intent.putExtra("sendto", username);
                        startActivity(intent);

                    }
        }
    });
}
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_notifications, container, false);
        initView();
        setUpView();

        return view;
    }



    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:

                    break;

                case MSG_REFRESH:
                {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * load conversation list
     *
     * @return
    +    */
    protected List<EMConversation> loadConversationList()
    {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }
    /**
     * 根据最后一条消息的时间排序消息
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
    @Override
    public void onResume() {
        super.onResume();
        refresh();

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }
    /**
     * refresh ui
     */
    public void refresh() {


        if(!handler.hasMessages(MSG_REFRESH)){
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {

            } else {
                handler.sendEmptyMessage(0);
            }

        }

        @Override
        public void onConnected() {

            handler.sendEmptyMessage(1);

        }
    };
    /**
     * connected to server
     */
    protected void onConnectionConnected(){

    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected(){

    }

    @Override
    public void onCoversationUpdate() {
        refresh();
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }
    /*

    *  note 右键删除回话功能
    *  有机会再做
    *  time 2020年5月3日18:49:40
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }
     @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
    	if (tobeDeleteCons == null) {
    	    return true;
    	}
        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
            // To delete the native stored adked users in this conversation.
            if (deleteMessage) {
                EaseDingMessageHelper.get().delete(tobeDeleteCons);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }*/

}