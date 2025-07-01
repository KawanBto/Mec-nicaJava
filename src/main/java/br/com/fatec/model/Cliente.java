package br.com.fatec.model;

public class Cliente {
    private int id_cliente;
    private String nome_completo;
    private String cpf;
    private String telefone;
    private String email;
    private String cep;
    private String endereco;

    public Cliente() {
    }

    public Cliente(int id_cliente, String nome_completo) {
        this.id_cliente = id_cliente;
        this.nome_completo = nome_completo;
    }

    public Cliente(int id_cliente, String nome_completo, String cpf, String telefone, String email, String cep, String endereco) {
        this.id_cliente = id_cliente;
        this.nome_completo = nome_completo;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
        this.endereco = endereco;
    }

    public Cliente(String nome_completo, String cpf, String telefone, String email, String cep, String endereco) {
        this.nome_completo = nome_completo;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
        this.endereco = endereco;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getCep() {
        return cep;
    }

    public String getEndereco() {
        return endereco;
    }

    // Setters
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Cliente{" +
               "id_cliente=" + id_cliente +
               ", nome_completo='" + nome_completo + '\'' +
               ", cpf='" + cpf + '\'' +
               ", telefone='" + telefone + '\'' +
               ", email='" + email + '\'' +
               ", cep='" + cep + '\'' +
               ", endereco='" + endereco + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id_cliente == cliente.id_cliente;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id_cliente);
    }
}
