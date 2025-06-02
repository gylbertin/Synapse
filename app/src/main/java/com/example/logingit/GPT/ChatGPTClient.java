package com.example.logingit.GPT;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatGPTClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final OkHttpClient client = new OkHttpClient();

    public static void enviarMensagem(String mensagemUsuario, ChatGPTCallback callback) {
        try {
            JSONArray mensagens = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", mensagemUsuario);
            mensagens.put(userMessage);

            JSONObject json = new JSONObject();
            json.put("model", "gpt-3.5-turbo");
            json.put("messages", mensagens);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        callback.onError("Erro: " + response.code());
                        return;
                    }

                    String resposta = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(resposta);
                        String texto = obj.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        callback.onResponse(texto.trim());
                    } catch (Exception e) {
                        callback.onError("Erro ao interpretar resposta");
                    }
                }
            });

        } catch (Exception e) {
            callback.onError("Erro: " + e.getMessage());
        }
    }

    public interface ChatGPTCallback {
        void onResponse(String resposta);
        void onError(String erro);
    }
}
