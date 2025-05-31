package com.example.logingit;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public String excluirDados (String email) {
        String msg = "Registro Excluido";

        db = banco.getReadableDatabase();

        String condicao = "email =" + email;

        int linha;
        linha = db.delete("Usuario", condicao, null);

        if (linha < 1) {
            msg = "Erro ao excluir";
        }

        db.close();
        return msg;
    }




}