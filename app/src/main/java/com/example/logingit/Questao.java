package com.example.logingit;

public class Questao {
    private String materia;
    private String conteudo;
    private int total;
    private int acertos;

    public Questao(String materia, String conteudo, int total, int acertos) {
        this.materia = materia;
        this.conteudo = conteudo;
        this.total = total;
        this.acertos = acertos;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAcertos() {
        return acertos;
    }

    public void setAcertos(int acertos) {
        this.acertos = acertos;
    }
}
