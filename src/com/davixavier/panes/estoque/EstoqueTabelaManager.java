package com.davixavier.panes.estoque;

import java.sql.SQLException;

import com.davixavier.application.MainController;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.FollowableTextFieldGroup;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Callback;

public class EstoqueTabelaManager
{
	private static ObservableList<TreeWrapper<Produto>> produtoCache = FXCollections.observableArrayList();
	
	private static int lastSearchId = -1;
	private static String lastSearchNome = "";
	private static ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
	
	public static void setUp(JFXTreeTableView<TreeWrapper<Produto>> tabela)
	{
		ExecuterServices.getExecutor().execute(() ->
		{
			ObservableList<TreeWrapper<Produto>> produtos = FXCollections.observableArrayList();
			try 
			{
				produtoDAO.getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
				{
					TreeWrapper<Produto> wrapper = new TreeWrapper<Produto>();
					wrapper.setValue(v);
					
					produtos.add(wrapper);
					produtoCache.add(wrapper);
				});
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			RecursiveTreeItem<TreeWrapper<Produto>> root = Utils.createRoot(produtos);
			
			Platform.runLater(() ->
			{
				ObservableList<TreeTableColumn<TreeWrapper<Produto>, ?>> colunas = setUpColumns(tabela);
				setUpContextMenu(colunas);
				tabela.getColumns().setAll(colunas);

				tabela.setRoot(root);
				tabela.setShowRoot(false);
				
				Utils.autoSizeColumn(tabela, 2);
			});
		});
	}
	
