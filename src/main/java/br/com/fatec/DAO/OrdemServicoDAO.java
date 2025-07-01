package br.com.fatec.DAO;

import br.com.fatec.model.OrdemServico;
import br.com.fatec.persistencia.Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO implements DAO<OrdemServico> {

    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;


    private static final String SERVICOS_DELIMITER = "\n---SERVICOS_REALIZADOS_START---\n";

    public OrdemServicoDAO() {
    }

    @Override
    public boolean cadastrar(OrdemServico os) {
        String observacoesCombinadas = os.getDescricao_problema() + SERVICOS_DELIMITER + os.getServicos_realizados();

        String sql = "INSERT INTO ordem_servico (id_cliente, id_veiculo, id_funcionario_responsavel, data_entrada, data_saida_prevista, data_saida_efetiva, observacoes, valor_total, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = Banco.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, os.getId_cliente());
            pst.setInt(2, os.getId_veiculo());
            pst.setInt(3, os.getId_funcionario());
            pst.setDate(4, java.sql.Date.valueOf(os.getData_abertura()));
            pst.setDate(5, os.getData_fechamento() != null ? java.sql.Date.valueOf(os.getData_fechamento()) : null);
            pst.setDate(6, os.getData_saida_efetiva() != null ? java.sql.Date.valueOf(os.getData_saida_efetiva()) : null);
            pst.setString(7, observacoesCombinadas);
            pst.setDouble(8, os.getValor_total());
            pst.setString(9, os.getStatus_os()); 
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar Ordem de Serviço: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Banco.closeStatement(pst);
            Banco.closeConnection(conn);
        }
    }

    @Override
    public boolean alterar(OrdemServico os) {

        String observacoesCombinadas = os.getDescricao_problema() + SERVICOS_DELIMITER + os.getServicos_realizados();

        String sql = "UPDATE ordem_servico SET id_cliente = ?, id_veiculo = ?, id_funcionario_responsavel = ?, data_entrada = ?, data_saida_prevista = ?, data_saida_efetiva = ?, observacoes = ?, valor_total = ?, status = ? WHERE id_ordem_servico = ?";
        try {
            conn = Banco.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, os.getId_cliente());
            pst.setInt(2, os.getId_veiculo());
            pst.setInt(3, os.getId_funcionario());
            pst.setDate(4, java.sql.Date.valueOf(os.getData_abertura()));
            pst.setDate(5, os.getData_fechamento() != null ? java.sql.Date.valueOf(os.getData_fechamento()) : null); 
            pst.setDate(6, os.getData_saida_efetiva() != null ? java.sql.Date.valueOf(os.getData_saida_efetiva()) : null); 
            pst.setString(7, observacoesCombinadas); 
            pst.setDouble(8, os.getValor_total());
            pst.setString(9, os.getStatus_os()); 
            pst.setInt(10, os.getId_ordem_servico());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar Ordem de Serviço: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Banco.closeStatement(pst);
            Banco.closeConnection(conn);
        }
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM ordem_servico WHERE id_ordem_servico = ?";
        try {
            conn = Banco.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir Ordem de Serviço: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Banco.closeStatement(pst);
            Banco.closeConnection(conn);
        }
    }

    @Override
    public OrdemServico buscarPorId(int id) {
        String sql = "SELECT id_ordem_servico, id_cliente, id_veiculo, id_funcionario_responsavel, data_entrada, data_saida_prevista, data_saida_efetiva, observacoes, valor_total, status FROM ordem_servico WHERE id_ordem_servico = ?";
        OrdemServico os = null;
        try {
            conn = Banco.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                LocalDate dataFechamento = null;
                if (rs.getDate("data_saida_prevista") != null) {
                    dataFechamento = rs.getDate("data_saida_prevista").toLocalDate();
                }
                LocalDate dataSaidaEfetiva = null;
                if (rs.getDate("data_saida_efetiva") != null) {
                    dataSaidaEfetiva = rs.getDate("data_saida_efetiva").toLocalDate();
                }

                String fullObservacoes = rs.getString("observacoes");
                String descricaoProblema = "";
                String servicosRealizados = "";

                if (fullObservacoes != null && fullObservacoes.contains(SERVICOS_DELIMITER)) {
                    String[] parts = fullObservacoes.split(SERVICOS_DELIMITER, 2);
                    descricaoProblema = parts[0];
                    if (parts.length > 1) {
                        servicosRealizados = parts[1];
                    }
                } else {
                    descricaoProblema = fullObservacoes;
                }

                os = new OrdemServico(
                    rs.getInt("id_ordem_servico"),
                    rs.getInt("id_cliente"),
                    rs.getInt("id_veiculo"),
                    rs.getInt("id_funcionario_responsavel"),
                    rs.getDate("data_entrada").toLocalDate(),
                    dataFechamento,
                    dataSaidaEfetiva,
                    descricaoProblema,   
                    servicosRealizados, 
                    rs.getDouble("valor_total"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Ordem de Serviço por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(pst);
            Banco.closeConnection(conn);
        }
        return os;
    }

    @Override
    public List<OrdemServico> buscarTodos() {
        String sql = "SELECT id_ordem_servico, id_cliente, id_veiculo, id_funcionario_responsavel, data_entrada, data_saida_prevista, data_saida_efetiva, observacoes, valor_total, status FROM ordem_servico";
        List<OrdemServico> ordens = new ArrayList<>();
        try {
            conn = Banco.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                LocalDate dataFechamento = null;
                if (rs.getDate("data_saida_prevista") != null) {
                    dataFechamento = rs.getDate("data_saida_prevista").toLocalDate();
                }
                LocalDate dataSaidaEfetiva = null;
                if (rs.getDate("data_saida_efetiva") != null) {
                    dataSaidaEfetiva = rs.getDate("data_saida_efetiva").toLocalDate();
                }

                String fullObservacoes = rs.getString("observacoes");
                String descricaoProblema = "";
                String servicosRealizados = "";

                if (fullObservacoes != null && fullObservacoes.contains(SERVICOS_DELIMITER)) {
                    String[] parts = fullObservacoes.split(SERVICOS_DELIMITER, 2);
                    descricaoProblema = parts[0];
                    if (parts.length > 1) {
                        servicosRealizados = parts[1];
                    }
                } else {
                    descricaoProblema = fullObservacoes;
                }

                ordens.add(new OrdemServico(
                    rs.getInt("id_ordem_servico"),
                    rs.getInt("id_cliente"),
                    rs.getInt("id_veiculo"),
                    rs.getInt("id_funcionario_responsavel"),
                    rs.getDate("data_entrada").toLocalDate(),
                    dataFechamento,
                    dataSaidaEfetiva,
                    descricaoProblema,   
                    servicosRealizados,  
                    rs.getDouble("valor_total"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as Ordens de Serviço: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(pst);
            Banco.closeConnection(conn);
        }
        return ordens;
    }

  
    public List<OrdemServico> listar(String criterio) {
        String sql = "SELECT id_ordem_servico, id_cliente, id_veiculo, id_funcionario_responsavel, data_entrada, data_saida_prevista, data_saida_efetiva, observacoes, valor_total, status FROM ordem_servico";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<OrdemServico> ordens = new ArrayList<>();

        try {
            conn = Banco.getConnection();
            if (criterio != null && !criterio.trim().isEmpty()) {
                // Filtra por 'observacoes', ou status
                sql += " WHERE observacoes LIKE ? OR status LIKE ?"; 
                pst = conn.prepareStatement(sql);
                String likeParam = "%" + criterio.trim() + "%";
                pst.setString(1, likeParam);
                pst.setString(2, likeParam);
            } else {
                pst = conn.prepareStatement(sql);
            }

            rs = pst.executeQuery();
            while (rs.next()) {
                LocalDate dataFechamento = null;
                if (rs.getDate("data_saida_prevista") != null) {
                    dataFechamento = rs.getDate("data_saida_prevista").toLocalDate();
                }
                LocalDate dataSaidaEfetiva = null;
                if (rs.getDate("data_saida_efetiva") != null) {
                    dataSaidaEfetiva = rs.getDate("data_saida_efetiva").toLocalDate();
                }

                String fullObservacoes = rs.getString("observacoes");
                String descricaoProblema = "";
                String servicosRealizados = "";

                if (fullObservacoes != null && fullObservacoes.contains(SERVICOS_DELIMITER)) {
                    String[] parts = fullObservacoes.split(SERVICOS_DELIMITER, 2);
                    descricaoProblema = parts[0];
                    if (parts.length > 1) {
                        servicosRealizados = parts[1];
                    }
                } else {
                    descricaoProblema = fullObservacoes;
                }

                ordens.add(new OrdemServico(
                    rs.getInt("id_ordem_servico"),
                    rs.getInt("id_cliente"),
                    rs.getInt("id_veiculo"),
                    rs.getInt("id_funcionario_responsavel"),
                    rs.getDate("data_entrada").toLocalDate(),
                    dataFechamento,
                    dataSaidaEfetiva,
                    descricaoProblema,
                    servicosRealizados,
                    rs.getDouble("valor_total"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar Ordens de Serviço: " + e.getMessage());
        } finally {
            Banco.closeResultSet(rs);
            Banco.closeStatement(pst);
            Banco.closeConnection(conn);
        }
        return ordens;
    }
}