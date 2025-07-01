package br.com.fatec.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrdemServicoDetalhes {
    private int idOrdemServico;
    private String nomeCliente;
    private String telefoneCliente;
    private String placaVeiculo;
    private String marcaVeiculo;
    private String modeloVeiculo;
    private String nomeFuncionario;
    private String observacoes;
    private String servicosRealizados;
    private BigDecimal valorTotal;
    private String statusOs; 
    private LocalDate dataEntrada; 
    private LocalDate dataSaidaPrevista; 
    private LocalDate dataSaidaEfetiva; 

    public OrdemServicoDetalhes() {
    }

    public OrdemServicoDetalhes(int idOrdemServico, String nomeCliente, String telefoneCliente, String placaVeiculo,
                                String marcaVeiculo, String modeloVeiculo, String nomeFuncionario,
                                String observacoes, String servicosRealizados, BigDecimal valorTotal,
                                String statusOs, LocalDate dataEntrada, LocalDate dataSaidaPrevista, LocalDate dataSaidaEfetiva) {
        this.idOrdemServico = idOrdemServico;
        this.nomeCliente = nomeCliente;
        this.telefoneCliente = telefoneCliente;
        this.placaVeiculo = placaVeiculo;
        this.marcaVeiculo = marcaVeiculo;
        this.modeloVeiculo = modeloVeiculo;
        this.nomeFuncionario = nomeFuncionario;
        this.observacoes = observacoes;
        this.servicosRealizados = servicosRealizados;
        this.valorTotal = valorTotal;
        this.statusOs = statusOs;
        this.dataEntrada = dataEntrada;
        this.dataSaidaPrevista = dataSaidaPrevista;
        this.dataSaidaEfetiva = dataSaidaEfetiva;
    }

    public int getIdOrdemServico() {
        return idOrdemServico;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public String getMarcaVeiculo() {
        return marcaVeiculo;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getServicosRealizados() {
        return servicosRealizados;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getStatusOs() { 
        return statusOs;
    }

    public LocalDate getDataEntrada() { 
        return dataEntrada;
    }

    public LocalDate getDataSaidaPrevista() { 
        return dataSaidaPrevista;
    }

    public LocalDate getDataSaidaEfetiva() {
        return dataSaidaEfetiva;
    }

    public void setIdOrdemServico(int idOrdemServico) {
        this.idOrdemServico = idOrdemServico;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public void setMarcaVeiculo(String marcaVeiculo) {
        this.marcaVeiculo = marcaVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setServicosRealizados(String servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setStatusOs(String statusOs) {
        this.statusOs = statusOs;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public void setDataSaidaPrevista(LocalDate dataSaidaPrevista) {
        this.dataSaidaPrevista = dataSaidaPrevista;
    }

    public void setDataSaidaEfetiva(LocalDate dataSaidaEfetiva) {
        this.dataSaidaEfetiva = dataSaidaEfetiva;
    }
}