	public static ObservableList<TreeTableColumn<TreeWrapper<Produto>, ?>> setUpColumns(JFXTreeTableView<TreeWrapper<Produto>> tabela)
	{
		JFXTreeTableColumn<TreeWrapper<Produto>, JFXCheckBox> checkColumn = new JFXTreeTableColumn<>();
		JFXTreeTableColumn<TreeWrapper<Produto>, String> idColumn = new JFXTreeTableColumn<>("Número");
		JFXTreeTableColumn<TreeWrapper<Produto>, String> nomeColumn = new JFXTreeTableColumn<>("Nome");
		JFXTreeTableColumn<TreeWrapper<Produto>, String> preçoColumn = new JFXTreeTableColumn<>("Preço ");
		JFXTreeTableColumn<TreeWrapper<Produto>, String> preçoCompraColumn = new JFXTreeTableColumn<>("Preço de Compra");
		JFXTreeTableColumn<TreeWrapper<Produto>, String> lucroColumn = new JFXTreeTableColumn<>("Lucro   ");
		JFXTreeTableColumn<TreeWrapper<Produto>, String> quantidadeColumn = new JFXTreeTableColumn<>("Quantidade(un.)");
		
		checkColumn.setSortable(false);
		idColumn.sortableProperty().bind(checkColumn.sortableProperty());
		nomeColumn.sortableProperty().bind(checkColumn.sortableProperty());
		preçoColumn.sortableProperty().bind(checkColumn.sortableProperty());
		preçoCompraColumn.sortableProperty().bind(checkColumn.sortableProperty());
		lucroColumn.sortableProperty().bind(checkColumn.sortableProperty());
		quantidadeColumn.sortableProperty().bind(checkColumn.sortableProperty());
		
		JFXCheckBox columnCheckBox = new JFXCheckBox();
		checkColumn.setGraphic(columnCheckBox);
		columnCheckBox.setOnAction(e ->
		{
			tabela.getRoot().getChildren().forEach(p ->
			{
				if (p.getValue().getUserData() != null && p.getValue().getUserData() instanceof JFXCheckBox)
					((JFXCheckBox)p.getValue().getUserData()).setSelected(columnCheckBox.isSelected());
			});
		});
		
		preçoColumn.setStyle("-fx-alignment: CENTER-RIGHT");
		preçoCompraColumn.styleProperty().bind(preçoColumn.styleProperty());
		lucroColumn.styleProperty().bind(preçoColumn.styleProperty());
		
		nomeColumn.setStyle("-fx-alignment: CENTER-LEFT");
		idColumn.styleProperty().bind(nomeColumn.styleProperty());
		
		preçoColumn.getStyleClass().add("rightAlignedColumn");
		preçoCompraColumn.getStyleClass().addAll(preçoColumn.getStyleClass());
		lucroColumn.getStyleClass().addAll(preçoColumn.getStyleClass());
		
		nomeColumn.getStyleClass().add("leftAlignedColumn");
		idColumn.getStyleClass().add("leftAlignedColumn");
		
		checkColumn.getStyleClass().add("centerAlignedColumn");
		quantidadeColumn.getStyleClass().add("centerAlignedColumn");
		
		quantidadeColumn.setStyle("-fx-alignment: CENTER");
		checkColumn.setStyle("-fx-alignment: CENTER;");
		
		checkColumn.setCellValueFactory(param ->
		{
			JFXCheckBox checkBox = new JFXCheckBox();
			param.getValue().getValue().setUserData(checkBox);
			
			return new ReadOnlyObjectWrapper<JFXCheckBox>(checkBox);
		});
		
		idColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<>(param.getValue().getValue().getValue().getCodigo() + "");
		});
		
		nomeColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<String>(Utils.formatName(param.getValue().getValue().getValue().getNome(), 50));
		});
		
		preçoColumn.setCellValueFactory(param ->
		{
			String ret = "R$" + String.format("%.2f", param.getValue().getValue().getValue().getPreço());
			ret = ret.replace(".", ",");
			return new ReadOnlyObjectWrapper<String>(ret);
		});
		
		preçoCompraColumn.setCellValueFactory(param ->
		{
			String ret = "R$" + String.format("%.2f", param.getValue().getValue().getValue().getPreçoCompra());
			ret = ret.replace(".", ",");
			return new ReadOnlyObjectWrapper<String>(ret);
		});
		
		lucroColumn.setCellValueFactory(param ->
		{
			double ret = param.getValue().getValue().getValue().getPreço() - param.getValue().getValue().getValue().getPreçoCompra();
			return new ReadOnlyObjectWrapper<String>(("R$" + String.format("%.2f", ret)).replace(".", ","));
		});
		
		quantidadeColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getValue().getQuantidade() + "");
		});
		
		preçoColumn.setMinWidth(100);
		preçoCompraColumn.minWidthProperty().bind(preçoColumn.minWidthProperty());
		lucroColumn.minWidthProperty().bind(preçoColumn.minWidthProperty());
		
		return FXCollections.observableArrayList(checkColumn, idColumn, nomeColumn, preçoColumn, preçoCompraColumn, lucroColumn, quantidadeColumn);
	}
	
	private static ObservableList<TreeTableColumn<TreeWrapper<Produto>, ?>> setUpContextMenu(ObservableList<TreeTableColumn<TreeWrapper<Produto>, ?>> colunas)
	{
		colunas.forEach(c ->
		{
			c.setCellFactory(newFactory());
		});
		
		return colunas;
	}
	
	private static <S, T> Callback<TreeTableColumn<TreeWrapper<Produto>,T>, TreeTableCell<TreeWrapper<Produto>,T>> newFactory() 
	{
	    return new Callback<TreeTableColumn<TreeWrapper<Produto>, T>, TreeTableCell<TreeWrapper<Produto>, T>>()
	    {
			@Override
			public TreeTableCell<TreeWrapper<Produto>, T> call(TreeTableColumn<TreeWrapper<Produto>, T> param)
			{
				TreeTableCell<TreeWrapper<Produto>, T> treeTableCell = new TreeTableCell<TreeWrapper<Produto>, T>()
				{
					 @Override
				     protected void updateItem(final T persistentObject, final boolean empty) 
					 {
				            super.updateItem(persistentObject, empty);
				            if (empty) 
				            {
				                setText(null);
				                setGraphic(null);
				            } 
				            else 
				            {
				                if (persistentObject instanceof String)
				                {
				                	setText(persistentObject.toString());
				                }
				                else if (persistentObject instanceof Node)
				                {
				                	setGraphic((Node) persistentObject);
				                }
				                else 
				                {
									setText(null);
									setGraphic(null);
								}
				            }
					 }
			    };
				
				MenuItem removerItem = new MenuItem("Remover");
				removerItem.setOnAction(e ->
				{
					TreeWrapper<Produto> produto = treeTableCell.getTreeTableRow().getItem();
					removerDialog((JFXTreeTableView<TreeWrapper<Produto>>)treeTableCell.getTreeTableView(), produto.getValue()).getDialog().show();
				});
				
				MenuItem editarItem = new MenuItem("Editar");
				editarItem.setOnAction(e ->
				{
					TreeWrapper<Produto> produto = treeTableCell.getTreeTableRow().getItem();
					editarDialog((JFXTreeTableView<TreeWrapper<Produto>>)treeTableCell.getTreeTableView(), produto.getValue()).getDialog().show();
				});
				
				ContextMenu menu = new ContextMenu(editarItem, removerItem);
				treeTableCell.setContextMenu(menu);
				return treeTableCell;
			}
	        
	    };
	}
	
	private static FlatJFXDialog editarDialog(JFXTreeTableView<TreeWrapper<Produto>> tabela, Produto produto)
	{
		StackPane pane = (StackPane)tabela.getScene().lookup("#primaryStackPane");
		
		JFXButton confirmarButton = new JFXButton("Salvar mudanças");
		
		String header = "Alterar informações de um produto";
		String body = "";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, confirmarButton);
		
		Text idText = new Text("Insira o código do produto");
		JFXTextField idField = new JFXTextField();
		idField.setPromptText("Código para o produto");
		Utils.onlyIntegerTextField(idField);
		idField.setText(produto.getCodigo() + "");
		
		Text nomeText = new Text("Nome do produto");
		JFXTextField nomeField = new JFXTextField(produto.getNome());
		nomeField.setPromptText("Insira o nome");
		
		Text preçoText = new Text("Preço do produto");
		JFXTextField preçoField = new JFXTextField("R$ " + (produto.getPreço() + "").replace(".", ","));
		Utils.onlyDecimalTextField(preçoField);
		preçoField.setPromptText("Insira o preço");
		
		Text preçoCompraText = new Text("Preço de compra do produto");
		JFXTextField preçoCompraField = new JFXTextField("R$ " + (produto.getPreçoCompra() + "").replace(".", ","));
		Utils.onlyDecimalTextField(preçoCompraField);
		preçoCompraField.setPromptText("Insira o preço em que o produto foi comprado");
		
		Text quantidadeText = new Text("Quantidade disponível do produto no estoque");
		JFXTextField quantidadeField = new JFXTextField(produto.getQuantidade() + "");
		Utils.onlyIntegerTextField(quantidadeField);
		quantidadeField.setPromptText("Insira a quantidade em estoque do produto");
		
		nomeField.setStyle("-jfx-unfocus-color: black; -fx-text-fill: black;");
		preçoField.styleProperty().bind(nomeField.styleProperty());
		preçoCompraField.styleProperty().bind(nomeField.styleProperty());
		quantidadeField.styleProperty().bind(nomeField.styleProperty());
		
		Text erroText = new Text();
		erroText.setFill(Color.RED);
		erroText.setVisible(false);
		
		VBox vBox = new VBox(idText, idField, nomeText, nomeField, preçoText, preçoField, preçoCompraText, 
				preçoCompraField, quantidadeText, quantidadeField, erroText);
		vBox.setAlignment(Pos.TOP_LEFT);
		vBox.setSpacing(15);
		
		dialog.getLayout().getBody().clear();
		dialog.getLayout().getBody().add(vBox);
		
		confirmarButton.setOnAction(e ->
		{
			if (nomeField.getText().isEmpty() || preçoField.getText().substring(3).isEmpty() 
					|| preçoCompraField.getText().substring(3).isEmpty() || quantidadeField.getText().isEmpty())
			{
				erroText.setText("Erro: um ou mais campos vazios.");
				erroText.setVisible(true);
				return;
			}
			
			final int oldCod = produto.getCodigo();
			
			produto.setNome(nomeField.getText());
			produto.setPreço(Double.parseDouble(preçoField.getText().replace(",", ".").substring(3)));
			produto.setPreçoCompra(Double.parseDouble(preçoCompraField.getText().replace(",", ".").substring(3)));
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
				if (produto.getCodigo() != 0 && produto.getCodigo() != oldCod && produtoDAO.existsByCod(produto, ConnectionFactory.getOfflineConnection()))
				{
					erroText.setText("Erro: já existe um produto com esse código.");
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
				erroText.setText("Erro: código 0 não permitido, escolha outro código.");
				erroText.setVisible(true);
				return;
			}
			
			dialog.setHeader("Salvando mudanças...");
			dialog.getLayout().getActions().clear();
			dialog.getLayout().getBody().clear();
			dialog.getLayout().getBody().add(new JFXSpinner());
			
			ExecuterServices.getExecutor().execute(() ->
			{
				SimpleBooleanProperty ret = new SimpleBooleanProperty();
				try 
				{
					ret.set(produtoDAO.update(produto, ConnectionFactory.getOfflineConnection()));
				} 
				catch (SQLException e1)
				{
					e1.printStackTrace();
					ret.set(false);
				}
				
				Platform.runLater(() ->
				{
					if (ret.get())
					{
						dialog.setHeader("Sucesso");
						dialog.setBody("Mudanças salvas com sucesso.");
					}
					else 
					{
						dialog.setHeader("Falha");
						dialog.setBody("Ocorreu uma falha ao salvar as mudanças, tente novamente mais tarde.");
					}
					
					refreshTable(tabela);
				});
			});
		});
		
		FollowableTextFieldGroup group = new FollowableTextFieldGroup(confirmarButton.getOnAction(), 
				idField, nomeField, preçoField, preçoCompraField, quantidadeField);
		
		dialog.getDialog().setOnDialogOpened(e ->
		{
			idField.requestFocus();
		});
		
		return dialog;
	}
	
	public static FlatJFXDialog removerDialog(JFXTreeTableView<TreeWrapper<Produto>> tabela, Produto produto)
	{
		StackPane pane = (StackPane)tabela.getScene().lookup("#primaryStackPane");
		
		JFXButton simButton = new JFXButton("Sim");
		JFXButton nãoButton = new JFXButton("Não");
		
		String header = "Confimar escolha";
		String body = "Tem certeza que deseja remover esse produto? (Isso não pode ser desfeito)";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, simButton, nãoButton);
		
		simButton.setOnAction(e ->
		{
			dialog.setHeader("Processando...");
			dialog.getLayout().getBody().clear();
			dialog.getLayout().getBody().add(new JFXSpinner());
			dialog.getLayout().getActions().clear();
			
			ExecuterServices.getExecutor().execute(() ->
			{
				SimpleBooleanProperty res = new SimpleBooleanProperty();
				try 
				{
					res.set(produtoDAO.remove(produto, ConnectionFactory.getOfflineConnection()));
				}
				catch (SQLException e1) 
				{
					e1.printStackTrace();
					res.set(false);
				}
				
				Platform.runLater(() ->
				{
					if (res.get())
					{
						dialog.setHeader("Sucesso");
						dialog.setBody("Operação concluida com sucesso.");
					}
					else 
					{
						dialog.setHeader("Falha");
						dialog.setBody("Ocorreu um erro ao realizar a operação, tente novamente mais tarde.");
					}
					
					refreshTable(tabela);
				});
			});
		});
		
		nãoButton.setOnAction(e ->
		{
			dialog.getDialog().close();
		});
		
		return dialog;
	}
	
	public static void refreshTable(JFXTreeTableView<TreeWrapper<Produto>> tabela)
	{
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			produtoCache.clear();
			
			ObservableList<TreeWrapper<Produto>> produtos = FXCollections.observableArrayList();
			try
			{
				produtoDAO.getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
				{
					TreeWrapper<Produto> wrapper = new TreeWrapper<>();
					wrapper.setValue(v);
					
					produtos.add(wrapper);
					produtoCache.add(wrapper);
				});
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			RecursiveTreeItem<TreeWrapper<Produto>> root = Utils.createRoot(produtos);
			
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}
	
	public static void filterTable(JFXTreeTableView<TreeWrapper<Produto>> tabela, String nome, int id)
	{	
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			ObservableList<TreeWrapper<Produto>> produtos = FXCollections.observableArrayList();
			produtoCache.forEach(p ->
			{
				produtos.add(p);
			});
			
			lastSearchId = id;
			lastSearchNome = nome;
			
			if (id != -1)
			{
				produtos.removeIf(p ->
				{
					return !(p.getValue().getId() == id);
				});
			}
			
			if (nome != null && !nome.isEmpty())
			{
				produtos.removeIf(p ->
				{
					return !p.getValue().getNome().toLowerCase().contains(nome.toLowerCase());
				});
			}
			
			RecursiveTreeItem<TreeWrapper<Produto>> root = Utils.createRoot(produtos);
			
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}

	public static int getLastSearchId() {
		return lastSearchId;
	}

	public static String getLastSearchNome() {
		return lastSearchNome;
	}
}
