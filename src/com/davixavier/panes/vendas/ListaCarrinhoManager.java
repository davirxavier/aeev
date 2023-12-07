package com.davixavier.panes.vendas;

import com.davixavier.application.img.IconsPath;
import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.nodes.materiallist.MaterialCellItem;
import com.davixavier.nodes.materiallist.MaterialListView;
import com.davixavier.nodes.materiallist.NoButtonMaterialCell;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ListaCarrinhoManager
{
	public static void setUp(MaterialListView<ProdutoVenda> lista)
	{
		lista.setCellFactory(new Callback<ListView<MaterialCellItem<ProdutoVenda>>, ListCell<MaterialCellItem<ProdutoVenda>>>() 
		{
			@Override
			public ListCell<MaterialCellItem<ProdutoVenda>> call(ListView<MaterialCellItem<ProdutoVenda>> param) 
			{
				NoButtonMaterialCell<ProdutoVenda> cell = new NoButtonMaterialCell<ProdutoVenda>()
				{
					@Override
					protected void updateItem(MaterialCellItem<ProdutoVenda> persistentObject, boolean empty) 
					{
						super.updateItem(persistentObject, empty);
						
						rightLabel.setVisible(true);
						editButton.setVisible(true);
						editButton.setGraphic(new ImageView(IconsPath.CLOSE18PXGRAY.getImage()));
						editButton.setBackgroundInHover("derive(white, -20%)");
						editButton.setBackgroundInUnhover("derive(white, -10%)");
						editButton.setOnAction(e ->
						{
							persistentObject.getObject().setQuantidade(0);
						});
					}
				};
				
				return cell;
			}
		});
	}
	
	public static FlatJFXDialog alterarQuantidadeDialog(JFXListView<TreeWrapper<Produto>> lista, ListCell<TreeWrapper<Produto>> cell)
	{
		StackPane pane = (StackPane)lista.getScene().lookup("#primaryStackPane");
		
		JFXButton confirmarButton = new JFXButton("Confirmar");
		
		String header = "Insira a quantidade";
		String body = "";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, confirmarButton);
		
		Text text = new Text("Insira a quantidade desejada:");
		JFXTextField field = new JFXTextField();
		field.setPromptText("Quantidade");
		field.setStyle("-jfx-unfocus-color: black; -fx-text-fill: black;");
		Utils.onlyIntegerTextField(field);
		
		Text errorText = new Text("Insira uma quantidade");
		errorText.setFill(Color.RED);
		errorText.setVisible(false);
		
		VBox vBox = new VBox(text, field, errorText);
		vBox.setAlignment(Pos.TOP_LEFT);
		vBox.setSpacing(15);
		
		dialog.getLayout().setBody(vBox);
		
		confirmarButton.setOnAction(e ->
		{
			if (field.getText().isEmpty())
			{
				errorText.setText("Insira uma quantidade");
				errorText.setVisible(true);
				return;
			}
			if (Integer.parseInt(field.getText()) == 0)
			{
				errorText.setText("Insira um número maior que zero");
				errorText.setVisible(true);
				return;
			}
			
			TreeWrapper<Produto> item = cell.getItem();
			
			int quantidadeMaxima = item.getValue().getQuantidade();
			int quantidadeAtual = Integer.parseInt(field.getText());
			
			if (quantidadeAtual > quantidadeMaxima)
			{
				errorText.setText("Só existem " + item.getValue().getQuantidade() + " desse produto em estoque, \nescolha uma quantidade menor ou igual a isso.");
				errorText.setVisible(true);
				return;
			}
			errorText.setVisible(false);
			
			item.setUserData((quantidadeAtual > quantidadeMaxima) ? quantidadeMaxima : quantidadeAtual);
			
			if ((int)cell.getItem().getUserData() > 0)
			{
				cell.getListView().getItems().remove(cell.getItem());
				cell.getListView().getItems().add(cell.getIndex(), item);
			}
			
			dialog.getDialog().close();
		});
		field.onActionProperty().bind(confirmarButton.onActionProperty());
		
		return dialog;
	}
}
