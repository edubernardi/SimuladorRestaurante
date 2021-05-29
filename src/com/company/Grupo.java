package com.company;

import org.w3c.dom.ranges.Range;

import java.util.Random;

public class Grupo {
    private int tamanho;
    private int id;

    public Grupo(int id, int tamanho) {
        this.tamanho = tamanho;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTamanho() {
        return tamanho;
    }
}
