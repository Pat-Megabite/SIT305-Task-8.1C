package com.example.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chat_recycler_view;
    EditText message;
    ImageButton send_btn;
    List<ChatMessage> message_list;
    ChatAdapter chat_adapter;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_recycler_view = findViewById(R.id.chat_recycler_view);
        message = findViewById(R.id.message);
        send_btn = findViewById(R.id.send_button);
        String username = getIntent().getStringExtra("username");

        message_list = new ArrayList<>();
        chat_adapter = new ChatAdapter(message_list, username);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chat_recycler_view.setLayoutManager(layoutManager);
        chat_recycler_view.setAdapter(chat_adapter);

        addMessage("Welcome "+username,ChatMessage.TYPE_BOT);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = message.getText().toString().trim();
                if (!userMessage.isEmpty()){
                    addMessage(userMessage, ChatMessage.TYPE_USER);
                    message.setText("");
                    sendMessage(userMessage);
                }
            }
        });
    }

    private void addMessage(String text, int type) {
        ChatMessage msg = new ChatMessage(text, type);
        message_list.add(msg);
        chat_adapter.notifyItemInserted(message_list.size() - 1);
        chat_recycler_view.scrollToPosition(message_list.size() - 1);
    }

    private void sendMessage(String message) {
        String json = "{\"userMessage\":\"" + message + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/chat")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> addMessage("Error: " + e.getMessage(), ChatMessage.TYPE_BOT));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> addMessage(responseBody, ChatMessage.TYPE_BOT));
                }
                else {
                    runOnUiThread(() -> addMessage("Server error", ChatMessage.TYPE_BOT));
                }
            }
        });
    }
}
