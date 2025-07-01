package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.model.Cliente;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DefaultStringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class CadCliente implements Initializable {

    private App app;
    private Cliente clienteParaEdicao; 

    @FXML private TextField txtRua;
    @FXML private TextField txtBairro;
    @FXML private TextField txtCidade;
    @FXML private TextField txtNomeCompleto;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtCep;

    @FXML private Button btnSalvar;
    @FXML private Button btnVoltar;

    private ClienteDAO clienteDAO;

    public void setApp(App app) {
        this.app = app;
    }

    public void setClienteParaEdicao(Cliente cliente) {
        this.clienteParaEdicao = cliente;
        if (cliente != null) {
            txtNomeCompleto.setText(cliente.getNome_completo());
            txtCpf.setText(cliente.getCpf());
            txtEmail.setText(cliente.getEmail());
            txtTelefone.setText(cliente.getTelefone());
            txtCep.setText(cliente.getCep());
            
            String enderecoCompleto = cliente.getEndereco();
            if (enderecoCompleto != null && !enderecoCompleto.isEmpty()) {
                String[] partes = enderecoCompleto.split(",\\s*");
                if (partes.length >= 1) {
                    txtRua.setText(partes[0].trim());
                }
                if (partes.length >= 2) {
                    txtBairro.setText(partes[1].trim());
                }
                if (partes.length >= 3) {
                    txtCidade.setText(partes[2].trim());
                }
            } else {
                txtRua.setText("");
                txtBairro.setText("");
                txtCidade.setText("");
            }

            txtCpf.setDisable(true);
        } else {
            limparCampos();
        }
        System.out.println("Modo: " + (clienteParaEdicao == null ? "Cadastro" : "Edição"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO = new ClienteDAO();
        limparCampos();
        
        txtCpf.focusedProperty().addListener((obs, oldVal, newVal) -> {
             if (!newVal) {
                 txtCpf.setText(formatarCpf(txtCpf.getText()));
             } else { //
                 txtCpf.setText(removerFormatacao(txtCpf.getText()));
             }
         });
         txtTelefone.focusedProperty().addListener((obs, oldVal, newVal) -> {
             if (!newVal) {
                 txtTelefone.setText(formatarTelefone(txtTelefone.getText()));
             } else {
                 txtTelefone.setText(removerFormatacao(txtTelefone.getText()));
             }
         });
         txtCep.focusedProperty().addListener((obs, oldVal, newVal) -> {
             if (!newVal) {
                 txtCep.setText(formatarCep(txtCep.getText()));
             } else { 
                 txtCep.setText(removerFormatacao(txtCep.getText()));
             }
         });

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (change.getControl() == txtCpf) {

                if (newText.replaceAll("[^0-9]", "").length() > 11) {
                    return null; 
                }
            } else if (change.getControl() == txtTelefone) {
                if (newText.replaceAll("[^0-9]", "").length() > 11) {
                    return null;
                }
            } else if (change.getControl() == txtCep) {
                if (newText.replaceAll("[^0-9]", "").length() > 8) {
                    return null;
                }
            } else if (change.getControl() == txtNomeCompleto) {

                if (newText.length() > 100) {
                    return null;
                }
            } else if (change.getControl() == txtEmail) {
                if (newText.length() > 100) {
                    return null;
                }
            } else if (change.getControl() == txtRua) {
                if (newText.length() > 255) {
                    return null;
                }
            } else if (change.getControl() == txtBairro) {
                if (newText.length() > 100) {
                    return null;
                }
            } else if (change.getControl() == txtCidade) {
                if (newText.length() > 100) {
                    return null;
                }
            }
            return change;
        };

        txtNomeCompleto.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtCpf.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtEmail.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtTelefone.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtCep.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtRua.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtBairro.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
        txtCidade.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));
    }
    
    private void limparCampos() {
        txtNomeCompleto.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtCep.setText("");
        txtRua.setText("");   
        txtBairro.setText("");   
        txtCidade.setText("");   
        txtCpf.setDisable(false);
        this.clienteParaEdicao = null;
    }

    @FXML
    private void btnSalvarClick(MouseEvent event) {
        String nome = txtNomeCompleto.getText();
        String cpf = removerFormatacao(txtCpf.getText());
        String email = txtEmail.getText();
        String telefone = removerFormatacao(txtTelefone.getText());
        String cep = removerFormatacao(txtCep.getText());
        String rua = txtRua.getText();
        String bairro = txtBairro.getText();
        String cidade = txtCidade.getText();

        String enderecoParaSalvar = "";
        if (!rua.isEmpty()) {
            enderecoParaSalvar += rua;
        }
        if (!bairro.isEmpty()) {
            if (!enderecoParaSalvar.isEmpty()) {
                enderecoParaSalvar += ", ";
            }
            enderecoParaSalvar += bairro;
        }
        if (!cidade.isEmpty()) {
            if (!enderecoParaSalvar.isEmpty()) {
                enderecoParaSalvar += ", ";
            }
            enderecoParaSalvar += cidade;
        }

        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || telefone.isEmpty() ||
            cep.isEmpty() || enderecoParaSalvar.isEmpty()) { 
            mostrarAlertaErro("Todos os campos (incluindo endereço completo) devem ser preenchidos.");
            return;
        }

        if (clienteParaEdicao == null) {
            Cliente novoCliente = new Cliente(nome, cpf, telefone, email, cep, enderecoParaSalvar); 
            if (clienteDAO.cadastrar(novoCliente)) {
                mostrarAlertaInformacao("Cliente cadastrado com sucesso!");
                limparCampos();
            } else {
                mostrarAlertaErro("Erro ao cadastrar cliente. Verifique o CPF, pode já existir.");
            }
        } else {
            clienteParaEdicao.setNome_completo(nome);
            clienteParaEdicao.setEmail(email);
            clienteParaEdicao.setTelefone(telefone);
            clienteParaEdicao.setCep(cep);
            clienteParaEdicao.setEndereco(enderecoParaSalvar);

            if (clienteDAO.alterar(clienteParaEdicao)) {
                mostrarAlertaInformacao("Cliente atualizado com sucesso!");
                if (app != null) {
                    app.voltarCliente();
                }
            } else {
                mostrarAlertaErro("Erro ao atualizar cliente.");
            }
        }
    }

    @FXML
    private void btnVoltarClick(MouseEvent event) {
        if (app != null) {
            app.voltarCliente();
        } else {
            System.out.println("Erro: Referência da App não definida para navegação.");
        }
    }

    private void mostrarAlertaInformacao(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
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
            return cep.substring(0, 5) + "-" +
                   cep.substring(5, 8);
        }
        return cep;
    }
}