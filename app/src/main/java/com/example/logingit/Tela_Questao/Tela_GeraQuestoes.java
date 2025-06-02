package com.example.logingit.Tela_Questao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logingit.Banco.BancoControllerConteudo;
import com.example.logingit.R;
import com.example.logingit.Configuracao.Tela_configuracao;
import com.example.logingit.Tela_Historico_Questao;
import com.example.logingit.Tela_Redacao;
import com.example.logingit.Tela_principal.Conteudo;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class Tela_GeraQuestoes extends AppCompatActivity {
    private Spinner spinnerMateria, spinnerConteudo;
    EditText editQuantidade;
    Button btnGerar;
    ImageButton btnHistory;
    BancoControllerConteudo bc;
    List<Conteudo> listaConteudos;
    List<Materia> listaMaterias;
    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_gera_questoes);

        Bundle dados = getIntent().getExtras();
        int cod_Usuario = dados.getInt("cod_Usuario");
        int cod_Cronograma = dados.getInt("cod_Cronograma");

        spinnerMateria = findViewById(R.id.spinnerMateria);
        spinnerConteudo = findViewById(R.id.spinnerConteudo);
        editQuantidade = findViewById(R.id.editTextNumber);
        btnGerar = findViewById(R.id.btnGerar);
        btnHistory = findViewById(R.id.btnHistory);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        BancoControllerConteudo bc = new BancoControllerConteudo(this);

        carregarMaterias();

        btnHistory.setOnClickListener(v -> {
            Intent tela = new Intent(Tela_GeraQuestoes.this, Tela_Historico_Questao.class);
            Bundle parametros = new Bundle();
            parametros.putInt("cod_Usuario",cod_Usuario);
            parametros.putInt("cod_Cronograma", cod_Cronograma);
            tela.putExtras(parametros);
            startActivity(tela);
        });

        spinnerMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Materia materiaSelecionada = listaMaterias.get(position);
                carregarConteudos(materiaSelecionada.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGerar.setOnClickListener(v -> {
            String qtdQuest = editQuantidade.getText().toString();
            if (qtdQuest.isEmpty()) {
                Toast.makeText(this, "Digite o número de questões", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantidade = Integer.parseInt(qtdQuest);
            Materia materia = listaMaterias.get(spinnerMateria.getSelectedItemPosition());
            Conteudo conteudo = listaConteudos.get(spinnerConteudo.getSelectedItemPosition());

            Intent tela = new Intent(Tela_GeraQuestoes.this, Tela_Resolucao.class);
            Bundle parametros = new Bundle();
            parametros.putString("materia",materia.getNome());
            parametros.putString("conteudo", conteudo.getNome());
            parametros.putInt("quantidade", quantidade);
            parametros.putInt("cod_Usuario", cod_Usuario);
            parametros.putInt("cod_Cronograma", cod_Cronograma);
            tela.putExtras(parametros);
            startActivity(tela);

        });

        bottom_navigation.setSelectedItemId(R.id.nav_questoes);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_questoes) {
                return true;
            } else if (itemId == R.id.nav_home) {
                Intent tela = new Intent(Tela_GeraQuestoes.this, Tela_Principal.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                Intent tela = new Intent(Tela_GeraQuestoes.this, Tela_configuracao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_Redacao) {
                Intent tela = new Intent(Tela_GeraQuestoes.this, Tela_Redacao.class);
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

    private void carregarMaterias() {
        listaMaterias = bc.getTodasMaterias();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,listaMaterias.stream().map(Materia::getNome).collect(Collectors.toList()));
        spinnerMateria.setAdapter(adapter);
    }

    public  void carregarConteudos(int idMateria) {
        listaConteudos = bc.getConteudoPorMateria(idMateria);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaConteudos.stream().map(Conteudo::getNome).collect(Collectors.toList()));
        spinnerConteudo.setAdapter(adapter);
    }
}