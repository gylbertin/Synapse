package com.example.logingit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public String criaCronograma() {


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
}
