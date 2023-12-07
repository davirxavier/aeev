package com.davixavier.autoupdate;

import java.net.MalformedURLException;
import java.net.URL;

import com.davixavier.application.img.IconsPath;
import com.davixavier.utils.ExecuterServices;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class AutoUpdateController 
{
	@FXML
    private Label statusLabel;
	
    @FXML
    private StackPane primaryPane;

    @FXML
    private ImageView logoImg;

    @FXML
    public void initialize()
    {
    	logoImg.setImage(IconsPath.ICON.getImage());
    	
    	AutoUpdate autoUpdate = new AutoUpdate();
    	ExecuterServices.getExecutor().execute(() ->
    	{
    		autoUpdate.update();
    	});
    }
}
