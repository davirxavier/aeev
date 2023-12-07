package com.davixavier.panes.estoque;

import java.sql.SQLException;

import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.nodes.VSpacer;
import com.davixavier.utils.TreeWrapper;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class EstoqueListaManager
{
	public static void setUp(JFXListView<Produto> lista)
	{
		lista.getStylesheets().add("/com/davixavier/panes/estoque/estoquelista.css");
		
		try 
		{
			ProdutoDAO.getInstance().getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
			{
				lista.getItems().add(v);
			});
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		lista.setCellFactory(new Callback<ListView<Produto>, ListCell<Produto>>()
		{
			@Override
			public ListCell<Produto> call(ListView<Produto> arg0)
			{
				JFXListCell<Produto> cell = new JFXListCell<Produto>()
				{
					@Override
				     protected void updateItem(final Produto persistentObject, final boolean empty) 
					 {
				            super.updateItem(persistentObject, empty);
				            if (empty) 
				            {
				                setText(null);
				                setGraphic(null);
				            } 
				            else 
				            {
				            	setText(null);
				            	
				            	Label topLabel = new Label(persistentObject.getNome());
				            	Label bottomLabel = new Label("Em estoque: " + persistentObject.getQuantidade() + "");
				            	Label rightLabel = new Label("R$" + String.format("%.2f", persistentObject.getPreço()));
				            	
				            	topLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: black;");
				            	rightLabel.styleProperty().bind(topLabel.styleProperty());
				            	bottomLabel.setTextFill(Color.GRAY);
				            	
				            	ImageView expandImage = new ImageView(new Image(("/com/davixavier/application/img/baseline_arrow_down24pxblack.png")));
				            	VBox rightVBox = new VBox(rightLabel, new VSpacer(),expandImage);
				            	rightVBox.setAlignment(Pos.CENTER_RIGHT);
				            	
				            	VBox leftVBox = new VBox(topLabel, bottomLabel);
				            	leftVBox.setSpacing(2);
				            	leftVBox.setAlignment(Pos.CENTER_LEFT);
				            	
				            	Region spacer = new Region();
				            	HBox.setHgrow(spacer, Priority.ALWAYS);
				            	HBox hBox = new HBox(leftVBox, spacer, rightVBox);
				            	
				            	setGraphic(hBox);
				            }
					 }
				};
				
				return cell;
			}
		});
	} 
}
