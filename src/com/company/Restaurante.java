package com.company;

import java.io.*;
import java.util.*;

public class Restaurante {
    Random r = new Random();
    private int cicloAtual;
    private int contagemGrupos;

    private Queue<Grupo> filaCaixa1 = new ArrayDeque<>();
    private Queue<Grupo> filaCaixa2 = new ArrayDeque<>();

    Caixa caixa1 = new Caixa();
    Caixa caixa2 = new Caixa();

    private Queue<Pedido> filaPedidos = new ArrayDeque<>();
    private List<Pedido> listaPedidosProntos = new ArrayList<>();
    private Cozinha cozinha = new Cozinha();

    private Assento[] assentos = new Assento[14];

    private Queue<Grupo> filaBalcao = new ArrayDeque<>();
    private Queue<Grupo> filaMesa2Lugares = new ArrayDeque<>();
    private Queue<Grupo> filaMesa4Lugares = new ArrayDeque<>();

    public Restaurante() {
        contagemGrupos = 0;

        for (int i = 0; i < 14; i++) {
            if (i < 6) {
                assentos[i] = new Assento(1);
            } else if (i < 10) {
                assentos[i] = new Assento(2);
            } else {
                assentos[i] = new Assento(4);
            }
        }
    }

    public void iniciarSimulacao(int totalCiclos) {
        cicloAtual = 0;
        try (PrintWriter out = new PrintWriter(new FileWriter("quantidadeEmCadaFila.csv"))) {
            out.println("Ciclo,Fila do caixa 1,Fila do caixa 2,Fila de Pedidos,Lista de Pedidos Prontos,Fila do Balcao" +
                    ",Fila das Mesas de 2 Lugares,Fila das Mesas de 4 lugares");

            while (cicloAtual < totalCiclos) {
                passaCiclo();
                out.println(cicloAtual + "," + filaCaixa1.size() + "," + filaCaixa2.size() + "," + filaPedidos.size() +
                        "," + listaPedidosProntos.size() + "," + filaBalcao.size() + "," + filaMesa2Lugares.size() +
                        "," + filaMesa4Lugares.size() + ",");
                cicloAtual++;
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void passaCiclo() {
        if (cicloAtual % 10 == 0) {
            chegaGrupo();
        }

        atendenterCaixas();

        sentarClientes();

        cozinhar();

        servir();

        comer();

        printStatusRestaurante();

    }

    private void cozinhar() {
        Pedido[] prontos = cozinha.pedidosProntos();
        for (int i = 0; i < 3; i++) {
            if (prontos[i] != null) {
                if (prontos[i].getId() != -1) {
                    listaPedidosProntos.add(prontos[i]);
                }
                if (filaPedidos.peek() != null) {
                    cozinha.chegaPedido(filaPedidos.poll());
                }
            }
        }
    }

    private void chegaGrupo() {
        int tamanho = r.nextInt(4) + 1;
        Grupo novoGrupo = new Grupo(contagemGrupos, tamanho);
        contagemGrupos++;

        //calcula o tamanho das filas, somando um se o caixa esta atendendo atualmente
        int fila1Tamanho = filaCaixa1.size() + caixa1.getTempoRestante() == 0 ? 0 : 1;
        int fila2Tamanho = filaCaixa2.size() + caixa2.getTempoRestante() == 0 ? 0 : 1;

        if (fila1Tamanho > fila2Tamanho) {
            filaCaixa2.add(novoGrupo);
        } else {
            filaCaixa1.add(novoGrupo);
        }


    }

    private void atendenterCaixas() {
        if (caixa1.atendimentoFinalizado()) {
            if (caixa1.getGrupo() != null) {
                encaminharParaMesa(caixa1.getGrupo());
                filaPedidos.add(new Pedido(caixa1.getGrupo().getId(), r.nextInt(31) + 50));
                caixa1.setGrupo(null);
            }
            if (!filaCaixa1.isEmpty()) {
                caixa1.iniciarAtendimento(filaCaixa1.poll(), r.nextInt(16) + 5);
            }
        }
        if (caixa2.atendimentoFinalizado()) {
            if (caixa2.getGrupo() != null) {
                encaminharParaMesa(caixa2.getGrupo());
                filaPedidos.add(new Pedido(caixa2.getGrupo().getId(), r.nextInt(31) + 50));
                caixa2.setGrupo(null);
            }
            if (!filaCaixa2.isEmpty()) {
                caixa2.iniciarAtendimento(filaCaixa2.poll(), r.nextInt(16) + 5);
            }
        }
    }

    private void encaminharParaMesa(Grupo grupo) {
        int tamanho = grupo.getTamanho();
        switch (tamanho) {
            case 1 -> filaBalcao.add(grupo);
            case 2 -> filaMesa2Lugares.add(grupo);
            default -> filaMesa4Lugares.add(grupo);
        }
    }

    private void sentarClientes() {
        for (int i = 0; i < 14; i++) {
            if (assentos[i].getOcupantes() == null) {
                int tamanho = assentos[i].getTamanho();
                switch (tamanho) {
                    case 1 -> assentos[i].sentar(filaBalcao.poll());
                    case 2 -> assentos[i].sentar(filaMesa2Lugares.poll());
                    default -> assentos[i].sentar(filaMesa4Lugares.poll());
                }
            }
        }
    }

    private void servir() {
        for (int i = 0; i < 14; i++) {
            if (assentos[i].getOcupantes() != null) {
                for (int j = 0; j < listaPedidosProntos.size(); j++) {
                    if (assentos[i].getOcupantes().getId() == listaPedidosProntos.get(j).getId()) {
                        assentos[i].comecaComer(r.nextInt(41) + 80);
                        listaPedidosProntos.remove(j);
                        j--;
                    }
                }
            }
        }
    }

    private void comer() {
        for (int i = 0; i < 14; i++) {
            if (assentos[i].terminouDeComer()) {
                assentos[i].sair();
            }
        }
    }

    private void printStatusRestaurante() {
        System.out.println("===-===-===-===-===-===-===-===-===-=== Ciclo " + cicloAtual + " ===-===-===-===-===-===-===-===-===-===");

        System.out.print("Fila caixa 1: ");

        for (Grupo p : filaCaixa1) {
            System.out.print(p.getId());
        }

        System.out.print("\nFila caixa 2: ");
        for (Grupo p : filaCaixa2) {
            System.out.print(p.getId());
        }

        System.out.println("\nCaixa 1" + caixa1.status());
        System.out.println("Caixa 2" + caixa2.status() + "\n");

        System.out.print("Fila de pedidos: ");
        for (Pedido p : filaPedidos) {
            System.out.print(p.getId() + " ");
        }

        System.out.println("\nLista de pedidos prontos: ");
        for (Pedido p : listaPedidosProntos) {
            System.out.print(p.getId() + " ");
        }

        System.out.println(cozinha.status());

        System.out.print("Fila do balcao: ");
        for (Grupo p : filaBalcao) {
            System.out.print(p.getId() + " ");
        }

        System.out.print("\nFila das mesas de 2 lugares: ");
        for (Grupo p : filaMesa2Lugares) {
            System.out.print(p.getId() + " ");
        }

        System.out.print("\nFila das mesas de 4 lugares: ");
        for (Grupo p : filaMesa4Lugares) {
            System.out.print(p.getId() + " ");
        }

        System.out.print("\n\n");
        for (int i = 0; i < 14; i++) {
            String statusMesa = "";
            if (i < 6) {
                statusMesa += "Balcao ";
                statusMesa += (i + 1) + ": " + assentos[i].status();
            } else if (i < 10) {
                statusMesa += "Mesa 2 lugares ";
                statusMesa += ((i + 1) % 6) + ": " + assentos[i].status();
            } else {
                statusMesa += "Mesa 4 lugares ";
                statusMesa += ((i + 1) % 10) + ": " + assentos[i].status();
            }

            System.out.println(statusMesa);
        }
    }
}

