package com.davixavier.nodes.materiallist;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NoButtonMaterialList<T> extends MaterialListView<T>
{
	public NoButtonMaterialList()
	{
		getStylesheets().add("/com/davixavier/nodes/expansiblelist/expansiblelist.css");
		
		setCellFactory(new Callback<ListView<MaterialCellItem<T>>, ListCell<MaterialCellItem<T>>>()
		{
			@Override
			public ListCell<MaterialCellItem<T>> call(ListView<MaterialCellItem<T>> arg0)
			{
				NoButtonMaterialCell<T> cell = new NoButtonMaterialCell<T>();
				
				return cell;
			}
		});
	}
}
