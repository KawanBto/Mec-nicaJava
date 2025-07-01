package br.com.fatec.controller;

import br.com.fatec.App; 
import br.com.fatec.model.Atendimento;
import br.com.fatec.model.FilaDeAtendimento;
import br.com.fatec.model.Servico;          
import br.com.fatec.DAO.DAOServico;          

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.Initializable;

public class CadFilaAtendimento implements Initializable {

    private App app; 

    @FXML
    private TextArea txtNome; 
    @FXML
    private TextArea txtHoraChegada; 
    @FXML
    private ComboBox<String> cbTipoServico; 
    @FXML
    private TextArea txtObservacao; 

    @FXML
    private Button btnAdicionarFila;
    @FXML
    private Button btnAlterar; 
    @FXML
    private Button btnVoltar;

    private FilaDeAtendimento filaDeAtendimento;
    private DAOServico daoServico; 
    private Atendimento atendimentoParaEdicao; 

    public void setApp(App app) {
        this.app = app; 
        System.out.println("DEBUG: setApp no CadFilaAtendimento chamado. App está " + (app != null ? "DEFINIDO" : "NULO"));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filaDeAtendimento = FilaDeAtendimento.getInstance();
        daoServico = new DAOServico(); 

        List<Servico> todosServicos = daoServico.listarTodos(); 
        List<String> nomesServicos = todosServicos.stream()
                                           .map(Servico::getNomeServico)
                                           .collect(Collectors.toList());
        cbTipoServico.setItems(FXCollections.observableArrayList(nomesServicos));

        txtHoraChegada.setText(LocalTime.now().withSecond(0).withNano(0).toString());
    }

    public void setAtendimentoParaEdicao(Atendimento atendimento) {
        this.atendimentoParaEdicao = atendimento;
        if (atendimentoParaEdicao != null) {
            txtNome.setText(atendimento.getNomeCliente());
            txtHoraChegada.setText(atendimento.getHoraChegada().toString());
            cbTipoServico.getSelectionModel().select(atendimento.getServico()); 
            txtObservacao.setText(atendimento.getObservacao());
            btnAdicionarFila.setText("ATUALIZAR"); 
        } else {
             btnAdicionarFila.setText("ADICIONAR À FILA");
        }
    }

    @FXML
    private void btnAdicionarOuAlterarClick(MouseEvent event) {
        String nome = txtNome.getText();
        String horaChegadaStr = txtHoraChegada.getText();
        String servicoSelecionado = cbTipoServico.getValue(); 
        String observacao = txtObservacao.getText();

        if (nome.isEmpty() || horaChegadaStr.isEmpty() || servicoSelecionado == null || servicoSelecionado.isEmpty()) {
            exibirAlertaAviso("Campos Obrigatórios", null, "Por favor, preencha todos os campos obrigatórios (Nome, Hora de Chegada, Tipo de Serviço).");
            return;
        }

        LocalTime horaChegada;
        try {
            horaChegada = LocalTime.parse(horaChegadaStr);
        } catch (DateTimeParseException e) {
            exibirAlertaErro("Formato de Hora Inválido", null, "Por favor, insira a hora no formato HH:MM:SS ou HH:MM.");
            return;
        }

        if (atendimentoParaEdicao == null) { 
            Atendimento novoAtendimento = new Atendimento(nome, horaChegada, servicoSelecionado, observacao); 
            boolean adicionado = filaDeAtendimento.adicionarAtendimento(novoAtendimento);
            if (adicionado) {
                exibirAlertaInfo("Sucesso", null, "Atendimento adicionado à fila!");
                limparCampos();
            } else {
                exibirAlertaErro("Erro", null, "Fila cheia! Não foi possível adicionar o atendimento.");
            }
        } else { 
            atendimentoParaEdicao.setNomeCliente(nome);
            atendimentoParaEdicao.setHoraChegada(horaChegada);
            atendimentoParaEdicao.setServico(servicoSelecionado); 
            atendimentoParaEdicao.setObservacao(observacao);
            boolean atualizado = filaDeAtendimento.atualizarAtendimento(atendimentoParaEdicao);
            if (atualizado) {
                exibirAlertaInfo("Sucesso", null, "Atendimento atualizado com sucesso!");
            } else {
                exibirAlertaErro("Erro", null, "Não foi possível atualizar o atendimento.");
            }
        }
    }
    
    @FXML
    private void btnVoltarClick(MouseEvent event) {
        System.out.println("DEBUG: btnVoltarClick em CadFilaAtendimento. App atual: " + (app != null ? "DEFINIDO" : "NULO"));
        try {
            if (app != null) {
                app.showGridAtendimento();
            } else {
                exibirAlertaErro("Erro de Navegação", "Referência à aplicação ausente.", "Não foi possível voltar à lista de atendimentos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlertaErro("Erro de Navegação", "Não foi possível voltar à lista de atendimentos.", e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtHoraChegada.setText(LocalTime.now().withSecond(0).withNano(0).toString());
        cbTipoServico.getSelectionModel().clearSelection();
        txtObservacao.clear();
        atendimentoParaEdicao = null; 
        btnAdicionarFila.setText("ADICIONAR À FILA");
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
