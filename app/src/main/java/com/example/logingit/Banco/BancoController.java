package com.example.logingit.Banco;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context) {
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

    public Cursor consultaDados(String txtEmail) {
        Cursor cursor;

        String[] campos = {"email", "nome_Usuario", "senha"};

        String where = "email=" + txtEmail;

        db = banco.getReadableDatabase();
        cursor = db.query("Usuario", campos, where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

    public String alteraDados(String username, String email, String senha) {
        String msg = "Dados alterados com sucesso!!";
        db = banco.getReadableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome_Usuario", username);
        valores.put("senha", senha);

        String condicao = "email =" + email;

        int linha;

        linha = db.update("Usuario", valores, condicao, null);

        if (linha < 1) {
            msg = "Erro ao alterar os dados";
        }

        db.close();
        return msg;
    }

    public String excluirDados (Integer _cod_Usuario, Integer _cod_Cronograma) {
        String msg = "Registro Excluido";

        db = banco.getReadableDatabase();

        String cod_Usuario = _cod_Usuario.toString();
        String cod_Cronograma = _cod_Cronograma.toString();

        db.beginTransaction();
        try {
            db.delete("Cronograma", "cod_Usuario = ?", new String[]{String.valueOf(cod_Usuario)});
            db.delete("Usuario", "cod_Usuario = ?", new String[]{String.valueOf(cod_Usuario)});
            db.delete("Cronograma_dia", "cod_Cronograma = ?", new String[]{String.valueOf(cod_Cronograma)});
            db.delete("Questao", "cod_Usuario = ?", new String[]{String.valueOf(cod_Usuario)});
            db.delete("Redacao", "cod_Usuario = ?", new String[]{String.valueOf(cod_Usuario)});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return msg;
    }




}