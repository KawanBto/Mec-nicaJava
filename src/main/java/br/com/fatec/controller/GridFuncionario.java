package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.FuncionarioDAO;
import br.com.fatec.model.Funcionario;

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

public class GridFuncionario implements Initializable {

    private App app;
    private FuncionarioDAO funcionarioDAO;

    @FXML private Button btnAdicionar;
    @FXML private Button btnEditarFuncionario;
    @FXML private Button btnExcluirFuncionario;
    @FXML private Button btn_voltar;
    @FXML private Button btnFiltrar;

    @FXML private TableView<Funcionario> funcionariosTable;
    @FXML private TableColumn<Funcionario, Integer> id_funcionarioColumn;
    @FXML private TableColumn<Funcionario, String> nome_completoColumn;
    @FXML private TableColumn<Funcionario, String> cpfColumn;
    @FXML private TableColumn<Funcionario, String> emailColumn;
    @FXML private TableColumn<Funcionario, String> telefoneColumn;
    @FXML private TableColumn<Funcionario, String> cargoColumn;
    @FXML private TableColumn<Funcionario, Double> salarioColumn;

    @FXML private TextField txtFiltroFuncionario;

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        funcionarioDAO = new FuncionarioDAO();
        configurarColunasTabela();
        carregarFuncionarios(""); 
      
        btnEditarFuncionario.setDisable(true);
        btnExcluirFuncionario.setDisable(true);

        funcionariosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEditarFuncionario.setDisable(!isSelected);
            btnExcluirFuncionario.setDisable(!isSelected);
        });

        funcionariosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !funcionariosTable.getSelectionModel().isEmpty()) {
                btnEditarFuncionarioClick(event);
            }
        });
    }

    private void configurarColunasTabela() {
        id_funcionarioColumn.setCellValueFactory(new PropertyValueFactory<>("id_funcionario"));
        nome_completoColumn.setCellValueFactory(new PropertyValueFactory<>("nome_completo"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));

        cpfColumn.setCellFactory(column -> new TableCell<Funcionario, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatarCpf(item));
                }
            }
        });
        telefoneColumn.setCellFactory(column -> new TableCell<Funcionario, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatarTelefone(item));
                }
            }
        });
        salarioColumn.setCellFactory(column -> new TableCell<Funcionario, Double>() {
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

    private void carregarFuncionarios(String criterio) {
        List<Funcionario> funcionarios;
        if (criterio == null || criterio.trim().isEmpty()) {
            funcionarios = funcionarioDAO.buscarTodos();
        } else {
            funcionarios = funcionarioDAO.listar(criterio);
        }
        ObservableList<Funcionario> observableList = FXCollections.observableArrayList(funcionarios);
        funcionariosTable.setItems(observableList);
    }

    @FXML
    private void handleFiltrarFuncionarios(MouseEvent event) {
        String textoFiltro = txtFiltroFuncionario.getText();
        carregarFuncionarios(textoFiltro);
    }

    @FXML
    private void btnAdicionarClick(MouseEvent event) {
        if (app != null) {
            app.showCadFuncionario(null);
        } else {
            System.out.println("Erro: Referência da App não definida para navegação.");
        }
    }

    @FXML
    private void btnEditarFuncionarioClick(MouseEvent event) {
        Funcionario funcionarioSelecionado = funcionariosTable.getSelectionModel().getSelectedItem();
        if (funcionarioSelecionado != null) {
            if (app != null) {
                app.showCadFuncionario(funcionarioSelecionado);
            } else {
                System.out.println("Erro: Referência da App não definida para navegação.");
            }
        } else {
            mostrarAlertaInformacao("Editar Funcionário", "Por favor, selecione um funcionário na tabela para editar.");
        }
    }

    @FXML
    private void btnExcluirFuncionarioClick(MouseEvent event) {
        Funcionario funcionarioSelecionado = funcionariosTable.getSelectionModel().getSelectedItem();
        if (funcionarioSelecionado != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Exclusão");
            confirmAlert.setHeaderText("Excluir Funcionário");
            confirmAlert.setContentText("Tem certeza que deseja excluir o funcionário " + funcionarioSelecionado.getNome_completo() + "? Esta ação é irreversível.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (funcionarioDAO.excluir(funcionarioSelecionado.getId_funcionario())) {
                    mostrarAlertaInformacao("Sucesso", "Funcionário excluído com sucesso!");
                    carregarFuncionarios("");
                } else {
                    mostrarAlertaErro("Erro", "Falha ao excluir funcionário. Pode haver registros associados ou erro de banco de dados.");
                }
            }
        } else {
            mostrarAlertaInformacao("Excluir Funcionário", "Por favor, selecione um funcionário na tabela para excluir.");
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

    private String removerFormatacao(String texto) {
        if (texto == null) return "";
        return texto.replaceAll("[^0-9]", "");
    }
    private String formatarCpf(String cpf) {
        cpf = removerFormatacao(cpf);
        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." +
                   cpf.substring(3, 6) + "." +
                   cpf.substring(6, 9) + "-" +
                   cpf.substring(9, 11);
        }
        return cpf;
    }
    private String formatarTelefone(String telefone) {
        telefone = removerFormatacao(telefone);
        if (telefone.length() == 11) {
            return "(" + telefone.substring(0, 2) + ") " +
                   telefone.substring(2, 7) + "-" +
                   telefone.substring(7, 11);
        } else if (telefone.length() == 10) {
            return "(" + telefone.substring(0, 2) + ") " +
                   telefone.substring(2, 6) + "-" +
                   telefone.substring(6, 10);
        }
        return telefone;
    }
}