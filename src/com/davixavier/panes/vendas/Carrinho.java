package com.davixavier.panes.vendas;

import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.nodes.materiallist.MaterialCellItem;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class Carrinho
{
	private static ObservableList<MaterialCellItem<ProdutoVenda>> items = FXCollections.observableArrayList(extractor());
	
	public static void addItem(ProdutoVenda produtoVenda, int maxQuantidade)
	{
		for (MaterialCellItem<ProdutoVenda> item : items)
		{
			int quantidade = item.getObject().getQuantidade();

			quantidade = (quantidade + produtoVenda.getQuantidade() >= maxQuantidade) ?
					maxQuantidade : quantidade + produtoVenda.getQuantidade();
			
			if (item.getObject().equals(produtoVenda))
			{
				item.getObject().setQuantidade(produtoVenda.getQuantidade());
				item.getObject().setQuantidade(quantidade);
				item.setDescriptionString("Quantidade: " + item.getObject().getQuantidade());
				
				return;
			}
		}
		
		MaterialCellItem<ProdutoVenda> item = new MaterialCellItem<ProdutoVenda>();
		item.setLeftString(produtoVenda.getNome());
		item.setDescriptionString("Quantidade: " + produtoVenda.getQuantidade());
		item.setRightString("R$" + String.format("%.2f", produtoVenda.getPreço()));
		item.setObject(produtoVenda);
		
		items.add(item);
	}

	public static ObservableList<MaterialCellItem<ProdutoVenda>> getItems() {
		return items;
	}

	public static void setItems(ObservableList<MaterialCellItem<ProdutoVenda>> items) {
		Carrinho.items = items;
	}
	
	public static Callback<MaterialCellItem<ProdutoVenda>, Observable[]> extractor() 
	{
        return (MaterialCellItem<ProdutoVenda> i) -> new Observable[] 
        {
        		i.getObject().idProperty(),
                i.getObject().nomeProperty(),
                i.getObject().quantidadeProperty(),
                i.getObject().preçoProperty()
        };
}
}
