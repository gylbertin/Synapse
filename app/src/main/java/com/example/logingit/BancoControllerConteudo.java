package com.example.logingit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BancoControllerConteudo {
    private SQLiteDatabase db;

    private CriaBanco banco;

    public BancoControllerConteudo(Context context) {
        banco = new CriaBanco(context);
    }

    public String TornarConteudoConluido(String nome, Integer exame) {
        String msg = "Conteudo concluido!";
        db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("concluido", 1);

        String condicao = "nome_Conteudo = '" + nome + "' and cod_Exame = '" + exame + "'";

        int linha;

        linha = db.update("Conteudo", valores, condicao, null);

        if (linha < 1) {
            msg = "Erro ao alterar os dados";
        }
        db.close();
        return msg;
    }

    public String TornarConteudoIncompleto(String nome, Integer exame) {
        String msg = "Conteudo Incompleto!";
        db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("concluido", 0);

        String condicao = "nome_Conteudo = '" + nome + "' and cod_Exame = '" + exame + "'";

        int linha;

        linha = db.update("Conteudo", valores, condicao, null);

        if (linha < 1) {
            msg = "Erro ao alterar os dados";
        }
        db.close();
        return msg;
    }

    public List<Conteudo> getConteudoNaoConcluido() {
        List<Conteudo> lista = new ArrayList<>();
        db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cod_Conteudo, nome_Conteudo, cod_Materia FROM Conteudo  WHERE concluido = 0 ORDER BY cod_Conteudo", null);
        while (cursor.moveToNext()) {
            int seletor = cursor.getColumnIndex("cod_Conteudo");
            int seletor2 = cursor.getColumnIndex("nome_Conteudo");
            int seletor3 = cursor.getColumnIndex("cod_Materia");
            Conteudo cont = new Conteudo();
            cont.id = cursor.getInt(seletor);
            cont.nome = cursor.getString(seletor2);
            cont.materia = cursor.getString(seletor3);
            lista.add(cont);
        }
        cursor.close();
        return lista;
    }

    public boolean isDiaPermitido(String data) {
        db = banco.getReadableDatabase();
        boolean permitido = false;
        String dia = data;
        Cursor cursor = db.rawQuery("SELECT dia_Estudo FROM Cronograma_dia WHERE dia_Semana = ?", new String[]{dia});

        if (cursor.moveToFirst()) {
            permitido = cursor.getInt(0) == 1;
        }
        cursor.close();
        return permitido;
    }

    public Map<String, List<Conteudo>> distruibuirConteudo(int limiteDiario) {

        Map<String, List<Conteudo>> planejamento = new LinkedHashMap<>();

        String[] diasSemana = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        List<Conteudo> naoFeitos = getConteudoNaoConcluido();

        int posicaoAtual = 0;

        for (String dia : diasSemana) {
            if (!isDiaPermitido(dia)) {
                continue;
            }

            List<Conteudo> conteudosDia = new ArrayList<>();

            for (int i = 0; i < limiteDiario && posicaoAtual < naoFeitos.size(); i++) {
                conteudosDia.add(naoFeitos.get(posicaoAtual));
                posicaoAtual++;
            }

            planejamento.put(dia, conteudosDia);

            if (posicaoAtual >= naoFeitos.size()) {
                break;
            }
        }
        return planejamento;


    }
}
