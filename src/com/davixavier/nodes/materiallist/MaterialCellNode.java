package com.davixavier.nodes.materiallist;

import com.davixavier.nodes.HSpacer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class MaterialCellNode extends HBox
{
	private Label leftLabel;
	private Label rightLabel;
	private HSpacer spacer;
	
	public MaterialCellNode()
	{
		leftLabel = new Label();
		rightLabel = new Label();
		spacer = new HSpacer();
		
		init();
	}
	
	public MaterialCellNode(String leftString, String rightString)
	{
		leftLabel = new Label(leftString);
		rightLabel = new Label(rightString);
		spacer = new HSpacer();
		
		init();
	}
	
	private void init()
	{
		leftLabel.setTextFill(Color.valueOf("#6b6b6b"));
		rightLabel.setTextFill(Color.valueOf("#6b6b6b"));
		
		leftLabel.setStyle("-fx-font-size: 9pt;");
		rightLabel.setStyle("-fx-font-size: 9pt;");
		
		setAlignment(Pos.TOP_LEFT);
		
		getChildren().addAll(leftLabel, rightLabel);
	}

	public Label getLeftLabel()
	{
		return leftLabel;
	}

	public void setLeftLabel(Label leftLabel)
	{
		this.leftLabel = leftLabel;
	}

	public Label getRightLabel()
	{
		return rightLabel;
	}

	public void setRightLabel(Label rightLabel)
	{
		this.rightLabel = rightLabel;
	}

	public HSpacer getSpacer()
	{
		return spacer;
	}

	public void setSpacer(HSpacer spacer)
	{
		this.spacer = spacer;
	}
}
