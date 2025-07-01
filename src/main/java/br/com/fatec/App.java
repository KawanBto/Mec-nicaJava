package br.com.fatec;

import br.com.fatec.model.Cliente;
import br.com.fatec.model.Funcionario;
import br.com.fatec.model.Veiculo; 
import br.com.fatec.model.OrdemServico; 

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Autonexus - Mecânica Automotiva");

        showMainMenu(); 
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public void voltarMenu() {
        showMainMenu();
    }

    public void showMainMenu() {
        System.out.println("DEBUG: App.showMainMenu() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/Menu.fxml"));
            Parent mainMenu = loader.load();

            br.com.fatec.controller.Menu controller = loader.getController();
            if (controller != null) {
                controller.setApp(this); 
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no MenuController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de Menu.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(mainMenu);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Dashboard");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void voltarCliente() {
        showGridCliente(); 
    }

    public void voltarFuncionario() {
        showGridFuncionario();
    }
    
    public void voltarVeiculo() {
        showGridVeiculo();
    }
    
    public void voltarOrdem() {
        showGridServico();
    }
    
    public void voltarAtendimento() {
        showGridAtendimento();
    }

    public void showGridCliente() {
        System.out.println("DEBUG: App.showGridCliente() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/GridCliente.fxml"));
            Parent gridClientePane = loader.load(); 

            br.com.fatec.controller.GridCliente controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no GridClienteController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de GridCliente.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(gridClientePane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Cliente");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGridFuncionario() {
        System.out.println("DEBUG: App.showGridFuncionario() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/GridFuncionario.fxml"));
            Parent gridFuncionarioPane = loader.load();

            br.com.fatec.controller.GridFuncionario controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no GridFuncionarioController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de GridFuncionario.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(gridFuncionarioPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Funcionario");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGridVeiculo() {
        System.out.println("DEBUG: App.showGridVeiculo() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/GridVeiculo.fxml"));
            Parent gridVeiculoPane = loader.load();

            br.com.fatec.controller.GridVeiculo controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no GridVeiculoController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de GridVeiculo.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(gridVeiculoPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Veiculo");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGridServico() {
        System.out.println("DEBUG: App.showGridServico() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/GridOrdemServico.fxml"));
            Parent gridServicoPane = loader.load();

            br.com.fatec.controller.GridOrdemServico controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no GridOrdemServicoController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de GridOrdemServico.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(gridServicoPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Ordem de Servico");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGridAvancada() {
        System.out.println("DEBUG: App.showGridAvancada() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/GridBuscaAvancada.fxml"));
            Parent gridBuscaAvancadaPane = loader.load();

            br.com.fatec.controller.GridBuscaAvancada controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no GridBuscaAvancadaController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de GridBuscaAvancada.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(gridBuscaAvancadaPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Busca Avancada");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGridAtendimento() {
        System.out.println("DEBUG: App.showGridAtendimento() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/GridListaAtendimento.fxml"));
            Parent gridAtendimentoPane = loader.load();

            br.com.fatec.controller.GridListaAtendimento controller = loader.getController();
            if (controller != null) {
                controller.setApp(this); 
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no GridListaAtendimentoController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de GridListaAtendimento.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(gridAtendimentoPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Lista de Atendimento");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCadCliente(Cliente cliente) {
        System.out.println("DEBUG: App.showCadCliente() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/CadCliente.fxml"));
            Parent cadClientePane = loader.load();

            br.com.fatec.controller.CadCliente controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no CadClienteController (hash: " + controller.hashCode() + ")");
                if (cliente != null) {
                    controller.setClienteParaEdicao(cliente);
                }
            } else {
                System.err.println("ERRO: Controlador de CadCliente.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(cadClientePane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Cliente");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showCadVeiculo(Veiculo veiculo) { 
        System.out.println("DEBUG: App.showCadVeiculo() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/CadVeiculo.fxml"));
            Parent cadVeiculoPane = loader.load();

            br.com.fatec.controller.CadVeiculo controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no CadVeiculoController (hash: " + controller.hashCode() + ")");
                if (veiculo != null) {
                    controller.setVeiculo(veiculo);
                }
            } else {
                System.err.println("ERRO: Controlador de CadVeiculo.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(cadVeiculoPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Veiculo");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showCadFuncionario(Funcionario funcionario) {
        System.out.println("DEBUG: App.showCadFuncionario() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/CadFuncionario.fxml"));
            Parent cadFuncionarioPane = loader.load();

            br.com.fatec.controller.CadFuncionario controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no CadFuncionarioController (hash: " + controller.hashCode() + ")");
                if (funcionario != null) {
                    controller.setFuncionario(funcionario);
                }
            } else {
                System.err.println("ERRO: Controlador de CadFuncionario.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(cadFuncionarioPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Funcionario");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showCadServico(OrdemServico ordemServico) { 
        System.out.println("DEBUG: App.showCadServico() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/CadOrdemServico.fxml"));
            Parent cadOrdemServicoPane = loader.load();

            br.com.fatec.controller.CadOrdemServico controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no CadOrdemServicoController (hash: " + controller.hashCode() + ")");
                if (ordemServico != null) {
                    controller.setOrdemServico(ordemServico);
                }
            } else {
                System.err.println("ERRO: Controlador de CadOrdemServico.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(cadOrdemServicoPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Ordem de Servico");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCadServico() {
        showCadServico(null);
    }
    
    public void showCadAtendimento(Object filaAtendimento) { 
        System.out.println("DEBUG: App.showCadAtendimento() chamado. App instance hash: " + this.hashCode());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/br/com/fatec/view/CadFilaAtendimento.fxml"));
            Parent cadFilaAtendimentoPane = loader.load();

            br.com.fatec.controller.CadFilaAtendimento controller = loader.getController();
            if (controller != null) {
                controller.setApp(this);
                System.out.println("DEBUG: Injetando App (hash: " + this.hashCode() + ") no CadFilaAtendimentoController (hash: " + controller.hashCode() + ")");
            } else {
                System.err.println("ERRO: Controlador de CadFilaAtendimento.fxml é nulo. Verifique 'fx:controller'.");
            }

            Scene scene = new Scene(cadFilaAtendimentoPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Autonexus - Gerenciamento de Fila de Atendimento"); 
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCadAtendimento() {
        showCadAtendimento(null);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
