package br.com.fatec.DAO;

import br.com.fatec.model.Cliente;
import br.com.fatec.persistencia.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements DAO<Cliente> {

    @Override
    public boolean cadastrar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome_completo, cpf, telefone, email, cep, endereco) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome_completo());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getCep());
            ps.setString(6, cliente.getEndereco());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId_cliente(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar Cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome_completo = ?, cpf = ?, telefone = ?, email = ?, cep = ?, endereco = ? WHERE id_cliente = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getNome_completo());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getCep());
            ps.setString(6, cliente.getEndereco());
            ps.setInt(7, cliente.getId_cliente());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao alterar Cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
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
            System.err.println("Erro ao excluir Cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nome_completo"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cep"),
                    rs.getString("endereco")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Cliente por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return cliente;
    }

    @Override
    public List<Cliente> buscarTodos() {
        String sql = "SELECT * FROM cliente";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nome_completo"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cep"),
                    rs.getString("endereco")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os Clientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return clientes;
    }

    @Override
    public List<Cliente> listar(String criterio) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome_completo LIKE ? OR cpf LIKE ? OR email LIKE ?";
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
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nome_completo"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("cep"),
                    rs.getString("endereco")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Clientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return clientes;
    }
}