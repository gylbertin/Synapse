package com.example.logingit.Tela_Questao;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logingit.Banco.BancoControllerAvaliacao;
import com.example.logingit.Configuracao.Tela_configuracao;
import com.example.logingit.GPT.ChatGPTClient;
import com.example.logingit.R;
import com.example.logingit.Redacao.Tela_Redacao;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tela_Resolucao extends AppCompatActivity {
    TextView textPergunta;
    RadioGroup grupoAlternativas;
    Button confirmar, proxima;
    private List<Integer> respostasUsuario = new ArrayList<>();
    private List<Questao> listaQuestoes = new ArrayList<>();
    private int indiceAtual = 0;
    Boolean respondeu = false;
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_resolucao);

        confirmar = findViewById(R.id.botaoConfirmar);
        proxima = findViewById(R.id.botaoProxima);
        textPergunta = findViewById(R.id.textPergunta);
        grupoAlternativas = findViewById(R.id.grupoAlternativas);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        String materia = dados.getString("materia");
        String conteudo = dados.getString("conteudo");
        int quantidadeQuestoes = dados.getInt("quantidade");
        int cod_Usuario = dados.getInt("cod_Usuario");
        int cod_Cronograma = dados.getInt("cod_Cronograma");

        String prompt = "gere " + quantidadeQuestoes + " questões de múltipla escoolha sobre o tema \"" + conteudo + "\" da matéria \"" + materia + "\". " + "Em formato de questões do Enem, cada questão deve ter 1 alternativa correta e 4 erradas. " +
                "Formate como JSON com a seguinte estrutura: \n" +
                "[\n" +
                "  {\n" +
                "    \"pergunta\": \"...\",\n" +
                "    \"alternativas\": [\"...\", \"...\", \"...\", \"...\"],\n" +
                "    \"correta\": 0\n" +
                "  }, ...\n" +
                "]";

        ChatGPTClient.enviarMensagem(prompt, new ChatGPTClient.ChatGPTCallback() {
            @Override
            public void onResponse(String resposta) {
                runOnUiThread(() -> processarRespostas(resposta));
            }

            @Override
            public void onError(String erro) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Erro: " + erro, Toast.LENGTH_LONG).show());
            }
        });

        confirmar.setOnClickListener(v -> validarResposta());
        proxima.setOnClickListener(v -> {
            indiceAtual++;
            mostrarQuestao();
        });


        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Intent tela = new Intent(Tela_Resolucao.this, Tela_Principal.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_questoes) {
                Intent tela = new Intent(Tela_Resolucao.this, Tela_GeraQuestoes.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                Intent tela = new Intent(Tela_Resolucao.this, Tela_configuracao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_Redacao) {
                Intent tela = new Intent(Tela_Resolucao.this, Tela_Redacao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });


    };

    private void processarRespostas(String respostaJson) {
        try {
            JSONArray array = new JSONArray(respostaJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String pergunta = obj.getString("pergunta");
                JSONArray altArray = obj.getJSONArray("alternativas");

                List<String> alternativas = new ArrayList<>();
                for (int j = 0; j < altArray.length(); j++) {
                    alternativas.add(altArray.getString(j));
                }

                int correta = obj.getInt("correta");

                listaQuestoes.add(new Questao(pergunta, alternativas, correta));
            }

            mostrarQuestao();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao processar perguntas", Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarQuestao() {
        if (indiceAtual >= listaQuestoes.size()) {
            Toast.makeText(this, "Fim das questoes", Toast.LENGTH_LONG).show();
            salvarResultado();
            finish();
            return;
        }

        Questao questao = listaQuestoes.get(indiceAtual);

        textPergunta.setText(questao.pergunta);

        grupoAlternativas.removeAllViews();


        for (int i = 0; i < questao.alternativas.size(); i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(questao.alternativas.get(i));
            rb.setId(i);
            rb.setTextColor(Color.WHITE);
            rb.setButtonTintList(ColorStateList.valueOf(Color.BLUE));
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            grupoAlternativas.addView(rb);
        }

        confirmar.setVisibility(View.VISIBLE);
        proxima.setVisibility(View.GONE);
        respondeu = false;
    }

    private void salvarResultado() {
        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        String materia = dados.getString("materia");
        String conteudo = dados.getString("conteudo");
        int quantidae = listaQuestoes.size();
        int acertos = contarAcertos();
        int cod_Usuario = dados.getInt("cod_Usuario");

        BancoControllerAvaliacao db = new BancoControllerAvaliacao(this);
        db.inserirResultado(materia, conteudo, quantidae, acertos, cod_Usuario);
    }

    private int contarAcertos() {
        int acertos = 0;
        for (int i =0; i < respostasUsuario.size(); i++) {
            int resposta = respostasUsuario.get(i);
            int correta = listaQuestoes.get(i).correta;

            if (resposta == correta) {
                acertos = acertos + 1;
            }
        }
        return acertos;
    }

    private void validarResposta() {

        int selecionado = grupoAlternativas.getCheckedRadioButtonId();
        if (selecionado == -1 || respondeu) {
            return;
        }

        respostasUsuario.add(selecionado);

        Questao questao = listaQuestoes.get(indiceAtual);
        respondeu = true;

        if (selecionado == questao.correta) {
            Toast.makeText(this, "Correta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Errada. Resposta correta: " + questao.alternativas.get(questao.correta), Toast.LENGTH_SHORT).show();
        }

        confirmar.setVisibility(View.GONE);
        proxima.setVisibility(View.VISIBLE);
    }



}