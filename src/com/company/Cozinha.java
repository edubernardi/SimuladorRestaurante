package com.company;

public class Cozinha {
    private Pedido vazio = new Pedido(-1, -1);
    private Pedido[] pedidos = new Pedido[]{vazio, vazio, vazio};

    public Cozinha() {
    }

    public Pedido[] pedidosProntos() {
        Pedido[] prontos = new Pedido[3];
        for (int i = 0; i < 3; i++) {
            if (pedidos[i] == vazio || pedidos[i].getTempoRestante() == 0) {
                prontos[i] = pedidos[i];
                pedidos[i] = vazio;
            } else {
                pedidos[i].setTempoRestante(pedidos[i].getTempoRestante() - 1);
            }
        }
        return prontos;
    }

    public void chegaPedido(Pedido pedido) {
        for (int i = 0; i < 3; i++) {
            if (pedidos[i].getTempoRestante() == -1) {
                pedidos[i] = pedido;
                break;
            }
        }
    }

    public String status() {
        String status = "";
        for (int i = 0; i < 3; i++) {
            status += "\nCozinheiro " + (i + 1);
            if (pedidos[i] == vazio) {
                status += " esperando pedido";
            } else {
                status += " preparando pedido " + pedidos[i].getId() + ", tempo restante: " + pedidos[i].getTempoRestante();
            }
        }
        return status + "\n";
    }
}
