package com.davixavier.entidades.usuarios;

public enum UsuárioType
{
	GERENTE("Gerente"), VENDEDOR("Gerente");
	
	private String string;
	
	private UsuárioType(String string)
	{
		this.string = string;
	}
	
	public UsuárioType fromString(String string)
	{
		if (string.toLowerCase().equals("gerente"))
		{
			return UsuárioType.GERENTE;
		}
		else
		{
			return UsuárioType.VENDEDOR;
		}
	}
	
	public String getString()
	{
		return string;
	}
}
