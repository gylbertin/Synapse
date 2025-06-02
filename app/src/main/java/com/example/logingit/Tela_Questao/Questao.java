package com.example.logingit.Tela_Questao;

import java.util.List;

public class Questao {
    public String pergunta;
    public List<String> alternativas;
    public int correta;

    public Questao(String _pergunta, List<String> _alternativas, int _correta) {
        this.pergunta = _pergunta;
        this.alternativas = _alternativas;
        this.correta = _correta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public List<String> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<String> alternativas) {
        this.alternativas = alternativas;
    }

    public int getCorreta() {
        return correta;
    }

    public void setCorreta(int correta) {
        this.correta = correta;
    }
}
