package com.example.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText username_input;
    MaterialButton go_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        username_input = findViewById(R.id.username_input);
        go_button = findViewById(R.id.go_btn);

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_input.getText().toString();
                if (username.isBlank()) {
                    Toast.makeText(v.getContext(),"Please enter a username!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            }
        });
    }
}