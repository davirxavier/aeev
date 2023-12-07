package com.davixavier.panes.login;

import java.sql.SQLException;

import com.davixavier.application.Main;
import com.davixavier.application.MainController;
import com.davixavier.application.img.IconsPath;
import com.davixavier.nodes.SearchBar;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.ExecuterServices;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class LoginController
{
	private SimpleBooleanProperty logged;
	
    @FXML
    private StackPane primaryPane;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField senhaField;
    
    @FXML
    private VBox vbox;
    
    @FXML
    private ImageView userimg;
    
    @FXML
    void initialize()
    {
    	userimg.setImage(IconsPath.ICON.getImage());
    	
    	logged = new SimpleBooleanProperty(false);
    	
    	usernameField.setOnAction(e ->
    	{
    		senhaField.requestFocus();
    	});
    	senhaField.setOnAction(e ->
    	{
    		entrar(e);
    	});
    	
    	primaryPane.parentProperty().addListener((l, oldVal, newVal) ->
    	{
    		if (newVal != null)
    		{
    			primaryPane.prefWidthProperty().bind(((Pane)primaryPane.getParent()).widthProperty());
    			primaryPane.prefHeightProperty().bind(((Pane)primaryPane.getParent()).heightProperty());
    		}
    	});
    	
    	Label versionLabel = new Label(Main.CURRENTVERSION);
    	versionLabel.setMouseTransparent(true);
    	versionLabel.setTextFill(Color.WHITE);
    	versionLabel.setEllipsisString(versionLabel.getText());
    	primaryPane.boundsInLocalProperty().addListener((l, oldVal, newVal) ->
    	{
    		versionLabel.setPadding(new Insets(0, newVal.getWidth()-28, newVal.getHeight()-30, 0));
    	});
    	
    	primaryPane.getChildren().add(versionLabel);
    	
//    	usernameField.setText("admin");
//    	senhaField.setText("admin");
//    	entrar(null);
    }

    @FXML
    void entrar(ActionEvent event)
    {
    	MainController.setLoading(true);
    	
    	ExecuterServices.getExecutor().execute(() ->
    	{
    		SimpleBooleanProperty result = new SimpleBooleanProperty();
			try 
			{
				result.set(Login.realizarLogin(usernameField.getText(), senhaField.getText()));
			} 
			catch (SQLException e1)
			{
				e1.printStackTrace();
				
				result.set(false);
			}
    		
    		Platform.runLater(() ->
    		{
    			if (result.get())
    			{
    				vbox.setVisible(false);
    				
    				Line path = new Line();
    				path.setStartX(primaryPane.getScene().getWidth()/2);
    				path.setStartY(primaryPane.getScene().getHeight()/2);
    				path.setEndX(0 - primaryPane.getScene().getWidth());
    				path.setEndY(primaryPane.getScene().getHeight()/2);
    				
    				PathTransition pathTransition = new PathTransition();
    				pathTransition.setPath(path);
    				pathTransition.setDuration(Duration.seconds(1));
    				pathTransition.setNode(primaryPane);
    				pathTransition.play();
    				
    				
    				pathTransition.setOnFinished(e ->
    				{
    					logged.set(true);
    					primaryPane.setVisible(false);
    				});
    			}
    			else 
    			{
    				errorDialog().getDialog().show();
				}
    			
    			MainController.setLoading(false);
    		});
    	});
    }
    
    private FlatJFXDialog errorDialog()
    {
    	StackPane pane = (StackPane)primaryPane.getScene().lookup("#primaryStackPane");
    	
    	JFXButton button = new JFXButton();
    	button.setVisible(false);
    	
    	String header = "Erro";
    	String body = "Nome de usuário ou senha incorretos.";
    	FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, button);
    	
    	return dialog;
    }
    
    public SimpleBooleanProperty loggedProperty()
    {
    	return logged;
    }

	public boolean isLogged()
	{
		return logged.get();
	}

}
