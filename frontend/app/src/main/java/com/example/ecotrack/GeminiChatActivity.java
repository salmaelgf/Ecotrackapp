package com.example.ecotrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeminiChatActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyD61FB27DLnMI0XCrmTeUN4S8g2rRfK97U";
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    private EditText inputQuestion;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gemini_chat);

        inputQuestion = findViewById(R.id.input_question);
        recyclerView = findViewById(R.id.chat_recycler);
        ImageButton sendButton = findViewById(R.id.send_button);

        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        addMessage("Hi! I'm your EcoBot 🌱, here to help you track and reduce your product's carbon footprint.", false);


        sendButton.setOnClickListener(v -> {
            String question = inputQuestion.getText().toString().trim();
            if (!question.isEmpty()) {
                addMessage(question, true); // user message
                askGemini(question);
                inputQuestion.setText(""); // clear field
            } else {
                Toast.makeText(this, "Pose une question", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessage(String content, boolean isUser) {
        runOnUiThread(() -> {
            messageList.add(new Message(content, isUser));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        });
    }

    private void askGemini(String userInput) {
        OkHttpClient client = new OkHttpClient();

        try {
            JSONObject part = new JSONObject().put("text", userInput);
            JSONObject content = new JSONObject()
                    .put("role", "user")
                    .put("parts", new JSONArray().put(part));
            JSONObject body = new JSONObject().put("contents", new JSONArray().put(content));

            RequestBody requestBody = RequestBody.create(
                    body.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(GEMINI_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    addMessage("Erreur : " + e.getMessage(), false);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resBody = response.body() != null ? response.body().string() : "";
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(resBody);
                            String reply = json.getJSONArray("candidates")
                                    .getJSONObject(0)
                                    .getJSONObject("content")
                                    .getJSONArray("parts")
                                    .getJSONObject(0)
                                    .getString("text");

                            addMessage(reply, false);
                        } catch (Exception e) {
                            addMessage("Erreur parsing : " + e.getMessage(), false);
                        }
                    } else {
                        addMessage("Erreur Gemini : " + resBody, false);
                    }
                }
            });

        } catch (Exception e) {
            addMessage("Erreur de requête : " + e.getMessage(), false);
        }
    }
}
