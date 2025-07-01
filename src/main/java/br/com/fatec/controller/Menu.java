package br.com.fatec.controller;

import br.com.fatec.App; 
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType; 
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;     
import javafx.scene.Node;      
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable; 

public class Menu implements Initializable { 

    private App app; 

    public void setApp(App app) { 
        this.app = app;
        System.out.println("DEBUG: setApp no Menu chamado. App está " + (app != null ? "DEFINIDO" : "NULO"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DEBUG: Menu.initialize chamado.");
    }

    @FXML
    private void btnClientesClick(MouseEvent event) {
        System.out.println("DEBUG: btnClientesClick - App antes da chamada: " + (app != null ? "DEFINIDO" : "NULO"));
        if (app != null) {
            app.showGridCliente(); 
        } else {
            exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível carregar a tela de Clientes.");
        }
    }

    @FXML
    private void btnAvancadaClick(MouseEvent event) {
        System.out.println("DEBUG: btnAvancadaClick - App antes da chamada: " + (app != null ? "DEFINIDO" : "NULO"));
        if (app != null) {
            app.showGridAvancada(); 
        } else {
            exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível carregar a tela de Busca Avançada.");
        }
    }
    
    @FXML
    private void btnAtendimentoClick(MouseEvent event) {
        System.out.println("DEBUG: btnAtendimentoClick - App antes da chamada: " + (app != null ? "DEFINIDO" : "NULO"));
        if (app != null) {
            app.showGridAtendimento(); 
        } else {
            exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível carregar a tela de Atendimento.");
        }
    }

    @FXML
    private void btnServicoClick(MouseEvent event) {
        System.out.println("DEBUG: btnServicoClick - App antes da chamada: " + (app != null ? "DEFINIDO" : "NULO"));
        if (app != null) {
            app.showGridServico(); 
        } else {
            exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível carregar a tela de Serviços.");
        }
    }

    @FXML
    private void btnVeiculoslick(MouseEvent event) {
        System.out.println("DEBUG: btnVeiculoslick - App antes da chamada: " + (app != null ? "DEFINIDO" : "NULO"));
        if (app != null) {
            app.showGridVeiculo(); 
        } else {
            exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível carregar a tela de Veículos.");
        }
    }

    @FXML
    private void btnFuncionariosClick(MouseEvent event) {
        System.out.println("DEBUG: btnFuncionariosClick - App antes da chamada: " + (app != null ? "DEFINIDO" : "NULO"));
        if (app != null) {
            app.showGridFuncionario(); 
        } else {
            exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível carregar a tela de Funcionários.");
        }
    }

    @FXML
    private void btnSairClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void exibirAlertaErro(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
