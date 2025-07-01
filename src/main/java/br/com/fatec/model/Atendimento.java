package br.com.fatec.model;

import java.time.LocalTime;

public class Atendimento {
    private static int proximoId = 1;
    private int idAtendimento;
    private String nomeCliente;
    private LocalTime horaChegada;
    private String servico;
    private String observacao;

    public Atendimento(String nomeCliente, LocalTime horaChegada, String servico, String observacao) {
        this.idAtendimento = proximoId++;
        this.nomeCliente = nomeCliente;
        this.horaChegada = horaChegada;
        this.servico = servico;
        this.observacao = observacao;
    }

    public int getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(int idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public LocalTime getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(LocalTime horaChegada) {
        this.horaChegada = horaChegada;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}