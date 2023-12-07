package com.davixavier.nodes;

import com.jfoenix.controls.JFXButton;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AppBarButtonStyler
{
	public static void style(JFXButton button)
	{
		String hoveredStyle = "-fx-background-color: derive(-buttoncolor, 20%); "
				   			+ "-fx-background-insets: 1 9 2 5; "
				   			+ "-fx-background-radius: 100;";
		
		String unhoverStyle = "-fx-background-color: transparent; "
							+ "-fx-background-insets: 1 9 2 5; "
							+ "-fx-background-radius: 100;";
		
		button.setStyle(unhoverStyle);
		
		button.setOnMouseEntered(e ->
		{
			button.setStyle(hoveredStyle);
		});
		button.setOnMouseExited(e ->
		{
			button.setStyle(unhoverStyle);
		});
	}
}	
