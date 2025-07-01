package br.com.fatec.model;

import java.math.BigDecimal;
import java.util.Arrays;

public class CatalogoDeServicos {
    private static final int CAPACIDADE_MAXIMA = 50;
    private Servico[] servicos;
    private int quantidadeAtual;
    private static int proximoId = 1;

    private static CatalogoDeServicos instance;

    private CatalogoDeServicos() {
        servicos = new Servico[CAPACIDADE_MAXIMA];
        quantidadeAtual = 0;
        adicionarServico(new Servico(proximoId++, "Revisão Básica", "Verificação de itens essenciais do veículo.", new BigDecimal("150.00")));
        adicionarServico(new Servico(proximoId++, "Troca de Óleo", "Substituição do óleo do motor e filtro.", new BigDecimal("100.00")));
        adicionarServico(new Servico(proximoId++, "Alinhamento e Balanceamento", "Ajuste de suspensão e pneus.", new BigDecimal("80.00")));
        adicionarServico(new Servico(proximoId++, "Troca de Pneus", "Substituição de pneus.", new BigDecimal("50.00")));
        adicionarServico(new Servico(proximoId++, "Revisão Completa", "Revisão detalhada com troca de fluidos.", new BigDecimal("300.00")));
    }

    public static CatalogoDeServicos getInstance() {
        if (instance == null) {
            instance = new CatalogoDeServicos();
        }
        return instance;
    }

    public boolean adicionarServico(Servico servico) {
        if (quantidadeAtual < CAPACIDADE_MAXIMA) {
            servicos[quantidadeAtual] = servico;
            quantidadeAtual++;
            return true;
        }
        return false;
    }

    public Servico[] getTodosServicos() {
        return Arrays.copyOf(servicos, quantidadeAtual);
    }

    public Servico buscarServicoPorNome(String nome) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (servicos[i].getNomeServico().equalsIgnoreCase(nome)) {
                return servicos[i];
            }
        }
        return null;
    }

    public Servico buscarServicoPorId(int id) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (servicos[i].getIdServico() == id) {
                return servicos[i];
            }
        }
        return null;
    }

    public boolean atualizarServico(Servico servicoAtualizado) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (servicos[i].getIdServico() == servicoAtualizado.getIdServico()) {
                servicos[i] = servicoAtualizado;
                return true;
            }
        }
        return false;
    }

    public boolean removerServico(int id) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (servicos[i].getIdServico() == id) {
                for (int j = i; j < quantidadeAtual - 1; j++) {
                    servicos[j] = servicos[j + 1];
                }
                servicos[quantidadeAtual - 1] = null;
                quantidadeAtual--;
                return true;
            }
        }
        return false;
    }
}
