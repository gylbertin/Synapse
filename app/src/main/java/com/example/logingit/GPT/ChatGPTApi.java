package com.example.logingit.GPT;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.*;

public class ChatGPTApi {

    private static final String API_KEY = "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"; // <-- COLE SUA API KEY AQUI
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    public interface ChatGPTCallback {
        void onSuccess(String resposta);
        void onError(String erro);
    }

    public static void enviarPergunta(String pergunta, ChatGPTCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String json = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"" + pergunta + "\"}\n" +
                "  ]\n" +
                "}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resposta = response.body().string();
                    callback.onSuccess(resposta);
                } else {
                    callback.onError("Erro: " + response.message());
                }
            }
        });
    }

    public static int extrairNota(String respostaGPT) {
        Pattern pattern = Pattern.compile("(nota final|nota total|nota:)\\D*(\\d{2,3})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(respostaGPT);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(2));
            } catch (NumberFormatException e) {
                return -1; // erro ao converter número
            }
        }
        return -1; // não achou a nota
    }
}
