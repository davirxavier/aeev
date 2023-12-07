package com.davixavier.entidades.clientes;

public class CPF implements Identificável
{
	private String cpf;
	
	public CPF() 
	{
		this.cpf = "";
	}
	
	public CPF(String cpf) 
	{
		this.cpf = cpf;
	}
	
	public String toFormattedString()
	{
		if (cpf.length() < 11)
			return cpf;
		
		StringBuilder builder = new StringBuilder(cpf);
		
		builder.insert(3, ".");
		builder.insert(7, ".");
		builder.insert(11, "-");
		
		return builder.toString();
	}
	
	public String toString()
	{
		return cpf;
	}

	public String getType() 
	{
		return "CPF";
	}
}
