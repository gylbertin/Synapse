package com.example.logingit.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.logingit.GPT.ChatGPTApi;
import com.example.logingit.Questao;
import com.example.logingit.Redacao.Redacao;

import java.util.ArrayList;
import java.util.List;

public class BancoControllerAvaliacao {
    SQLiteDatabase db;

    CriaBanco banco;

    public BancoControllerAvaliacao(Context context) {
        banco = new CriaBanco(context);
    }


    public String salvarRedacao(String tema, String texto, Integer cod_Usuario, String analise, int nota) {
        Long resultado;
        ContentValues valores;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("tema_redacao", tema);
        valores.put("text_Redacao", texto);
        valores.put("nota_Redacao", nota);
        valores.put("text_Analise", analise);
        valores.put("cod_Usuario", cod_Usuario);

        resultado = db.insert("Redacao", null, valores);
        db.close();
        if (resultado == -1)
            return "Erro ao inserir redação";
        else
            return "Redação inserido com sucesso";
    }

    public Boolean atualizarAvaliacaoMaisRecente(String avaliacao) {
        db = banco.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("text_Analise", avaliacao);

        int nota = ChatGPTApi.extrairNota(avaliacao);
        valores.put("nota_Redacao", nota);

        int linhasAfetadas = db.update("Redacao", valores, "cod_Redacao = (SELECT MAX(cod_Redacao) FROM Redacao)", null);
        db.close();

        return linhasAfetadas > 0;
    }

    public List<Redacao> buscarRedacoes(int cod_Usuario) {
        List<Redacao> lista = new ArrayList<>();
        db = banco.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT cod_Redacao, tema_redacao, nota_Redacao, text_Analise FROM Redacao WHERE cod_Usuario = ? ORDER BY cod_Redacao DESC", new String[]{String.valueOf(cod_Usuario)} );
        while (cursor.moveToNext()) {
            int seletor1 = cursor.getColumnIndex("cod_Redacao");
            int seletor2 = cursor.getColumnIndex("tema_redacao");
            int seletor3 = cursor.getColumnIndex("nota_Redacao");
            int seletor4 = cursor.getColumnIndex("text_Analise");

            int id = cursor.getInt(seletor1);
            String tema = cursor.getString(seletor2);
            int nota = cursor.getInt(seletor3);
            String analise = cursor.getString(seletor4);
            lista.add(new Redacao(id, tema, nota, analise));
        }

        cursor.close();
        db.close();
        return lista;
    }

    public List<Questao> buscarQuestoes(int cod_Usuario) {
        List<Questao> lista = new ArrayList<>();
        db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT materia, conteudo, num_Acerto, num_Pergunta FROM Questao WHERE cod_Usuario = ? ORDER BY cod_Questao DESC", new String[]{String.valueOf(cod_Usuario)});

        while (cursor.moveToNext()) {
            int seletor1 = cursor.getColumnIndex("materia");
            int seletor2 = cursor.getColumnIndex("conteudo");
            int seletor3 = cursor.getColumnIndex("num_Acerto");
            int seletor4 = cursor.getColumnIndex("num_Pergunta");

            String materia = cursor.getString(seletor1);
            String conteudo = cursor.getString(seletor2);
            int acertos = cursor.getInt(seletor3);
            int total = cursor.getInt(seletor4);
            lista.add(new Questao(materia, conteudo, total, acertos));
        }

        cursor.close();
        db.close();
        return lista;
    }

    public void inserirResultado(String materia, String conteudo, int total, int acertos, int codUsuario) {
        db = banco.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("materia",materia);
        valores.put("materia", materia);
        valores.put("conteudo", conteudo);
        valores.put("num_Pergunta", total);
        valores.put("num_Acerto", acertos);
        valores.put("cod_Usuario", codUsuario);

        db.insert("Questao", null, valores);
        db.close();
    }
}
