package com.example.logingit;

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
    Integer cod_Cronograma, cod_Exame;
    VazioAdapter adapterVazio;


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
        textMes = findViewById(R.id.textMes);

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



    }


    @Override
    public void onClick(View v) {

        Intent tela = getIntent();
        Bundle parametro = tela.getExtras();
        cod_Cronograma = parametro.getInt("cod_Cronograma");
        cod_Exame = parametro.getInt("cod_Exame");

        bancoController = new BancoControllerUsuario(this);

        Cursor horas = bancoController.ConsultaHoraDiarias(cod_Cronograma);

        if (horas.moveToFirst()) {
            int seletor = horas.getColumnIndex("horas_Diarias");
            horas_diarias = horas.getInt(seletor);
        }

        bd = new BancoControllerConteudo(this);

        Map<String, List<Conteudo>> plano = bd.distruibuirConteudo(horas_diarias);

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