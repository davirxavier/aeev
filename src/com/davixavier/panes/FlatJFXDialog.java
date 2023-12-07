package com.davixavier.panes;

import com.davixavier.application.img.IconsPath;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class FlatJFXDialog
{
	private JFXDialogLayout layout;
	private JFXDialog dialog;
	
	public FlatJFXDialog(StackPane pane, String header, String body, JFXButton... buttons)
	{
		layout = new JFXDialogLayout();
		layout.setHeading(new Text(header));
		layout.setBody(new Text(body));
		layout.setActions(buttons);
		
		ImageView closeImage = new ImageView(IconsPath.CLOSE18PXGRAY.getImage());
		closeImage.setFitHeight(18);
		closeImage.setFitWidth(18);
		closeImage.setPickOnBounds(true);
		closeImage.setCursor(Cursor.HAND);
		closeImage.setOnMouseClicked(e ->
		{
			dialog.close();
		});
		
		dialog = new JFXDialog(pane, layout, JFXDialog.DialogTransition.CENTER);
		
		StackPane dialogPane = (StackPane)dialog.getChildren().get(0);
		dialogPane.setAlignment(Pos.TOP_RIGHT);
		dialogPane.getChildren().add(closeImage);
		dialogPane.setPadding(new Insets(5, 5, 0, 0));
		
		dialog.setOverlayClose(false);
		dialog.setOnDialogOpened(e ->
		{
			dialogPane.requestFocus();
		});
	}
	
	public void setHeader(String header)
	{
		layout.setHeading(new Text(header));
	}
	
	public void setBody(String body)
	{
		layout.setBody(new Text(body));
	}

	public JFXDialogLayout getLayout()
	{
		return layout;
	}

	public JFXDialog getDialog()
	{
		return dialog;
	}
}
