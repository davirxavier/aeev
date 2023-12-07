package com.davixavier.nodes.materiallist;

import com.davixavier.application.img.IconsPath;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.nodes.HSpacer;
import com.davixavier.nodes.ToolbarButton;
import com.davixavier.nodes.VSpacer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MaterialListCell<T> extends JFXListCell<MaterialCellItem<T>>
{
	private SimpleBooleanProperty expanded;
	private MaterialCellItem<T> persistentObject;
	
	protected ToolbarButton deleteButton;
	protected ToolbarButton editButton;
	
	protected Label rightLabel;
	protected Label leftLabel;
	protected Label bottomLabel;
	protected VBox mainVBox;
	protected VBox leftVBox;
	protected VBox rightVBox;
	
	public MaterialListCell()
	{
		super();
		
		init();
	}
	
	public MaterialListCell(String leftString, String rightString, String descriptionString)
	{
		super();
		
		init();
	}
	
	private void init()
	{
		expanded = new SimpleBooleanProperty();
		
		expanded.bind(selectedProperty());
		
		deleteButton = new ToolbarButton(null);
		editButton = new ToolbarButton(null);
		rightLabel = new Label();
		
		rightVBox = new VBox();
		leftVBox = new VBox();
		mainVBox = new VBox();
		
		expanded.addListener((l, oldVal, newVal) ->
		{
			
		});
	}
	
	@Override
    protected void updateItem(final MaterialCellItem<T> persistentObject, final boolean empty) 
	{
		super.updateItem(persistentObject, empty);
		
		this.persistentObject = persistentObject;
		
        if (empty) 
        {
            setText(null);
            setGraphic(null);
        } 
        else 
        {
        	setText(null);
        	
        	leftLabel = new Label();
        	bottomLabel = new Label();
        	rightLabel = new Label();
        	
        	leftLabel.textProperty().bind(persistentObject.leftStringProperty());
        	bottomLabel.textProperty().bind(persistentObject.descriptionStringProperty());
        	rightLabel.textProperty().bind(persistentObject.rightStringProperty());
        	
        	leftLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: black;");
        	rightLabel.styleProperty().bind(leftLabel.styleProperty());
        	rightLabel.textFillProperty().bind(leftLabel.textFillProperty());
        	bottomLabel.setStyle("-fx-font-size: 9pt;");
        	bottomLabel.setTextFill(Color.valueOf("#6b6b6b"));
        	
        	rightLabel.setVisible(false);
        	
        	ToolbarButton editToolbarButton = new ToolbarButton(IconsPath.EDIT24PXGRAY.getImage());
        	ToolbarButton removeToolbarButton = new ToolbarButton(IconsPath.DELETE24PXGRAY.getImage());
        	
        	StackPane rightLabelStackPane = new StackPane(removeToolbarButton, rightLabel);
        	rightLabelStackPane.setAlignment(Pos.CENTER_RIGHT);
        	
        	rightVBox = new VBox(rightLabelStackPane, new VSpacer(), editToolbarButton);
        	rightVBox.setAlignment(Pos.CENTER_RIGHT);
        	
        	leftVBox = new VBox(leftLabel, bottomLabel);
        	leftVBox.setStyle("-fx-padding: 5 0 0 0;");
        	leftVBox.setSpacing(5);
        	leftVBox.setAlignment(Pos.CENTER_LEFT);
        	
        	mainVBox = new VBox(leftVBox);
        	mainVBox.setSpacing(5);
        	
        	HBox hBox = new HBox(mainVBox, new HSpacer(), rightVBox);
        	
        	persistentObject.getItems().forEach(e ->
        	{
        		mainVBox.getChildren().add(e);
        	});
        	
        	persistentObject.getItems().addListener((ListChangeListener.Change<? extends MaterialCellNode> l) ->
        	{
        		l.next();
        		
        		if (l.wasAdded())
        		{
        			l.getAddedSubList().forEach(e ->
        			{
        				mainVBox.getChildren().add(e);
        			});
        		}
        		
        		if (l.wasRemoved())
        		{
        			l.getRemoved().forEach(e ->
        			{
        				mainVBox.getChildren().remove(e);
        			});
        		}
        	});
        	
        	mainVBox.setFillWidth(true);

        	setGraphic(hBox);
        	
        	deleteButton = removeToolbarButton;
        	editButton = editToolbarButton;
        	
        	persistentObject.setCell(this);
        }
	}
	
	public SimpleBooleanProperty expandedProperty()
	{
		return expanded;
	}

	public boolean isExpanded()
	{
		return expanded.get();
	}

	public void setExpanded(boolean expanded)
	{
		this.expanded.set(expanded);
	}

	public MaterialCellItem<T> getPersistentObject() {
		return persistentObject;
	}

	public ToolbarButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(ToolbarButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public ToolbarButton getEditButton() {
		return editButton;
	}

	public void setEditButton(ToolbarButton editButton) {
		this.editButton = editButton;
	}

	public Label getRightLabel() {
		return rightLabel;
	}

	public Label getLeftLabel() {
		return leftLabel;
	}

	public Label getBottomLabel() {
		return bottomLabel;
	}
}
