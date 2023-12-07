package com.davixavier.panes.estoque;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;

import com.davixavier.application.img.IconsPath;
import com.davixavier.application.logging.Logger;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.compras.VendaDAO;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.nodes.AppBarButtonStyler;
import com.davixavier.nodes.MaterialToolBar;
import com.davixavier.nodes.SearchBar;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.panes.login.Login;
import com.davixavier.utils.FollowableTextFieldGroup;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.davixavier.utils.pdf.EstoquePDF;
import com.davixavier.utils.pdf.ProdutoPDFTable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class EstoqueController 
{
	@FXML
    private StackPane primaryPane;

    @FXML
    private JFXButton adicionarButton;

    @FXML
    private ImageView cancelimg;

    @FXML
    private AnchorPane estoqueToolbar;

    @FXML
    private JFXTreeTableView<TreeWrapper<Produto>> estoqueTable;
    
    private SearchBar searchBar;
    
    private String buscanome;
    private String buscaid;
    
    private ProdutoDAO produtoDAO;
    private static final Logger LOGGER = Logger.getInstance();
    
    @FXML
    public void initialize()
    {
    	produtoDAO = ProdutoDAO.getInstance();
    	
    	cancelimg.setImage(IconsPath.CLOSEIMG.getImage());
    	EstoqueTabelaManager.setUp(estoqueTable);
    	
    	MaterialToolBar toolBar = new MaterialToolBar();
    	toolBar.setTitleString("Estoque");
    	searchBar = toolBar.getSearchBar();
    	
    	estoqueToolbar.getChildren().add(toolBar);
    	AnchorPane.setTopAnchor(toolBar, 0.0);
    	AnchorPane.setLeftAnchor(toolBar, 0.0);
    	AnchorPane.setRightAnchor(toolBar, 0.0);
    	AnchorPane.setBottomAnchor(toolBar, 0.0);
    	
    	searchBar.getTextField().setOnAction(e ->
    	{
    		pesquisa(e);
    	});
    	searchBar.getClearButton().setOnAction(e ->
    	{
    		limparPesquisa(e);
    	});
    	searchBar.getSearchOptions().getItems().addAll("Nome", "C�digo");
    	searchBar.getSearchOptions().getSelectionModel().clearAndSelect(0);
    	
    	searchBar.getSearchOptions().setVisible(true);
    	buscanome = "";
    	buscaid = "";
    	searchBar.getSearchOptions().getSelectionModel().selectedItemProperty().addListener((l, oldval, newval) ->
    	{
    		searchBar.getTextField().clear();
    		
    		buscaid = "";
    		buscanome = "";
    		
    		if (newval.equals("Nome"))
    		{
    			searchBar.getTextField().setTextFormatter(null);
    		}
    		else if (newval.equals("C�digo")) 
    		{
				Utils.onlyIntegerTextField(searchBar.getTextField());
			}
    	});
    	
    	MenuItem adicionarMenuItem = new MenuItem("Adicionar novo produto");
    	adicionarMenuItem.setOnAction(e ->
    	{
    		adicionarProduto(e);
    	});
    	
    	MenuItem removerMenuItem = new MenuItem("Remover selecionados");
    	removerMenuItem.setOnAction(e ->
    	{
    		removerSelecionados(e);
    	});
    	
    	MenuItem relatorioItem = new MenuItem("Gerar relat�rio de estoque completo");
    	relatorioItem.setOnAction(e ->
    	{
    		gerarPdfRelatorio();
    	});
    	
    	toolBar.getMenuButton().getContextMenu().getItems().addAll(adicionarMenuItem, removerMenuItem, relatorioItem);
    }
    
    private void gerarPdfRelatorio()
    {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Escolha um local para salvar o relat�rio");
    	FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Arquivo PDF (*.pdf)", "*.pdf");
    	fileChooser.getExtensionFilters().add(extensionFilter);
    	
    	File file = fileChooser.showSaveDialog(primaryPane.getScene().getWindow());
    	
    	ProdutoPDFTable table = new ProdutoPDFTable();
    	try 
    	{
    		LOGGER.log("Salvando pdf de estoque para: " + file.getAbsolutePath(), Level.INFO);
			table.addRows(ProdutoDAO.getInstance().getAllList(ConnectionFactory.getOfflineConnection()));
			new EstoquePDF(file.getAbsolutePath(), table).closeDocument();
		}
    	catch (SQLException e)
    	{
			e.printStackTrace();
		}
	}
    
    @FXML
    void adicionarProduto(ActionEvent event) 
    {
    	setUpDialog().getDialog().show();
    }
    
    @FXML
    void pesquisa(ActionEvent event)
    {
    	if (searchBar.getSearchOptions().getSelectionModel().getSelectedItem().equals("Nome"))
    	{
    		buscanome = searchBar.getTextField().getText();
    		buscaid = "";
    	}
    	else 
    	{
			buscanome = "";
			buscaid = searchBar.getTextField().getText();
		}
    	
    	int id = -1;
    	
    	if (buscaid != null && !buscaid.isEmpty()) 
    	{
    		id = Integer.parseInt(buscaid);
		}
    	
    	EstoqueTabelaManager.filterTable(estoqueTable, buscanome, id);
    }
    
    @FXML
    void limparPesquisa(ActionEvent event)
    {
    	searchBar.getTextField().setText("");
    	pesquisa(null);
    }
    
    @FXML
    void removerSelecionados(ActionEvent event)
    {
    	removerDialog().getDialog().show();
    }
    
    private FlatJFXDialog removerDialog()
    {
    	JFXButton simButton = new JFXButton("Sim");
    	JFXButton naoButton = new JFXButton("N�o");
    	
    	String header = "Confirme sua a��o";
    	String body = "Tem certeza que deseja excluir todos os produtos selecionados? (isso n�o poder� ser desfeito)";
    	FlatJFXDialog dialog = new FlatJFXDialog(primaryPane, header, body, simButton, naoButton);
    	
    	simButton.setOnAction(e ->
    	{
    		dialog.setHeader("Processando...");
    		dialog.getLayout().setBody(new JFXSpinner());
    		dialog.getLayout().getActions().clear();
    		
    		ExecuterServices.getExecutor().execute(() ->
    		{
    			SimpleBooleanProperty success = new SimpleBooleanProperty(true); 
    			
    			estoqueTable.getRoot().getChildren().forEach(produtoWrapper ->
    			{
    				if (((JFXCheckBox)produtoWrapper.getValue().getUserData()).isSelected())
    				{
    					try 
    					{
							if (!produtoDAO.remove(produtoWrapper.getValue().getValue(), ConnectionFactory.getOfflineConnection()))
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
    					dialog.setBody("Opera��o realizada com sucesso!");
    				}
    				else 
    				{
    					dialog.setHeader("Falha");
    					dialog.setBody("Ocorreu um erro na opera��o, tente novamente.");
					}
    				
    				EstoqueTabelaManager.refreshTable(estoqueTable);
    			});
    		});
    	});
    	
    	naoButton.setOnAction(e -> 
    	{
    		dialog.getDialog().close();
    	});
    	
    	return dialog;
    }
    
    private FlatJFXDialog setUpDialog()
    {
    	JFXButton confimarButton = new JFXButton("Confirmar");
    	
    	String header = "Insira as informa��es do produto novo:";
    	FlatJFXDialog dialog = new FlatJFXDialog(primaryPane, header, "", confimarButton);
    	
    	Text idText = new Text("Insira o c�digo do produto:");
    	JFXTextField idField = new JFXTextField();
    	idField.setPromptText("C�digo desejado para o produto");
    	Utils.onlyIntegerTextField(idField);
    	
    	Text nomeText = new Text("Insira o nome do produto:");
    	JFXTextField nomeField = new JFXTextField();
    	nomeField.textProperty().addListener((l, oldVal, newVal) ->
    	{
    		if (newVal.length() > 100)
    		{
    			nomeField.setText(oldVal);
    		}
    	});
    	nomeField.setPromptText("Insira o nome do produto");
    	
    	Text pre�oText = new Text("Insira o pre�o do produto:");
    	JFXTextField pre�oField = new JFXTextField();
    	Utils.onlyDecimalTextField(pre�oField);
    	pre�oField.setPromptText("Insira o pre�o");
    	
    	Text pre�oCompraText = new Text("Insira o pre�o de compra do produto:");
    	JFXTextField pre�oCompraField = new JFXTextField();
    	Utils.onlyDecimalTextField(pre�oCompraField);
    	pre�oCompraField.setPromptText("Insira o pre�o em que produto foi comprado");
    	
    	Text quantidadeText = new Text("Insira a quantidade dispon�vel do produto:");
    	JFXTextField quantidadeField = new JFXTextField();
    	Utils.onlyIntegerTextField(quantidadeField);
    	quantidadeField.setPromptText("Insira a quantidade em estoque do produto");
    	
    	nomeField.setStyle("-fx-text-fill: black; -jfx-unfocus-color: black;");
    	pre�oField.styleProperty().bind(nomeField.styleProperty());
    	pre�oCompraField.styleProperty().bind(nomeField.styleProperty());
    	quantidadeField.styleProperty().bind(nomeField.styleProperty());
    	
    	Text erroText = new Text();
    	erroText.setFill(Color.RED);
    	erroText.setVisible(false);
    	
    	VBox vBox = new VBox(idText, idField, nomeText, nomeField, pre�oText, pre�oField, pre�oCompraText, 
    			pre�oCompraField, quantidadeText, quantidadeField, erroText);
    	vBox.setSpacing(15);
    	vBox.setAlignment(Pos.TOP_LEFT);
    	
    	dialog.getLayout().getBody().clear();
    	dialog.getLayout().getBody().add(vBox);
    	
    	confimarButton.setOnAction(e ->
    	{
    		if (nomeField.getText().isEmpty() || pre�oField.getText().substring(3).isEmpty() 
    				|| pre�oCompraField.getText().substring(3).isEmpty() || quantidadeField.getText().isEmpty())
    		{
    			erroText.setText("Erro: um ou mais campos vazios.");
    			erroText.setVisible(true);
    			return;
    		}
    		erroText.setVisible(false);
    		
    		Produto produto = new Produto();
    		produto.setNome(nomeField.getText());
    		produto.setPre�o(Double.parseDouble(pre�oField.getText().replace(",", ".").substring(3)));
    		produto.setPre�oCompra(Double.parseDouble(pre�oCompraField.getText().replace(",", ".").substring(3)));
    		produto.setQuantidade(Integer.parseInt(quantidadeField.getText()));
    		if (idField.getText().isEmpty())
    		{
    			produto.setCodigo(0);
    		}
    		else 
    		{
    			produto.setCodigo(Integer.parseInt(idField.getText()));
			}
    		
    		try 
    		{
				if (produto.getCodigo() != 0 && produtoDAO.existsByCod(produto, ConnectionFactory.getOfflineConnection()))
				{
					erroText.setText("Erro: j� existe um produto com esse c�digo.");
					erroText.setVisible(true);
					return;
				}
			} 
    		catch (SQLException e1)
    		{
				e1.printStackTrace();
				return;
			}
    		
    		if (produto.getCodigo() == 0)
    		{
    			erroText.setText("Erro: c�digo 0 n�o permitido, escolha outro c�digo.");
    			erroText.setVisible(true);
    			return;
    		}
    		
    		dialog.setHeader("Inserindo...");
    		dialog.getLayout().getBody().clear();
    		dialog.getLayout().getActions().clear();
    		dialog.getLayout().getBody().add(new JFXSpinner());
    		
    		ExecuterServices.getExecutor().execute(() ->
    		{
    			SimpleBooleanProperty result = new SimpleBooleanProperty();
    			
    			try 
    			{
					result.set(produtoDAO.insert(produto, ConnectionFactory.getOfflineConnection()));
				} 
    			catch (Exception e2) 
    			{
    				e2.printStackTrace();
    				result.set(false);
				}
    			
    			Platform.runLater(() ->
    			{
    				if (result.get())
    				{
    					dialog.setHeader("Sucesso");
    					dialog.setBody("Opera��o realizada com sucesso!");
    				}
    				else
    				{
    					dialog.setHeader("Falha");
    					dialog.setBody("Falha na opera��o, tente novamente mais tarde.");
    				}
    				
    				EstoqueTabelaManager.refreshTable(estoqueTable);
    			});
    		});
    	});
    	
    	FollowableTextFieldGroup group = new FollowableTextFieldGroup(confimarButton.getOnAction(),
    			idField, nomeField, pre�oField, pre�oCompraField, quantidadeField);
    	
    	dialog.getDialog().setOnDialogOpened(e ->
		{
			idField.requestFocus();
		});
    	
    	return dialog;
    }

}
