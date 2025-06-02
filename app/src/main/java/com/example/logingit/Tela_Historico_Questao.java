package com.example.logingit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logingit.Banco.BancoControllerAvaliacao;
import com.example.logingit.Configuracao.Tela_configuracao;
import com.example.logingit.Tela_Questao.Tela_GeraQuestoes;
import com.example.logingit.Tela_Questao.Tela_Resolucao;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Tela_Historico_Questao extends AppCompatActivity {
    RecyclerView recyclerHistoricoQuestoes;
    List<Questao> listaQuestoes;
    int cod_Usuario;
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_historico_questao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle dados = getIntent().getExtras();
        cod_Usuario = dados.getInt("cod_Usuario");
        int cod_Cronograma = dados.getInt("cod_Cronograma");

        bottom_navigation = findViewById(R.id.bottom_navigation);

        recyclerHistoricoQuestoes = findViewById(R.id.recyclerHistoricoQuestoes);
        recyclerHistoricoQuestoes.setLayoutManager(new LinearLayoutManager(this));

        BancoControllerAvaliacao db = new BancoControllerAvaliacao(this);
        listaQuestoes = db.buscarQuestoes(cod_Usuario);

        QuestoesAdapter adapter = new QuestoesAdapter(listaQuestoes, this);
        recyclerHistoricoQuestoes.setAdapter(adapter);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Intent tela = new Intent(Tela_Historico_Questao.this, Tela_Principal.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_questoes) {
                Intent tela = new Intent(Tela_Historico_Questao.this, Tela_GeraQuestoes.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                Intent tela = new Intent(Tela_Historico_Questao.this, Tela_configuracao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_Redacao) {
                Intent tela = new Intent(Tela_Historico_Questao.this, Tela_Redacao.class);
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

    }
}