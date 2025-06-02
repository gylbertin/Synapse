package com.example.logingit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.logingit.Banco.BancoControllerAvaliacao;
import com.example.logingit.Configuracao.Tela_configuracao;
import com.example.logingit.GPT.ChatGPTApi;
import com.example.logingit.GPT.ChatGPTClient;
import com.example.logingit.Tela_Questao.Tela_GeraQuestoes;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tela_Redacao extends AppCompatActivity {
    ImageButton btnGerarTema, btnHistorico;
    Button btnEnviar;
    TextView txtTema;
    EditText editRedacao;
    BancoControllerAvaliacao db;
    String resultado;
    Integer cod_Usuario, cod_Cronograma;
    BottomNavigationView bottom_navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_redacao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.redacaoMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Intent intent = getIntent();
        Bundle parametros = intent.getExtras();
        cod_Usuario = parametros.getInt("cod_Usuario");
        cod_Cronograma = parametros.getInt("cod_Cronograma");

        btnGerarTema = findViewById(R.id.btnGerarTema);
        btnHistorico = findViewById(R.id.btnHistorico);
        btnEnviar = findViewById(R.id.btnEnviar);
        txtTema = findViewById(R.id.txtTema);
        editRedacao = findViewById(R.id.editRedacao);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        btnGerarTema.setOnClickListener(v -> gerarTemaRedacao());

        btnHistorico.setOnClickListener(v -> {
            Intent tela = new Intent(Tela_Redacao.this, Tela_Historico_redacoes.class);
            Bundle dados = new Bundle();
            dados.putInt("cod_Usuario", cod_Usuario);
            dados.putInt("cod_Cronograma", cod_Cronograma);
            tela.putExtras(dados);
            startActivity(tela);
        });

        bottom_navigation.setSelectedItemId(R.id.nav_Redacao);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_Redacao) {
                return true;
            } else if (itemId == R.id.nav_home) {
                Intent tela = new Intent(Tela_Redacao.this, Tela_Principal.class);
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                Intent tela = new Intent(Tela_Redacao.this, Tela_configuracao.class);
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_questoes) {
                Intent tela = new Intent(Tela_Redacao.this, Tela_GeraQuestoes.class);
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });

        btnEnviar.setOnClickListener(v -> {
            String tema = txtTema.getText().toString().trim();
            String texto = editRedacao.getText().toString().trim();

            if (tema.isEmpty() || texto.isEmpty()) {
                Toast.makeText(this, "Gere um tema e escreva a redação antes de enviar!", Toast.LENGTH_SHORT).show();
                return;
            }

            db = new BancoControllerAvaliacao(this);

            resultado = db.salvarRedacao(tema, texto,cod_Usuario, null, 0);

            gerarAnalise(tema, texto);
            editRedacao.setText("");
        });


    }

    private void gerarTemaRedacao() {
        ChatGPTApi.enviarPergunta("Gere um tema de redação estilo ENEM", new ChatGPTApi.ChatGPTCallback() {
            @Override
            public void onSuccess(String resposta) {
                runOnUiThread(() -> txtTema.setText(resposta));
            }

            @Override
            public void onError(String erro) {
                runOnUiThread(() -> Toast.makeText(Tela_Redacao.this, "Erro: " + erro, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void gerarAnalise(String tema, String redacao) {
        ChatGPTApi.enviarPergunta("Avalie a seguinte redação conforme os critérios do ENEM. Dê notas de 0 a 200 para cada uma das 5 competências, explique cada uma e forneça a nota final. tema: " + tema + "\n\nRedação:\n" + redacao, new ChatGPTApi.ChatGPTCallback() {
            @Override
            public void onSuccess(String resposta) {
                Boolean sucesso = db.atualizarAvaliacaoMaisRecente(resposta);

                if (sucesso) {
                    runOnUiThread(() ->
                            Toast.makeText(Tela_Redacao.this, "Avaliação salva com sucesso!", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Tela_Redacao.this, "Erro ao salvar avaliação no banco.", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(String erro) {
                runOnUiThread(() ->
                        Toast.makeText(Tela_Redacao.this, "Erro ao gerar análise da redação.", Toast.LENGTH_SHORT).show()
                );
            }
        });

    }
}