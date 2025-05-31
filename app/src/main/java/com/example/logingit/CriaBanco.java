package com.example.logingit;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CriaBanco extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "banco_Synapse.db";

    private static final int VERSAO = 5;
    private Context context;

    public CriaBanco(@Nullable Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Exame ("
                + "cod_Exame integer primary key autoincrement,"
                + "nome_Exame text not null,"
                + "dia_Final text not null)";
        db.execSQL(sql);

        sql = "CREATE TABLE Usuario ("
                + "cod_Usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome_Usuario TEXT, "
                + "email TEXT UNIQUE NOT NULL, "
                + "senha TEXT NOT NULL, "
                + "cod_Exame INT DEFAULT 1, "
                + "data_Criacao text DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (cod_Exame) REFERENCES Exame(cod_Exame)"
                + ")";
        db.execSQL(sql);

        sql = "CREATE TABLE Cronograma ("
                + "cod_Cronograma integer primary key autoincrement,"
                + "cod_Usuario integer,"
                + "data_Criacao text DEFAULT CURRENT_TIMESTAMP,"
                + "horas_Diarias int not null,"
                + "cod_Exame integer,"
                + "foreign key (cod_Usuario) references Usuario(cod_Usuario),"
                + "foreign key (cod_Exame) references Exame(cod_Exame))";
        db.execSQL(sql);

        sql = "CREATE TABLE Cronograma_dia ("
                + "cod_Dia integer primary key autoincrement,"
                + "cod_Cronograma integer,"
                + "dia_Semana text not null, "
                + "dia_Estudo integer not null check(dia_Estudo in (0,1)),"
                + "foreign key (cod_Cronograma) references Cronograma(cod_Cronograma))";
        db.execSQL(sql);

        sql = "CREATE TABLE Materia ("
                + "cod_Materia integer primary key autoincrement,"
                + "nome_Materia text not null,"
                + "cor text unique not null)";
        db.execSQL(sql);

        sql = "CREATE TABLE Conteudo ("
                + "cod_Conteudo integer primary key autoincrement,"
                + "nome_Conteudo text not null,"
                + "cod_Materia integer,"
                + "concluido boolean not null default 0 check (concluido in (0, 1)),"
                + "cod_Exame integer,"
                + "foreign key (cod_Materia) references Materia(cod_Materia),"
                + "foreign key (cod_Exame) references Exame(cod_Exame))";
        db.execSQL(sql);

        sql = "CREATE TABLE Questao ("
                + "cod_Questao integer primary key autoincrement,"
                + "num_Pergunta integer not null,"
                + "num_Acerto integer default 0,"
                + "media decimal(2,2) default 0.00,"
                + "cod_Usuario integer,"
                + "foreign key (cod_Usuario) references Usuario(cod_Usuario))";
        db.execSQL(sql);

        sql = "CREATE TABLE Redacao ("
                + "cod_Redacao integer primary key autoincrement,"
                + "tema_redacao text not null,"
                + "text_Redacao text not null,"
                + "nota_Redacao decimal(3,2) default 0.00,"
                + "text_Analise text,"
                + "cod_Usuario integer,"
                + "foreign key (cod_Usuario) references Usuario(cod_Usuario))";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Exame");
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Cronograma");
        db.execSQL("DROP TABLE IF EXISTS Cronograma_dia");
        db.execSQL("DROP TABLE IF EXISTS Materia");
        db.execSQL("DROP TABLE IF EXISTS Conteudo");
        db.execSQL("DROP TABLE IF EXISTS Questao");
        db.execSQL("DROP TABLE IF EXISTS Redacao");
        onCreate(db);
        SharedPreferences prefs = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("dados_inseridos", false).apply();

    }

}
