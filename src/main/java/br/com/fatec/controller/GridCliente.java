package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.model.Cliente;

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

public class GridCliente implements Initializable {

    private App app;
    private ClienteDAO clienteDAO;

    @FXML private Button btnAdicionar;
    @FXML private Button btnEditarCliente;
    @FXML private Button btnExcluirCliente;
    @FXML private Button btn_voltar;
    @FXML private Button btnFiltrar;

    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, Integer> id_clienteColumn;
    @FXML private TableColumn<Cliente, String> nome_completoColumn;
    @FXML private TableColumn<Cliente, String> cpfColumn;
    @FXML private TableColumn<Cliente, String> telefoneColumn;
    @FXML private TableColumn<Cliente, String> emailColumn;
    @FXML private TableColumn<Cliente, String> cepColumn;
    @FXML private TableColumn<Cliente, String> enderecoColumn; 

    @FXML private TextField txtFiltroCliente;

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO = new ClienteDAO();
        configurarColunasTabela();
        carregarClientes("");

        btnEditarCliente.setDisable(true);
        btnExcluirCliente.setDisable(true);

        clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            btnEditarCliente.setDisable(!isSelected);
            btnExcluirCliente.setDisable(!isSelected);
        });

        clientesTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !clientesTable.getSelectionModel().isEmpty()) {
                btnEditarClienteClick(event);
            }
        });
    }

    private void configurarColunasTabela() {
        id_clienteColumn.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));
        nome_completoColumn.setCellValueFactory(new PropertyValueFactory<>("nome_completo"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cepColumn.setCellValueFactory(new PropertyValueFactory<>("cep"));
        enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        cpfColumn.setCellFactory(column -> new TableCell<Cliente, String>() {
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
        telefoneColumn.setCellFactory(column -> new TableCell<Cliente, String>() {
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
        cepColumn.setCellFactory(column -> new TableCell<Cliente, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatarCep(item));
                }
            }
        });
    }

    private void carregarClientes(String criterio) {
        List<Cliente> clientes;
        if (criterio == null || criterio.trim().isEmpty()) {
            clientes = clienteDAO.buscarTodos();
        } else {
            clientes = clienteDAO.listar(criterio);
        }
        ObservableList<Cliente> observableList = FXCollections.observableArrayList(clientes);
        clientesTable.setItems(observableList);
    }

    @FXML
    private void handleFiltrarClientes(MouseEvent event) {
        String textoFiltro = txtFiltroCliente.getText();
        carregarClientes(textoFiltro);
    }

    @FXML
    private void btnAdicionarClick(MouseEvent event) {
        if (app != null) {
            app.showCadCliente(null);
        } else {
            System.out.println("Erro: Referência da App não definida para navegação.");
        }
    }

    @FXML
    private void btnEditarClienteClick(MouseEvent event) {
        Cliente clienteSelecionado = clientesTable.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            if (app != null) {
                app.showCadCliente(clienteSelecionado);
            } else {
                System.out.println("Erro: Referência da App não definida para navegação.");
            }
        } else {
            mostrarAlertaInformacao("Editar Cliente", "Por favor, selecione um cliente na tabela para editar.");
        }
    }

    @FXML
    private void btnExcluirClienteClick(MouseEvent event) {
        Cliente clienteSelecionado = clientesTable.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Exclusão");
            confirmAlert.setHeaderText("Excluir Cliente");
            confirmAlert.setContentText("Tem certeza que deseja excluir o cliente " + clienteSelecionado.getNome_completo() + "? Esta ação é irreversível.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (clienteDAO.excluir(clienteSelecionado.getId_cliente())) {
                    mostrarAlertaInformacao("Sucesso", "Cliente excluído com sucesso!");
                    carregarClientes(""); // Recarrega a tabela
                } else {
                    mostrarAlertaErro("Erro", "Falha ao excluir cliente. Pode haver registros associados ou erro de banco de dados.");
                }
            }
        } else {
            mostrarAlertaInformacao("Excluir Cliente", "Por favor, selecione um cliente na tabela para excluir.");
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

    private String formatarCep(String cep) {
        cep = removerFormatacao(cep);
        if (cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5, 8);
        }
        return cep;
    }
}
