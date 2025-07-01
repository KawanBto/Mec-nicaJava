package br.com.fatec.model;

import java.math.BigDecimal;

public class Servico {
    private int idServico;
    private String nomeServico;
    private String descricao;
    private BigDecimal precoPadrao;

    public Servico(int idServico, String nomeServico, String descricao, BigDecimal precoPadrao) {
        this.idServico = idServico;
        this.nomeServico = nomeServico;
        this.descricao = descricao;
        this.precoPadrao = precoPadrao;
    }

    public int getIdServico() {
        return idServico;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPrecoPadrao() {
        return precoPadrao;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrecoPadrao(BigDecimal precoPadrao) {
        this.precoPadrao = precoPadrao;
    }

    @Override
    public String toString() {
        return nomeServico;
    }
}
