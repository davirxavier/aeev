package com.davixavier.application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.davixavier.application.img.IconsPath;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBUtils;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.compras.Venda;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.entidades.usuarios.Usuário;
import com.davixavier.entidades.usuarios.UsuárioDAO;
import com.davixavier.nodes.ToolbarButton;
import com.davixavier.nodes.VSpacer;
import com.davixavier.nodes.materiallist.MaterialListView;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.panes.PanePathing;
import com.davixavier.panes.clientes.ClientesListaManager;
import com.davixavier.panes.estoque.EstoqueTabelaManager;
import com.davixavier.panes.historico.HistoricoTabelaManager;
import com.davixavier.panes.login.Login;
import com.davixavier.panes.login.LoginController;
import com.davixavier.panes.vendas.VendasTabelaManager;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainController 
{
	//FXML nodes
    @FXML
    private AnchorPane primaryPane;

    @FXML
    private StackPane primaryStackPane;

    @FXML
    private BorderPane primaryBorderPane;

    @FXML
    private VBox optionsBox;

    @FXML
    private JFXButton vendasButton;

    @FXML
    private JFXButton estoqueButton;

    @FXML
    private JFXButton usuariosButton;
    
    @FXML
    private JFXSpinner spinner;
    
    @FXML
    private JFXListView<String> modulosList;
    
    @FXML
    private AnchorPane centerPane;
    
    @FXML
    private AnchorPane sidebarPane;
    
    //Variavel que diz se a janela esta no modo de carregamento
    private static SimpleBooleanProperty loading;
    private static Runnable updateModules = () -> {};
    
    private MenuModuleHandler moduleHandler;
    
    private Node activeModule;
    
    @FXML
    public void initialize() throws IOException
    {
    	//Listener para o valor de loading, sempre que mudar desativar a janela e mostrar um spinner
    	loading = new SimpleBooleanProperty();
    	loading.addListener((l, oldVal, newVal) ->
    	{
    		primaryStackPane.setDisable(newVal);
    		spinner.setVisible(newVal);
    	});
    	
    	spinner.setVisible(true);
    	
    	//Inicializar conexao
    	ConnectionFactory.getOfflineConnection();
    	
    	//Inicializar tela de login e adicionar a janela
    	FXMLLoader loginFxmlLoader = new FXMLLoader();
    	StackPane loginPane = loginFxmlLoader.load(getClass().getResource(PanePathing.LOGINPANE.toString()).openStream());
    	LoginController loginController = loginFxmlLoader.getController();
    	primaryStackPane.getChildren().add(1, loginPane);
    	
    	moduleHandler = new MenuModuleHandler();
    	
    	updateModules = () ->
    	{
    		JFXTreeTableView<TreeWrapper<Produto>> vendasTabela = (JFXTreeTableView<TreeWrapper<Produto>>)primaryPane.getScene().lookup("#vendasProdutosTable");
    		
    		if (vendasTabela != null)
    		{
    			VendasTabelaManager.setUp(vendasTabela);
    		}
    		
    		JFXTreeTableView<TreeWrapper<Produto>> esttabela = 
					(JFXTreeTableView<TreeWrapper<Produto>>)primaryPane.getScene().lookup("#estoqueTable");
			
			if (esttabela != null)
			{
				EstoqueTabelaManager.refreshTable(esttabela);
				EstoqueTabelaManager.filterTable(esttabela, EstoqueTabelaManager.getLastSearchNome(), EstoqueTabelaManager.getLastSearchId());
			}
    		
			JFXTreeTableView<TreeWrapper<Venda>> histTabela = (JFXTreeTableView<TreeWrapper<Venda>>)primaryPane.getScene().lookup("#tabelaHistorico");
			
			if (histTabela != null)
			{
				HistoricoTabelaManager.refreshTable(histTabela);
				HistoricoTabelaManager.filterTable(histTabela, 
						HistoricoTabelaManager.getLastStartData(), 
						HistoricoTabelaManager.getLastEndData(), 
						HistoricoTabelaManager.getLastTipoPagamento(), 
						HistoricoTabelaManager.getLastNomeProduto());
			}
    	};
    	
    	ImageView userimg = new ImageView();
    	userimg.setFitHeight(80);
    	userimg.setFitWidth(80);
    	userimg.setPreserveRatio(true);
    	sidebarPane.getChildren().add(userimg);
    	AnchorPane.setBottomAnchor(userimg, 50.0);
    	AnchorPane.setLeftAnchor(userimg, 10.0);
    	userimg.setEffect(new DropShadow(2, Color.WHITE));
    	
    	Label userLabel = new Label();
    	userLabel.setPrefHeight(80);
    	userLabel.setWrapText(true);
    	userLabel.setTextFill(Color.WHITE);
    	userLabel.setAlignment(Pos.BOTTOM_CENTER);
    	userLabel.setStyle("-fx-font-size: 11; -fx-font-weight: bold;");
    	sidebarPane.getChildren().add(userLabel);
    	AnchorPane.setBottomAnchor(userLabel, 135.0);
    	AnchorPane.setLeftAnchor(userLabel, 5.0);
    	AnchorPane.setRightAnchor(userLabel, 5.0);
    	
    	loginController.loggedProperty().addListener((l, oldVal, newVal) ->
    	{
    		userimg.setImage(Loja.getInstance().getLogo());
    		userLabel.setText(Loja.getInstance().getNome());
    		
    		initModules(newVal);
    		
    		moduleHandler.initModules();
    		moduleHandler.getModules().forEach((k, v) ->
    		{
    			centerPane.getChildren().add(v);
    			
    			AnchorPane.setTopAnchor(v, 0.0);
    			AnchorPane.setLeftAnchor(v, 0.0);
    			AnchorPane.setRightAnchor(v, 0.0);
    			AnchorPane.setBottomAnchor(v, 0.0);
    		});
    		
    		moduleHandler.initModulesList(modulosList);
    	});
    	
//    	
//    	ConnectionFactory.connectionCreatedProperty().addListener(l ->
//    	{
//    		if (ConnectionFactory.isConnectionCreated())
//    			userimg.setImage(UserImageDAO.get());
//    	});
    	
    	ToolbarButton configButton = new ToolbarButton(null);
    	//configButton.setText("Meu usuario");
    	
    	Text confText = new Text("Meu usuário");
    	confText.setFill(Color.WHITE);
    	VBox confBox = new VBox(new ImageView(IconsPath.ACCOUNT24PXWHITE.getImage()), confText);
    	confBox.setAlignment(Pos.CENTER);
    	configButton.setGraphic(confBox);
    	configButton.setRadius(2);
    	
    	sidebarPane.getChildren().add(configButton);
    	AnchorPane.setBottomAnchor(configButton, 8.0);
    	AnchorPane.setLeftAnchor(configButton, 7.0);
    	
    	configButton.setOnAction(e ->
    	{
    		configDialog().getDialog().show();
    	});
    }
    
    private void initModules(boolean logged)
    {
    	if (logged)
		{	
	    	moduleHandler.addModule("Vendas", null, () ->
	    	{
	    		VendasTabelaManager.setUp((JFXTreeTableView<TreeWrapper<Produto>>)primaryPane.getScene().lookup("#vendasProdutosTable"));
    			setActiveModule(moduleHandler.getModules().get("vendas"));
	    	});
	    	moduleHandler.addModule("Estoque", null, () ->
	    	{
	    		JFXTreeTableView<TreeWrapper<Produto>> tabela = 
    					(JFXTreeTableView<TreeWrapper<Produto>>)primaryPane.getScene().lookup("#estoqueTable");
    			
    			EstoqueTabelaManager.refreshTable(tabela);
    			EstoqueTabelaManager.filterTable(tabela, EstoqueTabelaManager.getLastSearchNome(), EstoqueTabelaManager.getLastSearchId());
    			setActiveModule(moduleHandler.getModules().get("estoque"));
	    	});
	    	moduleHandler.addModule("Histórico", null, () ->
	    	{
	    		JFXTreeTableView<TreeWrapper<Venda>> tabela = (JFXTreeTableView<TreeWrapper<Venda>>)primaryPane.getScene().lookup("#tabelaHistorico");
    			
    			HistoricoTabelaManager.refreshTable(tabela);
    			HistoricoTabelaManager.filterTable(tabela, 
    					HistoricoTabelaManager.getLastStartData(), 
    					HistoricoTabelaManager.getLastEndData(), 
    					HistoricoTabelaManager.getLastTipoPagamento(), 
    					HistoricoTabelaManager.getLastNomeProduto());
    			setActiveModule(moduleHandler.getModules().get("historico"));
	    	});
	    	moduleHandler.addModule("Clientes", null, () ->
	    	{
	    		setActiveModule(moduleHandler.getModules().get("clientes"));
	    	});
		}
    }
    
    private FlatJFXDialog configDialog()
    {
    	JFXButton confButton = new JFXButton("Salvar mudanças");
    	JFXButton voltarButton = new JFXButton("Voltar");
    	
    	String header = "Configurações de usuário";
    	String body = "";
    	FlatJFXDialog dialog = new FlatJFXDialog(primaryStackPane, header, body, voltarButton, confButton);
    	
    	confButton.setVisible(false);
    	voltarButton.setVisible(false);
    	
    	Text usernameText = new Text("Nome de usuário");
    	JFXTextField usernameField = new JFXTextField();
    	usernameField.setPromptText("Mude seu nome de usuário");
    	
    	JFXPasswordField passwordField = new JFXPasswordField();
    	passwordField.setPromptText("Insira uma senha nova");
    	
    	PasswordField passwordFieldConfirm = new JFXPasswordField();
    	passwordFieldConfirm.setPromptText("Insira a nova senha novamente");
    	
    	JFXButton usernameButton = new JFXButton("Alterar nome de usuário");
    	JFXButton passwordButton = new JFXButton("Alterar senha");
    	usernameButton.setPrefWidth(150);
    	passwordButton.setPrefWidth(150);
    	
    	VBox vBox = new VBox(usernameButton, passwordButton);
    	vBox.setAlignment(Pos.CENTER);
    	vBox.setSpacing(15);
    	
    	usernameButton.setOnAction(e ->
    	{
    		vBox.setAlignment(Pos.CENTER_LEFT);
    		vBox.getChildren().clear();
    		
    		Text errorText = new Text("Erro: insira um nome de usuário novo.");
    		errorText.setFill(Color.RED);
    		errorText.setVisible(false);
    		vBox.getChildren().addAll(usernameText, usernameField, errorText);
    		confButton.setVisible(true);
    		voltarButton.setVisible(true);
    		
    		dialog.setHeader("Alterar nome de usuário");
 
    		confButton.setOnAction(confirmar -> 
    		{
    			if (usernameField.getText().isEmpty())
    			{
    				errorText.setVisible(true);
    				return; 
    			}
    				
    			voltarButton.setVisible(false);
    			
    			confirmarSenha(dialog, vBox, confButton, (confirmarhandler) ->
    			{
    				dialog.getLayout().getActions().clear();
    				dialog.getLayout().setBody(new JFXSpinner());
    				dialog.setHeader("Processando...");
    				
    				ExecuterServices.getExecutor().execute(() ->
    				{
    					try
    					{
    						UsuárioDAO usuárioDAO = UsuárioDAO.getInstance();
        					
        					Usuário usuário = usuárioDAO.getDefault();
        					usuário.setUsername(usernameField.getText());
        					
        					final boolean success = usuárioDAO.update(usuário, ConnectionFactory.getOfflineConnection());
        					
        					Platform.runLater(() ->
        					{
        						if (success)
        						{
        							dialog.setHeader("Sucesso");
        							dialog.setBody("Operação concluída com sucesso.");
        						}
        						else 
        						{
    								dialog.setHeader("Erro");
    								dialog.setBody("Ocorreu uma falha, tente novamente mais tarde.");
    							}
        					});
						} 
    					catch (SQLException e2)
    					{
							e2.printStackTrace();
						}
    				});
    			});
    		});
    		
    		voltarButton.setOnAction(voltar ->
    		{
    			vBox.getChildren().clear();
    			vBox.setAlignment(Pos.CENTER);
    			vBox.getChildren().addAll(usernameButton, passwordButton);
    			
    			confButton.setVisible(false);
    			voltarButton.setVisible(false);
    		});
    	});
    	
    	passwordButton.setOnAction(e ->
    	{
    		dialog.setHeader("Alterar sua senha");
    		confButton.setVisible(true);
    		voltarButton.setVisible(true);
    		
    		Text errorText = new Text("Erro: as senhas não são iguais.");
    		errorText.setFill(Color.RED);
    		errorText.setVisible(false);
    		
    		vBox.setAlignment(Pos.CENTER_LEFT);
    		vBox.getChildren().clear();
    		vBox.getChildren().addAll(passwordField, new VSpacer(),passwordFieldConfirm, errorText);
    		
    		confButton.setOnAction(confirmar ->
    		{
    			if (!passwordField.getText().equals(passwordFieldConfirm.getText()))
    			{
    				errorText.setVisible(true);
    				errorText.setText("Erro: as senhas não são iguais.");
    				return;
    			}
    			
    			if (passwordField.getText().length() < 8)
    			{
    				errorText.setText("Erro: sua senha deve ter no mínimo 8 caracteres.");
    				errorText.setVisible(true);
    				return;
    			}
    			
    			voltarButton.setVisible(false);
    			confButton.setText("Confirmar");
    			
    			confirmarSenha(dialog, vBox, confButton, confirmarsenha ->
    			{
    				dialog.getLayout().getActions().clear();
    				dialog.getLayout().setBody(new JFXSpinner());
    				dialog.setHeader("Processando...");
    				
    				ExecuterServices.getExecutor().execute(() ->
    				{
    					try 
    					{
    						UsuárioDAO usuárioDAO = UsuárioDAO.getInstance();
        					
        					Usuário usuário = usuárioDAO.getDefault();
        					usuário.setSenhaHash(Utils.getHashString(passwordField.getText()));
        					
        					final boolean success = usuárioDAO.update(usuário, ConnectionFactory.getOfflineConnection());
        					
        					Platform.runLater(() ->
        					{
        						if (success)
        						{
        							dialog.setHeader("Sucesso");
        							dialog.setBody("Operação concluída com sucesso.");
        						}
        						else 
        						{
    								dialog.setHeader("Erro");
    								dialog.setBody("Ocorreu uma falha, tente novamente mais tarde.");
    							}
        					});
						} 
    					catch (Exception e2) 
    					{
    						e2.printStackTrace();
						}
    				});
    			});
    		});
    		
    		voltarButton.setOnAction(voltar ->
    		{
    			vBox.getChildren().clear();
    			vBox.setAlignment(Pos.CENTER);
    			vBox.getChildren().addAll(usernameButton, passwordButton);
    			
    			confButton.setVisible(false);
    			voltarButton.setVisible(false);
    		});
    	});
    	
    	dialog.getLayout().setBody(vBox);
    	
    	return dialog;
    }
    
    private void confirmarSenha(FlatJFXDialog dialog, VBox vBox, JFXButton confButton, EventHandler<ActionEvent> eventHandler)
    {
    	dialog.setHeader("Insira sua senha atual para continuar");
    	
    	JFXPasswordField senhaField = new JFXPasswordField();
    	senhaField.setPromptText("Sua senha atual");
    	
    	Text erroText = new Text("Erro: senha incorreta.");
    	erroText.setFill(Color.RED);
    	erroText.setVisible(false);
    	
    	vBox.getChildren().clear();
    	vBox.getChildren().addAll(senhaField, erroText);
    	
    	try
    	{
    		Usuário usuário = UsuárioDAO.getInstance().getDefault();
        	
        	confButton.setOnAction(e ->
        	{
        		if (Utils.getHashString(senhaField.getText()).equals(usuário.getSenhaHash()))
        		{
        			eventHandler.handle(e);
        		}
        		else 
        		{
    				erroText.setVisible(true);
    			}
        	});
		}
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    }
    
    //Deixa todos os modulos invisiveis menos o passado como argumento
    private void setActiveModule(Node module)
    {
    	moduleHandler.getModules().forEach((k, v) ->
    	{
    		v.setVisible(false);
    	});
    	
    	if (module != null)
    		module.setVisible(true);
    	
    	activeModule = module;
    }
    
    public static void updateModules()
    {
    	updateModules.run();
    }
    
    //Mudar o valor da propriedade de loading
    public static void setLoading(boolean loading)
    {
    	MainController.loading.set(loading);
    }

	public static SimpleBooleanProperty getLoading() {
		return loading;
	}
}
