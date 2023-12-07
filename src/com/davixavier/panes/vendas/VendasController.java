package com.davixavier.panes.vendas;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.compras.TipoPagamento;
import com.davixavier.entidades.compras.Venda;
import com.davixavier.entidades.compras.VendaDAO;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.nodes.HSpacer;
import com.davixavier.nodes.MaterialTextField;
import com.davixavier.nodes.SearchBar;
import com.davixavier.nodes.materiallist.MaterialCellItem;
import com.davixavier.nodes.materiallist.MaterialListCell;
import com.davixavier.nodes.materiallist.MaterialListView;
import com.davixavier.nodes.materiallist.NoButtonMaterialCell;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class VendasController 
{

    @FXML
    private AnchorPane primaryPane;

    @FXML
    private JFXTreeTableView<TreeWrapper<Produto>> vendasProdutosTable;

    @FXML
    private AnchorPane carrinhoPane;
    
    private MaterialListView<ProdutoVenda> carrinhoLista;
    
    private SearchBar searchBar;
    
    private ObservableList<ProdutoVenda> produtosCarrinho;
    
    private Label subtotalLabel;
    private Label totalLabel;
    private Label descontoLabel;
    private Label subtotalNumLabel;
    private Label totalNumLabel;
    private JFXTextField descontoField;
    
    private double subtotal;
    private double totalCarrinho;
    private double desconto;
    
    private JFXButton confirmarButton;
    
    @FXML
    void initialize()
    {
    	VendasTabelaManager.setUp(vendasProdutosTable);
    	
    	primaryPane.lookupAll(".split-pane-divider").stream().forEach(div ->  div.setMouseTransparent(true));
    	
    	searchBar = new SearchBar();
    	searchBar.getTextField().setPromptText("Pesquisar no estoque...");
    	searchBar.getClearButton().setOnAction(e ->
    	{
    		searchBar.getTextField().clear();
    		VendasTabelaManager.refreshTable(vendasProdutosTable);
    	});
    	searchBar.getTextField().setOnAction(e ->
    	{
    		VendasTabelaManager.filterTable(vendasProdutosTable, searchBar.getTextField().getText());
    	});
    	
    	primaryPane.getChildren().add(searchBar);
    	AnchorPane.setTopAnchor(searchBar, 8.0);
    	AnchorPane.setLeftAnchor(searchBar, 5.0);
    	AnchorPane.setRightAnchor(searchBar, 355.0);
    	
    	carrinhoLista = new MaterialListView<>();
    	ListaCarrinhoManager.setUp(carrinhoLista);
    	carrinhoLista.setId("carrinhoLista");
    	carrinhoLista.setStyle("-fx-background-color: white;");
    	carrinhoLista.setItems(Carrinho.getItems());
    	
    	carrinhoPane.getChildren().add(carrinhoLista);
    	AnchorPane.setTopAnchor(carrinhoLista, 0.0);
    	AnchorPane.setRightAnchor(carrinhoLista, 0.0);
    	AnchorPane.setLeftAnchor(carrinhoLista, 0.0);
    	AnchorPane.setBottomAnchor(carrinhoLista, 150.0);
    	
    	produtosCarrinho = FXCollections.observableArrayList();
    	
    	setUpLabels();
    	
    	Carrinho.getItems().addListener((ListChangeListener.Change<? extends MaterialCellItem<ProdutoVenda>> change) -> 
    	{
    		change.next();
    		
    		refresh();
    	});
    	
    	confirmarButton = new JFXButton("Confirmar Venda");
    	carrinhoPane.getChildren().add(confirmarButton);
    	AnchorPane.setBottomAnchor(confirmarButton, 8.0);
    	AnchorPane.setRightAnchor(confirmarButton, 6.0);
    	
    	confirmarButton.setOnAction(e ->
    	{
    		confirmarDialog().getDialog().show();
    	});
    }
    
    private FlatJFXDialog confirmarDialog()
    {
    	StackPane pane = (StackPane)primaryPane.getScene().lookup("#primaryStackPane");
    	
    	JFXButton confButton = new JFXButton("Avançar");
    	
    	String header = "Insira o tipo de pagamento da venda";
    	String body = "";
    	
    	FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, confButton);
    	
    	if (Carrinho.getItems().isEmpty())
    	{
    		dialog.setHeader("Erro");
    		dialog.setBody("Insira algum produto no carrinho para confirmar a compra.");
    		
    		confButton.setText("Fechar");
    		
    		confButton.setOnAction(e ->
    		{
    			dialog.getDialog().close();
			});
    		
    		return dialog;
    	}
    	
    	setUpPagamentos(dialog, confButton);	
    	
    	return dialog;
    }
    
    private void setUpPagamentos(FlatJFXDialog dialog, JFXButton confButton)
    {
    	dialog.getLayout().getBody().clear();
    	
    	VBox vBox = new VBox();
    	vBox.setAlignment(Pos.TOP_LEFT);
    	vBox.setSpacing(20);
    	
    	JFXRadioButton tempRadio = new JFXRadioButton();
    	
    	ToggleGroup toggleGroup = new ToggleGroup();
    	
    	for (int i = 0; i < TipoPagamento.values().length; i++)
    	{
    		JFXRadioButton radioButton = new JFXRadioButton(TipoPagamento.values()[i].toString());
    		radioButton.setStyle("-jfx-selected-color: -buttoncolor; -jfx-unselected-color: gray; -fx-cursor: HAND;");
    		
    		vBox.getChildren().add(radioButton);
    		
    		toggleGroup.getToggles().add(radioButton);
    		
    		if (i == 1)
    			radioButton.setSelected(true);
    		else if (i == TipoPagamento.values().length-1)
    		{
    			tempRadio = radioButton;
    		}
    	}
    	
    	final JFXRadioButton outrosRadio = tempRadio;
    	
    	MaterialTextField outrosField = new MaterialTextField();
    	outrosField.disableProperty().bind(outrosRadio.selectedProperty().not());
    	outrosField.setPromptText("Informe o tipo de pagamento...");
    	outrosField.setStyle(outrosField.getStyle() + "-fx-prompt-text-fill: gray;");
    	
    	outrosRadio.setStyle("-jfx-selected-color: -buttoncolor; -jfx-unselected-color: gray; -fx-cursor: HAND;");
    	
    	Text errorText = new Text("Erro: campo de tipo de pagamento vazio.");
    	errorText.setFill(Color.RED);
    	errorText.setVisible(false);
    	
    	vBox.getChildren().add(outrosField);
    	vBox.getChildren().add(errorText);
  
    	dialog.getLayout().setBody(vBox);
    	
    	confButton.setOnAction(e ->
    	{
    		if (outrosRadio.isSelected() && outrosField.getText().isEmpty())
    		{
    			errorText.setVisible(true);
    			return;
    		}
    		
    		String metodopagamento = ((JFXRadioButton)toggleGroup.getSelectedToggle()).getText();
    		if (metodopagamento.equals("Outros"))
    			metodopagamento = outrosField.getText();
    		
    		setUpListaClientes(dialog, confButton, metodopagamento);
    	});
    }
    
    private void setUpListaClientes(FlatJFXDialog dialog, JFXButton confButton, String metodoPagamento)
    {
    	dialog.getLayout().setPrefWidth(600);
    	
    	dialog.getLayout().getBody().clear();
    	dialog.setHeader("Relacione um cliente a essa venda");
    	
    	JFXButton pularButton = new JFXButton("Não relacionar cliente");
    	dialog.getLayout().getActions().add(0, pularButton);
    	
    	ToggleGroup toggleGroup = new ToggleGroup();
    	
    	MaterialListView<Cliente> listView = new MaterialListView<Cliente>();
    	listView.setPrefHeight(400);
    	listView.setCellFactory(new Callback<ListView<MaterialCellItem<Cliente>>, ListCell<MaterialCellItem<Cliente>>>() 
    	{
			@Override
			public ListCell<MaterialCellItem<Cliente>> call(ListView<MaterialCellItem<Cliente>> arg0) 
			{
				NoButtonMaterialCell<Cliente> cell = new NoButtonMaterialCell<Cliente>()
				{
					@Override
					protected void updateItem(MaterialCellItem<Cliente> persistentObject, boolean empty) 
					{
						super.updateItem(persistentObject, empty);
						
						if (rightVBox != null)
						{
							rightVBox.getChildren().clear();
							
							JFXRadioButton radioButton = new JFXRadioButton();
							radioButton.setStyle("-jfx-selected-color: -buttoncolor; -jfx-unselected-color: gray; -fx-cursor: hand;");
							toggleGroup.getToggles().add(radioButton);
							
							rightVBox.getChildren().add(radioButton);
							
							radioButton.setOnAction(e ->
							{
								listView.getSelectionModel().clearAndSelect(getIndex());
							});
							
							selectedProperty().addListener((l, oldval, newval) ->
							{
								radioButton.setSelected(newval);
							});
						}
					}
				};
				
				return cell;
			}
		});
    	
    	refreshListaClientes(listView);
    	
    	SearchBar clienteSearchBar = new SearchBar();
    	clienteSearchBar.setMaxWidth(525);
    	clienteSearchBar.getTextField().setPromptText("Pesquisar...");
    	clienteSearchBar.getTextField().setOnAction(e ->
    	{
    		refreshListaClientes(listView);
    		
    		listView.getItems().removeIf(i ->
    		{
    			return !i.getObject().getNome().toLowerCase().contains(clienteSearchBar.getTextField().getText().toLowerCase());
    		});
    	});
    	clienteSearchBar.getClearButton().setOnAction(e ->
    	{
    		clienteSearchBar.getTextField().clear();
    		refreshListaClientes(listView);
    	});
    	
    	Text errorText = new Text("Selecione algum cliente ou clique no botão \"Não relacionar cliente\"");
    	errorText.setFill(Color.RED);
    	errorText.setVisible(false);
    	
    	VBox vBox = new VBox(clienteSearchBar, listView, errorText);
    	vBox.setSpacing(6);
    	vBox.setStyle("-fx-background-color: lightgray;");
    	vBox.setPadding(new Insets(6, 0, 0, 0));
    	vBox.setAlignment(Pos.CENTER);
    	
    	dialog.getLayout().setBody(vBox);
    	
    	confButton.setOnAction(clienteEvent ->
    	{
    		if (listView.getSelectionModel().isEmpty())
    		{
    			errorText.setVisible(true);
    			return;
    		}
    		errorText.setVisible(false);
    		
    		confirmarCompra(dialog, metodoPagamento, listView.getSelectionModel().getSelectedItem().getObject());
    	});
    	
    	pularButton.setOnAction(e ->
    	{
    		confirmarCompra(dialog, metodoPagamento, null);
    	});
    }
    
    private void confirmarCompra(FlatJFXDialog dialog, String metodoPagamento, Cliente cliente)
    {
    	dialog.getLayout().getActions().clear();
    	dialog.setHeader("Processando...");
    	dialog.getLayout().setBody(new JFXSpinner());
    	
    	ExecuterServices.getExecutor().execute(() ->
    	{
    		Venda venda = new Venda();
    		venda.setCliente(cliente);
    		venda.setData(Timestamp.valueOf(LocalDateTime.now()));
    		
    		venda.setDescrição("Venda paga em " + metodoPagamento);
    		
    		if (desconto > 0)
    			venda.setDescrição(venda.getDescrição() + ", desconto de R$" + String.format("%.2f", desconto));
    		
    		venda.setPreço(totalCarrinho);
    		Carrinho.getItems().forEach(i ->
    		{
    			venda.getProdutos().add(i.getObject());
    		});
    		
    		SimpleBooleanProperty success = new SimpleBooleanProperty();
    		try 
    		{
				success.set(VendaDAO.getInstance().insert(venda, ConnectionFactory.getOfflineConnection()));
			}
    		catch (SQLException e) 
    		{
				e.printStackTrace();
				success.set(false);
			}
    		
    		if (success.get())
    		{
    			Carrinho.getItems().forEach(i ->
    			{
    				ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
    				
    				try
    				{
    					Produto produto = produtoDAO.get(i.getObject().getId(), ConnectionFactory.getOfflineConnection());
        				produto.setQuantidade(produto.getQuantidade() - (i.getObject().getQuantidade()));
        				produtoDAO.update(produto, ConnectionFactory.getOfflineConnection());
					} 
    				catch (SQLException e2)
    				{
    					e2.printStackTrace();
					}
    			});
    		}
    		
    		Platform.runLater(() ->
    		{
    			if (success.get())
    			{
    				dialog.setHeader("Sucesso");
    				dialog.setBody("Operação concluída com sucesso.");
    			}
    			else 
    			{
					dialog.setHeader("Erro");
					dialog.setBody("Uma falha ocorreu, tente novamente mais tarde.");
				}
    			
    			VendasTabelaManager.setUp(vendasProdutosTable);
    			Carrinho.getItems().clear();
    		});
    	});
    }
    
    private void refreshListaClientes(MaterialListView<Cliente> listView)
    {
    	listView.getItems().clear();
    	
    	try 
    	{
			ClienteDAO.getInstance().getAll().forEach((k, v) ->
			{
				MaterialCellItem<Cliente> item = new MaterialCellItem<>();
				item.setObject(v);
				
				item.setLeftString(v.getNome());
				if (v.getNomeFantasia() != null && !v.getNomeFantasia().isEmpty()) 
				{
					item.setLeftString(v.getNome() + " - " + v.getNomeFantasia());
				}
				
				item.setDescriptionString("Endereço: \n" + Utils.formatName(v.getEndereço().toFormattedString(), 70));
				
				listView.getItems().add(item);
			});
		} 
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
    }
    
    private void refresh()
    {
    	subtotal = 0;
    	Carrinho.getItems().forEach(p ->
    	{
    		if (p.getObject().getQuantidade() == 0)
    		{
    			Platform.runLater(() ->
    			{
    				Carrinho.getItems().remove(p);
    			});
    			return;
    		}
    		
    		subtotal += p.getObject().getPreço() * p.getObject().getQuantidade();
    	});
    	
    	if (descontoField.getText().replace("R$ ", "").isEmpty())
    	{
    		desconto = 0;
    	}
    	else
    	{
    		desconto = Double.parseDouble(descontoField.getText().replace("R$ ", "").replace(",", "."));
		}
    	if (desconto > subtotal)
    		desconto = 0;
    	
    	totalCarrinho = subtotal - desconto;
    	
    	totalNumLabel.setText("R$" + String.format("%.2f", totalCarrinho).replace(".", ","));
    	subtotalNumLabel.setText("R$" + String.format("%.2f", subtotal).replace(".", ","));
    }
    
    private void setUpLabels()
    {
    	subtotalLabel = new Label("Total do carrinho: ");
    	totalLabel = new Label("TOTAL ");
    	totalLabel.setTextFill(Color.GRAY);
    	totalLabel.setStyle("-fx-font-size: 11pt; -fx-font-weight: bold;");
    	
    	descontoLabel = new Label("Desconto: ");
    	
    	VBox leftVBox = new VBox(totalLabel, subtotalLabel, descontoLabel);
    	leftVBox.setSpacing(10);
    	leftVBox.setAlignment(Pos.TOP_LEFT);
    	
    	totalNumLabel = new Label("R$0,00");
    	totalNumLabel.setStyle("-fx-font-size: 11pt; -fx-font-weight: bold;");
    	subtotalNumLabel = new Label("R$0,00");
    	descontoField = new JFXTextField();
    	Utils.onlyDecimalTextField(descontoField);
    	descontoField.setText("R$ ");
    	descontoField.setMaxWidth(80);
    	descontoField.setAlignment(Pos.CENTER_RIGHT);
    	descontoField.textProperty().addListener(l -> refresh());
    	
    	VBox rightVBox = new VBox(totalNumLabel, subtotalNumLabel, descontoField);
    	rightVBox.setSpacing(10);
    	rightVBox.setAlignment(Pos.TOP_RIGHT);
    	
    	HBox labelsHBox = new HBox(leftVBox, new HSpacer(), rightVBox);
    	labelsHBox.setFillHeight(true);
    	labelsHBox.setPrefHeight(140);
    	labelsHBox.setAlignment(Pos.TOP_CENTER);
    	
    	carrinhoPane.getChildren().add(labelsHBox);
    	AnchorPane.setBottomAnchor(labelsHBox, 5.0);
    	AnchorPane.setLeftAnchor(labelsHBox, 5.0);
    	AnchorPane.setRightAnchor(labelsHBox, 5.0);
    }

}
