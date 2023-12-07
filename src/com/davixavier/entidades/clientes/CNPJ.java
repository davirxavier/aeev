package com.davixavier.entidades.clientes;

public class CNPJ implements Identificável
{
	private String cnpj;
	
	public CNPJ()
	{
		cnpj = "";
	}
	
	public CNPJ(String cpnj)
	{
		this.cnpj = cpnj;
	}
	
	public String toFormattedString()
	{
		if (cnpj.length() < 14)
			return cnpj;
		
		StringBuilder builder = new StringBuilder(cnpj);
		
		builder.insert(2, ".");
		builder.insert(6, ".");
		builder.insert(10, "/");
		builder.insert(15, "-");
		
		return builder.toString();
	}
	
	public String toString()
	{
		return cnpj;
	}

	public String getType() 
	{
		return "CNPJ";
	}
}
