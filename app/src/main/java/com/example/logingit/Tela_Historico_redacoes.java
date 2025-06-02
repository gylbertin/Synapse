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
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Tela_Historico_redacoes extends AppCompatActivity {
    RecyclerView recyclerHistorico;
    BancoControllerAvaliacao db;
    List<Redacao> listaRedacoes;
    int cod_Usuario;
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_historico_redacoes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        cod_Usuario = dados.getInt("cod_Usuario");
        int cod_Cronograma = dados.getInt("cod_Cronograma");

        bottom_navigation = findViewById(R.id.bottom_navigation);

        recyclerHistorico = findViewById(R.id.recyclerHistorico);
        recyclerHistorico.setLayoutManager(new LinearLayoutManager(this));

        db = new BancoControllerAvaliacao(this);
        listaRedacoes = db.buscarRedacoes(cod_Usuario);

        RedacaoAdapter adapter = new RedacaoAdapter(listaRedacoes, this);
        recyclerHistorico.setAdapter(adapter);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Intent tela = new Intent(Tela_Historico_redacoes.this, Tela_Principal.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_questoes) {
                Intent tela = new Intent(Tela_Historico_redacoes.this, Tela_GeraQuestoes.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                Intent tela = new Intent(Tela_Historico_redacoes.this, Tela_configuracao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_Redacao) {
                Intent tela = new Intent(Tela_Historico_redacoes.this, Tela_Redacao.class);
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