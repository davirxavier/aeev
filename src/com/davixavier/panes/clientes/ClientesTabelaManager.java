package com.davixavier.panes.clientes;

import java.sql.SQLException;

import com.davixavier.application.MainController;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
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
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ClientesTabelaManager
{
	private static ObservableList<TreeWrapper<Cliente>> clienteCache = FXCollections.observableArrayList();
	
	public static void setUp(JFXTreeTableView<TreeWrapper<Cliente>> tabela)
	{
		ExecuterServices.getExecutor().execute(() ->
		{
			ObservableList<TreeWrapper<Cliente>> clientes = FXCollections.observableArrayList();
			try 
			{
				ClienteDAO.getInstance().getAll().forEach((k, v) ->
				{
					TreeWrapper<Cliente> wrapper = new TreeWrapper<Cliente>();
					wrapper.setValue(v);
					
					clientes.add(wrapper);
					clienteCache.add(wrapper);
				});
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			RecursiveTreeItem<TreeWrapper<Cliente>> root = Utils.createRoot(clientes);
			
			Platform.runLater(() ->
			{
				ObservableList<TreeTableColumn<TreeWrapper<Cliente>, ?>> colunas = setUpColumns(tabela);
				//setUpContextMenu(colunas);
				colunas.forEach(c ->
				{
					c.setCellFactory(newFactory());
				});
				
				tabela.getColumns().setAll(colunas);

				tabela.setRoot(root);
				tabela.setShowRoot(false);
				
				Utils.autoSizeColumn(tabela, 2);
			});
		});
	}
	
	private static ObservableList<TreeTableColumn<TreeWrapper<Cliente>, ?>> setUpColumns(JFXTreeTableView<TreeWrapper<Cliente>> tabela)
	{
		JFXTreeTableColumn<TreeWrapper<Cliente>, JFXCheckBox> checkColumn = new JFXTreeTableColumn<>();
		JFXTreeTableColumn<TreeWrapper<Cliente>, Integer> idColumn = new JFXTreeTableColumn<>("Número");
		JFXTreeTableColumn<TreeWrapper<Cliente>, String> nomeColumn = new JFXTreeTableColumn<>("Nome");
		JFXTreeTableColumn<TreeWrapper<Cliente>, JFXButton> telefonesColumn = new JFXTreeTableColumn<>("Telefones");
		JFXTreeTableColumn<TreeWrapper<Cliente>, JFXButton> endereçoColumn = new JFXTreeTableColumn<>("Endereço");
		
		JFXCheckBox columnCheckBox = new JFXCheckBox();
		checkColumn.setGraphic(columnCheckBox);
		columnCheckBox.setOnAction(e ->
		{
			tabela.getRoot().getChildren().forEach(p ->
			{
				((JFXCheckBox)p.getValue().getUserData()).setSelected(columnCheckBox.isSelected());
			});
		});
		
		idColumn.setStyle("-fx-alignment: center;");
		nomeColumn.styleProperty().bind(idColumn.styleProperty());
		telefonesColumn.styleProperty().bind(idColumn.styleProperty());
		endereçoColumn.styleProperty().bind(idColumn.styleProperty());
		checkColumn.styleProperty().bind(idColumn.styleProperty());
		
		idColumn.getStyleClass().add("centerAlignedColumn");
		nomeColumn.getStyleClass().addAll(idColumn.getStyleClass());
		telefonesColumn.getStyleClass().addAll(idColumn.getStyleClass());
		endereçoColumn.getStyleClass().addAll(idColumn.getStyleClass());
		checkColumn.getStyleClass().addAll(idColumn.getStyleClass());
		
		checkColumn.setCellValueFactory(param ->
		{
			JFXCheckBox checkBox = new JFXCheckBox();
			param.getValue().getValue().setUserData(checkBox);
			
			return new ReadOnlyObjectWrapper<>(checkBox);
		});
		
		idColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<>(param.getValue().getValue().getValue().getId());
		});
		
		nomeColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<>(param.getValue().getValue().getValue().getNome());
		});
		
		telefonesColumn.setCellValueFactory(param ->
		{
			JFXButton button = new JFXButton("Visualizar");
			button.setOnAction(e ->
			{
				telefonesDialog(tabela, param.getValue().getValue().getValue()).getDialog().show();
			});
			
			return new ReadOnlyObjectWrapper<>(button);
		});
		
		endereçoColumn.setCellValueFactory(param ->
		{
			JFXButton button = new JFXButton("Visualizar");
			button.setOnAction(e ->
			{
				endereçoDialog(tabela, param.getValue().getValue().getValue()).getDialog().show();
			});
			
			return new ReadOnlyObjectWrapper<>(button);
		});
		
		return FXCollections.observableArrayList(checkColumn, idColumn, nomeColumn, telefonesColumn, endereçoColumn);
	}
	
	public static FlatJFXDialog telefonesDialog(JFXTreeTableView<TreeWrapper<Cliente>> tabela, Cliente cliente)
	{
		StackPane pane = (StackPane)tabela.getScene().lookup("#primaryStackPane");
		
		JFXButton fechar = new JFXButton("Fechar");
		
		String header = "Telefones desse cliente";
		String body = "";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, fechar);
		
		JFXListView<String> listView = new JFXListView<>();
		for (Telefone telefone : cliente.getTelefones())
		{
			listView.getItems().add(telefone.toFormattedString());
		}
		
		dialog.getLayout().setBody(listView);
		
		fechar.setOnAction(e ->
		{
			dialog.getDialog().close();
		});
		
		return dialog;
	}
	
	public static FlatJFXDialog endereçoDialog(JFXTreeTableView<TreeWrapper<Cliente>> tabela, Cliente cliente)
	{
		StackPane pane = (StackPane)tabela.getScene().lookup("#primaryStackPane");
		
		JFXButton fechar = new JFXButton("Fechar");
		
		String header = "Endereço referente a esse cliente";
		String body = "";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, fechar);
		
		VBox vBox = new VBox();
		vBox.setSpacing(8);
		vBox.setAlignment(Pos.TOP_LEFT);
		
		vBox.getChildren().add(new Text("Endereço: " + cliente.getEndereço().getRua()));
		vBox.getChildren().add(new Text("Número: " + cliente.getEndereço().getNumero()));
		vBox.getChildren().add(new Text("Bairro: " + cliente.getEndereço().getBairro()));
		vBox.getChildren().add(new Text("Cidade: " + cliente.getEndereço().getCidade()));
		vBox.getChildren().add(new Text("Estado: " + cliente.getEndereço().getEstado()));
		vBox.getChildren().add(new Text("Complemento: " + cliente.getEndereço().getComplemento()));
		
		dialog.getLayout().setBody(vBox);
		
		fechar.setOnAction(e ->
		{
			dialog.getDialog().close();
		});
		
		return dialog;
	}
	
	public static void refreshTable(JFXTreeTableView<TreeWrapper<Cliente>> tabela)
	{
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			clienteCache.clear();
			
			ObservableList<TreeWrapper<Cliente>> clientes = FXCollections.observableArrayList();
			try 
			{
				ClienteDAO.getInstance().getAll().forEach((k, v) ->
				{
					TreeWrapper<Cliente> wrapper = new TreeWrapper<>();
					wrapper.setValue(v);
					
					clientes.add(wrapper);
					clienteCache.add(wrapper);
				});
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			RecursiveTreeItem<TreeWrapper<Cliente>> root = Utils.createRoot(clientes);
			
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}
	
	public static void filterTable(JFXTreeTableView<TreeWrapper<Cliente>> tabela, String nome)
	{	
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			ObservableList<TreeWrapper<Cliente>> clientes = FXCollections.observableArrayList();
			clienteCache.forEach(p ->
			{
				clientes.add(p);
			});
			
			clientes.removeIf(p ->
			{
				return !p.getValue().getNome().toLowerCase().contains(nome.toLowerCase());
			});
			
			RecursiveTreeItem<TreeWrapper<Cliente>> root = Utils.createRoot(clientes);
			
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}
	
	private static <S, T> Callback<TreeTableColumn<TreeWrapper<Cliente>,T>, TreeTableCell<TreeWrapper<Cliente>,T>> newFactory() 
	{
	    return new Callback<TreeTableColumn<TreeWrapper<Cliente>, T>, TreeTableCell<TreeWrapper<Cliente>, T>>()
	    {
			@Override
			public TreeTableCell<TreeWrapper<Cliente>, T> call(TreeTableColumn<TreeWrapper<Cliente>, T> param)
			{
				TreeTableCell<TreeWrapper<Cliente>, T> treeTableCell = new TreeTableCell<TreeWrapper<Cliente>, T>()
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
				                else if (persistentObject instanceof Integer)
				                {
				                	int num = (int)persistentObject;
				                	setText(num + "");
				                }
				                else if (persistentObject instanceof Node)
				                {
				                	setGraphic((Node)persistentObject);
				                }
				            }
					 }
			    };
				
				MenuItem removerItem = new MenuItem("Remover");
				removerItem.setOnAction(e ->
				{
					TreeWrapper<Cliente> cliente = treeTableCell.getTreeTableRow().getItem();
					removerDialog((JFXTreeTableView<TreeWrapper<Cliente>>)treeTableCell.getTreeTableView(), cliente.getValue()).getDialog().show();
				});
				
//				MenuItem editarItem = new MenuItem("Editar");
//				editarItem.setOnAction(e ->
//				{
//					TreeWrapper<Cliente> cliente = treeTableCell.getTreeTableRow().getItem();
//					editarDialog((JFXTreeTableView<TreeWrapper<Cliente>>)treeTableCell.getTreeTableView(), cliente.getValue()).getDialog().show();
//				});
				
				ContextMenu menu = new ContextMenu(removerItem);;
				treeTableCell.setContextMenu(menu);
				return treeTableCell;
			}
	        
	    };
	}
	
	public static FlatJFXDialog removerDialog(JFXTreeTableView<TreeWrapper<Cliente>> tabela, Cliente cliente)
	{
		StackPane pane = (StackPane)tabela.getScene().lookup("#primaryStackPane");
		
		JFXButton simButton = new JFXButton("Sim");
		JFXButton nãoButton = new JFXButton("Não");
		
		String header = "Confirme sua escolha";
		String body = "Tem certeza que deseja remover esse cliente?";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, simButton, nãoButton);
		
		simButton.setOnAction(e ->
		{
			dialog.setHeader("Processando...");
			dialog.getLayout().getBody().clear();
			dialog.getLayout().getActions().clear();
			dialog.getLayout().setBody(new JFXSpinner());
			
			ExecuterServices.getExecutor().execute(() ->
			{
				SimpleBooleanProperty ret = new SimpleBooleanProperty();
				
				try
				{
					ret.set(ClienteDAO.getInstance().remove(cliente, ConnectionFactory.getOfflineConnection()));
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
						dialog.setBody("Sucesso");
						dialog.setHeader("A operação foi concluída com sucesso.");
					}
					else
					{
						dialog.setHeader("Erro");
						dialog.setBody("Falha na operação, tente novamente mais tarde.");
					}
					
					ClientesTabelaManager.refreshTable(tabela);
				});
			});
		});
		
		nãoButton.setOnAction(e ->
		{
			dialog.getDialog().close();
		});
		
		return dialog;
	}
}
