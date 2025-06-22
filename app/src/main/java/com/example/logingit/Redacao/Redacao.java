package com.example.logingit.Redacao;

public class Redacao {
    private int id;
    private String tema;
    private int nota;
    private String analise;
    private boolean expandido;

    public Redacao(int id, String tema, int nota, String analise) {
        this.analise = analise;
        this.nota = nota;
        this.tema = tema;
        this.id = id;
        this.expandido = false;
    }

    public int getId() {
        return id;
    }

    public boolean isExpandido() {
        return expandido;
    }

    public void setExpandido(boolean expandido) {
        this.expandido = expandido;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getAnalise() {
        return analise;
    }

    public void setAnalise(String analise) {
        this.analise = analise;
    }
}
