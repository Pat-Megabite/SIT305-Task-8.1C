package com.example.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> messages;
    private String username;

    public ChatAdapter(List<ChatMessage> messages, String username) {
        this.messages = messages;
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ChatMessage.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message.getMessage(), String.valueOf(username.charAt(0)).toUpperCase());

        } else if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).bind(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView message, avatar;

        public UserViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.user_message);
            avatar = itemView.findViewById(R.id.avatar);
        }

        public void bind(String msg, String user) {
            message.setText(msg);
            avatar.setText(user);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public BotViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bot_message);
        }

        public void bind(String message) {
            textView.setText(message);
        }
    }
}

