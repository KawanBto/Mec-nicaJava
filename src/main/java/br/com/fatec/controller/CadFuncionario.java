package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.DAO.FuncionarioDAO;
import br.com.fatec.model.Funcionario;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;

import java.net.URL;
import java.util.ResourceBundle;

public class CadFuncionario implements Initializable {

    private App app;
    private FuncionarioDAO funcionarioDAO;
    private Funcionario funcionarioEmEdicao;

    @FXML private TextField txtNomeCompleto;
    @FXML private TextField txtCpf;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCargo;
    @FXML private TextField txtSalario;

    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnVoltar;

    public void setApp(App app) {
        this.app = app;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionarioEmEdicao = funcionario;
        if (funcionario != null) {
            txtNomeCompleto.setText(funcionario.getNome_completo());
            txtCpf.setText(aplicarMascaraCpf(funcionario.getCpf()));
            txtTelefone.setText(aplicarMascaraTelefone(funcionario.getTelefone()));
            txtEmail.setText(funcionario.getEmail());
            txtCargo.setText(funcionario.getCargo());
            txtSalario.setText(String.valueOf(funcionario.getSalario()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        funcionarioDAO = new FuncionarioDAO();

        adicionarLimiteCaracteres(txtNomeCompleto, 150);

        adicionarMascaraCpf(txtCpf);
        
        adicionarMascaraTelefone(txtTelefone);

        adicionarLimiteCaracteres(txtEmail, 100);

        adicionarLimiteCaracteres(txtCargo, 50);

        adicionarFormatoNumericoSalario(txtSalario);
    }

    @FXML
    private void btnSalvarClick(MouseEvent event) {
        if (txtNomeCompleto.getText().isEmpty() || txtCpf.getText().isEmpty() ||
            txtTelefone.getText().isEmpty() || txtEmail.getText().isEmpty() ||
            txtCargo.getText().isEmpty() || txtSalario.getText().isEmpty()) {
            mostrarAlertaErro("Campos vazios", "Por favor, preencha todos os campos.");
            return;
        }

        String cpf = removerFormatacao(txtCpf.getText());
        if (cpf.length() != 11) {
            mostrarAlertaErro("CPF Inválido", "O CPF deve conter 11 dígitos numéricos.");
            return;
        }
        
        String telefone = removerFormatacao(txtTelefone.getText());
        if (telefone.length() < 10 || telefone.length() > 11) {
             mostrarAlertaErro("Telefone Inválido", "O telefone deve conter 10 ou 11 dígitos numéricos (DD+número).");
             return;
        }

        double salario;
        try {
            salario = Double.parseDouble(txtSalario.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaErro("Salário Inválido", "Por favor, insira um valor numérico válido para o salário.");
            return;
        }

        if (funcionarioEmEdicao == null) {
            Funcionario novoFuncionario = new Funcionario(
                txtNomeCompleto.getText(),
                cpf,
                telefone,
                txtEmail.getText(),
                txtCargo.getText(),
                salario
            );
            if (funcionarioDAO.cadastrar(novoFuncionario)) {
                mostrarAlertaInformacao("Sucesso", "Funcionário cadastrado com sucesso!");
                btnLimparClick(null); 
            } else {
                mostrarAlertaErro("Erro", "Falha ao cadastrar funcionário. Verifique os dados e tente novamente.");
            }
        } else {
            funcionarioEmEdicao.setNome_completo(txtNomeCompleto.getText());
            funcionarioEmEdicao.setCpf(cpf);
            funcionarioEmEdicao.setTelefone(telefone);
            funcionarioEmEdicao.setEmail(txtEmail.getText());
            funcionarioEmEdicao.setCargo(txtCargo.getText());
            funcionarioEmEdicao.setSalario(salario);

            if (funcionarioDAO.alterar(funcionarioEmEdicao)) {
                mostrarAlertaInformacao("Sucesso", "Funcionário atualizado com sucesso!");
                if (app != null) {
                    app.showGridFuncionario();
                }
            } else {
                mostrarAlertaErro("Erro", "Falha ao atualizar funcionário. Verifique os dados e tente novamente.");
            }
        }
    }

    @FXML
    private void btnLimparClick(MouseEvent event) {
        txtNomeCompleto.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCargo.setText("");
        txtSalario.setText("");
        funcionarioEmEdicao = null;
    }

    @FXML
    private void btnVoltarClick(MouseEvent event) {
        if (app != null) {
            app.showGridFuncionario();
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

    private void adicionarLimiteCaracteres(TextField textField, int limite) {
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > limite) {
                textField.setText(oldValue);
            }
        });
    }

    private void adicionarMascaraCpf(TextField textField) {
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            String cleanValue = removerFormatacao(newValue);
            String formattedValue = "";
            if (cleanValue.length() > 0) {
                if (cleanValue.length() <= 3) {
                    formattedValue = cleanValue;
                } else if (cleanValue.length() <= 6) {
                    formattedValue = cleanValue.substring(0, 3) + "." + cleanValue.substring(3);
                } else if (cleanValue.length() <= 9) {
                    formattedValue = cleanValue.substring(0, 3) + "." + cleanValue.substring(3, 6) + "." + cleanValue.substring(6);
                } else if (cleanValue.length() <= 11) {
                    formattedValue = cleanValue.substring(0, 3) + "." + cleanValue.substring(3, 6) + "." + cleanValue.substring(6, 9) + "-" + cleanValue.substring(9);
                } else {
                    formattedValue = oldValue;
                }
            }
            textField.setText(formattedValue);
        });
    }

    private String aplicarMascaraCpf(String cpfNumerico) {
        if (cpfNumerico == null || cpfNumerico.length() != 11) {
            return cpfNumerico;
        }
        return cpfNumerico.substring(0, 3) + "." + 
               cpfNumerico.substring(3, 6) + "." + 
               cpfNumerico.substring(6, 9) + "-" + 
               cpfNumerico.substring(9, 11);
    }

    private void adicionarMascaraTelefone(TextField textField) {
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            String cleanValue = removerFormatacao(newValue);
            String formattedValue = "";
            if (cleanValue.length() > 0) {
                if (cleanValue.length() <= 2) {
                    formattedValue = "(" + cleanValue;
                } else if (cleanValue.length() <= 6) {
                    formattedValue = "(" + cleanValue.substring(0, 2) + ")" + cleanValue.substring(2);
                } else if (cleanValue.length() <= 10) { 
                    formattedValue = "(" + cleanValue.substring(0, 2) + ")" + cleanValue.substring(2, 6) + "-" + cleanValue.substring(6);
                } else if (cleanValue.length() <= 11) { 
                    formattedValue = "(" + cleanValue.substring(0, 2) + ")" + cleanValue.substring(2, 7) + "-" + cleanValue.substring(7);
                } else {
                    formattedValue = oldValue;
                }
            }
            textField.setText(formattedValue);
        });
    }

    private String aplicarMascaraTelefone(String telefoneNumerico) {
        if (telefoneNumerico == null || (telefoneNumerico.length() != 10 && telefoneNumerico.length() != 11)) {
            return telefoneNumerico; 
        }
        if (telefoneNumerico.length() == 10) {
            return "(" + telefoneNumerico.substring(0, 2) + ")" + 
                   telefoneNumerico.substring(2, 6) + "-" + 
                   telefoneNumerico.substring(6, 10);
        } else {
            return "(" + telefoneNumerico.substring(0, 2) + ")" + 
                   telefoneNumerico.substring(2, 7) + "-" + 
                   telefoneNumerico.substring(7, 11);
        }
    }


    private void adicionarFormatoNumericoSalario(TextField textField) {
        textField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([.,]\\d{0,2})?")) { 
                textField.setText(oldValue);
            }
        });
    }
}