package br.com.fatec.model;

public class Funcionario {
    private int id_funcionario;
    private String nome_completo;
    private String cpf;
    private String telefone;
    private String email;
    private String cargo;
    private double salario;

    public Funcionario() {
    }

    public Funcionario(int id_funcionario, String nome_completo, String cpf, String telefone, String email, String cargo, double salario) {
        this.id_funcionario = id_funcionario;
        this.nome_completo = nome_completo;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cargo = cargo;
        this.salario = salario;
    }

    public Funcionario(String nome_completo, String cpf, String telefone, String email, String cargo, double salario) {
        this.nome_completo = nome_completo;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cargo = cargo;
        this.salario = salario;
    }

    public int getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
               "id_funcionario=" + id_funcionario +
               ", nome_completo='" + nome_completo + '\'' +
               ", cpf='" + cpf + '\'' +
               ", telefone='" + telefone + '\'' +
               ", email='" + email + '\'' +
               ", cargo='" + cargo + '\'' +
               ", salario=" + salario +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario funcionario = (Funcionario) o;
        return id_funcionario == funcionario.id_funcionario;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id_funcionario);
    }

}