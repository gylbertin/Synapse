package com.example.logingit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.os.IResultReceiver;
import android.util.Log;

public class BancoControllerUsuario {
    private SQLiteDatabase db;

    private CriaBanco banco;

    public BancoControllerUsuario (Context context) {
        banco = new CriaBanco(context);
    }

    public String insereDados (String txtEmail, String txtNome, String txtSenha) {
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("nome_Usuario", txtNome);
        valores.put("email", txtEmail);
        valores.put("senha", txtSenha);

        resultado = db.insert("Usuario", null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro";
        else
            return "Registro inserido com sucesso";
    }

    public String criaCronograma(int cod_Usuario, int horas_Diarias, int cod_Exame) {
        ContentValues valores;
        Long resultado;

        valores = new ContentValues();
        valores.put("cod_Usuario", cod_Usuario);
        valores.put("horas_Diarias", horas_Diarias);
        valores.put("cod_Exame", cod_Exame);

        db = banco.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        resultado = db.insert("Cronograma", null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir Cronograma";
        else
            return "Cronograma gravado";
    }

    public void CriaCronogramaDiario (Integer cod_Cronograma, String dia_Semana, Integer dia_Estudo) {
        ContentValues valores;
        Long resultado;
        db = banco.getWritableDatabase();


        valores = new ContentValues();
        valores.put("cod_Cronograma", cod_Cronograma);
        valores.put("dia_Semana", dia_Semana);
        valores.put("dia_Estudo", dia_Estudo);

        resultado = db.insert("Cronograma_dia", null, valores);
        db.close();

    }

    public Cursor ConsultaDadosLogin(String _email, String _senha){
        Cursor cursor;
        String[] campos = {"cod_Usuario", "nome_Usuario", "email", "senha"};
        String where = "email = '" + _email + "' and senha = '" + _senha + "'";
        db = banco.getReadableDatabase();

        cursor = db.query("Usuario", campos, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor ConsultaCodigo(String _email, String _senha) {
        Cursor cursor;
        String[] campos = {"cod_Usuario", "cod_Exame"};
        String where = "email = ? AND senha = ?";
        String[] whereArgs = new String[]{_email, _senha};
        db = banco.getWritableDatabase();

        cursor = db.query("Usuario", campos, where, whereArgs, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.e("ConsultaCodigo", "Nenhum dado encontrado ou erro na query.");
        }
        db.close();
        return cursor;
    }

    public Cursor ConsultaCodigoCronograma (Integer _cod_Usuario) {
        Cursor cursor;
        String[] campos = {"cod_Cronograma"};
        String where = "cod_Usuario = '" + _cod_Usuario + "'";
        db = banco.getWritableDatabase();

        cursor = db.query("Cronograma", campos, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor ConsultaHoraDiarias (Integer _cod_Cronograma) {
        Cursor cursor;
        String[] campos = {"horas_Diarias"};
        String where = "cod_Cronograma = '" + _cod_Cronograma + "'";
        db = banco.getWritableDatabase();

        cursor = db.query("Cronograma", campos, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
