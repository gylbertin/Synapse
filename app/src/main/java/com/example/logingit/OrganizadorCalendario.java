package com.example.logingit;

import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrganizadorCalendario {
    BancoControllerConteudo bc;

    public static void preencherDias(List<TextView> views) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < views.size(); i++) {
            if (i > 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            String dia = dateFormat.format(calendar.getTime());
            views.get(i).setText(dia);
        }
    }

    public static void preeencherSemana(List<TextView> views) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", new Locale("pt", "BR"));
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < views.size(); i++) {
            if (i > 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            String diaSemana = dateFormat.format(calendar.getTime());
            String inicial = diaSemana.substring(0, 1).toUpperCase();
            views.get(i).setText(inicial);
        }
    }

    public static void preencheMes(TextView view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", new Locale("pt", "BR"));
        Calendar calendar = Calendar.getInstance();

        String mes = dateFormat.format(calendar.getTime());
        view.setText(mes);
    }


}