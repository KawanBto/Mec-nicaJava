package br.com.fatec.DAO;

import br.com.fatec.model.OrdemServicoDetalhes;
import br.com.fatec.persistencia.Banco; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BuscaAvancadaDAO {

    public List<OrdemServicoDetalhes> buscarOrdensAvancada(String nomeCliente, String nomeFuncionario, String placaVeiculo, String status, LocalDate dataEntradaInicio, LocalDate dataEntradaFim, LocalDate dataSaidaEfetiva) {
        List<OrdemServicoDetalhes> ordensDetalhes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT os.id_ordem_servico, os.id_cliente, os.id_veiculo, os.id_funcionario_responsavel, " +
                                              "os.data_entrada, os.data_saida_prevista, os.data_saida_efetiva, os.status, os.valor_total, os.observacoes, " +
                                              "c.nome_completo AS nome_cliente, c.telefone AS telefone_cliente, " + 
                                              "v.placa AS placa_veiculo, v.modelo AS modelo_veiculo, v.marca AS marca_veiculo, " + 
                                              "f.nome_completo AS nome_funcionario " + 
                                              "FROM ordem_servico os " +
                                              "LEFT JOIN cliente c ON os.id_cliente = c.id_cliente " +
                                              "LEFT JOIN funcionario f ON os.id_funcionario_responsavel = f.id_funcionario " +
                                              "LEFT JOIN veiculo v ON os.id_veiculo = v.id_veiculo " +
                                              "WHERE 1=1"); 

        List<Object> parametros = new ArrayList<>();

        if (nomeCliente != null && !nomeCliente.isEmpty() && !nomeCliente.equalsIgnoreCase("Todos")) {
            sql.append(" AND c.nome_completo LIKE ?"); 
            parametros.add("%" + nomeCliente + "%");
        }
        if (nomeFuncionario != null && !nomeFuncionario.isEmpty() && !nomeFuncionario.equalsIgnoreCase("Todos")) {
            sql.append(" AND f.nome_completo LIKE ?"); 
            parametros.add("%" + nomeFuncionario + "%");
        }
        if (placaVeiculo != null && !placaVeiculo.isEmpty() && !placaVeiculo.equalsIgnoreCase("Todos")) {
            sql.append(" AND v.placa LIKE ?");
            parametros.add("%" + placaVeiculo + "%");
        }

        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Todos")) {
            sql.append(" AND os.status = ?");
            parametros.add(status);
        }
        if (dataEntradaInicio != null) {
            sql.append(" AND os.data_entrada >= ?");
            parametros.add(dataEntradaInicio);
        }
        if (dataEntradaFim != null) {
            sql.append(" AND os.data_entrada <= ?");
            parametros.add(dataEntradaFim);
        }
        // Correção: Agora verifica apenas dataSaidaEfetiva (não há mais dataSaidaInicio e dataSaidaFim separados)
        if (dataSaidaEfetiva != null) { 
            sql.append(" AND os.data_saida_efetiva = ?"); // Mudei para '=' para buscar uma data exata
            parametros.add(dataSaidaEfetiva);
        }
        
        sql.append(" GROUP BY os.id_ordem_servico"); 

        System.out.println("DEBUG DAO: Query SQL Gerada: " + sql.toString());
        System.out.println("DEBUG DAO: Parâmetros da Query: " + parametros);


        try (Connection conn = Banco.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrdemServicoDetalhes osDetalhes = new OrdemServicoDetalhes(); 
                osDetalhes.setIdOrdemServico(rs.getInt("id_ordem_servico"));
                osDetalhes.setNomeCliente(rs.getString("nome_cliente")); 
                osDetalhes.setTelefoneCliente(rs.getString("telefone_cliente")); 
                osDetalhes.setPlacaVeiculo(rs.getString("placa_veiculo"));   
                osDetalhes.setModeloVeiculo(rs.getString("modelo_veiculo")); 
                osDetalhes.setMarcaVeiculo(rs.getString("marca_veiculo")); 
                osDetalhes.setNomeFuncionario(rs.getString("nome_funcionario")); 

                if (rs.getDate("data_entrada") != null) {
                     osDetalhes.setDataEntrada(rs.getDate("data_entrada").toLocalDate());
                }
               
                if (rs.getDate("data_saida_prevista") != null) {
                    osDetalhes.setDataSaidaPrevista(rs.getDate("data_saida_prevista").toLocalDate());
                } else {
                    osDetalhes.setDataSaidaPrevista(null);
                }

                if (rs.getDate("data_saida_efetiva") != null) {
                    osDetalhes.setDataSaidaEfetiva(rs.getDate("data_saida_efetiva").toLocalDate());
                } else {
                    osDetalhes.setDataSaidaEfetiva(null);
                }
                
                osDetalhes.setStatusOs(rs.getString("status"));
                osDetalhes.setValorTotal(rs.getBigDecimal("valor_total")); 
                osDetalhes.setObservacoes(rs.getString("observacoes")); 
                
                osDetalhes.setServicosRealizados(null); // Agora sempre null, pois não há filtro de serviço
                
                ordensDetalhes.add(osDetalhes);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ordens de serviço avançadas: " + e.getMessage());
            e.printStackTrace();
        }
        return ordensDetalhes;
    }
}
