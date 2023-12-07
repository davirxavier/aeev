package com.davixavier.panes.vendas;

import java.sql.SQLException;

import com.davixavier.application.MainController;
import com.davixavier.application.img.IconsPath;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.nodes.ToolbarButton;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class VendasTabelaManager
{
	private static ObservableList<TreeWrapper<Produto>> produtoCache = FXCollections.observableArrayList();
	
	public static void setUp(JFXTreeTableView<TreeWrapper<Produto>> tabela)
	{
		ExecuterServices.getExecutor().execute(() ->
		{
			produtoCache.clear();
			
			ObservableList<TreeWrapper<Produto>> produtos = FXCollections.observableArrayList();
			try 
			{
				ProdutoDAO.getInstance().getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
				{
					TreeWrapper<Produto> wrapper = new TreeWrapper<>();
					wrapper.setValue(v);
					
					if (wrapper.getValue().getQuantidade() == 0)
						return;
					
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
				tabela.getColumns().setAll(setUpColumns(tabela));
				tabela.setRoot(root);
				tabela.setShowRoot(false);		
				tabela.setEditable(false);
				
				Utils.autoSizeColumn(tabela, 0);
			});
		});
	}
	
	public static ObservableList<JFXTreeTableColumn<TreeWrapper<Produto>, ?>> setUpColumns(JFXTreeTableView<TreeWrapper<Produto>> tabela)
	{
		JFXTreeTableColumn<TreeWrapper<Produto>, String> nomeColumn = new JFXTreeTableColumn<>("Nome");
		nomeColumn.setMinWidth(220);
		JFXTreeTableColumn<TreeWrapper<Produto>, String> preçoColumn = new JFXTreeTableColumn<>("Preço");
		JFXTreeTableColumn<TreeWrapper<Produto>, String> quantidadeColumn = new JFXTreeTableColumn<>("Estoque");
		JFXTreeTableColumn<TreeWrapper<Produto>, HBox> adicionarColumn = new JFXTreeTableColumn<>("");
		
		nomeColumn.setStyle("-fx-alignment: center;");
		preçoColumn.styleProperty().bind(nomeColumn.styleProperty());
		quantidadeColumn.styleProperty().bind(nomeColumn.styleProperty());
		
		nomeColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<String>(Utils.formatName(param.getValue().getValue().getValue().getNome(), 50));
		});
		
		preçoColumn.setCellValueFactory(param ->
		{
			String ret = "R$" + String.format("%.2f", param.getValue().getValue().getValue().getPreço()).replace(".", ",");
			return new ReadOnlyObjectWrapper<String>(ret);
		});
		
		quantidadeColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getValue().getQuantidade() + "");
		});
		
		adicionarColumn.setCellValueFactory(param ->
		{
			ToolbarButton button = new ToolbarButton(IconsPath.ADDCARRINHO24PXBLACK.getImage());
			button.setBackgroundInHover("derive(white, -10%)");
			JFXTextField quantidadeField = new JFXTextField("1");
			quantidadeField.setPromptText("Qntd");
			quantidadeField.setMaxWidth(65);
			//quantidadeField.setMinWidth(65);
			quantidadeField.setAlignment(Pos.CENTER);
			Utils.onlyIntegerTextField(quantidadeField);
			
			HBox hBox = new HBox(quantidadeField, button);
			hBox.setAlignment(Pos.CENTER_RIGHT);
			hBox.setStyle("-fx-padding: 4 0 0 0;");
			
			button.setOnAction(e ->
			{
				int quantidade = 1;
				if (!quantidadeField.getText().isEmpty())
				{
					quantidade = Integer.parseInt(quantidadeField.getText());
				}
				
				ProdutoVenda produtoVenda = new ProdutoVenda();
				produtoVenda.setId(param.getValue().getValue().getValue().getId());
				produtoVenda.setNome(Utils.formatName(param.getValue().getValue().getValue().getNome(), 35));
				produtoVenda.setPreço(param.getValue().getValue().getValue().getPreço());
				produtoVenda.setQuantidade(quantidade);
				
				Carrinho.addItem(produtoVenda, param.getValue().getValue().getValue().getQuantidade());
			});
			return new ReadOnlyObjectWrapper<>(hBox);
		});
		
		return FXCollections.observableArrayList(nomeColumn, preçoColumn, quantidadeColumn, adicionarColumn);
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
				ProdutoDAO.getInstance().getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
				{
					TreeWrapper<Produto> wrapper = new TreeWrapper<>();
					wrapper.setValue(v);
					
					if (wrapper.getValue().getQuantidade() == 0)
						return;
					
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
	
	public static void filterTable(JFXTreeTableView<TreeWrapper<Produto>> tabela, String nome)
	{	
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			ObservableList<TreeWrapper<Produto>> produtos = FXCollections.observableArrayList();
			produtoCache.forEach(p ->
			{
				produtos.add(p);
			});
			
			produtos.removeIf(p ->
			{
				return !p.getValue().getNome().toLowerCase().contains(nome.toLowerCase());
			});
			
			RecursiveTreeItem<TreeWrapper<Produto>> root = Utils.createRoot(produtos);
			
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}
}
