package com.davixavier.nodes.materiallist;

import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MaterialListView<T> extends JFXListView<MaterialCellItem<T>>
{
	public MaterialListView()
	{
		super();
		
		getStylesheets().add("/com/davixavier/nodes/materiallist/expansiblelist.css");
		
		setCellFactory(new Callback<ListView<MaterialCellItem<T>>, ListCell<MaterialCellItem<T>>>()
		{
			@Override
			public ListCell<MaterialCellItem<T>> call(ListView<MaterialCellItem<T>> arg0)
			{
				MaterialListCell<T> cell = new MaterialListCell<T>();
				
				return cell;
			}
		});
	}
}
