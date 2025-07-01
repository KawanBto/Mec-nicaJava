package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.model.OrdemServicoDetalhes;
import br.com.fatec.DAO.BuscaAvancadaDAO;
import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.DAO.FuncionarioDAO; 
import br.com.fatec.DAO.VeiculoDAO;
import br.com.fatec.model.Cliente;
import br.com.fatec.model.Funcionario; 
import br.com.fatec.model.Veiculo; 

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker; 
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.math.BigDecimal; 
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.Initializable;

public class GridBuscaAvancada implements Initializable {

    private App app;
    @FXML
    private TableView<OrdemServicoDetalhes> tvResultados;
    @FXML
    private TableColumn<OrdemServicoDetalhes, Integer> colID;
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colNome;
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colTelefone;
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colPlaca;
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colMarca; 
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colModelo;
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colFuncionarioResponsavel; 
    @FXML
    private TableColumn<OrdemServicoDetalhes, LocalDate> colDataEntrada; 
    @FXML
    private TableColumn<OrdemServicoDetalhes, LocalDate> colDataSaidaPrevista; 
    @FXML
    private TableColumn<OrdemServicoDetalhes, BigDecimal> colValorTotal; 
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colStatus;
    @FXML
    private TableColumn<OrdemServicoDetalhes, String> colObservacoes; 


    @FXML
    private ComboBox<String> cbPlaca;
    @FXML
    private ComboBox<String> cbNome;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private ComboBox<String> cbFuncionario; 

    @FXML
    private DatePicker dpDataEntradaInicio; 
    @FXML
    private DatePicker dpDataEntradaFim;    
    @FXML
    private DatePicker dpDataSaidaEfetivaInicio;

    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnLimparFiltros; 

    private BuscaAvancadaDAO buscaAvancadaDAO;
    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;
    private FuncionarioDAO funcionarioDAO; 

    public void setApp(App app) {
        this.app = app;
        System.out.println("DEBUG: setApp no GridBuscaAvancada chamado. App está " + (app != null ? "DEFINIDO" : "NULO"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("DEBUG: GridBuscaAvancada.initialize chamado.");
        buscaAvancadaDAO = new BuscaAvancadaDAO();
        clienteDAO = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();
        funcionarioDAO = new FuncionarioDAO(); 

        configurarTabela();
        popularComboBoxes();
        carregarDadosNaTabela(null, null, null, null, null, null, null); 
    }

    private void configurarTabela() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idOrdemServico"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefoneCliente"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placaVeiculo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marcaVeiculo")); 
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modeloVeiculo"));
        colFuncionarioResponsavel.setCellValueFactory(new PropertyValueFactory<>("nomeFuncionario")); 
        colDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada")); 
        colDataSaidaPrevista.setCellValueFactory(new PropertyValueFactory<>("dataSaidaPrevista"));  
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal")); 
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusOs"));
        colObservacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes")); 
    }

    private void popularComboBoxes() {
        cbStatus.setItems(FXCollections.observableArrayList("Todos", "Pendente", "Em Andamento", "Aguardando Peças", "Concluído", "Cancelado"));

        List<Cliente> clientes = clienteDAO.buscarTodos();
        List<String> nomesClientes = clientes.stream()
                .map(Cliente::getNome_completo)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        cbNome.setItems(FXCollections.observableArrayList(nomesClientes));
        cbNome.getItems().add(0, "Todos");

        List<Veiculo> veiculos = veiculoDAO.buscarTodos(); 
        List<String> placasVeiculos = veiculos.stream()
                .map(Veiculo::getPlaca) 
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        cbPlaca.setItems(FXCollections.observableArrayList(placasVeiculos));
        cbPlaca.getItems().add(0, "Todos");

        List<Funcionario> funcionarios = funcionarioDAO.buscarTodos(); 
        List<String> nomesFuncionarios = funcionarios.stream()
                .map(Funcionario::getNome_completo) 
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        cbFuncionario.setItems(FXCollections.observableArrayList(nomesFuncionarios));
        cbFuncionario.getItems().add(0, "Todos");

    }

    @FXML
    private void filtrarResultados() {
        String nomeCliente = cbNome.getValue();
        String placaVeiculo = cbPlaca.getValue(); 
        String nomeFuncionario = cbFuncionario.getValue(); 
        String status = cbStatus.getValue();

        LocalDate dataEntradaInicio = dpDataEntradaInicio.getValue();
        LocalDate dataEntradaFim = dpDataEntradaFim.getValue();
        LocalDate dataSaidaEfetiva = dpDataSaidaEfetivaInicio.getValue();

        System.out.println("DEBUG FILTRO: Nome Cliente: " + nomeCliente);
        System.out.println("DEBUG FILTRO: Placa Veículo: " + placaVeiculo);
        System.out.println("DEBUG FILTRO: Nome Funcionário: " + nomeFuncionario);
        System.out.println("DEBUG FILTRO: Status: " + status);
        System.out.println("DEBUG FILTRO: Data Entrada Início: " + dataEntradaInicio);
        System.out.println("DEBUG FILTRO: Data Entrada Fim: " + dataEntradaFim);
        System.out.println("DEBUG FILTRO: Data Saída Efetiva: " + dataSaidaEfetiva);

        carregarDadosNaTabela(nomeCliente, nomeFuncionario, placaVeiculo, status,
                              dataEntradaInicio, dataEntradaFim,
                              dataSaidaEfetiva); 
    }

    private void carregarDadosNaTabela(String nomeCliente, String nomeFuncionario, String placaVeiculo, String status,
                                       LocalDate dataEntradaInicio, LocalDate dataEntradaFim,
                                       LocalDate dataSaidaEfetiva) { 
        
        List<OrdemServicoDetalhes> ordens = buscaAvancadaDAO.buscarOrdensAvancada(
                nomeCliente, nomeFuncionario, placaVeiculo, status, 
                dataEntradaInicio, dataEntradaFim,
                dataSaidaEfetiva);
        ObservableList<OrdemServicoDetalhes> observableList = FXCollections.observableArrayList(ordens);
        tvResultados.setItems(observableList);
    }

    @FXML
    private void btnVoltarClick(MouseEvent event) {
        System.out.println("DEBUG: btnVoltarClick em GridBuscaAvancada. App atual: " + (app != null ? "DEFINIDO" : "NULO"));
        try {
            if (app != null) {
                app.showMainMenu(); 
            } else {
                exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível voltar ao menu. A referência 'app' está nula.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlertaErro("Erro de Navegação", "Não foi possível voltar ao menu.", e.getMessage());
        }
    }

    @FXML
    private void limparFiltros() {
        cbNome.getSelectionModel().select("Todos"); 
        cbPlaca.getSelectionModel().select("Todos");
        cbFuncionario.getSelectionModel().select("Todos");
        cbStatus.getSelectionModel().select("Todos");

        dpDataEntradaInicio.setValue(null);
        dpDataEntradaFim.setValue(null);
        dpDataSaidaEfetivaInicio.setValue(null);


        carregarDadosNaTabela(null, null, null, null, null, null, null);
        exibirAlertaInfo("Filtros Limpos", null, "Todos os filtros foram removidos e a tabela foi atualizada.");
    }

    private void exibirAlertaErro(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void exibirAlertaInfo(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
