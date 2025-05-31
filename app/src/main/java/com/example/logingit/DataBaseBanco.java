package com.example.logingit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DataBaseBanco {
    public static void carregarDadosDoJson(Context context) {
        CriaBanco banco = new CriaBanco(context);
        SQLiteDatabase db = banco.getWritableDatabase();

        try {
            InputStream is = context.getAssets().open("DadosIniciais.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonStr = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(jsonStr);

            // Exames
            JSONArray exames = jsonObject.getJSONArray("exames");
            for (int i = 0; i < exames.length(); i++) {
                JSONObject exame = exames.getJSONObject(i);
                db.execSQL("INSERT OR IGNORE INTO Exame (nome_Exame, dia_final) VALUES (?, ?)",
                        new Object[]{exame.getString("nome"), exame.getString("dia_final")});
            }

            // Matérias
            JSONArray materias = jsonObject.getJSONArray("materias");
            for (int i = 0; i < materias.length(); i++) {
                JSONObject materia = materias.getJSONObject(i);
                db.execSQL("INSERT OR IGNORE INTO Materia (cod_Materia, nome_Materia, cor) VALUES (?, ?, ?)",
                        new Object[]{materia.getInt("id"), materia.getString("nome"), materia.getString("cor")});
            }

            // Conteúdos
            JSONArray conteudos = jsonObject.getJSONArray("conteudos");
            for (int i = 0; i < conteudos.length(); i++) {
                JSONObject conteudo = conteudos.getJSONObject(i);
                db.execSQL("INSERT INTO Conteudo (nome_Conteudo, cod_Materia, concluido, cod_Exame) VALUES (?, ?, ?, ?)",
                        new Object[]{
                                conteudo.getString("nome"),
                                conteudo.getInt("cod_materia"),
                                conteudo.getString("status"),
                                conteudo.getInt("cod_exame")
                        });
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        }
    }
