package br.com.fatec.model;

import java.util.Objects;

public class Veiculo {
    private int id_veiculo;
    private int id_cliente;
    private String nomeCliente;
    private String placa;
    private String marca;
    private String modelo;
    private int ano_fabricacao;
    private String cor;

    public Veiculo() {
    }

    public Veiculo(int id_veiculo, int id_cliente, String placa, String marca, String modelo, int ano_fabricacao, String cor) {
        this.id_veiculo = id_veiculo;
        this.id_cliente = id_cliente;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano_fabricacao = ano_fabricacao;
        this.cor = cor;
    }

    public Veiculo(int id_cliente, String placa, String marca, String modelo, int ano_fabricacao, String cor) {
        this.id_cliente = id_cliente;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano_fabricacao = ano_fabricacao;
        this.cor = cor;
    }
    
    public Veiculo(int id_veiculo, int id_cliente, String nomeCliente, String placa, String marca, String modelo, int ano_fabricacao, String cor) {
        this.id_veiculo = id_veiculo;
        this.id_cliente = id_cliente;
        this.nomeCliente = nomeCliente;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano_fabricacao = ano_fabricacao;
        this.cor = cor;
    }

    public int getId_veiculo() {
        return id_veiculo;
    }

    public void setId_veiculo(int id_veiculo) {
        this.id_veiculo = id_veiculo;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno_fabricacao() {
        return ano_fabricacao;
    }

    public void setAno_fabricacao(int ano_fabricacao) {
        this.ano_fabricacao = ano_fabricacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id_veiculo=" + id_veiculo +
                ", id_cliente=" + id_cliente +
                ", nomeCliente='" + (nomeCliente != null ? nomeCliente : "N/A") + '\'' + // <-- Adicionado para exibir o nome do cliente
                ", placa='" + placa + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", ano_fabricacao=" + ano_fabricacao +
                ", cor='" + cor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return id_veiculo == veiculo.id_veiculo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_veiculo);
    }
}