package br.com.fatec.DAO;

import br.com.fatec.model.Funcionario;
import br.com.fatec.persistencia.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO implements DAO<Funcionario> {

    @Override
    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome_completo, cpf, telefone, email, cargo, salario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, funcionario.getNome_completo());
            ps.setString(2, funcionario.getCpf());
            ps.setString(3, funcionario.getTelefone());
            ps.setString(4, funcionario.getEmail());
            ps.setString(5, funcionario.getCargo());
            ps.setDouble(6, funcionario.getSalario());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        funcionario.setId_funcionario(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar Funcionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public boolean alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome_completo = ?, cpf = ?, telefone = ?, email = ?, cargo = ?, salario = ? WHERE id_funcionario = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, funcionario.getNome_completo());
            ps.setString(2, funcionario.getCpf());
            ps.setString(3, funcionario.getTelefone());
            ps.setString(4, funcionario.getEmail());
            ps.setString(5, funcionario.getCargo());
            ps.setDouble(6, funcionario.getSalario());
            ps.setInt(7, funcionario.getId_funcionario());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao alterar Funcionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM funcionario WHERE id_funcionario = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir Funcionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public Funcionario buscarPorId(int id) {
        String sql = "SELECT * FROM funcionario WHERE id_funcionario = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Funcionario funcionario = null;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                funcionario = new Funcionario(
                    rs.getInt("id_funcionario"),
                    rs.getString("nome_completo"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cargo"),
                    rs.getDouble("salario")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Funcionário por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return funcionario;
    }

    @Override
    public List<Funcionario> buscarTodos() {
        String sql = "SELECT * FROM funcionario";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Funcionario> funcionarios = new ArrayList<>();

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                    rs.getInt("id_funcionario"),
                    rs.getString("nome_completo"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cargo"),
                    rs.getDouble("salario")
                );
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os Funcionários: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return funcionarios;
    }

    @Override
    public List<Funcionario> listar(String criterio) {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionario WHERE nome_completo LIKE ? OR cpf LIKE ? OR cargo LIKE ? OR email LIKE ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            String searchCriterion = "%" + criterio + "%";
            ps.setString(1, searchCriterion);
            ps.setString(2, searchCriterion);
            ps.setString(3, searchCriterion);
            ps.setString(4, searchCriterion);
            rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                    rs.getInt("id_funcionario"),
                    rs.getString("nome_completo"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cargo"),
                    rs.getDouble("salario")
                );
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Funcionários: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return funcionarios;
    }
}