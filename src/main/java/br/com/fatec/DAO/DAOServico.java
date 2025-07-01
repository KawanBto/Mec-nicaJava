package br.com.fatec.DAO;

import br.com.fatec.model.Servico;
import br.com.fatec.persistencia.Banco;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOServico {

    public List<Servico> listarTodos() {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT id_servico, nome_servico, descricao, preco_padrao FROM servico ORDER BY nome_servico";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idServico = rs.getInt("id_servico");
                String nomeServico = rs.getString("nome_servico");
                String descricao = rs.getString("descricao");
                BigDecimal precoPadrao = rs.getBigDecimal("preco_padrao");
                
                Servico servico = new Servico(idServico, nomeServico, descricao, precoPadrao);
                servicos.add(servico);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar serviços do banco de dados: " + e.getMessage());
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
        return servicos;
    }

    public Servico buscarPorId(int id) {
        Servico servico = null;
        String sql = "SELECT id_servico, nome_servico, descricao, preco_padrao FROM servico WHERE id_servico = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int idServico = rs.getInt("id_servico");
                String nomeServico = rs.getString("nome_servico");
                String descricao = rs.getString("descricao");
                BigDecimal precoPadrao = rs.getBigDecimal("preco_padao");
                
                servico = new Servico(idServico, nomeServico, descricao, precoPadrao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar serviço por ID no banco de dados: " + e.getMessage());
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
        return servico;
    }

    public boolean adicionar(Servico servico) {
        String sql = "INSERT INTO servico (nome_servico, descricao, preco_padrao) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, servico.getNomeServico());
            stmt.setString(2, servico.getDescricao());
            stmt.setBigDecimal(3, servico.getPrecoPadrao());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar serviço ao banco de dados: " + e.getMessage());
            return false;
        } finally {
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    public boolean atualizar(Servico servico) {
        String sql = "UPDATE servico SET nome_servico = ?, descricao = ?, preco_padrao = ? WHERE id_servico = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Banco.getConnection(); 
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, servico.getNomeServico());
            stmt.setString(2, servico.getDescricao());
            stmt.setBigDecimal(3, servico.getPrecoPadrao());
            stmt.setInt(4, servico.getIdServico());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar serviço no banco de dados: " + e.getMessage());
            return false;
        } finally {
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM servico WHERE id_servico = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Banco.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover serviço do banco de dados: " + e.getMessage());
            return false;
        } finally {
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
    }
}
