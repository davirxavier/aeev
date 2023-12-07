package com.davixavier.nodes;

import com.davixavier.application.img.IconsPath;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class MaterialToolBar extends AnchorPane
{
	private Label title;
	private SearchBar searchBar;
	private ToolbarButton menuButton;
	
	public MaterialToolBar()
	{
		init();
	}
	
	private void init()
	{
		setMinHeight(45);
		setPrefWidth(700);
		
		title = new Label();
		title.setTextFill(Color.WHITE);
		title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
		
		searchBar = new SearchBar();
		
		menuButton = new ToolbarButton(IconsPath.MOREVERTICAL24PXWHITE.getImage());
		menuButton.setContextMenu(new ContextMenu());
		menuButton.setOnAction(e ->
		{
			Bounds bounds = menuButton.localToScreen(menuButton.getBoundsInLocal());
			menuButton.getContextMenu().show(getScene().getWindow(), bounds.getMinX() + (bounds.getWidth()/2), bounds.getMaxY());
		});
		
		AnchorPane.setLeftAnchor(title, 11.0);
		AnchorPane.setTopAnchor(title, 10.0);
		
		AnchorPane.setTopAnchor(searchBar, 9.0);
		AnchorPane.setLeftAnchor(searchBar, 102.0);
		AnchorPane.setRightAnchor(searchBar, 46.0);
		
		AnchorPane.setRightAnchor(menuButton, 0.0);
		AnchorPane.setTopAnchor(menuButton, 7.0);
		
		getChildren().addAll(title, searchBar, menuButton);
	}
	
	public void setTitleString(String title)
	{
		this.title.setText(title);
	}

	public Label getTitle()
	{
		return title;
	}

	public SearchBar getSearchBar()
	{
		return searchBar;
	}

	public JFXButton getMenuButton()
	{
		return menuButton;
	}
}
