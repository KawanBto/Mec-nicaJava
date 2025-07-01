package br.com.fatec.model;

import java.time.LocalDate;

public class OrdemServico {
    private int id_ordem_servico;
    private int id_cliente;
    private int id_veiculo;
    private int id_funcionario;
    private LocalDate data_abertura;
    private LocalDate data_fechamento;
    private LocalDate data_saida_efetiva;
    private String descricao_problema;
    private String servicos_realizados;
    private double valor_total;
    private String status_os;

    public OrdemServico() {
    }

    public OrdemServico(int id_ordem_servico, int id_cliente, int id_veiculo, int id_funcionario,
                        LocalDate data_abertura, LocalDate data_fechamento, LocalDate data_saida_efetiva,
                        String descricao_problema, String servicos_realizados, double valor_total,
                        String status_os) {
        this.id_ordem_servico = id_ordem_servico;
        this.id_cliente = id_cliente;
        this.id_veiculo = id_veiculo;
        this.id_funcionario = id_funcionario;
        this.data_abertura = data_abertura;
        this.data_fechamento = data_fechamento;
        this.data_saida_efetiva = data_saida_efetiva;
        this.descricao_problema = descricao_problema;
        this.servicos_realizados = servicos_realizados;
        this.valor_total = valor_total;
        this.status_os = status_os;
    }

    public OrdemServico(int id_cliente, int id_veiculo, int id_funcionario,
                        LocalDate data_abertura, LocalDate data_fechamento, LocalDate data_saida_efetiva,
                        String descricao_problema, String servicos_realizados, double valor_total,
                        String status_os) {
        this.id_cliente = id_cliente;
        this.id_veiculo = id_veiculo;
        this.id_funcionario = id_funcionario;
        this.data_abertura = data_abertura;
        this.data_fechamento = data_fechamento;
        this.data_saida_efetiva = data_saida_efetiva;
        this.descricao_problema = descricao_problema;
        this.servicos_realizados = servicos_realizados;
        this.valor_total = valor_total;
        this.status_os = status_os;
    }

    public int getId_ordem_servico() {
        return id_ordem_servico;
    }

    public void setId_ordem_servico(int id_ordem_servico) {
        this.id_ordem_servico = id_ordem_servico;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_veiculo() {
        return id_veiculo;
    }

    public void setId_veiculo(int id_veiculo) {
        this.id_veiculo = id_veiculo;
    }

    public int getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public LocalDate getData_abertura() {
        return data_abertura;
    }

    public void setData_abertura(LocalDate data_abertura) {
        this.data_abertura = data_abertura;
    }

    public LocalDate getData_fechamento() {
        return data_fechamento;
    }

    public void setData_fechamento(LocalDate data_fechamento) {
        this.data_fechamento = data_fechamento;
    }

    public LocalDate getData_saida_efetiva() {
        return data_saida_efetiva;
    }

    public void setData_saida_efetiva(LocalDate data_saida_efetiva) {
        this.data_saida_efetiva = data_saida_efetiva;
    }

    public String getDescricao_problema() {
        return descricao_problema;
    }

    public void setDescricao_problema(String descricao_problema) {
        this.descricao_problema = descricao_problema;
    }

    public String getServicos_realizados() {
        return servicos_realizados;
    }

    public void setServicos_realizados(String servicos_realizados) {
        this.servicos_realizados = servicos_realizados;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public String getStatus_os() {
        return status_os;
    }

    public void setStatus_os(String status_os) {
        this.status_os = status_os;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemServico that = (OrdemServico) o;
        return id_ordem_servico == that.id_ordem_servico;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id_ordem_servico);
    }
}
