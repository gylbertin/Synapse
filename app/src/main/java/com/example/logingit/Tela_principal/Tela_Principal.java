package com.example.logingit.Tela_principal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logingit.Banco.BancoControllerConteudo;
import com.example.logingit.Banco.BancoControllerUsuario;
import com.example.logingit.Cronograma.OrganizadorCalendario;
import com.example.logingit.R;
import com.example.logingit.Tela_Questao.Tela_GeraQuestoes;
import com.example.logingit.Configuracao.Tela_configuracao;
import com.example.logingit.Redacao.Tela_Redacao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Tela_Principal extends AppCompatActivity implements View.OnClickListener {
    TextView textMes, dia_mes_1,dia_mes_2, dia_mes_3, dia_mes_4, dia_mes_5, dia_mes_6, dia_mes_7;
    RecyclerView recycle_conteudo;
    ConteudoAdapter adapterConteudo;
    BancoControllerConteudo bd;
    int horas_diarias;
    BancoControllerUsuario bancoController;
    Integer cod_Cronograma, cod_Exame, cod_Usuario;
    VazioAdapter adapterVazio;
    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent tela = getIntent();
        Bundle parametro = tela.getExtras();
        cod_Cronograma = parametro.getInt("cod_Cronograma");
        cod_Exame = parametro.getInt("cod_Exame");
        cod_Usuario = parametro.getInt("cod_Usuario");

        textMes = findViewById(R.id.textMes);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        OrganizadorCalendario.preencheMes(textMes);

        List<TextView> listaDaSemana = Arrays.asList(
                findViewById(R.id.dia_semana_1),
                findViewById(R.id.dia_semana_2),
                findViewById(R.id.dia_semana_3),
                findViewById(R.id.dia_semana_4),
                findViewById(R.id.dia_semana_5),
                findViewById(R.id.dia_semana_6),
                findViewById(R.id.dia_semana_7)
        );
        OrganizadorCalendario.preeencherSemana(listaDaSemana);

        List<TextView> listaDosDias = Arrays.asList(

                findViewById(R.id.dia_mes_1),
                findViewById(R.id.dia_mes_2),
                findViewById(R.id.dia_mes_3),
                findViewById(R.id.dia_mes_4),
                findViewById(R.id.dia_mes_5),
                findViewById(R.id.dia_mes_6),
                findViewById(R.id.dia_mes_7)
        );
        OrganizadorCalendario.preencherDias(listaDosDias);

        dia_mes_1 = findViewById(R.id.dia_mes_1);
        dia_mes_2 = findViewById(R.id.dia_mes_2);
        dia_mes_3 = findViewById(R.id.dia_mes_3);
        dia_mes_4 = findViewById(R.id.dia_mes_4);
        dia_mes_5 = findViewById(R.id.dia_mes_5);
        dia_mes_6 = findViewById(R.id.dia_mes_6);
        dia_mes_7 = findViewById(R.id.dia_mes_7);

        recycle_conteudo = findViewById(R.id.recycle_conteudo);

        dia_mes_1.setOnClickListener(this);
        dia_mes_2.setOnClickListener(this);
        dia_mes_3.setOnClickListener(this);
        dia_mes_4.setOnClickListener(this);
        dia_mes_5.setOnClickListener(this);
        dia_mes_6.setOnClickListener(this);
        dia_mes_7.setOnClickListener(this);

        bottom_navigation.setSelectedItemId(R.id.nav_home);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_questoes) {
                Intent intent = new Intent(Tela_Principal.this, Tela_GeraQuestoes.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                intent.putExtras(parametros);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                Intent intent = new Intent(Tela_Principal.this, Tela_configuracao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                intent.putExtras(parametros);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_Redacao) {
                Intent intent = new Intent(Tela_Principal.this, Tela_Redacao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                intent.putExtras(parametros);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });


    }


    @Override
    public void onClick(View v) {

        bancoController = new BancoControllerUsuario(this);

        Cursor horas = bancoController.ConsultaHoraDiarias(cod_Cronograma, cod_Usuario);

        if (horas.moveToFirst()) {
            int seletor = horas.getColumnIndex("horas_Diarias");
            horas_diarias = horas.getInt(seletor);
        }

        bd = new BancoControllerConteudo(this);

        Map<String, List<Conteudo>> plano = bd.distruibuirConteudo(horas_diarias, cod_Cronograma);

        if (v.getId()==R.id.dia_mes_1) {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }

        if (v.getId()==R.id.dia_mes_2) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }
        if (v.getId()==R.id.dia_mes_3) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 2);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }

        if (v.getId()==R.id.dia_mes_4) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 3);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }

        if (v.getId()==R.id.dia_mes_5) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 4);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }

        if (v.getId()==R.id.dia_mes_6) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 5);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }

        if (v.getId()==R.id.dia_mes_7) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 6);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String diaDaSemana = dateFormat.format(calendar.getTime()).toUpperCase(Locale.ENGLISH);

            List<Conteudo> doDia = plano.get(diaDaSemana);

            recycle_conteudo.setLayoutManager(new LinearLayoutManager(this));
            if (doDia != null && !doDia.isEmpty()) {
                adapterConteudo = new ConteudoAdapter(doDia, this, cod_Exame);
                recycle_conteudo.setAdapter(adapterConteudo);
            } else {
                adapterVazio = new VazioAdapter();
                recycle_conteudo.setAdapter(adapterVazio);
            }
        }
    }
}