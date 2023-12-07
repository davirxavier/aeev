package com.davixavier.nodes.materiallist;

public class NoButtonMaterialCell<T> extends MaterialListCell<T>
{
	@Override
	protected void updateItem(MaterialCellItem<T> persistentObject, boolean empty) 
	{
		super.updateItem(persistentObject, empty);
		
		getDeleteButton().setVisible(false);
		getEditButton().setVisible(false);
		
		//getRightLabel().setVisible(true);
	}
}
