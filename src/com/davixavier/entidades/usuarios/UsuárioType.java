package com.davixavier.entidades.usuarios;

public enum Usu�rioType
{
	GERENTE("Gerente"), VENDEDOR("Gerente");
	
	private String string;
	
	private Usu�rioType(String string)
	{
		this.string = string;
	}
	
	public Usu�rioType fromString(String string)
	{
		if (string.toLowerCase().equals("gerente"))
		{
			return Usu�rioType.GERENTE;
		}
		else
		{
			return Usu�rioType.VENDEDOR;
		}
	}
	
	public String getString()
	{
		return string;
	}
}
