package com.davixavier.nodes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class InfoPane extends HBox
{
	private ObservableList<Node> items;
	
	public InfoPane()
	{
		items = FXCollections.observableArrayList();
		
		init();
	}
	
	private void init()
	{
		setStyle("-fx-background-color: white; -fx-background-radius: 4;");
		setHeight(30);
		setMaxHeight(30);
		setWidth(300);
		setEffect(new DropShadow(10, Color.BLACK));
		setSpacing(5);
		setAlignment(Pos.CENTER);
		
		items.addListener((ListChangeListener.Change<? extends Node> change) ->
		{
			change.next();
			
			if (change.wasAdded())
			{
				getChildren().add(createSpacer());
				change.getAddedSubList().forEach(n ->
				{
					getChildren().add(n);
					getChildren().add(createSpacer());
					getChildren().add(new Separator(Orientation.VERTICAL));
					getChildren().add(createSpacer());
				});
				
				getChildren().remove(getChildren().size()-1);
			}
			
			change.getRemoved().forEach(e ->
			{
				if (!getChildren().get(0).equals(e))
				{
					for (int i = 0; i < getChildren().size(); i++)
					{
						if (getChildren().get(i).equals(e))
						{
							getChildren().remove(i);
							break;
						}
					}
				}
				
				getChildren().remove(e);
			});
			
			getChildren().remove(getChildren().size()-1);
		});
	}
	
	private Node createSpacer() 
	{
	    final Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);
	    return spacer;
	}

	public ObservableList<Node> getItems()
	{
		return items;
	}

	public void setItems(ObservableList<Node> items)
	{
		this.items = items;
	}
}
