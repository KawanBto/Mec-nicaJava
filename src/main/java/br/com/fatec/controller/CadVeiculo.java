package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.VeiculoDAO;
import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.model.Veiculo;
import br.com.fatec.model.Cliente;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CadVeiculo implements Initializable {

    private App app;
    private VeiculoDAO veiculoDAO;
    private ClienteDAO clienteDAO;
    private Veiculo veiculoEmEdicao;

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnoFabricacao;
    @FXML private TextField txtCor;

    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnVoltar;

    private ObservableList<Cliente> masterClientList;

    public void setApp(App app) {
        this.app = app;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculoEmEdicao = veiculo;
        if (veiculo != null) {
            Cliente clienteProprietario = clienteDAO.buscarPorId(veiculo.getId_cliente());
            if (clienteProprietario != null) {
                cmbCliente.getSelectionModel().select(clienteProprietario);
            } else {
                mostrarAlertaErro("Erro", "Cliente proprietário do veículo não encontrado. O veículo pode estar associado a um ID de cliente inexistente.");
                cmbCliente.getSelectionModel().clearSelection();
            }

            txtPlaca.setText(veiculo.getPlaca());
            txtMarca.setText(veiculo.getMarca());
            txtModelo.setText(veiculo.getModelo());
            txtAnoFabricacao.setText(String.valueOf(veiculo.getAno_fabricacao()));
            txtCor.setText(veiculo.getCor());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO = new VeiculoDAO();
        clienteDAO = new ClienteDAO(); 

        configurarComboBoxClientes();

        adicionarLimiteCaracteres(txtPlaca, 10);
        adicionarMascaraPlaca(txtPlaca);

        adicionarLimiteCaracteres(txtMarca, 50);

        adicionarLimiteCaracteres(txtModelo, 50);

        adicionarFormatoNumericoAno(txtAnoFabricacao);

        adicionarLimiteCaracteres(txtCor, 30);
    }

    private void configurarComboBoxClientes() {
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente != null ? cliente.getNome_completo() + " (ID: " + cliente.getId_cliente() + ")" : "";
            }

            @Override
            public Cliente fromString(String string) {
                return masterClientList.stream()
                        .filter(c -> toString(c).equalsIgnoreCase(string) || String.valueOf(c.getId_cliente()).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        masterClientList = FXCollections.observableArrayList(clienteDAO.buscarTodos());
        cmbCliente.setItems(masterClientList);

        cmbCliente.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isEmpty()) {
                cmbCliente.setItems(masterClientList);
            } else {
                List<Cliente> filteredList = masterClientList.stream()
                    .filter(cliente -> cliente.getNome_completo().toLowerCase().contains(newText.toLowerCase()) ||
                                       String.valueOf(cliente.getId_cliente()).contains(newText))
                    .collect(Collectors.toList());
                
                if (!filteredList.isEmpty()) {
                    cmbCliente.setItems(FXCollections.observableArrayList(filteredList));
                    if (cmbCliente.getSelectionModel().getSelectedItem() == null || !cmbCliente.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase(newText)) {
                        cmbCliente.show(); 
                    }
                } else {
                    cmbCliente.hide();
                }
            }
        });

        cmbCliente.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Cliente selecionado: " + newSelection.getNome_completo() + " (ID: " + newSelection.getId_cliente() + ")");
            }
        });
    }


    @FXML
    private void btnSalvarClick(MouseEvent event) {
        if (cmbCliente.getSelectionModel().getSelectedItem() == null || txtPlaca.getText().isEmpty() ||
            txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() ||
            txtAnoFabricacao.getText().isEmpty() || txtCor.getText().isEmpty()) {
            mostrarAlertaErro("Campos vazios", "Por favor, preencha todos os campos e selecione um cliente.");
            return;
        }

        Cliente clienteSelecionado = cmbCliente.getSelectionModel().getSelectedItem();
        int idCliente = clienteSelecionado.getId_cliente();

        int anoFabricacao;
        try {
            anoFabricacao = Integer.parseInt(txtAnoFabricacao.getText());
            if (String.valueOf(anoFabricacao).length() != 4) {
                mostrarAlertaErro("Ano de Fabricação Inválido", "O ano de fabricação deve conter 4 dígitos.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlertaErro("Ano de Fabricação Inválido", "Por favor, insira um ano numérico válido.");
            return;
        }

        String placa = txtPlaca.getText().toUpperCase();

        if (veiculoEmEdicao == null) {
            Veiculo novoVeiculo = new Veiculo(
                idCliente,
                placa,
                txtMarca.getText(),
                txtModelo.getText(),
                anoFabricacao,
                txtCor.getText()
            );
            if (veiculoDAO.cadastrar(novoVeiculo)) {
                mostrarAlertaInformacao("Sucesso", "Veículo cadastrado com sucesso!");
                btnLimparClick(null);
            } else {
                mostrarAlertaErro("Erro", "Falha ao cadastrar veículo. Verifique os dados e tente novamente.");
            }
        } else { 
            veiculoEmEdicao.setId_cliente(idCliente);
            veiculoEmEdicao.setPlaca(placa);
            veiculoEmEdicao.setMarca(txtMarca.getText());
            veiculoEmEdicao.setModelo(txtModelo.getText());
            veiculoEmEdicao.setAno_fabricacao(anoFabricacao);
            veiculoEmEdicao.setCor(txtCor.getText());

            if (veiculoDAO.alterar(veiculoEmEdicao)) {
                mostrarAlertaInformacao("Sucesso", "Veículo atualizado com sucesso!");
                if (app != null) {
                    app.showGridVeiculo(); 
                }
            } else {
                mostrarAlertaErro("Erro", "Falha ao atualizar veículo. Verifique os dados e tente novamente.");
            }
        }
    }

    @FXML
    private void btnLimparClick(MouseEvent event) {
        cmbCliente.getSelectionModel().clearSelection();
        cmbCliente.getEditor().clear();
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnoFabricacao.setText("");
        txtCor.setText("");
        veiculoEmEdicao = null;
    }

    @FXML
    private void btnVoltarClick(MouseEvent event) {
        if (app != null) {
            app.voltarVeiculo();
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

    private void adicionarLimiteCaracteres(TextField textField, int limite) {
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > limite) {
                textField.setText(oldValue);
            }
        });
    }

    private void adicionarFormatoNumericoAno(TextField textField) {
        textField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 4) {
                textField.setText(oldValue);
            }
        });
    }

    private void adicionarMascaraPlaca(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanValue = newValue.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
            String formattedValue = "";

            if (cleanValue.length() > 0) {
                if (cleanValue.length() <= 3) {
                    formattedValue = cleanValue;
                } else if (cleanValue.length() <= 4) {
                    formattedValue = cleanValue.substring(0, 3) + "-" + cleanValue.substring(3);
                } else if (cleanValue.length() <= 7) {
                    if (cleanValue.matches("^[A-Z]{3}\\d[A-Z]\\d{2}$")) {
                        formattedValue = cleanValue.substring(0, 3) + cleanValue.substring(3, 4) + cleanValue.substring(4, 5) + cleanValue.substring(5, 7);
                        formattedValue = cleanValue.substring(0, 3) + "-" + cleanValue.substring(3, 4) + cleanValue.substring(4, 5) + cleanValue.substring(5, 7);
                    } else if (cleanValue.matches("^[A-Z]{3}\\d{4}$")) { 
                        formattedValue = cleanValue.substring(0, 3) + "-" + cleanValue.substring(3);
                    } else {
                        formattedValue = cleanValue.substring(0, 3) + "-" + cleanValue.substring(3);
                    }
                } else {
                    formattedValue = oldValue;
                }
            }
            if (!textField.getText().equals(formattedValue)) {
                textField.setText(formattedValue);
                textField.positionCaret(formattedValue.length());
            }
        });
    }
}
