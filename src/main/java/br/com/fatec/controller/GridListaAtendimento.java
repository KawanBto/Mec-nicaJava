package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.model.Atendimento;
import br.com.fatec.model.FilaDeAtendimento;
import br.com.fatec.model.Servico;           
import br.com.fatec.DAO.DAOServico;            

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node; 

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.Initializable;

public class GridListaAtendimento implements Initializable {

    private App app;
    @FXML
    private TableView<Atendimento> tvAtendimentos;
    @FXML
    private TableColumn<Atendimento, Integer> colNumero;
    @FXML
    private TableColumn<Atendimento, String> colNomeCliente;
    @FXML
    private TableColumn<Atendimento, LocalTime> colHoraChegada;
    @FXML
    private TableColumn<Atendimento, String> colServico;
    @FXML
    private TableColumn<Atendimento, String> colObservacao;

    @FXML
    private ComboBox<String> cbServicoFiltro;
    @FXML
    private ComboBox<String> cbHoraChegadaFiltro;
    @FXML
    private ComboBox<String> cbNomeFiltro;

    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnExcluir; 

    private FilaDeAtendimento filaDeAtendimento; 
    private DAOServico daoServico; 

    public void setApp(App app) {
        this.app = app;
        System.out.println("DEBUG: setApp no GridListaAtendimento chamado. App está " + (app != null ? "DEFINIDO" : "NULO"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("DEBUG: GridListaAtendimento.initialize chamado.");
        filaDeAtendimento = FilaDeAtendimento.getInstance(); 
        daoServico = new DAOServico(); 
        configurarTabela();
        popularComboBoxes();
        carregarAtendimentosNaTabela();
    }

    private void configurarTabela() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("idAtendimento"));
        colNomeCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colHoraChegada.setCellValueFactory(new PropertyValueFactory<>("horaChegada"));
        colServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        colObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
    }

    private void popularComboBoxes() {
        List<Servico> todosServicos = daoServico.listarTodos(); 
        List<String> nomesServicos = todosServicos.stream()
                                           .map(Servico::getNomeServico)
                                           .collect(Collectors.toList());
        cbServicoFiltro.setItems(FXCollections.observableArrayList(nomesServicos));
        
        atualizarFiltrosComDadosDaFila(); 
    }
    
    private void atualizarFiltrosComDadosDaFila() {
        List<Atendimento> todosAtendimentos = Arrays.asList(filaDeAtendimento.getAtendimentos());

        List<String> nomesUnicos = todosAtendimentos.stream()
                                                 .map(Atendimento::getNomeCliente)
                                                 .distinct()
                                                 .sorted()
                                                 .collect(Collectors.toList());
        cbNomeFiltro.setItems(FXCollections.observableArrayList(nomesUnicos));

        List<String> horasUnicas = todosAtendimentos.stream()
                                                    .map(atendimento -> atendimento.getHoraChegada().toString())
                                                    .distinct()
                                                    .sorted()
                                                    .collect(Collectors.toList());
        cbHoraChegadaFiltro.setItems(FXCollections.observableArrayList(horasUnicas));
    }

    private void carregarAtendimentosNaTabela() {
        ObservableList<Atendimento> dados = FXCollections.observableArrayList(filaDeAtendimento.getAtendimentos());
        tvAtendimentos.setItems(dados);
    }

    @FXML
    private void filtrarAtendimentos() {
        String nome = cbNomeFiltro.getValue();
        String horaChegadaStr = cbHoraChegadaFiltro.getValue();
        String servico = cbServicoFiltro.getValue();

        List<Atendimento> atendimentosFiltrados = Arrays.asList(filaDeAtendimento.getAtendimentos());

        if (nome != null && !nome.isEmpty()) {
            atendimentosFiltrados = atendimentosFiltrados.stream()
                                                .filter(a -> a.getNomeCliente().equalsIgnoreCase(nome))
                                                .collect(Collectors.toList());
        }
        if (horaChegadaStr != null && !horaChegadaStr.isEmpty()) {
            LocalTime horaChegadaFiltro = LocalTime.parse(horaChegadaStr); 
            atendimentosFiltrados = atendimentosFiltrados.stream()
                                                .filter(a -> a.getHoraChegada().equals(horaChegadaFiltro))
                                                .collect(Collectors.toList());
        }
        if (servico != null && !servico.isEmpty()) {
            atendimentosFiltrados = atendimentosFiltrados.stream()
                                                 .filter(a -> a.getServico().equalsIgnoreCase(servico))
                                                 .collect(Collectors.toList());
        }
        
        tvAtendimentos.setItems(FXCollections.observableArrayList(atendimentosFiltrados));
    }

    @FXML
    private void btnGerenciarAtendimentoClick(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/CadFilaAtendimento.fxml"));
            Parent root = loader.load();
            
            CadFilaAtendimento controller = loader.getController();
            if (app != null) {
                controller.setApp(app); 
            } else {
                 System.err.println("DEBUG: App é nulo ao tentar injetar em CadFilaAtendimento de GridListaAtendimento.");
            }

            Atendimento atendimentoSelecionado = tvAtendimentos.getSelectionModel().getSelectedItem();
            if (atendimentoSelecionado != null) {
                controller.setAtendimentoParaEdicao(atendimentoSelecionado);
            }
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gerenciar Atendimento");
            stage.show();
            
            stage.setOnHidden(e -> {
                carregarAtendimentosNaTabela();
                atualizarFiltrosComDadosDaFila();
            });

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlertaErro("Erro de Navegação", "Não foi possível carregar a tela de gerenciamento de atendimento.", e.getMessage());
        }
    }
    
    @FXML
    private void btnExcluirAtendimentoClick() {
        Atendimento atendimentoSelecionado = tvAtendimentos.getSelectionModel().getSelectedItem();
        if (atendimentoSelecionado != null) {
            boolean removido = filaDeAtendimento.removerAtendimento(atendimentoSelecionado.getIdAtendimento());
            if (removido) {
                exibirAlertaInfo("Atendimento Removido", null, "Atendimento removido com sucesso!");
                carregarAtendimentosNaTabela();
                atualizarFiltrosComDadosDaFila();
            } else {
                exibirAlertaErro("Erro ao Remover", null, "Não foi possível remover o atendimento.");
            }
        } else {
            exibirAlertaAviso("Nenhum Atendimento Selecionado", null, "Por favor, selecione um atendimento para excluir.");
        }
    }

    @FXML
    private void btnVoltarClick(MouseEvent event) {
        System.out.println("DEBUG: btnVoltarClick em GridListaAtendimento. App atual: " + (app != null ? "DEFINIDO" : "NULO"));
        try {
            if (app != null) {
                app.showMainMenu(); 
            } else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/Menu.fxml"));
                Parent root = loader.load();
                
                br.com.fatec.controller.Menu menuController = loader.getController();
                if (menuController != null) { 
                    System.err.println("DEBUG: App nulo em GridListaAtendimento ao voltar. Nao eh possivel injetar App no MenuController.");
                } else {
                    System.err.println("ERRO: Controlador de Menu.fxml é nulo ao voltar de GridListaAtendimento. Verifique 'fx:controller'.");
                }

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Menu Principal");
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            exibirAlertaErro("Erro de Navegação", "Não foi possível voltar ao menu.", e.getMessage());
        }
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

    private void exibirAlertaAviso(String title, String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
