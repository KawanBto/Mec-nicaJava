package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.VeiculoDAO;
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
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import java.util.ResourceBundle;

public class GridVeiculo implements Initializable {

    private App app;
    private VeiculoDAO veiculoDAO;

    @FXML private Button btnAdicionar;
    @FXML private Button btnEditarVeiculo;
    @FXML private Button btnExcluirVeiculo;
    @FXML private Button btn_voltar;
    @FXML private Button btnFiltrar;

    @FXML private TableView<Veiculo> veiculosTable;
    @FXML private TableColumn<Veiculo, Integer> id_veiculoColumn;
    @FXML private TableColumn<Veiculo, String> nome_clienteColumn;
    @FXML private TableColumn<Veiculo, String> placaColumn;
    @FXML private TableColumn<Veiculo, String> marcaColumn;
    @FXML private TableColumn<Veiculo, String> modeloColumn;
    @FXML private TableColumn<Veiculo, Integer> ano_fabricacaoColumn;
    @FXML private TableColumn<Veiculo, String> corColumn;

    @FXML private TextField txtFiltroVeiculo;

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO = new VeiculoDAO();
        configurarColunasTabela();
        carregarVeiculos("");

        btnEditarVeiculo.setDisable(true);
        btnExcluirVeiculo.setDisable(true);

        veiculosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEditarVeiculo.setDisable(!isSelected);
            btnExcluirVeiculo.setDisable(!isSelected);
        });

        veiculosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !veiculosTable.getSelectionModel().isEmpty()) {
                btnEditarVeiculoClick(event);
            }
        });
    }

    private void configurarColunasTabela() {
        id_veiculoColumn.setCellValueFactory(new PropertyValueFactory<>("id_veiculo"));
        nome_clienteColumn.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));

        placaColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        ano_fabricacaoColumn.setCellValueFactory(new PropertyValueFactory<>("ano_fabricacao"));
        corColumn.setCellValueFactory(new PropertyValueFactory<>("cor"));

        // REMOVA OU COMENTE ESTE BLOCO para exibir o nome como está no banco de dados
        // nome_clienteColumn.setCellFactory(column -> new TableCell<Veiculo, String>() {
        //     @Override
        //     protected void updateItem(String item, boolean empty) {
        //         super.updateItem(item, empty);
        //         if (empty || item == null) {
        //             setText(null);
        //         } else {
        //             setText(item.toUpperCase()); // ESTA LINHA CONVERTE PARA MAIÚSCULAS
        //         }
        //     }
        // });

        placaColumn.setCellFactory(column -> new TableCell<Veiculo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatarPlaca(item));
                }
            }
        });
    }

    private void carregarVeiculos(String criterio) {
        List<Veiculo> veiculos;
        if (criterio == null || criterio.trim().isEmpty()) {
            veiculos = veiculoDAO.buscarTodos();
        } else {
            veiculos = veiculoDAO.listar(criterio);
        }
        ObservableList<Veiculo> observableList = FXCollections.observableArrayList(veiculos);
        veiculosTable.setItems(observableList);
    }

    @FXML
    private void handleFiltrarVeiculos(MouseEvent event) {
        String textoFiltro = txtFiltroVeiculo.getText();
        carregarVeiculos(textoFiltro);
    }

    @FXML
    private void btnAdicionarClick(MouseEvent event) {
        if (app != null) {
            app.showCadVeiculo(null);
        } else {
            System.out.println("Erro: Referência da App não definida para navegação.");
        }
    }

    @FXML
    private void btnEditarVeiculoClick(MouseEvent event) {
        Veiculo veiculoSelecionado = veiculosTable.getSelectionModel().getSelectedItem();
        if (veiculoSelecionado != null) {
            if (app != null) {
                app.showCadVeiculo(veiculoSelecionado);
            } else {
                System.out.println("Erro: Referência da App não definida para navegação.");
            }
        } else {
            mostrarAlertaInformacao("Editar Veículo", "Por favor, selecione um veículo na tabela para editar.");
        }
    }

    @FXML
    private void btnExcluirVeiculoClick(MouseEvent event) {
        Veiculo veiculoSelecionado = veiculosTable.getSelectionModel().getSelectedItem();
        if (veiculoSelecionado != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Exclusão");
            confirmAlert.setHeaderText("Excluir Veículo");
            confirmAlert.setContentText("Tem certeza que deseja excluir o veículo de placa " + veiculoSelecionado.getPlaca() + "? Esta ação é irreversível.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (veiculoDAO.excluir(veiculoSelecionado.getId_veiculo())) {
                    mostrarAlertaInformacao("Sucesso", "Veículo excluído com sucesso!");
                    carregarVeiculos("");
                } else {
                    mostrarAlertaErro("Erro", "Falha ao excluir veículo. Pode haver registros associados (e.g., Ordens de Serviço) ou erro de banco de dados.");
                }
            }
        } else {
            mostrarAlertaInformacao("Excluir Veículo", "Por favor, selecione um veículo na tabela para excluir.");
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
    
    private String formatarPlaca(String placa) {
        if (placa == null || placa.isEmpty()) {
            return "";
        }
        String cleanPlaca = placa.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        if (cleanPlaca.length() == 7) {
            if (cleanPlaca.matches("^[A-Z]{3}\\d[A-Z]\\d{2}$")) {
                return cleanPlaca.substring(0, 3) + "-" + cleanPlaca.substring(3, 4) + cleanPlaca.substring(4, 5) + cleanPlaca.substring(5, 7);
            } else if (cleanPlaca.matches("^[A-Z]{3}\\d{4}$")) {
                return cleanPlaca.substring(0, 3) + "-" + cleanPlaca.substring(3, 7);
            }
        } else if (cleanPlaca.length() == 6) {
            return cleanPlaca.substring(0, 3) + "-" + cleanPlaca.substring(3, 6);
        }
        return placa;
    }
}