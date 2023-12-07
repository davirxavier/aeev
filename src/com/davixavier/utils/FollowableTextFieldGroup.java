package com.davixavier.utils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class FollowableTextFieldGroup
{
	private SimpleObjectProperty<ObservableList<TextField>> nodes;
	private SimpleObjectProperty<EventHandler<ActionEvent>> lastEventHandler;
	
	public FollowableTextFieldGroup()
	{
		nodes = new SimpleObjectProperty<ObservableList<TextField>>(FXCollections.observableArrayList());
		lastEventHandler = new SimpleObjectProperty<EventHandler<ActionEvent>>();
		setUp();
	}
	
	public FollowableTextFieldGroup(EventHandler<ActionEvent> lastEventHandler, TextField... nodes)
	{
		this.nodes = new SimpleObjectProperty<ObservableList<TextField>>(FXCollections.observableArrayList(nodes));
		this.lastEventHandler = new SimpleObjectProperty<EventHandler<ActionEvent>>(lastEventHandler);
		
		setUpActions();
		setUp();
	}
	
	private void setUp()
	{
		nodes.get().addListener((ListChangeListener.Change<? extends TextField> l) ->
		{
			l.next();
			
			setUpActions();
		});
	}
	
	private void setUpActions()
	{
		for (int i = 0; i < nodes.get().size()-1; i++)
		{
			final int j = i;
			this.nodes.get().get(j).setOnAction(e ->
			{
				this.nodes.get().get(j+1).requestFocus();
			});
		}
		
		this.nodes.get().get(this.nodes.get().size()-1).setOnAction(lastEventHandler.get());
	}
	
	public boolean isAnyEmpty()
	{
		for (TextField textField : getNodes())
		{
			if (textField.getText().isEmpty())
				return true;
		}
		
		return false;
	}

	public ObservableList<TextField> getNodes()
	{
		return nodes.get();
	}

	public EventHandler<ActionEvent> getLastEventHandler()
	{
		return lastEventHandler.get();
	}

	public void setLastEventHandler(EventHandler<ActionEvent> lastEventHandler)
	{
		this.lastEventHandler.set(lastEventHandler);
	}
	
	public SimpleObjectProperty<EventHandler<ActionEvent>> lastEventHandlerProperty()
	{
		return lastEventHandler;
	}
	
	public SimpleObjectProperty<ObservableList<TextField>> nodesProperty()
	{
		return nodes;
	}
}
