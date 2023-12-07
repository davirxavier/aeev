package com.davixavier.nodes.materiallist;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MaterialCellItem<T>
{
	private SimpleStringProperty leftString;
	private SimpleStringProperty rightString;
	private SimpleStringProperty descriptionString;
	private T object;
	private MaterialListCell<T> cell;
	
	private ObservableList<MaterialCellNode> items;
	
	public MaterialCellItem()
	{
		items = FXCollections.observableArrayList();
		leftString = new SimpleStringProperty();
		rightString = new SimpleStringProperty();
		descriptionString = new SimpleStringProperty();
	}
	
	public SimpleStringProperty leftStringProperty()
	{
		return leftString;
	}
	public SimpleStringProperty rightStringProperty()
	{
		return rightString;
	}
	public SimpleStringProperty descriptionStringProperty()
	{
		return descriptionString;
	}
	
	public String getLeftString()
	{
		return leftString.get();
	}

	public void setLeftString(String leftString)
	{
		this.leftString.set(leftString);
	}

	public String getRightString()
	{
		return rightString.get();
	}

	public void setRightString(String rightString)
	{
		this.rightString.set(rightString);
	}

	public String getDescriptionString()
	{
		return descriptionString.get();
	}

	public void setDescriptionString(String descriptionString)
	{
		this.descriptionString.set(descriptionString);
	}
	
	public ObservableList<MaterialCellNode> getItems()
	{
		return items;
	}

	public T getObject()
	{
		return object;
	}

	public void setObject(T object)
	{
		this.object = object;
	}

	public MaterialListCell<T> getCell() {
		return cell;
	}

	public void setCell(MaterialListCell<T> cell) {
		this.cell = cell;
	}
}
