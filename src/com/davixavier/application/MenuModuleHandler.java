package com.davixavier.application;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;

import com.davixavier.panes.PanePathing;
import com.davixavier.utils.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

public class MenuModuleHandler 
{
	private ObservableMap<String, Parent> modules;
	private ObservableMap<String, Runnable> modulesRunnables;
	private ObservableList<String> modulesNames;
	
	public MenuModuleHandler()
	{
		modules = FXCollections.observableHashMap();
		modulesRunnables = FXCollections.observableHashMap();
		modulesNames = FXCollections.observableArrayList();
	}
	
	public void addModule(String name, Parent parent, Runnable runnable)
	{
		modules.put(Utils.removerAcentos(name).toLowerCase(), parent);
		modulesRunnables.put(Utils.removerAcentos(name).toLowerCase(), runnable);
		modulesNames.add(name);
	}
	
	public void initModules()
	{
		modules.forEach((k, v) ->
    	{
    		String fxmlPath = PanePathing.PANES.toString() + k + "/" + k + ".fxml";

    		try
			{
    			v = FXMLLoader.load(getClass().getResource(fxmlPath));
				modules.put(k, v);
			}
    		catch (IOException e)
			{
				e.printStackTrace();
			}
    		
    		v.setVisible(false);
    	});
	}
	
	public void initModulesList(ListView<String> listView)
	{
		listView.getItems().addAll(modulesNames);
		
		listView.getSelectionModel().selectedItemProperty().addListener((l, oldval, newval) ->
		{
			Runnable runnable = modulesRunnables.get(Utils.removerAcentos(newval).toLowerCase());
			
			if (runnable != null)
				runnable.run();
		});
	}
	
	public void runRunnables()
	{
		modulesRunnables.forEach((k, v) ->
		{
			v.run();
		});
	}
	
	public ObservableMap<String, Parent> getModules()
	{
		return modules;
	}
	
	public ObservableList<Parent> getModulesAsList()
	{
		ObservableList<Parent> parents = FXCollections.observableArrayList();
		
		modules.forEach((k, v) -> parents.add(v));
		
		return parents;
	}
}
