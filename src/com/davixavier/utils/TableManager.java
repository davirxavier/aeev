package com.davixavier.utils;

import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.jfoenix.controls.RecursiveTreeItem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public interface TableManager<T>
{
	/*static ObservableList<?> cache = FXCollections.observableArrayList();
	
	public static <T> void setUp(TreeTableView<T> tabela, DAO dao) 
	{
		ThreadPool.getExecutor().execute(() ->
		{
			ObservableList<T> produtos = FXCollections.observableArrayList();
			ProdutoDAO.getAll().forEach((k, v) ->
			{
				T object = new TreeWrapper<T>();
				dao.setValue(v);
				
				produtos.add(wrapper);
				produtoCache.add(wrapper);
			});
			
			RecursiveTreeItem<TreeWrapper<Produto>> root = Utils.createRoot(produtos);
			
			Platform.runLater(() ->
			{
				ObservableList<TreeTableColumn<TreeWrapper<Produto>, ?>> colunas = setUpColumns(tabela);
				setUpContextMenu(colunas);
				tabela.getColumns().setAll(colunas);

				tabela.setRoot(root);
				tabela.setShowRoot(false);
				
				Utils.autoSizeColumn(tabela, 1);
			});
		});
	};
	
	public static <T> ObservableList<TreeTableColumn<T, ?>> setUpColumns(TreeTableView<T> tabela)
	{
		return FXCollections.observableArrayList();
	}*/
}
