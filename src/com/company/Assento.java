package com.company;

public class Assento {
    private int tamanho;
    private Grupo ocupantes;
    private int tempoRestante;

    public Assento(int tamanho) {
        this.tamanho = tamanho;
        this.ocupantes = null;
        this.tempoRestante = -1;
    }

    public void sentar(Grupo grupo){
        this.ocupantes = grupo;
    }

    public Grupo getOcupantes() {
        return ocupantes;
    }

    public void comecaComer(int tempoRestante){
        this.tempoRestante = tempoRestante;
    }

    public boolean terminouDeComer(){
        if (tempoRestante == 0){
            return true;
        }
        if (tempoRestante != -1){
            tempoRestante--;
        }
        return false;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void sair(){
        this.ocupantes = null;
        this.tempoRestante = -1;
    }

    public String status(){
        String status = "";
        if (ocupantes != null){
            status += "Grupo " + getOcupantes().getId() +  ", Numero de ocupantes: " + ocupantes.getTamanho();
            if (tempoRestante == -1){
                status += " Aguardando pedido";
            } else {
                status += ", Comendo, tempo restante: " + tempoRestante;
            }
        } else {
            status += "Vazia";
        }
        return status;
    }
}
