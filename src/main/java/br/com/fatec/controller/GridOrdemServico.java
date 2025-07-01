package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.OrdemServicoDAO;
import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.DAO.VeiculoDAO;
import br.com.fatec.DAO.FuncionarioDAO; 
import br.com.fatec.model.Cliente;
import br.com.fatec.model.Funcionario;
import br.com.fatec.model.OrdemServico; 
import br.com.fatec.model.Veiculo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableCell;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import java.util.ResourceBundle;

public class GridOrdemServico implements Initializable {

    private App app;
    private OrdemServicoDAO ordemServicoDAO;
    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;    
    private FuncionarioDAO funcionarioDAO;

    @FXML private Button btnAdicionar;
    @FXML private Button btnEditarOrdem;
    @FXML private Button btnExcluirOrdem;
    @FXML private Button btn_voltar;
    @FXML private Button btnFiltrar;

    @FXML private TableView<OrdemServico> ordensTable;
    @FXML private TableColumn<OrdemServico, Integer> id_ordem_servicoColumn;
    @FXML private TableColumn<OrdemServico, Integer> id_clienteColumn;
    @FXML private TableColumn<OrdemServico, Integer> id_veiculoColumn;    
    @FXML private TableColumn<OrdemServico, Integer> id_funcionarioColumn; 
    @FXML private TableColumn<OrdemServico, LocalDate> data_aberturaColumn;
    @FXML private TableColumn<OrdemServico, LocalDate> data_fechamentoColumn;
    @FXML private TableColumn<OrdemServico, LocalDate> data_saida_efetivaColumn;
    @FXML private TableColumn<OrdemServico, String> servicos_realizadosColumn;
    @FXML private TableColumn<OrdemServico, Double> valor_totalColumn;
    @FXML private TableColumn<OrdemServico, String> status_osColumn;

    @FXML private TextField txtFiltroOrdem;

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemServicoDAO = new OrdemServicoDAO();
        clienteDAO = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();
        funcionarioDAO = new FuncionarioDAO();

        configurarColunasTabela();
        carregarOrdens("");

        btnEditarOrdem.setDisable(true);
        btnExcluirOrdem.setDisable(true);

        ordensTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEditarOrdem.setDisable(!isSelected);
            btnExcluirOrdem.setDisable(!isSelected);
        });

        ordensTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !ordensTable.getSelectionModel().isEmpty()) {
                btnEditarOrdemClick(event);
            }
        });
    }

    private void configurarColunasTabela() {
        id_ordem_servicoColumn.setCellValueFactory(new PropertyValueFactory<>("id_ordem_servico"));
        data_aberturaColumn.setCellValueFactory(new PropertyValueFactory<>("data_abertura"));
        data_fechamentoColumn.setCellValueFactory(new PropertyValueFactory<>("data_fechamento"));
        data_saida_efetivaColumn.setCellValueFactory(new PropertyValueFactory<>("data_saida_efetiva"));
        
        
        servicos_realizadosColumn.setCellValueFactory(new PropertyValueFactory<>("servicos_realizados"));
        valor_totalColumn.setCellValueFactory(new PropertyValueFactory<>("valor_total"));
        status_osColumn.setCellValueFactory(new PropertyValueFactory<>("status_os"));

        id_clienteColumn.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));
        id_clienteColumn.setCellFactory(column -> new TableCell<OrdemServico, Integer>() {
            @Override
            protected void updateItem(Integer itemId, boolean empty) {
                super.updateItem(itemId, empty);
                if (empty || itemId == null) {
                    setText(null);
                } else {
                    Cliente cliente = clienteDAO.buscarPorId(itemId);
                    setText(cliente != null ? cliente.getNome_completo() : "ID: " + itemId);
                }
            }
        });

        id_veiculoColumn.setCellValueFactory(new PropertyValueFactory<>("id_veiculo"));
        id_veiculoColumn.setCellFactory(column -> new TableCell<OrdemServico, Integer>() {
            @Override
            protected void updateItem(Integer itemId, boolean empty) {
                super.updateItem(itemId, empty);
                if (empty || itemId == null) {
                    setText(null);
                } else {
                    Veiculo veiculo = veiculoDAO.buscarPorId(itemId);
                    setText(veiculo != null ? veiculo.getModelo() + " (" + veiculo.getPlaca() + ")" : "ID: " + itemId);
                }
            }
        });

        id_funcionarioColumn.setCellValueFactory(new PropertyValueFactory<>("id_funcionario"));
        id_funcionarioColumn.setCellFactory(column -> new TableCell<OrdemServico, Integer>() {
            @Override
            protected void updateItem(Integer itemId, boolean empty) {
                super.updateItem(itemId, empty);
                if (empty || itemId == null) {
                    setText(null);
                } else {
                    Funcionario funcionario = funcionarioDAO.buscarPorId(itemId);
                    setText(funcionario != null ? funcionario.getNome_completo() : "ID: " + itemId);
                }
            }
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        data_aberturaColumn.setCellFactory(column -> new TableCell<OrdemServico, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });
        data_fechamentoColumn.setCellFactory(column -> new TableCell<OrdemServico, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });
        data_saida_efetivaColumn.setCellFactory(column -> new TableCell<OrdemServico, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        valor_totalColumn.setCellFactory(column -> new TableCell<OrdemServico, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", item));
                }
            }
        });
    }

    private void carregarOrdens(String criterio) {
        List<OrdemServico> ordens;
        if (criterio == null || criterio.trim().isEmpty()) {
            ordens = ordemServicoDAO.buscarTodos();
        } else {
            ordens = ordemServicoDAO.listar(criterio);
        }
        ObservableList<OrdemServico> observableList = FXCollections.observableArrayList(ordens);
        ordensTable.setItems(observableList);
    }

    @FXML
    private void handleFiltrarOrdens(MouseEvent event) {
        String textoFiltro = txtFiltroOrdem.getText();
        carregarOrdens(textoFiltro);
    }

    @FXML
    private void btnAdicionarClick(MouseEvent event) {
        if (app != null) {
            app.showCadServico(null);
        } else {
            System.out.println("Erro: Referência da App não definida para navegação.");
        }
    }

    @FXML
    private void btnEditarOrdemClick(MouseEvent event) {
        OrdemServico ordemSelecionada = ordensTable.getSelectionModel().getSelectedItem();
        if (ordemSelecionada != null) {
            if (app != null) {
                app.showCadServico(ordemSelecionada);
            } else {
                System.out.println("Erro: Referência da App não definida para navegação.");
            }
        } else {
            mostrarAlertaInformacao("Editar Ordem de Serviço", "Por favor, selecione uma Ordem de Serviço na tabela para editar.");
        }
    }

    @FXML
    private void btnExcluirOrdemClick(MouseEvent event) {
        OrdemServico ordemSelecionada = ordensTable.getSelectionModel().getSelectedItem();
        if (ordemSelecionada != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Exclusão");
            confirmAlert.setHeaderText("Excluir Ordem de Serviço");
            confirmAlert.setContentText("Tem certeza que deseja excluir a Ordem de Serviço ID: " + ordemSelecionada.getId_ordem_servico() + "? Esta ação é irreversível.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (ordemServicoDAO.excluir(ordemSelecionada.getId_ordem_servico())) {
                    mostrarAlertaInformacao("Sucesso", "Ordem de Serviço excluída com sucesso!");
                    carregarOrdens(""); // Recarrega a tabela
                } else {
                    mostrarAlertaErro("Erro", "Falha ao excluir Ordem de Serviço. Pode haver registros associados ou erro de banco de dados.");
                }
            }
        } else {
            mostrarAlertaInformacao("Excluir Ordem de Serviço", "Por favor, selecione uma Ordem de Serviço na tabela para excluir.");
        }
    }

    @FXML
    private void btnVoltarClick(MouseEvent event) {
        if (app != null) {
            app.voltarMenu();
        } else {
            System.out.println("Erro: Referência da App não definida para navegação.");
        }
    }

    private void mostrarAlertaInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}