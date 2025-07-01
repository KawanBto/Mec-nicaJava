package br.com.fatec.DAO;

import br.com.fatec.model.Veiculo;
import br.com.fatec.persistencia.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO implements DAO<Veiculo> {

    @Override
    public boolean cadastrar(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (id_cliente, placa, marca, modelo, ano_fabricacao, cor) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, veiculo.getId_cliente());
            ps.setString(2, veiculo.getPlaca());
            ps.setString(3, veiculo.getMarca());
            ps.setString(4, veiculo.getModelo());
            ps.setInt(5, veiculo.getAno_fabricacao());
            ps.setString(6, veiculo.getCor());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        veiculo.setId_veiculo(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar Veículo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET id_cliente = ?, placa = ?, marca = ?, modelo = ?, ano_fabricacao = ?, cor = ? WHERE id_veiculo = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        boolean sucesso = false;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, veiculo.getId_cliente());
            ps.setString(2, veiculo.getPlaca());
            ps.setString(3, veiculo.getMarca());
            ps.setString(4, veiculo.getModelo());
            ps.setInt(5, veiculo.getAno_fabricacao());
            ps.setString(6, veiculo.getCor());
            ps.setInt(7, veiculo.getId_veiculo());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                sucesso = true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao alterar Veículo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM veiculo WHERE id_veiculo = ?";
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
            System.err.println("Erro ao excluir Veículo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return sucesso;
    }

    @Override
    public Veiculo buscarPorId(int id) {
        String sql = "SELECT v.*, c.nome_completo AS nome_cliente FROM veiculo v JOIN cliente c ON v.id_cliente = c.id_cliente WHERE v.id_veiculo = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Veiculo veiculo = null;

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                veiculo = new Veiculo(
                    rs.getInt("id_veiculo"),
                    rs.getInt("id_cliente"),
                    rs.getString("nome_cliente"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("ano_fabricacao"),
                    rs.getString("cor")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Veículo por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return veiculo;
    }

    @Override
    public List<Veiculo> buscarTodos() {
        String sql = "SELECT v.*, c.nome_completo AS nome_cliente FROM veiculo v JOIN cliente c ON v.id_cliente = c.id_cliente";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Veiculo> veiculos = new ArrayList<>();

        try {
            conn = Banco.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Veiculo veiculo = new Veiculo(
                    rs.getInt("id_veiculo"),
                    rs.getInt("id_cliente"),
                    rs.getString("nome_cliente"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("ano_fabricacao"),
                    rs.getString("cor")
                );
                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os Veículos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return veiculos;
    }

    @Override
    public List<Veiculo> listar(String criterio) {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT v.*, c.nome_completo AS nome_cliente FROM veiculo v JOIN cliente c ON v.id_cliente = c.id_cliente WHERE v.placa LIKE ? OR v.marca LIKE ? OR v.modelo LIKE ? OR v.cor LIKE ? OR c.nome_completo LIKE ?";
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
            ps.setString(5, searchCriterion);
            rs = ps.executeQuery();

            while (rs.next()) {
                Veiculo veiculo = new Veiculo(
                    rs.getInt("id_veiculo"),
                    rs.getInt("id_cliente"),
                    rs.getString("nome_cliente"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("ano_fabricacao"),
                    rs.getString("cor")
                );
                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Veículos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(ps);
            Banco.closeConnection(conn);
        }
        return veiculos;
    }
}