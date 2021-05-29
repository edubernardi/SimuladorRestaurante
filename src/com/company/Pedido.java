package com.company;

public class Pedido {
    private int id;
    private int tempoRestante;

    public Pedido(int id, int tempoRestante) {
        this.id = id;
        this.tempoRestante = tempoRestante;
    }

    public int getId() {
        return id;
    }

    public int getTempoRestante() {
        return tempoRestante;
    }

    public void setTempoRestante(int tempoRestante) {
        this.tempoRestante = tempoRestante;
    }
}
