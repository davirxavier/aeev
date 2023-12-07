package com.davixavier.application.img;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import javafx.scene.image.Image;

public enum IconsPath 
{
	ACCOUNT24PXBLACK("/com/davixavier/application/img/account24pxblack.png"),
	ACCOUNT24PXWHITE("/com/davixavier/application/img/account24pxwhite.png"),
	ADDCARRINHO24PXBLACK("/com/davixavier/application/img/addcarrinho24pxblack.png"),
	ARROW_DOWN24PXBLACK("/com/davixavier/application/img/arrow_down24pxblack.png"),
	ARROWRIGHT24PXBLACK("/com/davixavier/application/img/arrowright24pxblack.png"),
	ARROWRIGHT24PXGRAY("/com/davixavier/application/img/arrowright24pxgray.png"),
	BUSCAICON18PXWHITE("/com/davixavier/application/img/buscaicon18pxwhite.png"),
	BUSCAICON24PXWHITE("/com/davixavier/application/img/buscaicon24pxwhite.png"),
	BUSCAICON24PXBLACK("/com/davixavier/application/img/buscaicon24pxblack.png"),
	BUSCAICON24PXGRAY("/com/davixavier/application/img/buscaicon24pxgray.png"),
	BUSCAICON36PXWHITE("/com/davixavier/application/img/buscaicon36pxwhite.png"),
	CLOSE18PXBLACK("/com/davixavier/application/img/close18pxblack.png"),
	CLOSE18PXGRAY("/com/davixavier/application/img/close18pxgray.png"),
	CLOSE18PXWHITE("/com/davixavier/application/img/close18pxwhite.png"),
	CLOSE24PXWHITE("/com/davixavier/application/img/close24pxwhite.png"),
	CLOSE24PXBLACK("/com/davixavier/application/img/close24pxblack.png"),
	CLOSE24PXGRAY("/com/davixavier/application/img/close24pxgray.png"),	
	CLOSEIMG("/com/davixavier/application/img/closeimg.png"),
	DELETE18PXBLACK("/com/davixavier/application/img/delete18pxblack.png"),
	DELETE18PXGRAY("/com/davixavier/application/img/delete18pxgray.png"),
	DELETE24PXBLACK("/com/davixavier/application/img/delete24pxblack.png"),
	DELETE24PXGRAY("/com/davixavier/application/img/delete24pxgray.png"),
	DELETEFOREVER18PXBLACK("/com/davixavier/application/img/deleteforever18pxblack.png"),
	DELETEFOREVER24PXBLACK("/com/davixavier/application/img/deleteforever24pxblack.png"),
	EDIT18PXBLACK("/com/davixavier/application/img/edit18pxblack.png"),
	EDIT18PXGRAY("/com/davixavier/application/img/edit18pxgray.png"),
	EDIT24PXBLACK("/com/davixavier/application/img/edit24pxblack.png"),
	EDIT24PXGRAY("/com/davixavier/application/img/edit24pxgray.png"),
	MOREVERTICAL24PXWHITE("/com/davixavier/application/img/morevertical24pxwhite.png"),
	SEARCH18PXBLACK("/com/davixavier/application/img/search18pxblack.png"),
	SEARCH18PXGRAY("/com/davixavier/application/img/search18pxgray.png"),
	SETTING24PXBLACK("/com/davixavier/application/img/settings24pxblack.png"),
	SETTINGS24PXWHITE("/com/davixavier/application/img/settings24pxwhite.png"),
	
	ICON("/com/davixavier/application/img/icon.png"),
	LOGO("/com/davixavier/application/img/logo.png"),
	USERBIG("/com/davixavier/application/img/userbig.png"),
	;
	
	private String path;
	
	private IconsPath(String path)
	{
		this.path = path;
	}
	
	public Image getImage()
	{
		return new Image(path);
	}
	
	public String getPath()
	{
		return path;
	}
}
