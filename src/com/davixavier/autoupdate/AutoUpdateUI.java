package com.davixavier.autoupdate;

import com.davixavier.application.img.IconsPath;
import com.davixavier.panes.PanePathing;
import com.davixavier.utils.ExecuterServices;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AutoUpdateUI extends Application
{
	@Override
	public void start(Stage arg0) throws Exception
	{
		Pane root = FXMLLoader.load(getClass().getResource(PanePathing.AUTOUPDATE.toString()));
		Scene mainScene = new Scene(root);
		
		Stage primaryStage = new Stage();
		primaryStage.setScene(mainScene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e ->
		{
			e.consume();
		});
		primaryStage.getIcons().add(IconsPath.ICON.getImage());
		primaryStage.setTitle("AEEV - Atualizador");
		
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception
	{
		super.stop();
		
		ExecuterServices.getExecutor().shutdownNow();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
