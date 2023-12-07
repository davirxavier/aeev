package com.davixavier.nodes;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

public class ToolbarButton extends JFXButton
{
	private SimpleStringProperty backgroundcolorHovered;
	private SimpleStringProperty hoveredStyle;
	private SimpleStringProperty unhoverStyle;
	private SimpleIntegerProperty radius;
	
	public ToolbarButton(Image graphic)
	{
		radius = new SimpleIntegerProperty(100);
		
		hoveredStyle = new SimpleStringProperty(
				"-fx-background-color: derive(-buttoncolor, 20%); "
	   			+ "-fx-background-insets: -1 4 0 3; "
	   			+ "-fx-background-radius: " + radius.get() +";");
		unhoverStyle = new SimpleStringProperty(
						"-fx-background-color: transparent; "
						+ "-fx-background-insets: -1 4 0 3; "
						+ "-fx-background-radius: " + radius.get() + ";");
		
		setGraphic(new ImageView(graphic));
		getStyleClass().add("searchBarButton");
		
		setStyle(unhoverStyle.get());
		setOnMouseEntered(e ->
		{
			setStyle(hoveredStyle.get());
		});
		setOnMouseExited(e ->
		{
			setStyle(unhoverStyle.get());
		});
	}
	
	public void setBackgroundInHover(String color)
	{
		hoveredStyle.set(
				"-fx-background-color: " + color + "; "
	   			+ "-fx-background-insets: -1 4 0 3; "
	   			+ "-fx-background-radius: 100;");
		if (isHover())
			setStyle(hoveredStyle.get());
	}
	
	public void setBackgroundInUnhover(String color)
	{
		unhoverStyle.set(
				"-fx-background-color: " + color + "; "
	   			+ "-fx-background-insets: -1 4 0 3; "
	   			+ "-fx-background-radius: 100;");
		if (!isHover())
			setStyle(unhoverStyle.get());
	}
	
	public void setRadius(int radius)
	{
		this.radius.set(radius);
		
		hoveredStyle.set(
				"-fx-background-color: derive(-buttoncolor, 20%); "
	   			+ "-fx-background-insets: -1 4 0 3; "
	   			+ "-fx-background-radius: " + this.radius.get() +";");
		unhoverStyle.set(
						"-fx-background-color: transparent; "
						+ "-fx-background-insets: -1 4 0 3; "
						+ "-fx-background-radius: " + this.radius.get() + ";");
	}
}
