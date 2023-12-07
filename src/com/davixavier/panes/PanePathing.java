package com.davixavier.panes;

public enum PanePathing
{
	LOGINPANE("/com/davixavier/panes/login/login.fxml"), 
//	CLIENTESPANE(""), 
//	VENDASPANE(""), 
//	ESTOQUEPANE(""), 
//	HISTORICOPANE(""),
	PANES("/com/davixavier/panes/"),
	AUTOUPDATE("/com/davixavier/autoupdate/autoupdate.fxml");
	
	private String string;
	
	private PanePathing(String string)
	{
		this.string = string;
	}
	
	public String toString()
	{
		return string;  
	}
}
