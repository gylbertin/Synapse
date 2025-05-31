package com.example.logingit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class Tela_Cronograma extends AppCompatActivity implements View.OnClickListener {
    Integer  cod_Cronograma, cod_Usuario, cod_Exame;
    String email="", senha="";
    TextView txtHora;
    ToggleButton BTDomingo, BTSegunda, BTTerca, BTQuarta, BTQuinta, BTSexta, BTSabado;
    SeekBar SBarHoras;
    Button BTEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cronograma);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BTDomingo = findViewById(R.id.BTDomingo);
        BTSegunda = findViewById(R.id.BTSegunda);
        BTTerca = findViewById(R.id.BTTerca);
        BTQuarta = findViewById(R.id.BTQuarta);
        BTQuinta = findViewById(R.id.BTQuinta);
        BTSexta = findViewById(R.id.BTSexta);
        BTSabado = findViewById(R.id.BTSabado);
        BTEntrar = findViewById(R.id.BTEntrar);
        SBarHoras = findViewById(R.id.SBarHoras);
        txtHora = findViewById(R.id.txtHora);

        Intent tela = getIntent();
        Bundle parametros = tela.getExtras();
        email = parametros.getString("email");
        senha = parametros.getString("senha");

        SBarHoras.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 1) {
                    txtHora.setText(progress + "hr");
                } else {
                    txtHora.setText(progress + "hrs");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BTEntrar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        ArrayList<DayOfWeek> dia_Estudo = new ArrayList<>();
        if (BTDomingo.isChecked() || BTSegunda.isChecked() || BTTerca.isChecked() || BTQuarta.isChecked() || BTQuinta.isChecked() || BTSexta.isChecked() || BTSabado.isChecked()) {
            if (BTDomingo.isChecked()) {
                dia_Estudo.add(DayOfWeek.SUNDAY);
            }

            if (BTSegunda.isChecked()) {
                dia_Estudo.add(DayOfWeek.MONDAY);
            }

            if (BTTerca.isChecked()) {
                dia_Estudo.add(DayOfWeek.TUESDAY);
            }

            if (BTQuarta.isChecked()) {
                dia_Estudo.add(DayOfWeek.WEDNESDAY);
            }

            if (BTQuinta.isChecked()) {
                dia_Estudo.add(DayOfWeek.THURSDAY);
            }

            if (BTSexta.isChecked()) {
                dia_Estudo.add(DayOfWeek.FRIDAY);
            }

            if (BTSabado.isChecked()) {
                dia_Estudo.add(DayOfWeek.SATURDAY);
            }
            BancoControllerUsuario bd = new BancoControllerUsuario(this);

            Cursor dados = bd.ConsultaCodigo(email, senha);


            if (dados.moveToFirst()) {
                int seletor = dados.getColumnIndex("cod_Usuario");
                int seletor2 = dados.getColumnIndex("cod_Exame");

                cod_Usuario = dados.getInt(seletor);
                cod_Exame = dados.getInt(seletor2);
            }

            int horas = SBarHoras.getProgress();

            String resultado;

            resultado = bd.criaCronograma(cod_Usuario,horas,cod_Exame);

            Toast.makeText(this, resultado,Toast.LENGTH_LONG);

            Cursor codigo = bd.ConsultaCodigoCronograma(cod_Usuario);
            if (codigo.moveToFirst()) {
                int seletor = codigo.getColumnIndex("cod_Cronograma");
                cod_Cronograma = codigo.getInt(seletor);
            }

            for (DayOfWeek dia : DayOfWeek.values()) {
                int estudo = 0;
                if (dia_Estudo.contains(dia)) {
                    estudo = 1;
                }
                bd.CriaCronogramaDiario(cod_Cronograma,dia.toString(), estudo);
            }

            Intent  tela = new Intent(Tela_Cronograma.this, Tela_Principal.class);
            Bundle parametros = new Bundle();
            parametros.putInt("cod_Cronograma", cod_Cronograma);
            parametros.putInt("cod_Exame", cod_Exame);
            tela.putExtras(parametros);
            startActivity(tela);

        } else {
            Toast.makeText(this, "Selecione pelo menos uma opção de dia para estudo", Toast.LENGTH_LONG).show();
        }
    }
}