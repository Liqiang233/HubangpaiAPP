package com.example.menudemo.ui.notifications.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.menudemo.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.example.menudemo.ui.notifications.widget.EaseConversationList.EaseConversationListHelper;

import java.util.Date;
import java.util.List;

public class ChatListAdapter extends ArrayAdapter<EMConversation> {

    private Context context;
    private List<EMConversation> conversationList;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;



    public ChatListAdapter(Context context, int resource,  List<EMConversation> objects)
        {
        super(context,resource,objects);
        conversationList = objects;
    }
    @Override
    public int getCount()
    {
        return conversationList.size();
    }
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatlist_item, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            holder.motioned = (TextView) convertView.findViewById(R.id.mentioned);
            convertView.setTag(holder);
        }
        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);


        EMConversation conversation = getItem(position);


        String username = conversation.conversationId();
        holder.name.setText(username);
        Log.i("name",username);
        holder.avatar.setImageResource(R.drawable.ic_girl);
        holder.motioned.setVisibility(View.GONE);
        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }
        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            content = lastMessage.getBody().toString();
            holder.message.setText("暂无新消息");
            if(content != null){
                holder.message.setText(content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }


        holder.name.setTextColor(primaryColor);
        holder.message.setTextColor(secondaryColor);
        holder.time.setTextColor(timeColor);


        if(primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if(secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if(timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }
    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

     //设置item数

    public int getItemCount()
    {
        return conversationList.size();

    }

   private EaseConversationListHelper cvsListHelper;

   public void setCvsListHelper(EaseConversationListHelper cvsListHelper){
       this.cvsListHelper = cvsListHelper;
   }

    private static class ViewHolder {
        /** 名字 */
        TextView name;
        /** 未读消息数*/
        TextView unreadLabel;
        /** 消息 */
        TextView message;
        /** 上条短信时间*/
        TextView time;
        /** 头像 */
        ImageView avatar;
        /** status of last message */
        View msgState;
        /** layout */
        RelativeLayout list_itease_layout;
        TextView motioned;
    }


}
