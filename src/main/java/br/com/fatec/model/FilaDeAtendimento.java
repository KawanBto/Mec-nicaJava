package br.com.fatec.model;

import java.util.Arrays;

public class FilaDeAtendimento {
    private static final int CAPACIDADE_MAXIMA = 100;
    private Atendimento[] atendimentos;
    private int quantidadeAtual;

    private static FilaDeAtendimento instance;

    private FilaDeAtendimento() {
        atendimentos = new Atendimento[CAPACIDADE_MAXIMA];
        quantidadeAtual = 0;
    }

    public static FilaDeAtendimento getInstance() {
        if (instance == null) {
            instance = new FilaDeAtendimento();
        }
        return instance;
    }

    public boolean adicionarAtendimento(Atendimento atendimento) {
        if (quantidadeAtual < CAPACIDADE_MAXIMA) {
            atendimentos[quantidadeAtual] = atendimento;
            quantidadeAtual++;
            return true;
        }
        return false;
    }

    public Atendimento[] getAtendimentos() {
        return Arrays.copyOf(atendimentos, quantidadeAtual);
    }

    public Atendimento buscarAtendimentoPorId(int id) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (atendimentos[i].getIdAtendimento() == id) {
                return atendimentos[i];
            }
        }
        return null;
    }

    public boolean atualizarAtendimento(Atendimento atendimentoAtualizado) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (atendimentos[i].getIdAtendimento() == atendimentoAtualizado.getIdAtendimento()) {
                atendimentos[i] = atendimentoAtualizado;
                return true;
            }
        }
        return false;
    }

    public boolean removerAtendimento(int id) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (atendimentos[i].getIdAtendimento() == id) {
                for (int j = i; j < quantidadeAtual - 1; j++) {
                    atendimentos[j] = atendimentos[j + 1];
                }
                atendimentos[quantidadeAtual - 1] = null;
                quantidadeAtual--;
                return true;
            }
        }
        return false;
    }
}