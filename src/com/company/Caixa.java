package com.company;

public class Caixa {
    private Grupo grupo;
    private int tempoRestante = 0;

    public Caixa() {
    }

    public void iniciarAtendimento(Grupo grupo, int tempoRestante) {
        this.grupo = grupo;
        this.tempoRestante = tempoRestante;
    }

    public boolean atendimentoFinalizado() {
        if (tempoRestante == 0) {
            return true;
        } else {
            tempoRestante--;
            return false;
        }
    }

    public int getTempoRestante() {
        return tempoRestante;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String status(){
        String status = "";
        if (tempoRestante == 0){
            status += " esperando clientes";
        } else {
            status += " atendendo grupo " + grupo.getId() + ", tempo restante: " + tempoRestante;
        }
        return status;
    }
}
