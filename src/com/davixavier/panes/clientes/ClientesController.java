package com.davixavier.panes.clientes;

import java.awt.Menu;
import java.sql.SQLException;
import java.util.function.Consumer;

import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.clientes.Endereço;
import com.davixavier.entidades.clientes.IdentificaçãoFactory;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.nodes.AppBarButtonStyler;
import com.davixavier.nodes.MaterialToolBar;
import com.davixavier.nodes.SearchBar;
import com.davixavier.nodes.materiallist.MaterialCellItem;
import com.davixavier.nodes.materiallist.MaterialCellNode;
import com.davixavier.nodes.materiallist.MaterialListCell;
import com.davixavier.nodes.materiallist.MaterialListView;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.panes.estoque.EstoqueTabelaManager;
import com.davixavier.utils.FollowableTextFieldGroup;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ClientesController 
{
    @FXML
    private BorderPane primaryPane;

    @FXML
    private JFXTreeTableView<TreeWrapper<Cliente>> clientesTabela;
    
    @FXML
    private AnchorPane clientesToolbar;
    
    private JFXTextField pesquisaField;
    
    private JFXButton clientesMenuButton;
    
    private MaterialListView<Cliente> listView;
    
    private SearchBar searchBar;
    
    private String buscanome;
    private String buscacpf;
    private String buscacnpj;
    
    @FXML 
    void initialize()
    {
    	//ClientesTabelaManager.setUp(clientesTabela);
    	
    	MaterialToolBar toolBar = new MaterialToolBar();
    	toolBar.setTitleString("Clientes");
    	clientesToolbar.getChildren().add(toolBar);
    	
    	AnchorPane.setTopAnchor(toolBar, 0.0);
    	AnchorPane.setBottomAnchor(toolBar, 0.0);
    	AnchorPane.setLeftAnchor(toolBar, 0.0);
    	AnchorPane.setRightAnchor(toolBar, 0.0);
    	
    	pesquisaField = toolBar.getSearchBar().getTextField();
    	searchBar = toolBar.getSearchBar();
    	clientesMenuButton = toolBar.getMenuButton();
    	
    	pesquisaField.setOnAction(e -> pesquisar(e));
    	
    	MenuItem adicionarMenuItem = new MenuItem("Adicionar novo cliente");
    	adicionarMenuItem.setOnAction(e -> adicionarCliente(e));
    	
    	//MenuItem removerMenuItem = new MenuItem("Remover selecionados");
    	//removerMenuItem.setOnAction(e -> removerDialog().getDialog().show());
    	
    	clientesMenuButton.getContextMenu().getItems().addAll(adicionarMenuItem);//, removerMenuItem);
    	
    	listView = ClientesListaManager.setUp();
    	
    	listView.setId("clientesModuleList");
    	primaryPane.setCenter(listView);
    	
    	searchBar.getClearButton().setOnAction(e ->
    	{
    		limparPesquisa(e);
    	});
    	
    	buscanome = "";
    	buscacpf = "";
    	buscacnpj = "";
    	
    	searchBar.getSearchOptions().setVisible(true);
    	searchBar.getSearchOptions().getItems().addAll("Nome", "CPF", "CNPJ");
    	searchBar.getSearchOptions().getSelectionModel().clearAndSelect(0);
    	
    	searchBar.getSearchOptions().getSelectionModel().selectedItemProperty().addListener((l, oldval, newval) ->
    	{
    		pesquisaField.setTextFormatter(null);
    		
    		pesquisaField.clear();
    		buscanome = "";
        	buscacpf = "";
        	buscacnpj = "";
    		
    		if (newval.equals("CPF")) 
    		{
				Utils.cpfFormatter(pesquisaField);
			}
    		else if (newval.equals("CNPJ")) 
    		{
				Utils.cnpjFormatter(pesquisaField);
			}
    	});
    }

    @FXML
    void limparPesquisa(ActionEvent event)
    {
    	ClientesListaManager.refresh(listView, "", "", "");
    	
    	buscacnpj = "";
    	buscacpf = "";
    	buscanome = "";
    	
    	pesquisaField.setText("");
    }
    
    @FXML
    void adicionarCliente(ActionEvent event) 
    {
    	adicionarDialog().getDialog().show();
    }

    @FXML
    void pesquisar(ActionEvent event) 
    {
    	if (searchBar.getSearchOptions().getSelectionModel().getSelectedItem().equals("Nome"))
    	{
    		buscanome = pesquisaField.getText();
    		buscacnpj = "";
    		buscacpf = "";
    	}
    	else if (searchBar.getSearchOptions().getSelectionModel().getSelectedItem().equals("CPF"))
    	{
    		buscanome = "";
    		buscacnpj = "";
    		buscacpf = pesquisaField.getText().replace(".", "").replace("-", "").replace(" ", "");
    	}
    	else if (searchBar.getSearchOptions().getSelectionModel().getSelectedItem().equals("CNPJ"))
    	{
    		buscanome = "";
    		buscacnpj = pesquisaField.getText().replace(".", "").replace("-", "").replace("/", "").replace(" ", "");
    		buscacpf = "";
    	}
    	
    	ClientesListaManager.refresh(listView, buscanome, buscacpf, buscacnpj);
    }
    
    private FlatJFXDialog removerDialog()
    {
    	JFXButton simButton = new JFXButton("Sim");
    	JFXButton naoButton = new JFXButton("Não");
    	
    	StackPane pane = (StackPane)primaryPane.getScene().lookup("#primaryStackPane");
    	
    	String header = "Confirme sua ação";
    	String body = "Tem certeza que deseja excluir todos os produtos selecionados? (isso não poderá ser desfeito)";
    	FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, simButton, naoButton);
    	
    	simButton.setOnAction(e ->
    	{
    		dialog.setHeader("Processando...");
    		dialog.getLayout().setBody(new JFXSpinner());
    		dialog.getLayout().getActions().clear();
    		
    		ExecuterServices.getExecutor().execute(() ->
    		{
    			SimpleBooleanProperty success = new SimpleBooleanProperty(true); 
    			
    			clientesTabela.getRoot().getChildren().forEach(produtoWrapper ->
    			{
    				if (((JFXCheckBox)produtoWrapper.getValue().getUserData()).isSelected())
    				{
    					try 
    					{
							if (!ClienteDAO.getInstance().remove(produtoWrapper.getValue().getValue(), ConnectionFactory.getOfflineConnection()))
							{
								success.set(false);
							}
						}
    					catch (SQLException e1) 
    					{
							e1.printStackTrace();
							success.set(false);
						}
    				}
    			});
    			
    			Platform.runLater(() ->
    			{
    				if(success.get())
    				{
    					dialog.setHeader("Sucesso");
    					dialog.setBody("Operação realizada com sucesso!");
    				}
    				else 
    				{
    					dialog.setHeader("Falha");
    					dialog.setBody("Ocorreu um erro na operação, tente novamente.");
					}
    				
    				ClientesTabelaManager.refreshTable(clientesTabela);
    			});
    		});
    	});
    	
    	naoButton.setOnAction(e -> 
    	{
    		dialog.getDialog().close();
    	});
    	
    	return dialog;
    }
    
    private FlatJFXDialog adicionarDialog()
    {
    	StackPane pane = (StackPane)primaryPane.getScene().lookup("#primaryStackPane");
    	
    	JFXButton confirmarButton = new JFXButton("Confirmar");
    	
    	String header = "Insira as informações do cliente novo";
    	String body = "";
    	FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, confirmarButton);
    	
    	Text nomeText = new Text("Insira o nome do cliente");
    	JFXTextField nomeField = new JFXTextField();
    	nomeField.setPromptText("Nome do cliente");
    	nomeField.textProperty().addListener((l, oldVal, newVal) ->
    	{
    		if (newVal.length() > 100)
    			nomeField.setText(oldVal);
    	});
    	
    	Text nomefantasiaText = new Text("Insira o nome do estabelecimento do cliente, se existir");
    	JFXTextField nomefantasiaField = new JFXTextField();
    	nomefantasiaField.setPromptText("Nome fantasia do cliente");
    	
    	Text cpfText = new Text("Insira o CPF ou CPNJ do cliente\n(somente os números, sem ponto ou traço)");
    	JFXTextField cpfField = new JFXTextField();
    	cpfField.setPromptText("CPF do cliente");
    	
    	Text endereçoText = new Text("Insira as informações do endereço do cliente");
    	JFXTextField endereçoField = new JFXTextField();
    	endereçoField.setPromptText("Insira a rua do endereço do cliente");
    	JFXTextField numeroField = new JFXTextField();
    	numeroField.setPromptText("Insira o número do endereço cliente");
    	JFXTextField bairroField = new JFXTextField();
    	bairroField.setPromptText("Insira o bairro do endereço do cliente");
    	JFXTextField cidadeField = new JFXTextField();
    	cidadeField.setPromptText("Insira a cidade do endereço do cliente");
    	JFXTextField estadoField = new JFXTextField();
    	estadoField.setPromptText("Insira o estado do endereço do cliente");
    	JFXTextField complementoField = new JFXTextField();
    	complementoField.setPromptText("Insira um complemento para o endereço se necessário");
    	
    	nomeField.setStyle("");
    	endereçoField.styleProperty().bind(nomeField.styleProperty());
    	numeroField.styleProperty().bind(nomeField.styleProperty());
    	bairroField.styleProperty().bind(nomeField.styleProperty());
    	cidadeField.styleProperty().bind(nomeField.styleProperty());
    	estadoField.styleProperty().bind(nomeField.styleProperty());
    	complementoField.styleProperty().bind(nomeField.styleProperty());
    	cpfField.styleProperty().bind(nomeField.styleProperty());
    	nomefantasiaField.styleProperty().bind(nomeField.styleProperty());
    	
    	Text telefonesText = new Text("Insira um ou dois números de telefone para o cliente");
    	Text telefone1Text = new Text("Telefone 1");
    	Text telefone2Text = new Text("Telefone 2");
    	JFXTextField telefone1Field = new JFXTextField();
    	telefone1Field.setPromptText("Telefone 1");
    	JFXTextField telefone2Field = new JFXTextField();
    	telefone2Field.setPromptText("Telefone 2");
    	telefone1Field.styleProperty().bind(nomeField.styleProperty());
    	telefone2Field.styleProperty().bind(nomeField.styleProperty());
    	
    	Utils.applyPhoneFormatter(telefone1Field);
    	Utils.applyPhoneFormatter(telefone2Field);
    	
    	JFXTextField cnpjField = new JFXTextField();
    	cnpjField.setVisible(false);
    	cnpjField.setPromptText("CNPJ do cliente");
    	
    	Utils.cpfFormatter(cpfField);
    	Utils.cnpjFormatter(cnpjField);
    	
    	JFXComboBox<String> comboBox = new JFXComboBox<String>();
    	comboBox.getItems().add("CPF");
    	comboBox.getItems().add("CNPJ");
    	comboBox.getSelectionModel().select(0);
    	comboBox.setStyle("-fx-background-color: transparent;");
    	
    	comboBox.getSelectionModel().selectedItemProperty().addListener((l, oldval, newval) ->
    	{
    		if (newval.equals("CPF"))
    		{
    			String text = cnpjField.getText().replace(".", "");
    			text = text.replace("-", "").replace("/", "");
    			
    			cpfField.setText(text);
    			
    			cpfField.setVisible(true);
    			cnpjField.setVisible(false);
    		}
    		else 
    		{
    			String text = cpfField.getText().replace(".", "");
    			text = text.replace("-", "");
    			
    			cnpjField.setText(text);
    			
    			cnpjField.setVisible(true);
    			cpfField.setVisible(false);
			}
    	});
    	
    	StackPane cpfcnpjPane = new StackPane(cpfField, cnpjField, comboBox);
    	cpfcnpjPane.prefHeightProperty().bind(cpfField.heightProperty());
    	cpfcnpjPane.prefWidthProperty().bind(cpfField.widthProperty());
    	cpfcnpjPane.setAlignment(Pos.CENTER_RIGHT);
    	
    	Text erroText = new Text();
    	erroText.setFill(Color.RED);
    	
    	VBox vBox = new VBox(nomeText, nomeField, nomefantasiaText, nomefantasiaField, cpfText, 
    			cpfcnpjPane, endereçoText, endereçoField, numeroField, 
    			bairroField, cidadeField, estadoField, complementoField, telefonesText,
    			telefone1Text, telefone1Field, telefone2Text, telefone2Field);
    	vBox.setAlignment(Pos.TOP_LEFT);
    	vBox.setSpacing(15);
    	
    	ScrollPane scrollPane = new ScrollPane(vBox);
    	scrollPane.setFitToHeight(true);
    	scrollPane.setFitToWidth(true);
    	scrollPane.setStyle("-fx-background-color:transparent;");
    	
    	VBox mainvBox = new VBox(scrollPane, erroText);
    	
    	FollowableTextFieldGroup textFieldGroup = new FollowableTextFieldGroup();
    	textFieldGroup.getNodes().setAll(nomeField, nomefantasiaField, cpfField, numeroField, bairroField, cidadeField, 
    			estadoField, complementoField, telefone1Field, telefone2Field);
    	
    	dialog.getLayout().setBody(mainvBox);
    	
    	confirmarButton.setOnAction(e ->
    	{
    		if (endereçoField.getText().isEmpty() || cpfField.getText().isEmpty() || bairroField.getText().isEmpty() 
    				|| numeroField.getText().isEmpty() || cidadeField.getText().isEmpty() || nomeField.getText().isEmpty() 
    				|| estadoField.getText().isEmpty())
    		{
    			erroText.setText("Erro: campo(s) vazio(s)."); 
    			erroText.setVisible(true);
    			return;
    		}
    		
    		String CPF = cpfField.getText().replace(".", "").replace("-", "");
    		String CNPJ = cnpjField.getText().replace(".", "").replace("-", "").replace("/", "");
    		
    		if (comboBox.getSelectionModel().getSelectedIndex() == 0 && !Utils.isCPF(CPF))
    		{
    			erroText.setText("Erro: CPF inválido.");
    			erroText.setVisible(true);
    			return;
    		}
    		
    		if (comboBox.getSelectionModel().getSelectedIndex() == 1 && !Utils.isCNPJ(CNPJ))
    		{
    			erroText.setText("Erro: CNPJ inválido.");
    			erroText.setVisible(true);
    			return;
    		}
    		
    		if ((telefone1Field.getText().contains(" ") || telefone2Field.getText().contains(" ")))
    		{
    			erroText.setText("Erro: número(s) de telefone(s) inválido(s).");
    			erroText.setVisible(true);
    			return;
    		}
    		erroText.setVisible(false);
    		
    		Cliente cliente = new Cliente();
    		cliente.setNome(nomeField.getText());
    		cliente.setNomeFantasia(nomefantasiaField.getText());
    		cliente.setEndereço(new Endereço(endereçoField.getText(), numeroField.getText(), bairroField.getText(), 
    				cidadeField.getText(), estadoField.getText(), complementoField.getText()));
    		if (comboBox.getSelectionModel().getSelectedIndex() == 0)
    		{
    			cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(CPF));
    		}
    		else 
    		{
    			cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(CNPJ));
			}
    		
    		if (complementoField.getText().isEmpty())
    		{
    			cliente.getEndereço().setComplemento("Sem complemento");
    		}
    		
    		ObservableList<Telefone> telefones = FXCollections.observableArrayList();
    		
    		Consumer<JFXTextField> consumer = field ->
    		{
    			String[] split = field.getText().replace("(", " ").replace(")", " ").replace("-", " ").split("\\ ");
    			
    			String pais = "0055";
    			String ddd = split[1];
    			String numero = split[2] + split[3];
    			
    			telefones.add(new Telefone(pais, ddd, numero));
    		};
    		
    		if (!telefone1Field.getText().contains(" "))
    		{
    			consumer.accept(telefone1Field); 
    		}
    		if (!telefone2Field.getText().contains(" "))
    		{
    			consumer.accept(telefone2Field);
    		}
    		
    		cliente.setTelefones(telefones);
    		
    		dialog.getLayout().getBody().clear();
    		dialog.getLayout().getActions().clear();
    		dialog.setHeader("Processando...");
    		dialog.getLayout().setBody(new JFXSpinner());
    		
    		ExecuterServices.getExecutor().execute(() ->
    		{
    			SimpleBooleanProperty ret = new SimpleBooleanProperty();
    			try 
    			{
					ret.set(ClienteDAO.getInstance().insert(cliente, ConnectionFactory.getOfflineConnection()));
				} 
    			catch (Exception e2) 
    			{
					e2.printStackTrace();
					ret.set(false);
				}
    			
    			Platform.runLater(() ->
    			{
    				if (ret.get())
    				{
    					dialog.setHeader("Sucesso");
    					dialog.setBody("Operação concluída com sucesso.");
    				}
    				else
    				{
    					dialog.setHeader("Falha");
    					dialog.setBody("Falha ao realizar a operação, tente novamente mais tarde.");
    				}
    				
    				ClientesTabelaManager.refreshTable(clientesTabela);
    				ClientesListaManager.refresh(listView, "", "", "");
    			});
    		});
    	});
    	textFieldGroup.lastEventHandlerProperty().bind(confirmarButton.onActionProperty());
    	
    	return dialog;
    }

}
