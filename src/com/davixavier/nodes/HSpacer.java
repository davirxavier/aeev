package com.davixavier.nodes;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HSpacer extends Region
{
	public HSpacer()
	{
    	HBox.setHgrow(this, Priority.ALWAYS);
	}
}
