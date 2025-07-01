package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.DAO.FuncionarioDAO;
import br.com.fatec.DAO.OrdemServicoDAO;
import br.com.fatec.DAO.VeiculoDAO;
import br.com.fatec.model.Cliente;
import br.com.fatec.model.Funcionario;
import br.com.fatec.model.OrdemServico;
import br.com.fatec.model.Veiculo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CadOrdemServico implements Initializable {

    private App app;
    private OrdemServicoDAO ordemServicoDAO;
    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;
    private FuncionarioDAO funcionarioDAO;
    private OrdemServico ordemEmEdicao;

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Veiculo> cmbVeiculo;
    @FXML private ComboBox<Funcionario> cmbFuncionario;
    @FXML private DatePicker dtpDataAbertura;
    @FXML private DatePicker dtpDataFechamento;
    @FXML private DatePicker dtpDataSaidaEfetiva;
    @FXML private TextArea txtDescricaoProblema;
    @FXML private TextArea txtServicosRealizados;
    @FXML private TextField txtValorTotal;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnVoltar;

    private ObservableList<Cliente> clientesDisponiveis;
    private ObservableList<Veiculo> veiculosDisponiveis;
    private ObservableList<Funcionario> funcionariosDisponiveis;

    private ObservableList<Cliente> masterClientList;
    private ObservableList<Veiculo> masterVeiculoList;
    private ObservableList<Funcionario> masterFuncionarioList;

    public void setApp(App app) {
        this.app = app;
    }

    public void setOrdemServico(OrdemServico os) {
        this.ordemEmEdicao = os;
        if (os != null) {

            Cliente cliente = clienteDAO.buscarPorId(os.getId_cliente());
            if (cliente != null) {
                if (!masterClientList.contains(cliente)) {
                    masterClientList.add(cliente);
                }
                if (!cmbCliente.getItems().contains(cliente)) {
                    cmbCliente.getItems().add(cliente);
                }
                cmbCliente.getSelectionModel().select(cliente);
            } else {
                System.err.println("Aviso: Cliente com ID " + os.getId_cliente() + " não encontrado no banco de dados.");
            }

            Veiculo veiculo = veiculoDAO.buscarPorId(os.getId_veiculo());
            if (veiculo != null) {
                if (!masterVeiculoList.contains(veiculo)) {
                    masterVeiculoList.add(veiculo);
                }
                filtrarVeiculosPorCliente(cliente);
                
                if (!cmbVeiculo.getItems().contains(veiculo)) {
                    cmbVeiculo.getItems().add(veiculo);
                }
                cmbVeiculo.getSelectionModel().select(veiculo);
            } else {
                System.err.println("Aviso: Veículo com ID " + os.getId_veiculo() + " não encontrado no banco de dados.");
            }
            Funcionario funcionario = funcionarioDAO.buscarPorId(os.getId_funcionario());
            if (funcionario != null) {
                if (!masterFuncionarioList.contains(funcionario)) {
                    masterFuncionarioList.add(funcionario);
                }
                if (!cmbFuncionario.getItems().contains(funcionario)) {
                    cmbFuncionario.getItems().add(funcionario);
                }
                cmbFuncionario.getSelectionModel().select(funcionario);
            } else {
                System.err.println("Aviso: Funcionário com ID " + os.getId_funcionario() + " não encontrado no banco de dados.");
            }

            dtpDataAbertura.setValue(os.getData_abertura());
            dtpDataFechamento.setValue(os.getData_fechamento());
            dtpDataSaidaEfetiva.setValue(os.getData_saida_efetiva());
            txtDescricaoProblema.setText(os.getDescricao_problema());
            txtServicosRealizados.setText(os.getServicos_realizados());
            txtValorTotal.setText(String.format("%.2f", os.getValor_total()).replace(".", ","));
            cmbStatus.setValue(os.getStatus_os());

        } else {
            dtpDataAbertura.setValue(LocalDate.now());
            cmbStatus.setValue("Pendente");
            limparCampos();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemServicoDAO = new OrdemServicoDAO();
        clienteDAO = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();
        funcionarioDAO = new FuncionarioDAO();
        
        masterClientList = FXCollections.observableArrayList(clienteDAO.buscarTodos());
        masterVeiculoList = FXCollections.observableArrayList(veiculoDAO.buscarTodos());
        masterFuncionarioList = FXCollections.observableArrayList(funcionarioDAO.buscarTodos());

        clientesDisponiveis = FXCollections.observableArrayList(masterClientList);
        veiculosDisponiveis = FXCollections.observableArrayList(masterVeiculoList);
        funcionariosDisponiveis = FXCollections.observableArrayList(masterFuncionarioList);

        configurarCmbCliente();
        configurarCmbVeiculo();
        configurarCmbFuncionario();
        configurarCmbStatus();

        adicionarFormatoNumerico(txtValorTotal);
        adicionarLimiteTextArea(txtDescricaoProblema, 1000);
        adicionarLimiteTextArea(txtServicosRealizados, 1000);

        cmbCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
            filtrarVeiculosPorCliente(newVal);
            cmbVeiculo.getSelectionModel().clearSelection();
            cmbVeiculo.getEditor().clear();
        });
        
        if (ordemEmEdicao == null) {
            limparCampos();
        }
    }

    private void configurarCmbCliente() {
        cmbCliente.setItems(clientesDisponiveis);
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override public String toString(Cliente c) { return c != null ? c.getNome_completo() + " (ID: " + c.getId_cliente() + ")" : ""; }
            @Override public Cliente fromString(String s) {
                return masterClientList.stream()
                    .filter(c -> toString(c).equalsIgnoreCase(s) || (s != null && String.valueOf(c.getId_cliente()).equals(s.trim())))
                    .findFirst().orElse(null);
            }
        });
        cmbCliente.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                cmbCliente.setItems(clientesDisponiveis); 
            } else {
                List<Cliente> filteredList = masterClientList.stream()
                    .filter(cliente -> cliente.getNome_completo().toLowerCase().contains(newText.toLowerCase()) ||
                                       String.valueOf(cliente.getId_cliente()).contains(newText))
                    .collect(Collectors.toList());
                cmbCliente.setItems(FXCollections.observableArrayList(filteredList));
                if (!filteredList.isEmpty() && !cmbCliente.isShowing()) {
                    cmbCliente.show();
                } else if (filteredList.isEmpty()) {
                    cmbCliente.hide();
                }
            }
        });
        cmbCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {
                 cmbCliente.getEditor().setText(cmbCliente.getConverter().toString(newVal));
            }
        });
    }

    private void configurarCmbVeiculo() {
        cmbVeiculo.setItems(veiculosDisponiveis);
        cmbVeiculo.setConverter(new StringConverter<Veiculo>() {
            @Override public String toString(Veiculo v) { return v != null ? v.getModelo() + " (" + v.getPlaca() + ")" : ""; }
            @Override public Veiculo fromString(String s) {
                return masterVeiculoList.stream()
                    .filter(v -> toString(v).equalsIgnoreCase(s) || (s != null && v.getPlaca().equalsIgnoreCase(s.trim())))
                    .findFirst().orElse(null);
            }
        });
        cmbVeiculo.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                if (cmbCliente.getValue() == null) {
                    cmbVeiculo.setItems(veiculosDisponiveis); 
                } else {
                    filtrarVeiculosPorCliente(cmbCliente.getValue()); 
                }
            } else {
                List<Veiculo> filteredList = masterVeiculoList.stream() 
                    .filter(veiculo -> veiculo.getModelo().toLowerCase().contains(newText.toLowerCase()) ||
                                       veiculo.getPlaca().toLowerCase().contains(newText.toLowerCase()))
                    .collect(Collectors.toList());
                cmbVeiculo.setItems(FXCollections.observableArrayList(filteredList));
                if (!filteredList.isEmpty() && !cmbVeiculo.isShowing()) {
                    cmbVeiculo.show();
                } else if (filteredList.isEmpty()) {
                    cmbVeiculo.hide();
                }
            }
        });
        cmbVeiculo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {
                 cmbVeiculo.getEditor().setText(cmbVeiculo.getConverter().toString(newVal));
            }
        });
    }

    private void configurarCmbFuncionario() {
        cmbFuncionario.setItems(funcionariosDisponiveis);
        cmbFuncionario.setConverter(new StringConverter<Funcionario>() {
            @Override public String toString(Funcionario f) { return f != null ? f.getNome_completo() + " (ID: " + f.getId_funcionario() + ")" : ""; }
            @Override public Funcionario fromString(String s) {
                return masterFuncionarioList.stream()
                    .filter(f -> toString(f).equalsIgnoreCase(s) || (s != null && String.valueOf(f.getId_funcionario()).equals(s.trim())))
                    .findFirst().orElse(null);
            }
        });
        cmbFuncionario.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                cmbFuncionario.setItems(funcionariosDisponiveis);
            } else {
                List<Funcionario> filteredList = masterFuncionarioList.stream()
                    .filter(f -> f.getNome_completo().toLowerCase().contains(newText.toLowerCase()) ||
                                 String.valueOf(f.getId_funcionario()).contains(newText))
                    .collect(Collectors.toList());
                cmbFuncionario.setItems(FXCollections.observableArrayList(filteredList));
                if (!filteredList.isEmpty() && !cmbFuncionario.isShowing()) {
                    cmbFuncionario.show();
                } else if (filteredList.isEmpty()) {
                    cmbFuncionario.hide();
                }
            }
        });
        cmbFuncionario.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {
                 cmbFuncionario.getEditor().setText(cmbFuncionario.getConverter().toString(newVal));
            }
        });
    }

    private void configurarCmbStatus() {
        cmbStatus.setItems(FXCollections.observableArrayList(
            "Pendente", "Em Andamento", "Aguardando Peças", "Concluido", "Cancelado" // 
        ));
    }

    private void filtrarVeiculosPorCliente(Cliente cliente) {
        if (cliente == null) {
            veiculosDisponiveis.setAll(masterVeiculoList);
        } else {
            List<Veiculo> lista = masterVeiculoList.stream()
                .filter(v -> v.getId_cliente() == cliente.getId_cliente())
                .collect(Collectors.toList());
            veiculosDisponiveis.setAll(lista); 
        }
        cmbVeiculo.setItems(veiculosDisponiveis);
        cmbVeiculo.getSelectionModel().clearSelection();
        cmbVeiculo.getEditor().clear();
    }

    @FXML private void btnSalvarClick(MouseEvent event) {
        if (cmbCliente.getValue() == null || cmbVeiculo.getValue() == null || cmbFuncionario.getValue() == null ||
            dtpDataAbertura.getValue() == null || txtDescricaoProblema.getText().isEmpty() ||
            txtValorTotal.getText().isEmpty() || cmbStatus.getValue() == null) {
            mostrarAlertaErro("Campos Obrigatórios Vazios",
                "Preencha todos os campos: Cliente, Veículo, Funcionário, Data de Abertura, Descrição do Problema, Valor Total e Status.");
            return;
        }

        int idC = cmbCliente.getValue().getId_cliente();
        int idV = cmbVeiculo.getValue().getId_veiculo();
        int idF = cmbFuncionario.getValue().getId_funcionario();
        LocalDate dataA = dtpDataAbertura.getValue();
        LocalDate dataF = dtpDataFechamento.getValue();
        LocalDate dataS = dtpDataSaidaEfetiva.getValue();
        String descr = txtDescricaoProblema.getText();
        String servicos = txtServicosRealizados.getText();
        double valor;
        try {
            valor = Double.parseDouble(txtValorTotal.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaErro("Valor Total Inválido", "Informe um valor numérico válido.");
            return;
        }
        String status = cmbStatus.getValue();

        if (ordemEmEdicao == null) {
            OrdemServico nova = new OrdemServico(idC, idV, idF, dataA, dataF, dataS, descr, servicos, valor, status);
            if (ordemServicoDAO.cadastrar(nova)) {
                mostrarAlertaInformacao("Sucesso", "Ordem de Serviço cadastrada com sucesso!");
                btnLimparClick(null);
            } else mostrarAlertaErro("Erro", "Falha ao cadastrar ordem de serviço.");
        } else {
            ordemEmEdicao.setId_cliente(idC);
            ordemEmEdicao.setId_veiculo(idV);
            ordemEmEdicao.setId_funcionario(idF);
            ordemEmEdicao.setData_abertura(dataA);
            ordemEmEdicao.setData_fechamento(dataF);
            ordemEmEdicao.setData_saida_efetiva(dataS);
            ordemEmEdicao.setDescricao_problema(descr);
            ordemEmEdicao.setServicos_realizados(servicos);
            ordemEmEdicao.setValor_total(valor);
            ordemEmEdicao.setStatus_os(status);

            if (ordemServicoDAO.alterar(ordemEmEdicao)) {
                mostrarAlertaInformacao("Sucesso", "Ordem de Serviço atualizada!");
                if (app != null) app.showGridServico();
            } else mostrarAlertaErro("Erro", "Falha ao atualizar ordem de serviço.");
        }
    }

    @FXML private void btnLimparClick(MouseEvent event) {
        limparCampos();
    }

    @FXML private void btnVoltarClick(MouseEvent event) {
        if (app != null) app.voltarOrdem();
        else System.err.println("App não inicializada.");
    }

    private void limparCampos() {
        cmbCliente.getSelectionModel().clearSelection(); cmbCliente.getEditor().clear();
        cmbVeiculo.getSelectionModel().clearSelection(); cmbVeiculo.getEditor().clear();
        cmbFuncionario.getSelectionModel().clearSelection(); cmbFuncionario.getEditor().clear();
        dtpDataAbertura.setValue(LocalDate.now());
        dtpDataFechamento.setValue(null);
        dtpDataSaidaEfetiva.setValue(null);
        txtDescricaoProblema.clear();
        txtServicosRealizados.clear();
        txtValorTotal.clear();
        cmbStatus.setValue("Pendente");
        ordemEmEdicao = null;
        
        clientesDisponiveis.setAll(masterClientList);
        veiculosDisponiveis.setAll(masterVeiculoList);
        funcionariosDisponiveis.setAll(masterFuncionarioList);
        cmbCliente.setItems(clientesDisponiveis);
        cmbVeiculo.setItems(veiculosDisponiveis);
        cmbFuncionario.setItems(funcionariosDisponiveis);
    }

    private void mostrarAlertaInformacao(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    private void mostrarAlertaErro(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m); a.showAndWait();
    }

    private void adicionarFormatoNumerico(TextField tf) {
        tf.textProperty().addListener((ChangeListener<String>) (ov, oldVal, newVal) -> {
            if (!newVal.matches("\\d*([.,]\\d{0,2})?")) tf.setText(oldVal);
        });
    }

    private void adicionarLimiteTextArea(TextArea ta, int max) {
        ta.textProperty().addListener((ov, oldVal, newVal) -> {
            if (newVal.length() > max) ta.setText(oldVal);
        });
    }
}